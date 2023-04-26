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

package com.example.videoplayerusingmedia3.player;


import static com.example.videoplayerusingmedia3.util.misc.Preconditions.checkNonNull;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.LoadControl;
import androidx.media3.exoplayer.RenderersFactory;
import androidx.media3.exoplayer.ima.ImaAdsLoader;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.source.MediaSourceFactory;
import androidx.media3.exoplayer.trackselection.TrackSelector;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import androidx.media3.ui.PlayerView;

import com.example.videoplayerusingmedia3.player.util.DefaultVolumeController;
import com.example.videoplayerusingmedia3.player.util.PlayerEventListenerRegistry;
import com.example.videoplayerusingmedia3.player.util.VolumeController;
import com.example.videoplayerusingmedia3.util.misc.Preconditions;


/**
 * A default implementation of the {@link Player} which has the most essential player-related
 * handling functionality implemented for you.
 */
public class AdSupportedPlayer implements Player {

    private final Context context;

    private final PlayerEventListenerRegistry eventHandler;

    private final RenderersFactory renderersFactory;
    private final TrackSelector trackSelector;
    private final LoadControl loadControl;
    private final BandwidthMeter bandwidthMeter;
    private final DefaultMediaSourceFactory mediaSourceFactory;
    private MediaItem mediaItem;

    private ExoPlayer exoPlayer;
    private VolumeController volumeController;

    private final PlayerView playerView;
    private ImaAdsLoader adsLoader;

    private AttachmentStateDelegate attachmentStateDelegate;

    public AdSupportedPlayer(@NonNull Context context,
                             @NonNull RenderersFactory renderersFactory,
                             @NonNull TrackSelector trackSelector,
                             @NonNull LoadControl loadControl,
                             @NonNull DefaultMediaSourceFactory mediaSourceFactory,
                             @NonNull PlayerView playerView,
                             @Nullable BandwidthMeter bandwidthMeter) {

        this.context = checkNonNull(context).getApplicationContext();
        this.eventHandler = new PlayerEventListenerRegistry();
        this.renderersFactory = checkNonNull(renderersFactory);
        this.trackSelector = checkNonNull(trackSelector);
        this.loadControl = checkNonNull(loadControl);
        this.mediaSourceFactory = checkNonNull(mediaSourceFactory);
        this.bandwidthMeter = bandwidthMeter;
        this.playerView = playerView;
    }

    @OptIn(markerClass = UnstableApi.class)
    private ImaAdsLoader getAdsLoader() {
        if(adsLoader == null) {
            adsLoader = new ImaAdsLoader.Builder(context)
                    .setVastLoadTimeoutMs(5000)
                    .build();
        }
        return adsLoader;
    }

    @OptIn(markerClass = UnstableApi.class) @Override
    public final void init() {
        if (isInitialized()) {
            return;
        }

        DefaultMediaSourceFactory mediaSourceFactory = this.mediaSourceFactory
                        .setLocalAdInsertionComponents(adsConfiguration -> getAdsLoader(), playerView);

        this.exoPlayer = new ExoPlayer.Builder(this.context, this.renderersFactory)
                .setMediaSourceFactory(mediaSourceFactory)
                .setTrackSelector(this.trackSelector)
                .setLoadControl(this.loadControl)
                .setBandwidthMeter(this.bandwidthMeter)
                .build();

        this.exoPlayer.addListener(this.eventHandler);
        this.volumeController = new DefaultVolumeController(this.exoPlayer);
    }

    @Override
    public final void prepare(final boolean resetPosition) {
        checkPlayerState();
        checkMediaItem();

        this.exoPlayer.setMediaItem(this.mediaItem, resetPosition);
        this.exoPlayer.prepare();
    }

    @Override
    public final void play() {
        checkPlayerState();

        this.exoPlayer.setPlayWhenReady(true);
    }

    @Override
    public final void pause() {
        checkPlayerState();

        this.exoPlayer.setPlayWhenReady(false);
    }

    /**
        Use stop() and clearMediaItems() (if reset is true) or just stop() (if reset is false).
        Any player error will be cleared when re-preparing the player.
        @param resetPosition whether to reset the playback position
     */
    @Override
    public final void stop(final boolean resetPosition) {
        checkPlayerState();
        if(resetPosition) {
            exoPlayer.stop();
            exoPlayer.clearMediaItems();
        } else {
            exoPlayer.stop();
        }

    }

    @Override
    public final void seek(final long positionInMillis) {
        checkPlayerState();

        this.exoPlayer.seekTo(positionInMillis);
    }

    @Override
    public final void release() {
        if (!isInitialized()) {
            return;
        }

        this.exoPlayer.release();
        this.exoPlayer = null;
        this.attachmentStateDelegate = null;

        removeAllEventListeners();
    }

    @Override
    public final void attach(@NonNull final PlayerView playerView) {
        Preconditions.nonNull(playerView);
        checkPlayerState();

        playerView.setPlayer(this.exoPlayer);
        getAdsLoader().setPlayer(this.exoPlayer);
    }

    @Override
    public final void detach(@NonNull PlayerView playerView) {
        Preconditions.nonNull(playerView);
        checkPlayerState();

        if(adsLoader != null) adsLoader.setPlayer(null);
        playerView.setPlayer(null);
    }

    @Override
    public final void postAttachedEvent() {
        if (this.attachmentStateDelegate != null) {
            this.attachmentStateDelegate.onAttach(this);
        }
    }

    @Override
    public final void postDetachedEvent() {
        if (this.attachmentStateDelegate != null) {
            this.attachmentStateDelegate.onDetach(this);
        }
    }

    private void checkPlayerState() {
        if (!isInitialized()) {
            throw new IllegalStateException("The Player must be initialized first.");
        }
    }

    private void checkMediaItem() {
        if (this.mediaItem == null) {
            throw new IllegalStateException("The Media Source is required.");
        }
    }

    @Override
    public final void setAttachmentStateDelegate(@Nullable AttachmentStateDelegate attachmentStateDelegate) {
        this.attachmentStateDelegate = attachmentStateDelegate;
    }

    @Override
    public final void addEventListener(@NonNull EventListener eventListener) {
        Preconditions.nonNull(eventListener);

        this.eventHandler.addListener(eventListener);
    }

    @Override
    public final void removeEventListener(@NonNull EventListener eventListener) {
        Preconditions.nonNull(eventListener);

        this.eventHandler.removeListener(eventListener);
    }

    @Override
    public final void removeAllEventListeners() {
        this.eventHandler.removeAllListeners();
    }

    @Override
    public final void setMediaItem(@NonNull MediaItem mediaItem) {
        this.mediaItem = checkNonNull(mediaItem);
    }

    @Nullable
    @Override
    public final MediaItem getMediaItem() {
        return this.mediaItem;
    }

    @NonNull
    @Override
    public final VolumeController getVolumeController() {
        checkPlayerState();
        return this.volumeController;
    }

    @Override
    public final int getPlaybackState() {
        return (isInitialized() ? this.exoPlayer.getPlaybackState() : PlaybackState.IDLE);
    }

    @Override
    public final long getPlaybackPosition() {
        return (isInitialized() ? this.exoPlayer.getCurrentPosition() : 0L);
    }

    @Override
    public final long getDuration() {
        return (isInitialized() ? this.exoPlayer.getDuration() : 0L);
    }

    @Override
    public final float getBufferedPercentage() {
        return (isInitialized() ? this.exoPlayer.getBufferedPercentage() : 0L);
    }

    @Override
    public final boolean isLooping() {
        return exoPlayer.getRepeatMode() == androidx.media3.common.Player.REPEAT_MODE_ONE ||
                exoPlayer.getRepeatMode() == androidx.media3.common.Player.REPEAT_MODE_ALL;
    }

    @Override
    public final boolean isInitialized() {
        return (this.exoPlayer != null);
    }

    @Override
    public final boolean isPlaying() {
        final int playbackState = getPlaybackState();

        return (
            isInitialized()
                && this.exoPlayer.getPlayWhenReady()
                && (playbackState != PlaybackState.IDLE)
                && ((playbackState != PlaybackState.ENDED) || isLooping())
        );
    }

    @Override
    public final boolean isAttached(@NonNull PlayerView playerView) {
        Preconditions.nonNull(playerView);
        return ((playerView.getPlayer() != null) && (playerView.getPlayer() == this.exoPlayer));
    }

    @Override
    public final boolean isAttached() {
        return ((this.attachmentStateDelegate != null) && this.attachmentStateDelegate.isAttached(this));
    }

}
