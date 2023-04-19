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

package com.example.videoplayerusingmedia3.demo.providers

import android.net.Uri
import com.example.videoplayerusingmedia3.demo.model.Video
import com.example.videoplayerusingmedia3.demo.util.misc.randomPositiveInt

object VideoProvider {

    private val VIDEO_URLS = arrayListOf(
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
        "https://d1popuzhpikj0o.cloudfront.net/transcoded/850_26_peacock.mp4/playlist.mpd"
    )

    @JvmStatic
    fun getVideos(count: Int, mute: Boolean = true): List<Video> {
        val items = ArrayList<Video>()

        for (i in 0 until count) {
            val index = (i % VIDEO_URLS.size)
            val url = VIDEO_URLS[index]

            items.add(
                Video(
                    id = randomPositiveInt(),
                    title = (Uri.parse(url).lastPathSegment ?: ""),
                    videoUrl = url,
                    isMuted = mute
                )
            )
        }

        return items
    }

}
