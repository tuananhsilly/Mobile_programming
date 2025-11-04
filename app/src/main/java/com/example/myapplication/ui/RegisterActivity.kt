package com.example.myapplication.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.view.isVisible
import android.content.Context
import android.view.inputmethod.InputMethodManager

class RegisterActivity : AppCompatActivity() {

    private lateinit var etFirst: EditText
    private lateinit var etLast: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var etBirthday: EditText
    private lateinit var calendar: CalendarView
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var cbAgree: CheckBox
    private lateinit var btnSelect: Button
    private lateinit var btnRegister: Button
    private lateinit var scrollRoot: ScrollView


    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etFirst = findViewById(R.id.etFirstName)
        etLast = findViewById(R.id.etLastName)
        rgGender = findViewById(R.id.rgGender)
        etBirthday = findViewById(R.id.etBirthday)
        calendar = findViewById(R.id.calendarView)
        etAddress = findViewById(R.id.etAddress)
        etEmail = findViewById(R.id.etEmail)
        cbAgree = findViewById(R.id.cbAgree)
        btnSelect = findViewById(R.id.btnSelectDate)
        btnRegister = findViewById(R.id.btnRegister)
        scrollRoot = findViewById(R.id.scrollRoot)


        // Toggle CalendarView


                btnSelect.setOnClickListener {
                    // Ẩn bàn phím nếu đang mở
                        currentFocus?.let { v ->
                            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(v.windowToken, 0)
                            v.clearFocus()
                        }

                    calendar.isVisible = !calendar.isVisible

                    if (calendar.isVisible) {
                        // Đợi layout xong rồi cuộn tới lịch
                        calendar.post {
                            scrollRoot.smoothScrollTo(0, calendar.top)
                        }
                    }
                }


        // Khi chọn ngày -> cập nhật EditText và ẩn lịch
        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance().apply { set(year, month, dayOfMonth) }
            etBirthday.setText(sdf.format(cal.time))
            calendar.visibility = View.GONE
        }

        btnRegister.setOnClickListener { validateAndSubmit() }
    }

    private fun validateAndSubmit() {
        // reset màu (về mặc định)
        fun clearBg(v: View) { v.setBackgroundColor(Color.TRANSPARENT) }
        listOf(etFirst, etLast, etBirthday, etAddress, etEmail).forEach(::clearBg)

        var ok = true

        fun mark(v: View) { v.setBackgroundColor(0x33FF0000) } // đỏ nhạt

        if (etFirst.text.isNullOrBlank()) { mark(etFirst); ok = false }
        if (etLast.text.isNullOrBlank())  { mark(etLast);  ok = false }
        if (rgGender.checkedRadioButtonId == -1) { // chưa chọn
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show()
            ok = false
        }
        if (etBirthday.text.isNullOrBlank()) { mark(etBirthday); ok = false }
        if (etAddress.text.isNullOrBlank())  { mark(etAddress); ok = false }
        val email = etEmail.text.toString()
        val emailOk = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (!emailOk) { mark(etEmail); ok = false }
        if (!cbAgree.isChecked) { Toast.makeText(this, "Please agree to Terms", Toast.LENGTH_SHORT).show(); ok = false }

        if (ok) {
            Toast.makeText(this, "Register Success!", Toast.LENGTH_LONG).show()
            // TODO: xử lý submit thật (gửi server, lưu DB…)
        }
    }
}
