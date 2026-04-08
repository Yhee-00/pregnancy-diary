import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { createDiaryEntry, updateDiaryEntry, getDiaryList } from "../api/diary";
import { uploadPhoto } from "../api/photo";
import "../styles/DiaryWritePage.css";

function DiaryWritePage() {
  const { date } = useParams();
  const navigate = useNavigate();
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [entryId, setEntryId] = useState(null);
  const [existingPhotoUrls, setExistingPhotoUrls] = useState([]);
  const [newPhotos, setNewPhotos] = useState([]);
  const [newPhotoUrls, setNewPhotoUrls] = useState([]);
  const [saving, setSaving] = useState(false);
  const [deletedPhotos, setDeletedPhotos] = useState([]);

  useEffect(() => {
    if (date) {
      getDiaryList(1).then((res) => {
        const existing = res.data.find((e) => e.entryDate === date);
        if (existing) {
          setTitle(existing.title || "");
          setContent(existing.content || "");
          setEntryId(existing.id);
          setExistingPhotoUrls(
            (existing.photoUrls || []).map(
              (url) => `http://localhost:8080${url}`,
            ),
          );
        }
      });
    }
  }, [date]);

  const handlePhotoChange = (e) => {
    const files = Array.from(e.target.files);
    setNewPhotos((prev) => [...prev, ...files]);
    const urls = files.map((f) => URL.createObjectURL(f));
    setNewPhotoUrls((prev) => [...prev, ...urls]);
  };

  const handleRemoveNewPhoto = (idx) => {
    setNewPhotos((prev) => prev.filter((_, i) => i !== idx));
    setNewPhotoUrls((prev) => prev.filter((_, i) => i !== idx));
  };

  const handleRemoveExistingPhoto = (idx) => {
    setDeletedPhotos((prev) => [...prev, existingPhotoUrls[idx]]);
    setExistingPhotoUrls((prev) => prev.filter((_, i) => i !== idx));
  };

  const handleSave = async () => {
    console.log("삭제할 사진들:", deletedPhotos);
    if (!title.trim() && !content.trim()) return;
    setSaving(true);
    try {
      const data = {
        entryDate: date || new Date().toISOString().split("T")[0],
        title,
        content,
        deletedPhotoUrls: deletedPhotos,
      };

      let savedEntryId = entryId;
      if (entryId) {
        await updateDiaryEntry(entryId, data);
      } else {
        const res = await createDiaryEntry(1, data);
        savedEntryId = res.data.id;
      }

      // 사진 업로드
      for (const photo of newPhotos) {
        await uploadPhoto(savedEntryId, photo);
      }

      alert("✅ 일기가 저장됐어요!");
      navigate(`/diary/${date}`, { replace: true });
    } catch (err) {
      console.error(err);
      alert("저장에 실패했습니다.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="write-container">
      <header className="write-header">
        <button onClick={() => navigate(-1)} className="back-btn">
          ← 뒤로
        </button>
        <div className="write-date">{date.replace(/-/g, ". ")}</div>
        <button onClick={handleSave} className="save-btn" disabled={saving}>
          {saving ? "저장 중..." : "저장"}
        </button>
      </header>

      <div className="write-body">
        <input
          className="title-input"
          placeholder="제목을 입력하세요"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />

        <textarea
          className="content-input"
          placeholder="오늘 아기에게 하고 싶은 이야기를 적어보세요..."
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />

        {/* 기존 사진 */}
        {existingPhotoUrls.length > 0 && (
          <div className="photo-preview-list">
            {existingPhotoUrls.map((url, idx) => (
              <div key={`existing-${idx}`} className="photo-preview-wrap">
                <img src={url} className="photo-preview" />
                <button
                  className="photo-remove-btn"
                  onClick={() => handleRemoveExistingPhoto(idx)}
                >
                  ✕
                </button>
              </div>
            ))}
          </div>
        )}

        {/* 새 사진 */}
        {newPhotoUrls.length > 0 && (
          <div className="photo-preview-list">
            {newPhotoUrls.map((url, idx) => (
              <div key={`new-${idx}`} className="photo-preview-wrap">
                <img src={url} className="photo-preview" />
                <button
                  className="photo-remove-btn"
                  onClick={() => handleRemoveNewPhoto(idx)}
                >
                  ✕
                </button>
              </div>
            ))}
          </div>
        )}

        {/* 업로드 */}
        <label className="photo-upload-btn">
          📷 사진 추가
          <input
            type="file"
            accept="image/*"
            multiple
            onChange={handlePhotoChange}
            style={{ display: "none" }}
          />
        </label>
      </div>
    </div>
  );
}

export default DiaryWritePage;
