import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getPregnancy } from "../api/pregnancy";
import Calendar from "../components/Calendar";
import "../styles/MainPage.css";

function MainPage() {
  const [pregnancy, setPregnancy] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    getPregnancy()
      .then((res) => setPregnancy(res.data))
      .catch((err) => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="loading">로딩 중...</div>;

  return (
    <div className="main-container">
      <header className="main-header">
        <h1>🌸 {pregnancy?.babyNickname}의 태교일기</h1>
        <p className="due-date">예정일: {pregnancy?.dueDate}</p>
      </header>

      <div className="progress-section">
        <p>
          {pregnancy?.writtenDays}일 / {pregnancy?.totalDays}일 기록 완료
        </p>
        <div className="progress-bar">
          <div
            className="progress-fill"
            style={{
              width: `${(pregnancy?.writtenDays / pregnancy?.totalDays) * 100}%`,
            }}
          />
        </div>
      </div>

      <Calendar
        pregnancyId={1}
        startDate={pregnancy?.startDate}
        dueDate={pregnancy?.dueDate}
      />

      <div className="action-buttons">
        <button onClick={() => navigate("/order")} className="order-btn">
          포토북 주문하기
        </button>
      </div>
    </div>
  );
}

export default MainPage;
