import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getDiaryList } from "../api/diary";
import dayjs from "dayjs";
import "../styles/Calendar.css";

function Calendar({ pregnancyId, startDate, dueDate }) {
  const [currentMonth, setCurrentMonth] = useState(dayjs());
  const [writtenDates, setWrittenDates] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    if (pregnancyId) {
      getDiaryList(pregnancyId).then((res) => {
        const dates = res.data.map((e) => e.entryDate);
        setWrittenDates(dates);
      });
    }
  }, [pregnancyId]);

  useEffect(() => {
    if (startDate) {
      setCurrentMonth(dayjs(startDate));
    }
  }, [startDate]);

  const startOfMonth = currentMonth.startOf("month");
  const endOfMonth = currentMonth.endOf("month");
  const startDay = startOfMonth.day();
  console.log(
    "시작 요일:",
    startDay,
    "날짜:",
    startOfMonth.format("YYYY-MM-DD"),
  );
  const daysInMonth = currentMonth.daysInMonth();

  const isInPregnancyRange = (date) => {
    if (!startDate || !dueDate) return false;
    return date >= startDate && date <= dueDate;
  };

  const handleDayClick = (date) => {
    if (!isInPregnancyRange(date)) return;
    navigate(`/diary/${date}`);
  };

  const renderDays = () => {
    const days = [];

    // 빈 칸 채우기
    for (let i = 0; i < startDay; i++) {
      days.push(<div key={`empty-${i}`} className="calendar-cell empty" />);
    }

    for (let d = 1; d <= daysInMonth; d++) {
      const date = currentMonth.date(d).format("YYYY-MM-DD");
      const isWritten = writtenDates.includes(date);
      const inRange = isInPregnancyRange(date);
      const isToday = date === dayjs().format("YYYY-MM-DD");

      days.push(
        <div
          key={date}
          className={`calendar-cell
            ${inRange ? "in-range" : "out-range"}
            ${isWritten ? "written" : ""}
            ${isToday ? "today" : ""}
          `}
          onClick={() => handleDayClick(date)}
        >
          <span className="day-number">{d}</span>
          {isWritten && <span className="written-dot">●</span>}
        </div>,
      );
    }

    const TOTAL_CELLS = 42;
    const remainingCells = TOTAL_CELLS - days.length;

    for (let i = 0; i < remainingCells; i++) {
      days.push(<div key={`end-empty-${i}`} className="calendar-cell empty" />);
    }

    return days;
  };

  return (
    <div className="calendar-container">
      <div className="calendar-nav">
        <button
          onClick={() => setCurrentMonth(currentMonth.subtract(1, "month"))}
        >
          ◀
        </button>
        <span>{currentMonth.format("YYYY년 MM월")}</span>
        <button onClick={() => setCurrentMonth(currentMonth.add(1, "month"))}>
          ▶
        </button>
      </div>

      <div className="calendar-weekdays">
        {["일", "월", "화", "수", "목", "금", "토"].map((day) => (
          <div key={day} className="weekday">
            {day}
          </div>
        ))}
      </div>

      <div className="calendar-grid">{renderDays()}</div>
    </div>
  );
}

export default Calendar;
