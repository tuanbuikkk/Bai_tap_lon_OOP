Tổng hợp chức năng 22 file (Người 3: Flashcard và Streak)
Luồng đi của dữ liệu

Fragment → ViewModel → Service → Repository → DAO → Room (database)

Mỗi lớp chỉ nói chuyện với lớp ngay bên dưới nó. Ví dụ Fragment không bao giờ gọi DAO.

1.Lớp dữ liệu: data/local/entity/ (3 bảng)
-Flashcard.java: bảng flashcards, một bộ thẻ của một người dùng. Có id, userId, name, description, createdAt.
-FlashcardItem.java: bảng flashcard_items, một từ nằm trong một bộ thẻ. Có flashcardId, vocabularyId, word, meaning, learned. Xóa bộ thẻ thì các thẻ của nó tự bị xóa. Một từ chỉ xuất hiện một lần trong một bộ thẻ.
-DailyActivity.java: bảng daily_activities, ghi số thẻ đã học trong một ngày. Có userId, date (dạng yyyy-MM-dd), cardsStudied. Mỗi người dùng chỉ có một dòng cho mỗi ngày.
2.Truy vấn: data/local/dao/ (3 file)
-FlashcardDao.java: thêm, xóa bộ thẻ, và lấy các bộ thẻ của một người dùng (mới nhất trước), trả về LiveData.
-FlashcardItemDao.java: thêm từ (nếu trùng thì Room trả -1 thay vì báo lỗi), xóa thẻ, lấy các thẻ của một bộ (xếp theo id để thứ tự không đổi), đánh dấu đã thuộc hoặc chưa thuộc.
-DailyActivityDao.java: lấy lịch sử học theo ngày, và hàm record trong một giao dịch: tạo dòng của ngày đó nếu chưa có, rồi cộng số thẻ bằng SQL.
3.Repository: repository/ (6 file)

Mỗi cặp gồm một interface (danh sách việc được làm) và một Impl (làm bằng cách gọi DAO). Lớp này chỉ chuyển tiếp, chưa có quy tắc nghiệp vụ.

-FlashcardRepository / FlashcardRepositoryImpl: lấy, thêm, xóa bộ thẻ.
-FlashcardItemRepository / FlashcardItemRepositoryImpl: lấy, thêm, xóa thẻ và cập nhật learned. Hàm add trả true nếu thêm được, false nếu từ đã có.
-DailyActivityRepository / DailyActivityRepositoryImpl: lấy lịch sử học và ghi nhận số thẻ học trong ngày.
4.Service: service/ (2 file, nơi giữ quy tắc)
-FlashcardService.java: lấy DAO từ AppDatabase, dựng các repository, cắt khoảng trắng đầu cuối, từ chối tên bộ thẻ rỗng. addVocabulary trả false khi từ đã có trong bộ thẻ.
-StreakService.java: chứa toàn bộ cách tính streak. dateKey tạo chuỗi ngày chuẩn, recordStudy ghi một lần học vào ngày hôm nay, calculateCurrentStreak tính chuỗi hiện tại, calculateLongestStreak tính chuỗi dài nhất. Đây là nơi duy nhất tính streak.
5.Giao diện: feature/flashcard/ (5 file)
-FlashcardViewModel.java: giữ trạng thái của cả ba màn flashcard. Gồm người dùng hiện tại, danh sách bộ thẻ, danh sách thẻ, thẻ đang học (index), thẻ đang lật hay chưa. Nó kiểm tra tên bộ thẻ, chạy các thao tác ghi trên luồng nền. Khi người dùng trả lời một thẻ thì đánh dấu đã thuộc rồi gọi StreakService.recordStudy.
-FlashcardAdapter.java: hiển thị mỗi bộ thẻ thành một dòng trong danh sách. Báo cho Fragment khi bấm vào dòng hoặc bấm nút xóa.
-FlashcardListFragment.java: màn danh sách bộ thẻ. Bấm một bộ thì sang màn học, bấm nút + thì sang màn tạo, hiện dòng “chưa có bộ thẻ” khi trống.
-FlashcardCreateFragment.java: màn tạo bộ thẻ với tên và mô tả. Lưu xong thì quay lại danh sách, lỗi thì hiện thông báo.
-FlashcardStudyFragment.java: màn học. Hiện mặt trước của thẻ, chạm để lật sang nghĩa, rồi chọn “Đã thuộc” hoặc “Chưa thuộc”. Có đủ trạng thái đang tải, bộ thẻ rỗng và học xong.
6.Giao diện: feature/streak/ (3 file)
-StreakViewModel.java: cung cấp streak hiện tại, streak dài nhất, các ngày đã học và tháng đang xem. Tính lại streak khi qua ngày mới, và không cho lịch đi vào tương lai.
-CalendarAdapter.java: vẽ lưới lịch một tháng bắt đầu từ thứ Hai. Ngày đã học tô cam, hôm nay có viền xanh, ngày tương lai mờ.
-StreakFragment.java: màn Streak. Hiện hai con số streak, tên tháng, nút tháng trước và tháng sau, rồi gắn lịch vào ViewModel.
