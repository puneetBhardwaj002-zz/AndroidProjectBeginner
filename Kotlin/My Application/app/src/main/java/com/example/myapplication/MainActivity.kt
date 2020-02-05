package com.example.myapplication

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.random.Random


class MainActivity : AppCompatActivity(), View.OnClickListener, ServiceCallBacks {

    private lateinit var adapter: SongItemAdapter
    private lateinit var mSongList: ArrayList<SongItem>
    private lateinit var recyclerView: RecyclerView
    private lateinit var relativeLayout: RelativeLayout
    private lateinit var linearLayout: LinearLayout
    private lateinit var seekBar: SeekBar
    private lateinit var playPauseButton: ImageView
    private lateinit var currentDurationText: TextView
    private lateinit var totalDurationText: TextView
    private lateinit var descPanelArtist: TextView
    private lateinit var descPanelImage: ImageView
    private lateinit var descPanelName: TextView
    private lateinit var prevButton: ImageView
    private lateinit var nextButton: ImageView
    private lateinit var repeatButton: ImageView
    private lateinit var shuffleButton: ImageView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ViewGroup>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var myActionReceiver:MyActionReceiver
    private var timer: CountDownTimer? = null
    private var mDurationTotal = 0
    private var currentPlaybackTime: Int = 0
    private var mIsUserSeeking = false
    private var myService: MyService? = null
    private var playIntent: Intent? = null
    private var musicBound = false
    private var isSongPlaying = false
    private var currentSongIndex: Int = 0
    private var isShuffleSelected = false
    private var isRepeatSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.songRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        getAndroidRunTimePermission()
    }

    private fun getAndroidRunTimePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                0
            )
        } else {
            startMyService()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startMyService()
                } else {
                    finish()
                }
            }
        }
    }


    private fun initializeViews() {
        sharedPreferences =
            applicationContext.getSharedPreferences(packageName, Context.MODE_PRIVATE)
        linearLayout = findViewById(R.id.topSongDescription)
        relativeLayout = findViewById(R.id.rootContainer)
        descPanelImage = findViewById(R.id.songImageDesc)
        descPanelName = findViewById(R.id.songNameDesc)
        descPanelArtist = findViewById(R.id.songArtistDesc)
        seekBar = findViewById(R.id.timeSeekBar)
        currentDurationText = findViewById(R.id.currentPlayBackTime)
        totalDurationText = findViewById(R.id.totalPlayBackTime)
        prevButton = findViewById(R.id.prevSong)
        nextButton = findViewById(R.id.nextSong)
        playPauseButton = findViewById(R.id.playPauseSong)
        shuffleButton = findViewById(R.id.iv_shuffle_music)
        repeatButton = findViewById(R.id.iv_repeat_music)
        timer = object : CountDownTimer(0, 100) {
            override fun onFinish() {}
            override fun onTick(p0: Long) {}

        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var userSelectedPosition = 0
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) {
                    myService!!.setMusicProgress(p1)
                    userSelectedPosition = p1
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                mIsUserSeeking = true
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                mIsUserSeeking = false
                seekBar.progress = userSelectedPosition
                val mSeconds = (mDurationTotal * (userSelectedPosition.toDouble() / 100.0)).toInt()
                currentDurationText.text = durationToTime(mSeconds.toString())
                startTimer((mDurationTotal - mSeconds).toLong())
            }

        })
        bottomSheetBehavior = BottomSheetBehavior.from(relativeLayout)
        val topDescLayout = findViewById<LinearLayout>(R.id.topSongDescription)
        bottomSheetBehavior.bottomSheetCallback =
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            topDescLayout.background = ColorDrawable(0xFFFFFFFF.toInt())
                        }
                        else -> topDescLayout.background = ColorDrawable(0xE1FFFFFF.toInt())
                    }
                }
            }
        relativeLayout.visibility = View.VISIBLE
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.peekHeight = 0
        prevButton.setOnClickListener(this)
        nextButton.setOnClickListener(this)
        playPauseButton.setOnClickListener(this)
        linearLayout.setOnClickListener(this)
        relativeLayout.setOnClickListener(this)
        shuffleButton.setOnClickListener(this)
        repeatButton.setOnClickListener(this)
        //populateSongFromSharedPreference()
    }

    private fun populateSongFromSharedPreference() {
        if (!sharedPreferences.getString(getString(R.string.song_name), "").isNullOrEmpty()) {
            val name = sharedPreferences.getString(getString(R.string.song_name), "")
            val index = sharedPreferences.getInt(getString(R.string.song_index), 0)
            for (song in mSongList) {
                if (song.songName == name) {
                    setValues(song, true)
                    currentSongIndex = index
                }
            }
            isSongPlaying = false
        }
    }

    private fun startMyService() {
        if (playIntent == null) {
            playIntent = Intent(this, MyService::class.java)
            ContextCompat.startForegroundService(this, playIntent!!)
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE)
            myActionReceiver = MyActionReceiver()
            registerReceiver(myActionReceiver, IntentFilter(packageName))
        }
    }

    private val musicConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            musicBound = false
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder: MyService.MusicBinder = p1 as MyService.MusicBinder
            myService = binder.getService()!!
            musicBound = true
            myService!!.setServiceCallbacks(this@MainActivity)
            mSongList = myService!!.findSongsList()
            displaySongs(mSongList)
            initializeViews()
        }
    }

    private fun displaySongs(list: ArrayList<SongItem>) {
        val mLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = mLayoutManager
        val dividerItemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(recyclerView.context, mLayoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        adapter =
            SongItemAdapter(list) { songItem: SongItem -> onSongItemClicked(songItem) }
        recyclerView.adapter = adapter

    }

    private fun onSongItemClicked(songItem: SongItem) {
        updateCurrentSongContent(mSongList.indexOf(songItem))
        isSongPlaying = true
        saveCurrentSongInfoIntoSharedPreferences(songItem)
    }

    private fun saveCurrentSongInfoIntoSharedPreferences(item: SongItem) {
        try {
            editor = sharedPreferences.edit()
            editor.putString(getString(R.string.song_name), item.songName)
            editor.putString(getString(R.string.song_artist), item.artistName)
            editor.putInt(getString(R.string.song_index), currentSongIndex)
            editor.putInt(getString(R.string.song_duration), currentPlaybackTime)
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.prevSong -> {
                if (isShuffleSelected) {
                    currentSongIndex = Random.nextInt(0, mSongList.size)
                    onSongItemClicked(mSongList[currentSongIndex])
                } else {
                    myService!!.playPrevOrNextSong(0)
                    saveCurrentSongInfoIntoSharedPreferences(mSongList[currentSongIndex])
                }
            }
            R.id.nextSong -> {
                if (isShuffleSelected) {
                    currentSongIndex = Random.nextInt(0, mSongList.size)
                    onSongItemClicked(mSongList[currentSongIndex])
                } else {
                    myService!!.playPrevOrNextSong(1)
                    saveCurrentSongInfoIntoSharedPreferences(mSongList[currentSongIndex])
                }
            }
            R.id.playPauseSong -> {
                myService!!.playPauseSong(isSongPlaying)
                when (isSongPlaying) {
                    true -> {
                        isSongPlaying = false
                        playPauseButton.setImageResource(R.drawable.ic_play_circle_filled_48px)
                        currentPlaybackTime = myService!!.getCurrentPlayingPosition()
                        timer!!.cancel()
                    }
                    else -> {
                        isSongPlaying = true
                        playPauseButton.setImageResource(R.drawable.ic_pause_circle_filled_48px)
                        startTimer((mDurationTotal - currentPlaybackTime).toLong())
                    }
                }
                seekBar.progress =
                    ((myService!!.getCurrentPlayingPosition().toDouble() / mDurationTotal.toDouble()) * 100).toInt()
            }
            R.id.topSongDescription -> {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                } else if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
            R.id.rootContainer -> {

            }
            R.id.iv_repeat_music -> {
                toggleRepeatPlayback()
            }
            R.id.iv_shuffle_music -> {
                toggleShufflePlayback()
            }
        }
    }

    private fun setValues(songItem: SongItem, isFromSharedPreferences: Boolean) {
        loadImageIntoView(
            null, mSongList.indexOf(songItem),
            shouldUseThumbnail = false,
            shouldPopulateLayoutImage = true
        )
        setTopPanelDescriptionValues(songItem)
        setDurationValues(songItem)
        relativeLayout.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    relativeLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val hidden: View = relativeLayout.getChildAt(0)
                    bottomSheetBehavior.peekHeight = hidden.bottom
                }
            })
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        if (!isFromSharedPreferences) {
            startTimer(songItem.duration.toLong())
        } else {
            playPauseButton.setImageResource(R.drawable.ic_play_circle_filled_48px)
            isSongPlaying = false
        }
    }

    private fun setDurationValues(songItem: SongItem) {
        currentDurationText.text = getString(R.string.initial_duration)
        mDurationTotal = songItem.duration
        currentPlaybackTime = 0
        totalDurationText.text = durationToTime(songItem.duration.toString())
    }

    private fun setTopPanelDescriptionValues(songItem: SongItem) {
        loadImageIntoView(
            descPanelImage, mSongList.indexOf(songItem),
            shouldUseThumbnail = false,
            shouldPopulateLayoutImage = false
        )
        descPanelName.text = songItem.songName
        descPanelArtist.text = songItem.artistName
        relativeLayout.background = descPanelImage.drawable
    }

    private fun durationToTime(time: String): String {
        val t = time.toLong()
        var seconds = t / 1000
        val minutes = seconds / 60
        seconds %= 60
        return if (seconds < 10) {
            "$minutes:0$seconds"
        } else {
            "$minutes:$seconds"
        }
    }


    private fun startTimer(time: Long) {
        timer!!.cancel()
        timer = object : CountDownTimer(time, 1000) {
            override fun onFinish() {
                currentDurationText.text = durationToTime(mDurationTotal.toString())
                seekBar.progress = 100
            }

            override fun onTick(p0: Long) {
                seekBar.progress =
                    ((1.0 - (p0.toDouble() / mDurationTotal.toDouble())) * 100).toInt()
                currentDurationText.text = durationToTime((mDurationTotal - p0.toInt()).toString())
            }
        }
        timer!!.start()
    }

    private fun loadImageIntoView(
        imageView: ImageView?, index: Int,
        shouldUseThumbnail: Boolean,
        shouldPopulateLayoutImage: Boolean, layout: RelativeLayout = relativeLayout
    ) {
        val uri = ContentUris.withAppendedId(
            Uri.parse("content://media/external/audio/albumart"),
            mSongList[index].pathResource
        )
        when {
            shouldPopulateLayoutImage -> Glide.with(this).load(uri)
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.c1)
                        .error(R.drawable.c1)
                )
                .into(object : CustomViewTarget<RelativeLayout, Drawable>(layout) {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        relativeLayout.background = errorDrawable
                        myService!!.setSong(index, errorDrawable!!)
                    }

                    override fun onResourceCleared(placeholder: Drawable?) {
                        relativeLayout.background = placeholder
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        relativeLayout.background = resource
                        myService!!.setSong(index, resource)
                    }
                })
            shouldUseThumbnail -> Glide.with(this).load(uri).thumbnail(0.1f)
                .apply(
                    RequestOptions()
                        .error(R.drawable.custom_image_1)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(imageView!!)
            else -> Glide.with(this).load(uri)
                .apply(
                    RequestOptions()
                        .error(R.drawable.custom_image_1)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(imageView!!)
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onDestroy() {
        unbindService(musicConnection)
        stopService(playIntent)
        myService = null
        unregisterReceiver(myActionReceiver)
        super.onDestroy()
    }

    override fun updateCurrentSongContent(songItemIndex: Int) {
        setValues(mSongList[songItemIndex], false)
        currentSongIndex = songItemIndex
    }

    override fun currentPlaybackComplete() {
        if (isShuffleSelected) {
            currentSongIndex = Random.nextInt(0, mSongList.size)
            onSongItemClicked(mSongList[currentSongIndex])
        } else if (isRepeatSelected) {
            myService!!.playPrevOrNextSong(1)
        }
    }

    private fun toggleShufflePlayback() {
        isShuffleSelected = when (isShuffleSelected) {
            true -> false
            else -> true
        }
        shuffleButton.setImageResource(
            when (isShuffleSelected) {
                true -> R.drawable.ic_shuffle_selected_48px
                else -> R.drawable.ic_shuffle_unselected_48px
            }
        )
    }

    private fun toggleRepeatPlayback() {
        isRepeatSelected = when (isRepeatSelected) {
            true -> false
            else -> true
        }
        repeatButton.setImageResource(
            when (isRepeatSelected) {
                true -> R.drawable.ic_repeat_selected_48px
                else -> R.drawable.ic_repeat_unselected_48px
            }
        )
    }
    inner class MyActionReceiver:BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            when (p1!!.getIntExtra("action",0)){
                0 -> onClick(prevButton)
                2 -> onClick(nextButton)
                1 -> onClick(playPauseButton)
            }
        }

    }
}