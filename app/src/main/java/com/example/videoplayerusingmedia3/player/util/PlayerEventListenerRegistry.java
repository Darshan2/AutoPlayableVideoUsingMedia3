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

package com.example.videoplayerusingmedia3.player.util;

import androidx.annotation.NonNull;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Tracks;

import com.example.videoplayerusingmedia3.player.Player;
import com.example.videoplayerusingmedia3.util.misc.Preconditions;

import java.util.HashSet;
import java.util.Set;

/**
 * A manager for the {@link Player.EventListener}s. Used to observe the {@link androidx.media3.common.Player.Listener} events
 * and propagate them to all the subscribed {@link Player.EventListener}s.
 */
public final class PlayerEventListenerRegistry implements androidx.media3.common.Player.Listener {

    private final Set<Player.EventListener> mEventListeners;

    public PlayerEventListenerRegistry() {
        mEventListeners = new HashSet<>();
    }

    public final void addListener(@NonNull Player.EventListener eventListener) {
        Preconditions.nonNull(eventListener);

        mEventListeners.add(eventListener);
    }

    public final void removeListener(@NonNull Player.EventListener eventListener) {
        Preconditions.nonNull(eventListener);

        mEventListeners.remove(eventListener);
    }

    public final void removeAllListeners() {
        mEventListeners.clear();
    }

    @Override
    public void onTracksChanged(@NonNull Tracks tracks) {
        for (Player.EventListener eventListener : mEventListeners) {
            eventListener.onTracksChanged(tracks);
        }
    }

    @Override
    public void onIsLoadingChanged(boolean isLoading) {
        for (Player.EventListener eventListener : mEventListeners) {
            eventListener.onLoadingChanged(isLoading);
        }
    }

    @Override
    public void onPlaybackStateChanged(int playbackState) {
        for (Player.EventListener eventListener : mEventListeners) {
            eventListener.onPlayerStateChanged(playbackState);
        }
    }

    @Override
    public void onPlayerError(@NonNull PlaybackException error) {
        for (Player.EventListener eventListener : mEventListeners) {
            eventListener.onPlayerError(error);
        }
    }


}
