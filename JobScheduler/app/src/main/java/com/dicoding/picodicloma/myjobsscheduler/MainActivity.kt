package com.dicoding.picodicloma.myjobsscheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val JOB_ID = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_start -> {
                startJob()
            }

            R.id.btn_cancel -> {
                cancelJob()
            }
        }
    }

    private fun startJob() {
        if (isJobRunning(this)) {
            Toast.makeText(
                this,
                "Job service has already been scheduled",
                Toast.LENGTH_SHORT
            )
                .show()

            return
        }

        val mServiceComponent = ComponentName(this, GetCurrentWeatherJobService::class.java)

        val builder = JobInfo.Builder(JOB_ID, mServiceComponent)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setRequiresDeviceIdle(false)
            .setRequiresCharging(false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            builder.setPeriodic(900_000)
        else
            builder.setPeriodic(180_000)

        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.schedule(builder.build())
        Toast.makeText(
            this,
            "Job service has been started",
            Toast.LENGTH_SHORT
        )
            .show()
    }

    private fun cancelJob() {
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(JOB_ID)
        Toast.makeText(
            this,
            "Job service has been cancelled",
            Toast.LENGTH_SHORT
        )
            .show()
    }

    private fun isJobRunning(context: Context): Boolean {
        var isScheduled = false

        var scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        for (jobInfo in scheduler.allPendingJobs) {
            if (jobInfo.id == JOB_ID) {
                isScheduled = true

                break
            }
        }

        return isScheduled
    }
}
