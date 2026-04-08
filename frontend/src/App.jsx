import { Routes, Route } from "react-router-dom";
import MainPage from "./pages/MainPage";
import DiaryPage from "./pages/DiaryPage";
import DiaryDetailPage from "./pages/DiaryDetailPage";
import DiaryWritePage from "./pages/DiaryWritePage";
import OrderPage from "./pages/OrderPage";

function App() {
  return (
    <Routes>
      <Route path="/" element={<MainPage />} />
      <Route path="/diary" element={<DiaryPage />} />
      <Route path="/diary/:date" element={<DiaryDetailPage />} />
      <Route path="/diary/write/:date" element={<DiaryWritePage />} />
      <Route path="/order" element={<OrderPage />} />
    </Routes>
  );
}

export default App;
