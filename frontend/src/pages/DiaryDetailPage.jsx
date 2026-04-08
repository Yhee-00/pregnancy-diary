import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getDiaryList, deleteDiaryEntry } from "../api/diary";
import { getAiMessage } from "../api/ai";
import "../styles/DiaryDetailPage.css";
import { Sparkles } from "lucide-react";

function DiaryDetailPage() {
  const { date } = useParams();
  const navigate = useNavigate();
  const [entry, setEntry] = useState(null);
  const [loading, setLoading] = useState(true);
  const [aiMessage, setAiMessage] = useState("");

  useEffect(() => {
    getDiaryList(1)
      .then((res) => {
        const found = res.data.find((e) => e.entryDate === date);
        setEntry(found || null);
      })
      .finally(() => setLoading(false));
  }, [date]);

  useEffect(() => {
    if (!date) return;

    getAiMessage(1, date).then((res) => {
      setAiMessage(res.data);
    });
  }, [date]);

  const handleDelete = async () => {
    if (!window.confirm("일기를 삭제할까요?")) return;
    await deleteDiaryEntry(entry.id);
    navigate("/");
  };

  if (loading) return <div className="loading">로딩 중...</div>;

  // 일기가 없으면 바로 작성 화면으로
  if (!entry) {
    return (
      <div className="detail-container">
        <header className="detail-header">
          <button onClick={() => navigate(-1)} className="back-btn">
            ← 뒤로
          </button>
          <div className="detail-date">{date.replace(/-/g, ". ")}</div>
          <button
            onClick={() => navigate(`/diary/write/${date}`)}
            className="edit-btn"
          >
            작성
          </button>
        </header>
        <div className="detail-empty">
          <p>🌸 아직 기록이 없어요</p>
          <button
            onClick={() => navigate(`/diary/write/${date}`)}
            className="write-btn"
          >
            일기 쓰기
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="detail-container">
      <header className="detail-header">
        <button onClick={() => navigate(-1)} className="back-btn">
          ← 뒤로
        </button>
        <div className="detail-date">{date.replace(/-/g, ". ")}</div>
        <div className="header-actions">
          <button
            onClick={() => navigate(`/diary/write/${date}`)}
            className="edit-btn"
          >
            수정
          </button>
          <button onClick={handleDelete} className="delete-btn">
            삭제
          </button>
        </div>
      </header>

      <div className="detail-body">
        {entry.title && <h3 className="detail-title">{entry.title}</h3>}
        <div className="ai-banner">
          <div className="ai-text">
            <Sparkles className="ai-icon" />
            오늘의 태교 한마디
          </div>
          <p>{aiMessage}</p>
        </div>
        <div className="divider" />
        <p className="detail-content">{entry.content}</p>

        {/* 사진 */}
        {entry.photoUrls && entry.photoUrls.length > 0 && (
          <div className="photo-slider">
            {entry.photoUrls.map((url, idx) => (
              <img
                key={idx}
                src={`http://localhost:8080${url}`}
                alt={`사진${idx + 1}`}
                className="slider-image"
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default DiaryDetailPage;
