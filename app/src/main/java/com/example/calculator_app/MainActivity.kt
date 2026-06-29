package com.example.calculator_app


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.function.Function

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private val KEY_EXPR = "saved_expr"
    private val KEY_RES = "saved_res"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        setupKeypad()

        // Navigation Intent to Bonus Activity
        findViewById<Button>(R.id.btnOpenBonus).setOnClickListener {
            val intent = Intent(this, BonusActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_EXPR, tvExpression.text.toString())
        outState.putString(KEY_RES, tvResult.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        tvExpression.text = savedInstanceState.getString(KEY_EXPR, "")
        tvResult.text = savedInstanceState.getString(KEY_RES, "0")
    }

    private fun getFactorial(n: Int): Double {
        var result = 1.0
        for (i in 1..n) result *= i
        return result
    }

    private fun setupKeypad() {
        val standardButtons = mapOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2", R.id.btn3 to "3",
            R.id.btn4 to "4", R.id.btn5 to "5", R.id.btn6 to "6", R.id.btn7 to "7",
            R.id.btn8 to "8", R.id.btn9 to "9", R.id.btnDot to ".",
            R.id.btnPlus to "+", R.id.btnMinus to "-", R.id.btnMultiply to "*", R.id.btnDivide to "/",
            R.id.btnOpenP to "(", R.id.btnCloseP to ")", R.id.btnPower to "^"
        )

        for ((id, text) in standardButtons) {
            findViewById<Button>(id).setOnClickListener { tvExpression.append(text) }
        }

        // Trig & Hyperbolic Trig
        val funcButtons = mapOf(
            R.id.btnSin to "sin(", R.id.btnCos to "cos(", R.id.btnTan to "tan(",
            R.id.btnSinh to "sinh(", R.id.btnCosh to "cosh(", R.id.btnTanh to "tanh(",
            R.id.btnSqrt to "sqrt(", R.id.btnFact to "fact(",
            R.id.btnNpr to "nPr(", R.id.btnNcr to "nCr("
        )

        for ((id, text) in funcButtons) {
            findViewById<Button>(id).setOnClickListener { tvExpression.append(text) }
        }

        findViewById<Button>(R.id.btnAC).setOnClickListener {
            tvExpression.text = ""
            tvResult.text = "0"
        }

        findViewById<Button>(R.id.btnDel).setOnClickListener {
            val text = tvExpression.text.toString()
            if (text.isNotEmpty()) {
                tvExpression.text = text.dropLast(1)
                tvResult.text = ""
            }
        }

        val factFunc = object : Function("fact", 1) {
            override fun apply(vararg args: Double): Double = getFactorial(args[0].toInt())
        }

        val nCrFunc = object : Function("nCr", 2) {
            override fun apply(vararg args: Double): Double {
                val n = args[0].toInt()
                val r = args[1].toInt()
                return getFactorial(n) / (getFactorial(r) * getFactorial(n - r))
            }
        }

        val nPrFunc = object : Function("nPr", 2) {
            override fun apply(vararg args: Double): Double {
                val n = args[0].toInt()
                val r = args[1].toInt()
                return getFactorial(n) / getFactorial(n - r)
            }
        }

        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            try {
                val expr = ExpressionBuilder(tvExpression.text.toString())
                    .function(factFunc)
                    .function(nCrFunc)
                    .function(nPrFunc)
                    .build()
                val result = expr.evaluate()

                val longRes = result.toLong()
                tvResult.text = if (result == longRes.toDouble()) longRes.toString() else result.toString()
            } catch (e: Exception) {
                tvResult.text = "Error"
            }
        }
    }
}