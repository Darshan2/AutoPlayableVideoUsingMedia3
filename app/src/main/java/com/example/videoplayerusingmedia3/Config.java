package com.example.videoplayerusingmedia3;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.cache.Cache;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.LoadControl;
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter;

import com.example.videoplayerusingmedia3.player.util.BaseMeter;
import com.example.videoplayerusingmedia3.util.misc.Preconditions;

public final class Config {

    @DefaultRenderersFactory.ExtensionRendererMode
    public final int extensionMode;

    @NonNull
    public final BaseMeter<?, ?> meter;

    @NonNull
    public final LoadControl loadControl;

    @Nullable
    public final Cache cache;

    @Nullable
    public final DataSource.Factory dataSourceFactory;

    private Config(Builder builder) {
        this.extensionMode = builder.extensionMode;
        this.meter = builder.meter;
        this.loadControl = builder.loadControl;
        this.cache = builder.cache;
        this.dataSourceFactory = builder.dataSourceFactory;
    }

    /**
     * Determines if the {@link Cache} is set.
     */
    public final boolean hasCache() {
        return (this.cache != null);
    }

    /**
     * Determines if the {@link DataSource.Factory} is set.
     */
    public final boolean hasDataSourceFactory() {
        return (this.dataSourceFactory != null);
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 17;
        result = ((prime * result) + this.extensionMode);
        result = ((prime * result) + this.meter.hashCode());
        result = ((prime * result) + this.loadControl.hashCode());
        result = ((prime * result) + (hasCache() ? this.cache.hashCode() : 0));
        result = ((prime * result) + (hasDataSourceFactory() ? this.dataSourceFactory.hashCode() : 0));

        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        return ((obj instanceof Config) && (obj.hashCode() == hashCode()));
    }


    @UnstableApi
    public static final class Builder {

        private final DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        private int extensionMode;

        private BaseMeter<?, ?> meter;
        private LoadControl loadControl;

        private Cache cache;
        private DataSource.Factory dataSourceFactory;

        public Builder() {
            this.extensionMode = DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
            this.meter = new BaseMeter<>(bandwidthMeter, bandwidthMeter);
            this.loadControl = new DefaultLoadControl();
            this.cache = null;
            this.dataSourceFactory = null;
        }

        public Builder extensionMode(@DefaultRenderersFactory.ExtensionRendererMode int extensionMode) {
            this.extensionMode = extensionMode;
            return this;
        }

        public Builder meter(@NonNull BaseMeter<?, ?> meter) {
            this.meter = Preconditions.checkNonNull(meter);
            return this;
        }

        public Builder loadControl(@NonNull LoadControl loadControl) {
            this.loadControl = Preconditions.checkNonNull(loadControl);
            return this;
        }


        public Builder cache(@Nullable Cache cache) {
            this.cache = cache;
            return this;
        }

        public Builder dataSourceFactory(@Nullable DataSource.Factory dataSourceFactory) {
            this.dataSourceFactory = dataSourceFactory;
            return this;
        }

        public Config build() {
            return new Config(this);
        }

    }

}

