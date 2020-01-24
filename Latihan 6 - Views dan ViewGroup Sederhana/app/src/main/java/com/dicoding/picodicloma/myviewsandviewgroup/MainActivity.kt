package com.dicoding.picodicloma.myviewsandviewgroup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_move_to_scroll_activity.setOnClickListener(this)
        btn_move_to_constraint_activity.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_move_to_scroll_activity -> {
                val moveIntent = Intent(this@MainActivity, ScrollActivity::class.java)

                startActivity(moveIntent)
            }

            R.id.btn_move_to_constraint_activity -> {
                val moveIntent = Intent(this@MainActivity, ConstraintActivity::class.java)

                startActivity(moveIntent)
            }

        }
    }
}
