package com.dicoding.picodicloma.myviewsandviewgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ScrollActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll)

        supportActionBar?.title = "Google Pixel"
    }
}
