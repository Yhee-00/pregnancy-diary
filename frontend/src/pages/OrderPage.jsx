import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createOrder } from "../api/book";
import "../styles/OrderPage.css";

function OrderPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    recipientName: "",
    recipientPhone: "",
    address: "",
    addressDetail: "",
    zipCode: "",
  });
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleOrder = async () => {
    if (
      !form.recipientName ||
      !form.recipientPhone ||
      !form.address ||
      !form.zipCode
    ) {
      alert("필수 항목을 모두 입력해주세요.");
      return;
    }
    setLoading(true);
    try {
      const res = await createOrder({ ...form, pregnancyId: 1 });
      setResult(res.data);
    } catch (err) {
      console.error(err);
      alert("주문에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  if (result) {
    return (
      <div className="order-container">
        <div className="order-success">
          <h2>🎉 주문이 완료됐어요!</h2>
          <p>주문번호: {result.sweetbookOrderId}</p>
          <p>상태: 결제완료</p>
          <button onClick={() => navigate("/")}>홈으로 돌아가기</button>
        </div>
      </div>
    );
  }

  return (
    <div className="order-container">
      <header className="order-header">
        <button onClick={() => navigate(-1)} className="back-btn">
          ← 뒤로
        </button>
        <h2>포토북 주문</h2>
      </header>

      <div className="order-form">
        <h3>배송 정보</h3>
        <input
          name="recipientName"
          placeholder="받는 분 이름"
          value={form.recipientName}
          onChange={handleChange}
        />
        <input
          name="recipientPhone"
          placeholder="연락처 (010-1234-5678)"
          value={form.recipientPhone}
          onChange={handleChange}
        />
        <input
          name="zipCode"
          placeholder="우편번호"
          value={form.zipCode}
          onChange={handleChange}
        />
        <input
          name="address"
          placeholder="주소"
          value={form.address}
          onChange={handleChange}
        />
        <input
          name="addressDetail"
          placeholder="상세주소"
          value={form.addressDetail}
          onChange={handleChange}
        />

        <button
          onClick={handleOrder}
          className="order-submit-btn"
          disabled={loading}
        >
          {loading ? "주문 처리 중... (1~2분 소요)" : "📚 포토북 주문하기"}
        </button>
      </div>
    </div>
  );
}

export default OrderPage;
