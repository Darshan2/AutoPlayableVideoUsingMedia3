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

package com.example.videoplayerusingmedia3.util.misc;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.cache.Cache;
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor;
import androidx.media3.datasource.cache.SimpleCache;

import com.example.videoplayerusingmedia3.widget.PlayableItemsContainer;
import com.example.videoplayerusingmedia3.widget.Playable;


/**
 * A set of utils specific to Exo Player.
 */
public final class ExoPlayerUtils {

    /**
     * The default size of cache in bytes. (500MB in Bytes)
     */
    public static final long DEFAULT_CACHE_SIZE = (500 * 1024 * 1024);

    private static Cache sCache;

    /**
     * Creates/retrieves the {@link androidx.media3.exoplayer.ExoPlayer} {@link Cache} of the default
     * size {@link #DEFAULT_CACHE_SIZE}.
     *
     * @param context the context
     * @return the {@link androidx.media3.exoplayer.ExoPlayer} {@link Cache}
     */
    public static synchronized Cache getCache(@NonNull Context context) {
        return getCache(context, DEFAULT_CACHE_SIZE);
    }

    @OptIn(markerClass = UnstableApi.class) /**
     * Creates/retrieves the {@link com.google.android.exoplayer2.ExoPlayer} {@link Cache} of the specified size.
     *
     * @param context   the context
     * @param cacheSize the desired cache size in bytes
     * @return the {@link com.google.android.exoplayer2.ExoPlayer} {@link Cache}
     */
    public static synchronized Cache getCache(@NonNull Context context, long cacheSize) {
        Preconditions.nonNull(context);

        if (sCache == null) {
            sCache = new SimpleCache(
                context.getCacheDir(),
                new LeastRecentlyUsedCacheEvictor(cacheSize)
            );
        }

        return sCache;
    }
//
    /**
     * Calculates the amount of the visibility of the {@link androidx.media3.ui.PlayerView}
     * on the screen.
     * Used to determine the visibility area ratio (a value between 0.0 and 1.0), so that further
     * playback management related actions can be taken by the host {@link PlayableItemsContainer}.
     *
     * @param playable the playable
     * @return the visibility are offset (a value between 0.0 and 1.0)
     */
    @FloatRange(from = 0.0, to = 1.0)
    public static float getVisibleAreaOffset(@NonNull Playable playable) {
        Preconditions.nonNull(playable);

        if (playable.getParent() == null) {
            return 0f;
        }

        final View playerView = playable.getPlayerView();

        final Rect drawRect = new Rect();
        final Rect playerRect = new Rect();

        playerView.getDrawingRect(drawRect);

        final int drawArea = (drawRect.width() * drawRect.height());
        final boolean isVisible = playerView.getGlobalVisibleRect(playerRect, new Point());

        if (isVisible && (drawArea > 0)) {
            final int visibleArea = (playerRect.height() * playerRect.width());
            return (visibleArea / (float) drawArea);
        }

        return 0f;
    }

}
