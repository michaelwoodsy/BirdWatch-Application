package nz.ac.uclive.ojc31.seng440assignment2

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import nz.ac.uclive.ojc31.seng440assignment2.notification.WeeklyNotificationService
import timber.log.Timber

@HiltAndroidApp
class BirdApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                WeeklyNotificationService.WEEKLY_CHANNEL_ID,
                WeeklyNotificationService.WEEKLY_CHANNEL_NAME,
                importance).apply {
                description = WeeklyNotificationService.WEEKLY_CHANNEL_DESC
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}