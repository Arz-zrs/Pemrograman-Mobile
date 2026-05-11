package com.example.scrollablemodul3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.scrollablemodul3.ui.theme.ScrollableModul3Theme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScrollableModul3Theme {
                ScrollableApp()
            }
        }
    }
}