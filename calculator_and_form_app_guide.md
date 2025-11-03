# Hướng dẫn tạo ứng dụng Calculator và Form nhập liệu trong Android Studio

Tài liệu này hướng dẫn chi tiết từng bước để bạn có thể xây dựng hai ứng dụng Android đơn giản theo yêu cầu bài tập: một ứng dụng **Calculator** với các phép tính số nguyên và một ứng dụng **Form đăng ký** với CalendarView.

## 1. Chuẩn bị môi trường
1. Cài đặt **Android Studio** (phiên bản mới nhất đề xuất Giraffe hoặc Hedgehog).
2. Cài đặt **Android SDK** và **Android Emulator** hoặc chuẩn bị thiết bị Android để chạy thử.
3. Khởi động Android Studio và đăng nhập tài khoản nếu cần.

---

## 2. Bài 1 – Xây dựng ứng dụng Calculator số nguyên

### 2.1. Tạo project
1. Chọn **New Project → Empty Views Activity**.
2. Đặt tên project: `IntegerCalculator`.
3. Ngôn ngữ: **Kotlin** hoặc **Java** (ví dụ bên dưới dùng Kotlin).
4. Minimum SDK: **API 21 (Android 5.0)**.
5. Nhấn **Finish** và chờ Android Studio tạo project.

### 2.2. Thiết kế giao diện (activity_main.xml)
1. Mở `app/src/main/res/layout/activity_main.xml` ở chế độ **Design**.
2. Đổi **root layout** thành `LinearLayout` (vertical) hoặc `ConstraintLayout` tùy bạn.
3. Thêm `TextView` hiển thị kết quả ở phía trên, đặt `id="tvResult"`, cỡ chữ 48sp, căn phải, `text="0"`.
4. Thêm một `GridLayout` hoặc nhiều `LinearLayout` con để bố trí các nút như trong hình mẫu (4 cột x 5 hàng).
5. Với mỗi nút dùng `Button`, đặt `layout_width="0dp"`, `layout_height="wrap_content"`, `layout_columnWeight="1"` để chia đều (nếu dùng `GridLayout` thì dùng `android:layout_columnWeight`).
6. Thiết lập `text` cho từng nút: `CE`, `C`, `BS`, `/`, `7`, `8`, `9`, `x`, ... tới `+/-`, `0`, `.` (có thể bỏ dấu `.` nếu chỉ cần số nguyên).
7. Đặt `id` rõ ràng: ví dụ `btnCE`, `btnClear`, `btnBackspace`, `btnDivide`, ...
8. Dùng thuộc tính `android:onClick` hoặc `setOnClickListener` trong Activity để bắt sự kiện.

### 2.3. Xử lý sự kiện trong MainActivity.kt
1. Khai báo các biến để lưu toán hạng hiện tại (`currentValue`), toán hạng trước (`previousValue`), phép toán (`pendingOperator`) và cờ đánh dấu trạng thái nhập.
2. Khi người dùng nhấn các nút số:
   - Nếu đang nhập số mới sau khi chọn phép toán, reset `currentValue`.
   - Nối chữ số vào `currentValue` (dạng chuỗi) và cập nhật TextView.
3. Khi nhấn nút phép toán (`+`, `-`, `x`, `/`):
   - Ép `currentValue` sang `Long` hoặc `Int`.
   - Nếu đã có `pendingOperator`, thực hiện phép toán với `previousValue` và `currentValue` rồi hiển thị kết quả.
   - Lưu giá trị vừa tính vào `previousValue`, đặt `pendingOperator` là phép toán vừa chọn.
4. Khi nhấn `=`: tính kết quả dựa trên `pendingOperator`, cập nhật TextView và reset trạng thái để tiếp tục phép toán mới.
5. **Nút `BS` (Backspace):** Xóa chữ số cuối cùng của `currentValue`. Nếu chỉ còn 1 chữ số, trả về `0`.
6. **Nút `CE` (Clear Entry):** Đặt `currentValue = 0` và cập nhật TextView, không ảnh hưởng phép toán hiện tại.
7. **Nút `C`:** Reset toàn bộ (`previousValue = 0`, `currentValue = 0`, `pendingOperator = null`).
8. **Nút `+/-`:** Đổi dấu `currentValue` (nhân với `-1`).
9. Đảm bảo phép chia xử lý trường hợp chia cho 0 (hiển thị thông báo và reset).

### 2.4. Thử nghiệm
1. Kết nối emulator hoặc thiết bị thật.
2. Chạy app bằng nút **Run** (Shift + F10).
3. Kiểm tra từng chức năng: nhập số, cộng/trừ/nhân/chia, `CE`, `C`, `BS`, đổi dấu.
4. So sánh hành vi với Calculator của Windows để đảm bảo tương đương.

---

## 3. Bài 2 – Xây dựng ứng dụng Form nhập dữ liệu với CalendarView

### 3.1. Tạo project mới hoặc module
1. Tương tự bài 1, tạo project **Empty Views Activity** tên `RegistrationForm` (ngôn ngữ Kotlin).
2. Minimum SDK: API 21.

### 3.2. Thiết kế giao diện
1. Mở `activity_main.xml` và chuyển root layout sang `ScrollView` chứa `LinearLayout` để tránh tràn màn hình.
2. Thêm `TextInputLayout` + `TextInputEditText` (thuộc Material Components) hoặc `EditText` thường cho các trường: First Name, Last Name, Address, Email.
3. Thêm `RadioGroup` chứa hai `RadioButton` cho giới tính: Male, Female.
4. Phần Birthday:
   - Thêm `TextInputLayout` chứa `EditText` không cho nhập trực tiếp (`focusable=false`, `clickable=false`).
   - Thêm `Button` hoặc `MaterialButton` nhãn "Select" ngay cạnh.
   - Thêm `CalendarView` đặt `visibility="gone"`, đặt dưới phần Birthday.
5. Thêm `CheckBox` cho "I agree to Terms of Use".
6. Thêm `Button` "Register" ở cuối.
7. Dùng `ConstraintLayout` hoặc `LinearLayout` để căn chỉnh giống ảnh minh họa.

### 3.3. Lập trình trong MainActivity.kt
1. Dùng **View Binding** hoặc `findViewById` để tham chiếu tới các view.
2. Đặt sự kiện cho nút **Select**:
   - Toggle trạng thái hiện/ẩn của `CalendarView` (dùng `isVisible` nếu dùng KTX).
   - Khi hiện `CalendarView`, người dùng chọn ngày → lắng nghe qua `setOnDateChangeListener`.
3. Trong listener của `CalendarView`, lấy `year`, `month`, `dayOfMonth`, định dạng (ví dụ `SimpleDateFormat("dd/MM/yyyy")`) và gán chuỗi vào EditText ngày sinh.
4. Khi người dùng chọn xong có thể tự động ẩn CalendarView hoặc chờ họ bấm lại nút Select.
5. Sự kiện nút **Register**:
   - Tạo danh sách các trường cần kiểm tra (EditText, RadioGroup, CheckBox).
   - Reset màu nền về mặc định trước khi kiểm tra (`setBackgroundResource(android.R.color.transparent)` hoặc style gốc).
   - Kiểm tra rỗng: `text.isNullOrBlank()` cho EditText, `checkedRadioButtonId == -1` cho RadioGroup, `isChecked` cho CheckBox.
   - Nếu trường rỗng, dùng `setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light))` hoặc tạo style `Color.RED`.
   - Có thể hiển thị `Toast` thông báo người dùng điền đủ thông tin.
6. Nếu tất cả hợp lệ, hiển thị `Toast` "Register success" hoặc điều hướng sang màn hình khác.

### 3.4. Lưu ý UI/UX
- Dùng `android:inputType` phù hợp (textPersonName, textEmailAddress).
- Dùng `TextInputLayout` để có hint đẹp.
- Thêm padding, margin theo Material Design (`16dp`).
- CalendarView mặc định chiếm nhiều diện tích, nên đặt trong `CardView` hoặc `FrameLayout` để dễ ẩn/hiện.

### 3.5. Kiểm thử
1. Chạy app, thử nhấn Select để mở CalendarView, chọn ngày → kiểm tra EditText cập nhật đúng.
2. Nhấn Register khi thiếu thông tin → các trường thiếu sẽ đổi nền màu đỏ.
3. Điền đầy đủ thông tin, tích checkbox, nhấn Register → hiển thị thông báo thành công.

---

## 4. Mẹo và tài liệu tham khảo
- [Hướng dẫn Android Calculator (developer.android.com)](https://developer.android.com/guide/topics/ui).
- [Material Components](https://m3.material.io/develop/android)
- Sử dụng `ViewModel` và `SavedStateHandle` nếu muốn bảo toàn dữ liệu khi xoay màn hình.

Chúc bạn hoàn thành tốt hai bài tập!
