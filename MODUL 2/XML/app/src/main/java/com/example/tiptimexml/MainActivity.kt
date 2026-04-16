package com.example.tiptimexml

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var numberField : TextInputEditText
    private lateinit var tipOptions : AutoCompleteTextView
    private lateinit var roundUpSwitch : MaterialSwitch
    private lateinit var tipAmount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        numberField = findViewById(R.id.number_field)
        tipOptions = findViewById(R.id.tip_options)
        roundUpSwitch = findViewById(R.id.round_up_switch)
        tipAmount = findViewById(R.id.tip_amount)

        val tipList = listOf(
            getString(R.string.tip_option_1),
            getString(R.string.tip_option_2),
            getString(R.string.tip_option_3)
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tipList)
        tipOptions.setAdapter(adapter)
        tipOptions.setText(tipList[0], false)

        tipOptions.setOnItemClickListener { _, _, _, _ -> calculateTip() }
        roundUpSwitch.setOnCheckedChangeListener { _, _ -> calculateTip() }

        calculateTip()
    }

    private fun calculateTip() {
        val billAmount = numberField.text.toString().toDoubleOrNull() ?: 0.0
        val tipPercentage = when (tipOptions.text.toString()) {
            getString(R.string.tip_option_1) -> 0.15
            getString(R.string.tip_option_2) -> 0.18
            else -> 0.2
        }

        var tip = billAmount * tipPercentage
        if (roundUpSwitch.isChecked) tip = kotlin.math.ceil(tip)

        val formatTip = NumberFormat.getCurrencyInstance(Locale.US).format(tip)
        tipAmount.text = getString(R.string.tip_amount, formatTip)
    }
}