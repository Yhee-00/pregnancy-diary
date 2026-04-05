package com.oyh.pregnancydiary.service;

import com.oyh.pregnancydiary.dto.BookOrderRequestDto;
import com.oyh.pregnancydiary.dto.BookOrderResponseDto;
import com.oyh.pregnancydiary.entity.BookOrder;
import com.oyh.pregnancydiary.entity.DiaryEntry;
import com.oyh.pregnancydiary.entity.Pregnancy;
import com.oyh.pregnancydiary.repository.BookOrderRepository;
import com.oyh.pregnancydiary.repository.DiaryEntryRepository;
import com.oyh.pregnancydiary.repository.PregnancyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;
import tools.jackson.databind.ObjectMapper;

import javax.net.ssl.SSLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BookPrintService {

    private final BookOrderRepository bookOrderRepository;
    private final PregnancyRepository pregnancyRepository;
    private final DiaryEntryRepository diaryEntryRepository;
    private final WebClient webClient;


    public BookPrintService(
            BookOrderRepository bookOrderRepository,
            PregnancyRepository pregnancyRepository, DiaryEntryRepository diaryEntryRepository,
            @Value("${sweetbook.api.key}") String apiKey,
            @Value("${sweetbook.api.base-url}") String baseUrl) throws SSLException {
        this.bookOrderRepository = bookOrderRepository;
        this.pregnancyRepository = pregnancyRepository;
        this.diaryEntryRepository = diaryEntryRepository;

        // SSL 검증 무시 설정
        var sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        var httpClient = HttpClient.create()
                .secure(ssl -> ssl.sslContext(sslContext));

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public String createBook(Long pregnancyId) {
        Pregnancy pregnancy = pregnancyRepository.findById(pregnancyId)
                .orElseThrow(() -> new RuntimeException("임신 정보를 찾을 수 없습니다."));

        Map<String, Object> body = Map.of(
                "title", pregnancy.getBabyNickname() + "의 태교일기",
                "bookSpecUid", "SQUAREBOOK_HC",
                "bookAuthor", "엄마",
                "externalRef", "pregnancy-" + pregnancyId
        );

        Map response = webClient.post()
                .uri("/books")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> data = (Map<String, Object>) response.get("data");
        String bookUid =  (String) data.get("bookUid");

        //커버 추가
        Map<String, Object> coverParams = new HashMap<>();
        coverParams.put("title", pregnancy.getBabyNickname() + "의 태교일기");
        coverParams.put("coverPhoto", "https://images.unsplash.com/photo-1492725764893-90b379c2b6e7?w=800");
        coverParams.put("dateRange",
                pregnancy.getStartDate().format(DateTimeFormatter.ofPattern("yy.MM"))
                +"-"+
                pregnancy.getDueDate().format(DateTimeFormatter.ofPattern("yy.MM")));

        MultiValueMap<String, Object> coverFormData = new LinkedMultiValueMap<>();
        coverFormData.add("templateUid", "79yjMH3qRPly");
        coverFormData.add("parameters", new ObjectMapper().writeValueAsString(coverParams));

        webClient.post()
                .uri("/books/" + bookUid + "/cover")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(coverFormData)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .doOnNext(err -> System.out.println("❌ cover 에러: " + err))
                                .map(err -> new RuntimeException("cover API 에러: " + err))
                )
                .bodyToMono(Map.class)
                .block();

        //일기내용 추가
        List<DiaryEntry> entries = diaryEntryRepository.findByPregnancyIdOrderByEntryDateAsc(pregnancyId);
        for(DiaryEntry entry:entries){
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("date", entry.getEntryDate().format(DateTimeFormatter.ofPattern("M.d")));
            parameters.put("title", entry.getTitle() != null ? entry.getTitle() : "");
            parameters.put("diaryText", entry.getContent() != null ? entry.getContent() : "");

            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("templateUid", "vHA59XPPKqak");
            formData.add("parameters", new ObjectMapper().writeValueAsString(parameters));

            webClient.post()
                    .uri("/books/" + bookUid + "/contents?breakBefore=page")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(formData)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .doOnNext(err -> System.out.println("❌ content 에러: " + err))
                                    .map(err -> new RuntimeException("content API 에러: " + err))
                    )
                    .bodyToMono(Map.class)
                    .block();
        }
        webClient.post()
                .uri("/books/" + bookUid + "/finalization")
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .doOnNext(errorBody -> System.out.println("❌ 400 에러 상세: " + errorBody))
                                .map(errorBody -> new RuntimeException("book API 400 에러: " + errorBody))
                )
                .bodyToMono(Map.class)
                .block();

        return bookUid;
    }

    public BookOrderResponseDto createOrder(BookOrderRequestDto dto) {
        Pregnancy pregnancy = pregnancyRepository.findById(dto.getPregnancyId())
                .orElseThrow(() -> new RuntimeException("임신 정보를 찾을 수 없습니다."));

        String bookUid = createBook(dto.getPregnancyId());

        Map<String, Object> shipping = Map.of(
                "recipientName", dto.getRecipientName(),
                "recipientPhone", dto.getRecipientPhone(),
                "postalCode", dto.getZipCode(),
                "address1", dto.getAddress(),
                "address2", dto.getAddressDetail()
        );

        Map<String, Object> body = Map.of(
                "items", List.of(Map.of("bookUid", bookUid, "quantity", 1)),
                "shipping", shipping,
                "externalRef", "pregnancy-order-" + dto.getPregnancyId()
        );

        Map response = webClient.post()
                .uri("/orders")
                .header("Idempotency-Key", UUID.randomUUID().toString())
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .doOnNext(errorBody -> System.out.println("❌ 400 에러 상세: " + errorBody))
                                .map(errorBody -> new RuntimeException("Orders API 400 에러: " + errorBody))
                )
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> data = (Map<String, Object>) response.get("data");
        String orderUid = (String) data.get("orderUid");
        String orderStatus = String.valueOf(data.get("orderStatus"));

        BookOrder bookOrder = BookOrder.builder()
                .pregnancy(pregnancy)
                .sweelbookBookId(bookUid)
                .sweetbookOrderId(orderUid)
                .status(orderStatus)
                .build();

        bookOrderRepository.save(bookOrder);

        return BookOrderResponseDto.builder()
                .id(bookOrder.getId())
                .sweetbookBookId(bookUid)
                .sweetbookOrderId(orderUid)
                .status(orderStatus)
                .createdAt(bookOrder.getCreatedAt())
                .build();
    }

    public List<BookOrderResponseDto> findByPregnancyId(Long pregnancyId) {
        return bookOrderRepository.findByPregnancyId(pregnancyId)
                .stream()
                .map(order -> BookOrderResponseDto.builder()
                        .id(order.getId())
                        .sweetbookBookId(order.getSweelbookBookId())
                        .sweetbookOrderId(order.getSweetbookOrderId())
                        .status(order.getStatus())
                        .createdAt(order.getCreatedAt())
                        .build())
                .toList();
    }
}