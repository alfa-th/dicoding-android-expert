package com.dicoding.picodicloma.mybroadcastreceiver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_sms_receiver.*

class SmsReceiverActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_SMS_NO = "extra_sms_no"
        const val EXTRA_SMS_MESSAGE = "extra_sms_message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_receiver)

        title = getString(R.string.incoming_message)

        btn_close.setOnClickListener(this)

        val senderNo = intent.getStringExtra(EXTRA_SMS_NO)
        val senderMessage = intent.getStringExtra(EXTRA_SMS_MESSAGE)

        tv_from.text = "${getString(R.string.coming_from)} $senderNo"
        tv_message.text = senderMessage
    }

    override fun onClick(p0: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
