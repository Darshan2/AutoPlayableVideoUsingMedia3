/*
 * Copyright 2017 Arthur Ivanets, arthur.ivanets.work@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.videoplayerusingmedia3.demo

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.OptIn
import androidx.core.os.bundleOf
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videoplayerusingmedia3.demo.base.BaseFragment
import com.arthurivanets.sample.util.markers.CanManagePlayback
import com.arthurivanets.sample.util.markers.HasTitle
import com.example.videoplayerusingmedia3.Config
import com.example.videoplayerusingmedia3.R
import com.example.videoplayerusingmedia3.demo.providers.VideoProvider
import com.example.videoplayerusingmedia3.util.misc.ExoPlayerUtils
import com.example.videoplayerusingmedia3.widget.PlayableItemsContainer
import com.example.videoplayerusingmedia3.widget.PlayableItemsRecyclerView

class BasicVideosFragment : BaseFragment(), CanManagePlayback, HasTitle {

    private var autoplayMode = PlayableItemsContainer.AutoplayMode.ONE_AT_A_TIME
    private lateinit var recyclerView: PlayableItemsRecyclerView

    companion object {

        const val TAG = "BasicVideosFragment"
        private const val EXTRA_AUTOPLAY_MODE = "autoplay_mode"

        @JvmStatic
        fun newInstance(autoplayMode: PlayableItemsContainer.AutoplayMode): BasicVideosFragment {
            return BasicVideosFragment().apply {
                arguments = newBundleInstance(autoplayMode)
            }
        }

        @JvmStatic
        fun newBundleInstance(autoplayMode: PlayableItemsContainer.AutoplayMode): Bundle {
            return bundleOf(EXTRA_AUTOPLAY_MODE to autoplayMode)
        }

    }

    override fun fetchExtras(extras: Bundle) {
        super.fetchExtras(extras)

        autoplayMode = ((extras.getSerializable(EXTRA_AUTOPLAY_MODE) as PlayableItemsContainer.AutoplayMode?)
            ?: autoplayMode)
    }


    @OptIn(UnstableApi::class) override fun init(savedInstanceState: Bundle?, parentView: View) {
        recyclerView = parentView.findViewById(R.id.recyclerView)
        val context = context ?: return

        with(recyclerView) {
            setPlaybackTriggeringStates(
                PlayableItemsContainer.PlaybackTriggeringState.IDLING,
                PlayableItemsContainer.PlaybackTriggeringState.DRAGGING
            )

            autoplayMode = this@BasicVideosFragment.autoplayMode
            layoutManager = LinearLayoutManager(context).also {
                it.isItemPrefetchEnabled = true
                it.initialPrefetchItemCount = 4
            }
            adapter = BasicVideoItemsRecyclerViewAdapter(
                context = context,
                items = VideoProvider.getVideos(count = 10, mute = false).toMutableList(),
                arviConfig = Config.Builder()
                    .cache(ExoPlayerUtils.getCache(context))
                    .build()
            )
        }
    }

    override fun startPlayback() {
        recyclerView?.startPlayback()
    }

    override fun stopPlayback() {
        recyclerView?.stopPlayback()
    }

    override fun onResume() {
        super.onResume()

        if (userVisibleHint) {
            recyclerView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()

        recyclerView.onPause()
    }

    override fun onBecameVisibleToUser() {
        super.onBecameVisibleToUser()

        if (isViewCreated) {
            recyclerView.onResume()
        }
    }

    override fun onBecameInvisibleToUser() {
        super.onBecameInvisibleToUser()

        if (isViewCreated) {
            recyclerView.onPause()
        }
    }

    override fun onRecycle() {
        super.onRecycle()

        recyclerView?.onDestroy()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_videos
    }

    override fun getTitle(context: Context): CharSequence {
        return when (autoplayMode) {
            PlayableItemsContainer.AutoplayMode.MULTIPLE_SIMULTANEOUSLY -> context.getString(R.string.title_videos_fragment_playback_multiple_simultaneously)
            else -> context.getString(R.string.title_videos_fragment_playback_one_at_a_time)
        }
    }

}
