package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

/**
 * Calculator số nguyên: CE, C, BS, +/- , + - x / và =
 * Quy ước:
 *  - Chỉ phép tính số nguyên (Long)
 *  - BS: xóa 1 chữ số cuối của toán hạng đang nhập
 *  - CE: đặt toán hạng đang nhập = 0 (không đụng đến phép toán đang chờ)
 *  - C : xóa toàn bộ ngữ cảnh để nhập phép tính mới
 */
class MainActivity : AppCompatActivity() {

    private lateinit var tv: TextView

    // Trạng thái phép tính
    private var operand1: Long? = null      // toán hạng trước
    private var pendingOp: Char? = null     // phép toán đang chờ: + - x /
    private var isNewEntry = true           // đang bắt đầu nhập số mới?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv = findViewById(R.id.tvResult)
        show("0")
    }

    // ===== Helpers =====
    private fun txt(): String = tv.text.toString()
    private fun cur(): Long = txt().toLongOrNull() ?: 0L
    private fun show(s: String) { tv.text = s }

    private fun appendDigit(d: String) {
        if (isNewEntry || txt() == "0") show(d) else show(txt() + d)
        isNewEntry = false
    }

    private fun calc(a: Long, b: Long, op: Char): Long? = when (op) {
        '+' -> a + b
        '-' -> a - b
        'x' -> a * b
        '/' -> if (b == 0L) null else a / b
        else -> b
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    // ====== XML onClick handlers ======

    fun onDigitClick(v: View) {
        val d = (v as Button).text.toString()
        appendDigit(d)
    }

    fun onDotClick(@Suppress("UNUSED_PARAMETER") v: View) {
        // Bài yêu cầu số nguyên -> không hỗ trợ dấu chấm
        toast("Chỉ hỗ trợ số nguyên")
    }

    fun onOperatorClick(v: View) {
        val op = (v as Button).text.first()   // '+', '-', 'x', '/'
        val current = cur()

        if (operand1 == null) {
            operand1 = current
        } else if (pendingOp != null && !isNewEntry) {
            val res = calc(operand1!!, current, pendingOp!!)
            if (res == null) {
                toast("Không thể chia cho 0")
                onClearAll(v)
                return
            }
            operand1 = res
            show(res.toString())
        }
        pendingOp = op
        isNewEntry = true
    }

    fun onEqualsClick(@Suppress("UNUSED_PARAMETER") v: View) {
        val a = operand1
        val op = pendingOp
        if (a == null || op == null) {
            // không có phép toán đang chờ: giữ nguyên màn hình
            isNewEntry = true
            return
        }
        val b = cur()
        val res = calc(a, b, op)
        if (res == null) {
            toast("Không thể chia cho 0")
            onClearAll(v)
            return
        }
        show(res.toString())
        // reset để bắt đầu phép tính mới
        operand1 = null
        pendingOp = null
        isNewEntry = true
    }

    fun onBackspace(@Suppress("UNUSED_PARAMETER") v: View) {
        // xóa 1 chữ số ở toán hạng hiện tại
        if (isNewEntry) return
        val s = txt()
        if (s.length <= 1) {
            show("0")
            isNewEntry = true
        } else {
            show(s.dropLast(1))
        }
    }

    fun onClearEntry(@Suppress("UNUSED_PARAMETER") v: View) {
        // CE: chỉ xóa toán hạng đang nhập
        show("0")
        isNewEntry = true
    }

    fun onClearAll(@Suppress("UNUSED_PARAMETER") v: View) {
        // C: xóa toàn bộ ngữ cảnh
        show("0")
        operand1 = null
        pendingOp = null
        isNewEntry = true
    }

    fun onToggleSign(@Suppress("UNUSED_PARAMETER") v: View) {
        val toggled = -cur()
        show(toggled.toString())
        isNewEntry = false
    }
}
