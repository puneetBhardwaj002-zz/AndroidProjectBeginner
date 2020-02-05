package com.example.myapplication

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap


class MyService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{

    private var mCurrentPosition = 0
    private lateinit var player: MediaPlayer
    private var mSongsList = ArrayList<SongItem>()
    private val musicBind: IBinder = MusicBinder()
    private lateinit var builder: NotificationCompat.Builder
    private var currentSongIndex = 0
    private lateinit var prevPendingIntent: PendingIntent
    private lateinit var nextPendingIntent: PendingIntent
    private lateinit var playPausePendingIntent: PendingIntent
    private lateinit var mServiceCallBacks: ServiceCallBacks
    private var currentPlayingPosition = 0
    private val mChannelId = "mChannelId"
    private val mChannelName = "mChannelName"
    private val notificationId = 1234
    private val actionPlayPause = "play_pause"
    private val actionPrev = "prev"
    private val actionNext = "next"
    private val musicFilePath = "/storage/emulated/0/MIUI/ShareMe/"
    private var isSongPlaying = false

    override fun onCreate() {
        mCurrentPosition = 0
        currentSongIndex = 0
        player = MediaPlayer()
        initializeMediaPlayer()

        builder = NotificationCompat.Builder(this,mChannelId)
        val intentPrev = Intent(this,MainActivity.MyActionReceiver::class.java)
        prevPendingIntent = PendingIntent.getBroadcast(this,0,intentPrev, PendingIntent.FLAG_UPDATE_CURRENT)
        intentPrev.putExtra("action",0)
        val intentNext = Intent(this,MainActivity.MyActionReceiver::class.java)
        intentNext.putExtra("action",2)
        nextPendingIntent = PendingIntent.getBroadcast(this,1,intentNext, PendingIntent.FLAG_UPDATE_CURRENT)
        val intentPlayPause = Intent(this,MainActivity.MyActionReceiver::class.java)
        intentPlayPause.putExtra("action",1)
        playPausePendingIntent = PendingIntent.getBroadcast(this,2,intentPlayPause, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.apply {
            setSmallIcon(R.drawable.ic_launcher)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.ic_skip_previous_24px, "Previous",prevPendingIntent ) // #0
                .addAction(R.drawable.ic_pause_circle_filled_24px, "Pause", playPausePendingIntent) // #1
                .addAction(R.drawable.ic_skip_next_24px, "Next", nextPendingIntent) // #2
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0,1,2))
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(false)
            setOngoing(true)
        }
        super.onCreate()
    }
    override fun onPrepared(p0: MediaPlayer?) {
        player.start()
        currentPlayingPosition = 0
        isSongPlaying = true
    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        return false
    }

    override fun onCompletion(p0: MediaPlayer?) {
        mServiceCallBacks.currentPlaybackComplete()
    }

    override fun onBind(intent: Intent): IBinder {
        return musicBind
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when {
            intent!!.action.equals(actionPrev) -> playPrevOrNextSong(0)
            intent.action.equals((actionNext)) -> playPrevOrNextSong(1)
            intent.action.equals(actionPlayPause) -> playPauseSong(isSongPlaying)
            else -> {
                createNotificationChannel(this)
                startForeground(notificationId, builder.build())
            }
        }
        return START_NOT_STICKY
    }
    private fun initializeMediaPlayer(){
        player.setWakeMode(applicationContext,PowerManager.PARTIAL_WAKE_LOCK)
        player.setOnPreparedListener(this)
        player.setOnCompletionListener(this)
        player.setOnErrorListener(this)
    }

    inner class MusicBinder: Binder(){
        fun getService():MyService?{
            return this@MyService
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        player.stop()
        player.release()
        return super.onUnbind(intent)
    }

    private fun playSong(){
        player.reset()
        //TODO: Use contentResolver.openFileDescriptor(Uri,String)
        val uriString =  musicFilePath + mSongsList[currentSongIndex].songName
        try {
            player.setDataSource(applicationContext, Uri.parse(uriString))
        } catch (e:Exception){
            Log.e("MUSIC SERVICE", "Error setting data source", e)
        }
        player.prepareAsync()
    }

    fun setSong(songIndex:Int,drawable: Drawable){
        currentSongIndex = songIndex
        changeNotificationContent(songIndex,drawable)
        playSong()
    }

    fun playPauseSong(isSongPlaying: Boolean) {
        if(isSongPlaying){
            player.pause()
            currentPlayingPosition = player.currentPosition
        } else {
            player.seekTo(currentPlayingPosition)
            player.start()
        }
    }

    fun setMusicProgress(progress:Int){
        player.seekTo(((progress.toDouble()/100.0)*player.duration).toInt())
    }

    fun getCurrentPlayingPosition():Int{
        return currentPlayingPosition
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = mChannelName
            val descriptionText = mChannelName
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(mChannelId, name, importance).apply {
                description = descriptionText
            }
            NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }
    }

    private fun changeNotificationContent(index:Int,drawable: Drawable){
        builder.apply {
            setLargeIcon(drawable.toBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight,Bitmap.Config.ARGB_8888))
            setContentTitle(mSongsList[index].songName)
            setContentText(mSongsList[index].artistName)
        }
        with(NotificationManagerCompat.from(this)) {
            if (getNotificationChannel(mChannelId) == null) {
                createNotificationChannel(this@MyService)
            }
            notify(notificationId, builder.build())
        }
    }

    fun setServiceCallbacks(callBacks: ServiceCallBacks){
        this.mServiceCallBacks = callBacks
    }

    fun playPrevOrNextSong(int: Int){
        when (int == 1) {
            true -> {
                when (currentSongIndex == mSongsList.size - 1) {
                    true -> currentSongIndex = 0
                    else -> currentSongIndex++
                }
            }
            else -> {
                when (currentSongIndex == 0) {
                    true -> currentSongIndex = mSongsList.size - 1
                    else -> currentSongIndex--
                }

            }
        }
        mServiceCallBacks.updateCurrentSongContent(currentSongIndex)
    }

    @SuppressLint("InlinedApi")
    fun findSongsList(): ArrayList<SongItem> {
        val resolver: ContentResolver = contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        Log.d("Uri",uri.toString())
        val cursor = resolver.query(uri, null, null, null, MediaStore.Audio.Media.DISPLAY_NAME+" ASC")
        if (cursor == null) {
            Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_SHORT).show()
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(this, "No music files found on SD Card", Toast.LENGTH_SHORT).show()
        } else {
            do {
                val name =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val duration =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                val albums = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                if (duration.toLong() >= 60000) {
                    mSongsList.add(SongItem(name, artist, duration.toInt(), albums))
                }
            } while (cursor.moveToNext())
            cursor.close()
        }
        return mSongsList
    }

}
