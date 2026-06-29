package com.example.calculator_app


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow
import kotlin.math.sqrt

class BonusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bonus)

        // Statistics Calculator Logic
        findViewById<Button>(R.id.btnCalcStats).setOnClickListener {
            val input = findViewById<EditText>(R.id.etStatsInput).text.toString()
            val tvRes = findViewById<TextView>(R.id.tvStatsResult)
            try {
                val nums = input.split(",").map { it.trim().toDouble() }
                val mean = nums.average()
                val sorted = nums.sorted()
                val median = if (sorted.size % 2 == 0) {
                    (sorted[sorted.size / 2 - 1] + sorted[sorted.size / 2]) / 2.0
                } else sorted[sorted.size / 2]

                val stdDev = sqrt(nums.map { (it - mean).pow(2) }.average())

                tvRes.text = "Count: ${nums.size}\nMean: $mean\nMedian: $median\nStd Dev: %.4f".format(stdDev)
            } catch (e: Exception) {
                tvRes.text = "Invalid Input. Ensure valid numbers separated by commas."
            }
        }

        // Matrix Determinant Logic
        findViewById<Button>(R.id.btnMatrixCalc).setOnClickListener {
            val tvRes = findViewById<TextView>(R.id.tvMatrixResult)
            try {
                val a = findViewById<EditText>(R.id.m00).text.toString().toDouble()
                val b = findViewById<EditText>(R.id.m01).text.toString().toDouble()
                val c = findViewById<EditText>(R.id.m10).text.toString().toDouble()
                val d = findViewById<EditText>(R.id.m11).text.toString().toDouble()

                val det = (a * d) - (b * c)
                tvRes.text = "Determinant (Δ) = $det"
            } catch (e: Exception) {
                tvRes.text = "Please fill all 4 matrix cells."
            }
        }
    }
}