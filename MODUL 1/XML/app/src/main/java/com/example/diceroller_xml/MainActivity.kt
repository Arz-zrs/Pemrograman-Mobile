package com.example.diceroller_xml

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var diceImage: ImageView
    lateinit var diceImage2: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rollButton: Button = findViewById(R.id.roll_button)
        rollButton.setOnClickListener {
            rollDice()
        }
        diceImage = findViewById(R.id.dice_image)
        diceImage2 = findViewById(R.id.dice_image2)
    }

    private fun rollDice() {
        val result1 = (1..6).random()
        val imageResource1 = when (result1) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }

        diceImage.setImageResource(imageResource1)
        diceImage.contentDescription = result1.toString()

        val result2 = (1..6).random()
        val imageResource2 = when (result2) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }

        diceImage2.setImageResource(imageResource2)
        diceImage2.contentDescription = result2.toString()

        val message = if (result1 == result2) getString(R.string.the_double)
        else getString(R.string.unlucky)

        val layout: LinearLayout = findViewById(R.id.layout)
        val snackbar = Snackbar
            .make(layout, message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}