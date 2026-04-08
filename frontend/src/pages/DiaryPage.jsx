import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getDiaryList } from "../api/diary";
import "../styles/DiaryPage.css";

function DiaryPage() {
  const [entries, setEntries] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    getDiaryList(1).then((res) => setEntries(res.data));
  }, []);

  return (
    <div className="diary-container">
      <header className="diary-header">
        <button onClick={() => navigate("/")} className="back-btn">
          ← 뒤로
        </button>
        <h2>일기 목록</h2>
      </header>

      <div className="diary-list">
        {entries.length === 0 ? (
          <p className="empty">아직 작성된 일기가 없어요.</p>
        ) : (
          entries.map((entry) => (
            <div
              key={entry.id}
              className="diary-item"
              onClick={() => navigate(`/diary/write/${entry.entryDate}`)}
            >
              <span className="entry-date">{entry.entryDate}</span>
              <span className="entry-title">{entry.title || "제목 없음"}</span>
              <span className="entry-preview">
                {entry.content?.substring(0, 30)}...
              </span>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default DiaryPage;
