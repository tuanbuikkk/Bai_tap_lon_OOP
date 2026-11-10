package com.example.englishapp.data.local;

import com.example.englishapp.data.local.entity.StudyMaterial;

import java.util.ArrayList;
import java.util.List;


public class MaterialSeedData {
    public static List<StudyMaterial> create() {
        long now = System.currentTimeMillis();
        List<StudyMaterial> list = new ArrayList<>();

        list.add(make("Mẹo làm Part 1: Mô tả tranh",
                "Cách quan sát tranh và tránh các bẫy thường gặp.",
                StudyMaterial.TYPE_TEXT,
                "1. Xem tranh trước khi nghe: xác định người, vật và hành động chính.\n\n"
                        + "2. Hành động đang diễn ra thường dùng thì hiện tại tiếp diễn: "
                        + "\"A man is writing on a whiteboard.\"\n\n"
                        + "3. Trạng thái của đồ vật thường dùng hiện tại hoàn thành bị động hoặc "
                        + "\"There is/are...\": \"Some boxes have been stacked on the floor.\"\n\n"
                        + "4. Bẫy hay gặp: đáp án có từ phát âm gần giống nhưng sai nghĩa, hoặc nhắc "
                        + "đến vật, hành động không có trong tranh. Hãy loại các đáp án này.",
                "Part 1", "450+", now - 1000));

        list.add(make("Mẹo làm Part 2: Hỏi - Đáp",
                "Nhận biết loại câu hỏi từ từ đầu tiên.",
                StudyMaterial.TYPE_TEXT,
                "1. Nghe kỹ từ để hỏi đầu câu: Who, What, When, Where, Why, How.\n\n"
                        + "2. Where trả lời bằng địa điểm, When bằng thời gian, Who bằng người hoặc "
                        + "tên bộ phận.\n\n"
                        + "3. Câu hỏi Yes/No thường có đáp án gián tiếp, ví dụ: "
                        + "\"Did you finish the report?\" - \"I'm still working on it.\"\n\n"
                        + "4. Bẫy hay gặp: đáp án lặp lại từ trong câu hỏi nhưng khác nghĩa, "
                        + "hoặc trả lời sai loại câu hỏi.",
                "Part 2", "450+", now - 2000));

        list.add(make("Ngữ pháp Part 5: Vị trí của từ loại",
                "Đoán nhanh loại từ cần điền dựa vào vị trí.",
                StudyMaterial.TYPE_TEXT,
                "- Danh từ: sau mạo từ (a/an/the), sau tính từ sở hữu (my, their), "
                        + "sau tính từ. Ví dụ: the decision, a new policy.\n\n"
                        + "- Tính từ: đứng trước danh từ hoặc sau động từ to be/become/seem. "
                        + "Ví dụ: an effective plan, The plan is effective.\n\n"
                        + "- Trạng từ: bổ nghĩa cho động từ, tính từ hoặc cả câu. "
                        + "Ví dụ: She works carefully.\n\n"
                        + "- Động từ: sau chủ ngữ, hoặc sau trợ động từ/modal (will, can, must).\n\n"
                        + "Mẹo: xác định loại từ cần điền trước, rồi mới nhìn các đáp án.",
                "Ngữ pháp", "650+", now - 3000));

        list.add(make("Ngữ pháp: Quá khứ đơn và hiện tại hoàn thành",
                "Phân biệt hai thì hay gặp nhất trong Part 5.",
                StudyMaterial.TYPE_TEXT,
                "Quá khứ đơn (V-ed): hành động đã xảy ra và kết thúc tại thời điểm xác định "
                        + "trong quá khứ. Dấu hiệu: yesterday, last week, ago, in 2020.\n"
                        + "Ví dụ: We signed the contract last month.\n\n"
                        + "Hiện tại hoàn thành (have/has + V3): hành động bắt đầu trong quá khứ và "
                        + "còn liên quan đến hiện tại. Dấu hiệu: since, for, already, yet, recently.\n"
                        + "Ví dụ: We have worked here since 2019.",
                "Ngữ pháp", "650+", now - 4000));

        list.add(make("Từ vựng văn phòng thường gặp",
                "Các từ hay xuất hiện trong bài thi TOEIC.",
                StudyMaterial.TYPE_TEXT,
                "- agenda: chương trình nghị sự\n"
                        + "- deadline: hạn chót\n"
                        + "- invoice: hóa đơn\n"
                        + "- colleague: đồng nghiệp\n"
                        + "- reimburse: hoàn tiền (chi phí)\n"
                        + "- candidate: ứng viên\n"
                        + "- vacancy: vị trí đang tuyển\n"
                        + "- negotiate: đàm phán\n"
                        + "- shipment: lô hàng\n"
                        + "- warranty: bảo hành",
                "Từ vựng", "450+", now - 5000));

        list.add(make("Chiến lược Part 7: Đọc hiểu",
                "Cách đọc nhanh để kịp thời gian.",
                StudyMaterial.TYPE_TEXT,
                "1. Đọc câu hỏi trước để biết cần tìm thông tin gì.\n\n"
                        + "2. Lướt (skim) tiêu đề, dòng đầu mỗi đoạn để nắm ý chính.\n\n"
                        + "3. Quét (scan) tìm từ khóa, con số, tên riêng liên quan đến câu hỏi.\n\n"
                        + "4. Với bài có nhiều văn bản, câu hỏi \"kết nối\" cần thông tin từ cả "
                        + "hai văn bản, hãy gạch chân chi tiết ở từng bài.\n\n"
                        + "5. Không dừng quá lâu ở một câu, hãy đánh dấu và quay lại sau.",
                "Part 7", "800+", now - 6000));

        list.add(make("Trang chính thức của kỳ thi TOEIC (ETS)",
                "Thông tin cấu trúc đề thi và cách tính điểm.",
                StudyMaterial.TYPE_LINK,
                "https://www.ets.org/toeic.html",
                "Part 1", "450+", now - 7000));

        list.add(make("Video luyện nghe Part 3 trên YouTube",
                "Kết quả tìm kiếm các video hướng dẫn làm Part 3.",
                StudyMaterial.TYPE_LINK,
                "https://www.youtube.com/results?search_query=TOEIC+Part+3+tips",
                "Part 3", "650+", now - 8000));

        list.add(make("Bài tập ngữ pháp của British Council",
                "Tổng hợp bài học và bài tập ngữ pháp tiếng Anh.",
                StudyMaterial.TYPE_LINK,
                "https://learnenglish.britishcouncil.org/grammar",
                "Ngữ pháp", "650+", now - 9000));

        return list;
    }

    private static StudyMaterial make(String title, String description, String type,
                                      String content, String category, String level,
                                      long createdAt) {
        StudyMaterial m = new StudyMaterial();
        m.setTitle(title);
        m.setDescription(description);
        m.setType(type);
        m.setContent(content);
        m.setCategory(category);
        m.setLevel(level);
        m.setCreatedAt(createdAt);
        return m;
    }
}
