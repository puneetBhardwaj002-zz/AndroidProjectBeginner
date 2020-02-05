package com.example.myapplication

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.util.*


class SongItemAdapter(
    private var songList: ArrayList<SongItem>,
    private val clickListener: (SongItem) -> Unit
) : RecyclerView.Adapter<SongItemAdapter.SongItemViewHolder>() {

    private lateinit var mContext: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_song_item, parent, false)
        mContext = parent.context
        return SongItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return when {
            songList.isNullOrEmpty() -> 0
            else -> songList.size
        }
    }

    override fun onBindViewHolder(holder: SongItemViewHolder, position: Int) {
        val songItem = songList[position]
        holder.bind(songItem, clickListener)
    }

    inner class SongItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private var songName: TextView = itemView.findViewById(R.id.songName)
        private var artistName: TextView = itemView.findViewById(R.id.songArtist)
        private var duration: TextView = itemView.findViewById(R.id.songDuration)
        private var songImage: ImageView = itemView.findViewById(R.id.songImage)

        fun bind(songItem: SongItem, clickListener: (SongItem) -> Unit) {
            songName.text = songItem.songName
            artistName.text = songItem.artistName
            duration.text = durationToTime(songItem.duration.toString())
            itemView.setOnClickListener { clickListener(songItem) }
            setSongImage(songItem.pathResource, mContext, songImage)
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

        private fun setSongImage(id:Long, mContext: Context, view: ImageView) {
            val uri = ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"),
                id
            )
            Glide
                .with(mContext)
                .load(uri)
                .apply(RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.c1))
                .thumbnail(0.1f)
                .into(view)
        }
    }
}