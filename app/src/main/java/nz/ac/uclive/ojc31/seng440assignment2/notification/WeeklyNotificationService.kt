package nz.ac.uclive.ojc31.seng440assignment2.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import nz.ac.uclive.ojc31.seng440assignment2.MainActivity
import nz.ac.uclive.ojc31.seng440assignment2.R


class WeeklyNotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification() {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val rawPathUri: Uri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.bird_chirp)
        var notification = NotificationCompat.Builder(context, WEEKLY_CHANNEL_ID)
            .setSmallIcon(R.drawable.bird)
            .setContentTitle("BirdWatch")
            .setContentText("Achievement Complete")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("You have successfully completed an achievement, good work!"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(activityPendingIntent)
            .setSound(rawPathUri)
            .setColor(0xFF4E9F3D.toInt())
            .setVisibility(VISIBILITY_PUBLIC)
            .build()
        notificationManager.notify(
            1,
            notification
        )
    }

    companion object {
        const val WEEKLY_CHANNEL_ID = "achievement_channel"
        const val WEEKLY_CHANNEL_NAME = "Achievements"
        const val WEEKLY_CHANNEL_DESC = "Used to indicate to the user when an achievement has been completed."
    }
}