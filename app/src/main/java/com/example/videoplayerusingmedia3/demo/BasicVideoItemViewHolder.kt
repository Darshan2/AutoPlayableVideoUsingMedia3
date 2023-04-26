package com.example.videoplayerusingmedia3.demo

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.media3.common.Tracks
import androidx.media3.ui.PlayerView
import com.example.videoplayerusingmedia3.Config
import com.example.videoplayerusingmedia3.R
import com.example.videoplayerusingmedia3.demo.model.Video
import com.example.videoplayerusingmedia3.demo.util.extensions.getColorCompat
import com.example.videoplayerusingmedia3.demo.util.extensions.makeGone
import com.example.videoplayerusingmedia3.demo.util.extensions.makeVisible
import com.example.videoplayerusingmedia3.demo.util.extensions.setColor
import com.example.videoplayerusingmedia3.widget.PlayableItemViewHolder
import com.example.videoplayerusingmedia3.widget.PlaybackState


class BasicVideoItemViewHolder(
    parent: ViewGroup,
    itemView: View,
    val arviConfig: Config
) : PlayableItemViewHolder(parent, itemView) {

    val titleTv = itemView.findViewById<TextView>(R.id.titleTv)
    val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)
    val errorIconIv = itemView.findViewById<ImageView>(R.id.errorIconIv)

    var video: Video? = null

    fun bindData(data: Video?) {
        data?.also {
            handleData(it)
            video = it
        }
    }

    private fun handleData(data: Video) {
        handleInfoViews()

        titleTv.text = data.title
    }

    override fun getViewHolderPlayerView(): PlayerView {
        return itemView.findViewById(R.id.player_view)
    }

    private fun handleInfoViews() {
        progressBar.makeGone()
        progressBar.setColor(progressBar.context.getColorCompat(R.color.video_item_progress_bar_color))

        errorIconIv.makeGone()
    }


    override fun getVideoUrl(): String {
        return (video?.videoUrl ?: "")
    }

    override fun getAdTagUrl(): String {
//        return "https://pubads.g.doubleclick.net/gampad/ads?iu=/21775744923/external/single_ad_samples&sz=640x480&cust_params=sample_ct%3Dlinear&ciu_szs=300x250%2C728x90&gdfp_req=1&output=vast&unviewed_position_start=1&env=vp&impl=s&correlator="
        return "https://pubads.g.doubleclick.net/gampad/ads?iu=/21775744923/external/vmap_ad_samples&sz=640x480&cust_params=sample_ar%3Dpremidpost&ciu_szs=300x250&gdfp_req=1&ad_rule=1&output=vmap&unviewed_position_start=1&env=vp&impl=s&cmsid=496&vid=short_onecue&correlator="
    }

    override fun showInStreamVideAds(): Boolean {
       return true;
    }

    override fun getConfig(): Config {
        return arviConfig
    }

    override fun isLooping(): Boolean {
        return false
    }

    override fun onTracksChanged(tracks: Tracks?) {

    }

    override fun onStateChanged(playbackState: PlaybackState) {
        super.onStateChanged(playbackState)

        when (playbackState) {
            PlaybackState.BUFFERING -> onBufferingState()
            PlaybackState.READY -> onReadyState()
            PlaybackState.PAUSED -> onPausedState()
            PlaybackState.STOPPED -> onStoppedState()
            PlaybackState.ERROR -> onErrorState()
        }
    }

    private fun onBufferingState() {
        progressBar.makeVisible()
        errorIconIv.makeGone()
    }

    private fun onReadyState() {
        progressBar.makeGone()
        errorIconIv.makeGone()

        video?.isMuted?.let(::setMuted)
    }

    private fun onPausedState() {
        progressBar.makeGone()
    }

    private fun onStoppedState() {
        progressBar.makeGone()
    }

    private fun onErrorState() {
        progressBar.makeGone()
        errorIconIv.makeVisible()
    }

}
