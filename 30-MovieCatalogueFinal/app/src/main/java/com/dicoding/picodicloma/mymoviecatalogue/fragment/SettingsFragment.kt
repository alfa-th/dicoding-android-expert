package com.dicoding.picodicloma.mymoviecatalogue.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.dicoding.picodicloma.mymoviecatalogue.AlarmReceiver
import com.dicoding.picodicloma.mymoviecatalogue.R
import java.text.SimpleDateFormat
import java.util.*


class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        private val TAG = SettingsFragment::class.java.simpleName
        private const val DEFAULT_VALUE = false
    }

    private lateinit var dailyReminder: String
    private lateinit var releaseReminder: String

    private lateinit var isDailyReminderActivatedPreference: SwitchPreferenceCompat
    private lateinit var isReleasesReminderActivatedPreference: SwitchPreferenceCompat

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        alarmReceiver = AlarmReceiver()
        init()
        setSummaries()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun init() {
        dailyReminder = resources.getString(R.string.key_daily)
        releaseReminder = resources.getString(R.string.key_releases)

        isDailyReminderActivatedPreference =
            findPreference<SwitchPreferenceCompat>(dailyReminder) as SwitchPreferenceCompat
        isReleasesReminderActivatedPreference =
            findPreference<SwitchPreferenceCompat>(releaseReminder) as SwitchPreferenceCompat
    }

    private fun setSummaries() {
        preferenceManager.sharedPreferences.run {
            isDailyReminderActivatedPreference.isChecked =
                this.getBoolean(dailyReminder, DEFAULT_VALUE)
            isReleasesReminderActivatedPreference.isChecked =
                this.getBoolean(releaseReminder, DEFAULT_VALUE)
        }
    }

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences?,
        key: String?
    ) {
        Log.d(TAG, "onSharedPreferenceChanged : currentTime : ${getCurrentTime()}")
        sharedPreferences?.run {
            when (key) {
                dailyReminder -> {
                    isDailyReminderActivatedPreference.isChecked =
                        this.getBoolean(dailyReminder, DEFAULT_VALUE)

                    Log.d(TAG, "dailyReminder : ${isDailyReminderActivatedPreference.isChecked}")

                    context?.run {
                        if (isDailyReminderActivatedPreference.isChecked) {
                            alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_DAILY, "07:00")
                        } else {
                            alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_DAILY)
                        }
                    }
                }
                releaseReminder -> {
                    isReleasesReminderActivatedPreference.isChecked =
                        this.getBoolean(releaseReminder, DEFAULT_VALUE)

                    Log.d(TAG, "releaseReminder : ${isReleasesReminderActivatedPreference.isChecked}")

                    context?.run {
                        if (isReleasesReminderActivatedPreference.isChecked) {
                            alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_RELEASE, "08:00")
                        } else {
                            alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_RELEASE)
                        }
                    }
                }
                else -> {
                    Log.d(TAG, "$key")
                }
            }
        }
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}
