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

package com.example.videoplayerusingmedia3.player.creator;


import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultDataSourceFactory;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.LoadControl;
import androidx.media3.exoplayer.RenderersFactory;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.MediaSourceFactory;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.exoplayer.trackselection.TrackSelector;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import androidx.media3.ui.PlayerView;

import com.example.videoplayerusingmedia3.player.AdSupportedPlayer;
import com.example.videoplayerusingmedia3.player.DefaultPlayer;
import com.example.videoplayerusingmedia3.player.Player;
import com.example.videoplayerusingmedia3.util.misc.Preconditions;
import com.example.videoplayerusingmedia3.Config;
import com.example.videoplayerusingmedia3.PlayerProvider;

/**
 * A concrete implementation of the {@link PlayerCreator}, used internally
 * by the {@link PlayerProvider} to create the {@link Player}s based on the
 * specific {@link Config}s.
 */
@UnstableApi
public final class DefaultPlayerCreator implements PlayerCreator {

    public final PlayerProvider playerProvider;

    private final TrackSelector trackSelector;
    private final RenderersFactory renderersFactory;
    private final LoadControl loadControl;
    private final BandwidthMeter bandwidthMeter;
    private final DefaultMediaSourceFactory mediaSourceFactory;


    public DefaultPlayerCreator(@NonNull PlayerProvider playerProvider, @NonNull Config config) {
        Preconditions.nonNull(playerProvider);
        Preconditions.nonNull(config);

        this.playerProvider = Preconditions.checkNonNull(playerProvider);
        this.trackSelector = new DefaultTrackSelector(playerProvider.getContext());
        this.loadControl = config.loadControl;
        this.bandwidthMeter = config.meter;
        this.renderersFactory = new DefaultRenderersFactory(playerProvider.getContext());
        this.mediaSourceFactory = createMediaSourceFactory(playerProvider, config);
    }

    private DefaultMediaSourceFactory createMediaSourceFactory(PlayerProvider playerProvider, Config config) {
        DataSource.Factory baseFactory = config.dataSourceFactory;

        if (baseFactory == null) {
            baseFactory = new DefaultDataSource.Factory(playerProvider.getContext())
                    .setTransferListener(config.meter);
        }

        DataSource.Factory factory = new DefaultDataSource.Factory(playerProvider.getContext(), baseFactory)
                .setTransferListener(config.meter);

        if (config.cache != null) {
            factory = new CacheDataSource.Factory()
                    .setCache(config.cache)
                    .setUpstreamDataSourceFactory(factory);
        }

        return new DefaultMediaSourceFactory(playerProvider.getContext())
                .setDataSourceFactory(factory);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public final Player createPlayer() {
        return new DefaultPlayer(
            this.playerProvider.getContext(),
            this.renderersFactory,
            this.trackSelector,
            this.loadControl,
            this.mediaSourceFactory,
            this.bandwidthMeter
        );
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public final Player createAdSupportedPlayer(@NonNull PlayerView playerView) {
        return new AdSupportedPlayer(
                this.playerProvider.getContext(),
                this.renderersFactory,
                this.trackSelector,
                this.loadControl,
                this.mediaSourceFactory,
                playerView,
                this.bandwidthMeter
        );
    }

    @NonNull
    @Override
    public final MediaItem createMediaItem(@NonNull Uri uri) {
        return MediaItem.fromUri(uri);
    };

    @NonNull
    @Override
    public final MediaItem createAdSupportedMediaItem(@NonNull Uri videoUri, @NonNull Uri adTagUri) {
        return new MediaItem.Builder()
                        .setUri(videoUri)
                        .setAdsConfiguration(new MediaItem.AdsConfiguration.Builder(adTagUri).build())
                        .build();
    };


    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 17;
        result = ((prime * result) + this.playerProvider.hashCode());
        result = ((prime * result) + this.trackSelector.hashCode());
        result = ((prime * result) + this.loadControl.hashCode());
        result = ((prime * result) + this.renderersFactory.hashCode());
        result = ((prime * result) + this.mediaSourceFactory.hashCode());

        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        return ((obj instanceof DefaultPlayerCreator) && (obj.hashCode() == hashCode()));
    }

}
