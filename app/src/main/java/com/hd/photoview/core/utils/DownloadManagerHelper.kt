package com.hd.photoview.core.utils

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.hd.photoview.domain.model.Photo
import java.io.File
import kotlin.math.log10
import kotlin.math.pow

class DownloadManagerHelper(private val context: Context) {

    private val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "download_channel"
    private val notificationId = 1001

    init {
        createNotificationChannel()
    }

    fun downloadFile(url: String, photo: Photo) {
        // Create the download request
        val downloadRequest = DownloadManager.Request(url.toUri()).apply {
            val fileName = "${photo.description}_Unsplash_${photo.id}.jpg"

            // Set notification properties
            setTitle("Downloading ${photo.description}")
            setDescription("Downloading photo from Unsplash")

            // Make notification visible during and after download
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            // Set destination
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            setMimeType("image/jpeg")

            // Network options
            setAllowedOverMetered(true)
            setAllowedOverRoaming(false)

            // Add additional headers if needed
            addRequestHeader("User-Agent", "Your App Name")
        }

        // Enqueue the download and get the download ID
        val downloadId = downloadManager.enqueue(downloadRequest)

        // Register a receiver to track download progress and completion
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: -1

                if (id == downloadId) {
                    // Check download status
                    val query = DownloadManager.Query().setFilterById(downloadId)
                    val cursor = downloadManager.query(query)

                    if (cursor.moveToFirst()) {
                        val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                        val status = cursor.getInt(statusIndex)

                        when (status) {
                            DownloadManager.STATUS_SUCCESSFUL -> {
                                // Show completion notification with action to open the file
                                showDownloadCompleteNotification(downloadId, photo)
                            }
                            DownloadManager.STATUS_FAILED -> {
                                // Show failure notification
                                showDownloadFailedNotification(photo)
                            }
                        }
                    }
                    cursor.close()

                    // Unregister receiver after completion
                    context?.unregisterReceiver(this)
                }
            }
        }

        // Register for download completion
        ContextCompat.registerReceiver(
            context,
            receiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        // Start tracking progress for custom notification updates
        startProgressTracking(downloadId, photo)
    }

    private fun startProgressTracking(downloadId: Long, photo: Photo) {
        val handler = android.os.Handler(android.os.Looper.getMainLooper())
        val progressRunnable = object : Runnable {
            override fun run() {
                val query = DownloadManager.Query().setFilterById(downloadId)
                val cursor = downloadManager.query(query)

                if (cursor.moveToFirst()) {
                    val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    val status = cursor.getInt(statusIndex)

                    if (status == DownloadManager.STATUS_RUNNING || status == DownloadManager.STATUS_PAUSED) {
                        val bytesDownloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                        val bytesTotalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)

                        val bytesDownloaded = cursor.getLong(bytesDownloadedIndex)
                        val bytesTotal = cursor.getLong(bytesTotalIndex)

                        val progress = if (bytesTotal > 0) {
                            (bytesDownloaded * 100 / bytesTotal).toInt()
                        } else {
                            0
                        }

                        // Update notification with progress
                        updateProgressNotification(photo, progress, bytesDownloaded, bytesTotal, status == DownloadManager.STATUS_PAUSED)

                        // Continue tracking if still downloading
                        handler.postDelayed(this, 500) // Update every 500ms
                    }
                }
                cursor.close()
            }
        }

        // Start progress tracking
        handler.post(progressRunnable)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Downloads"
            val description = "Download notifications"
            val importance = NotificationManager.IMPORTANCE_LOW // LOW importance prevents sound for progress updates

            val channel = NotificationChannel(channelId, name, importance).apply {
                this.description = description
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(false)
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateProgressNotification(photo: Photo, progress: Int, bytesDownloaded: Long, bytesTotal: Long, isPaused: Boolean) {
        val formattedDownloaded = formatFileSize(bytesDownloaded)
        val formattedTotal = formatFileSize(bytesTotal)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle("Downloading ${photo.description}")
            .setContentText("$formattedDownloaded of $formattedTotal (${progress}%)")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setProgress(100, progress, bytesTotal <= 0)

        if (isPaused) {
            builder.setContentText("Download paused - $formattedDownloaded of $formattedTotal")
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
        }

        notificationManager.notify(notificationId, builder.build())
    }

    private fun showDownloadCompleteNotification(downloadId: Long, photo: Photo) {
        // Get the file URI from the download manager
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor = downloadManager.query(query)

        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
            val uriString = cursor.getString(columnIndex)
            val uri = uriString.toUri()

            // Create intent to open the file
            val file = File(uri.path!!)
            val fileUri =
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )

            val openIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(fileUri, "image/jpeg")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val pendingIntent = android.app.PendingIntent.getActivity(
                context,
                0,
                openIntent,
                android.app.PendingIntent.FLAG_IMMUTABLE
            )

            // Build the notification
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Download Complete")
                .setContentText("${photo.description} has been downloaded")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

            notificationManager.notify(notificationId + 1, builder.build()) // Use different ID to avoid replacing progress notification
        }
        cursor.close()
    }

    private fun showDownloadFailedNotification(photo: Photo) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentTitle("Download Failed")
            .setContentText("Failed to download ${photo.description}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(notificationId + 2, builder.build()) // Use different ID
    }

    private fun formatFileSize(size: Long): String {
        if (size <= 0) return "0 B"

        val units = arrayOf("B", "KB", "MB", "GB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()

        return "%.1f %s".format(
            size / 1024.0.pow(digitGroups.toDouble()),
            units[digitGroups]
        )
    }
}

