package nz.ac.uclive.ojc31.seng440assignment2.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class WeeklyNotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val service = WeeklyNotificationService(context)
        service.showNotification(0)
    }
}