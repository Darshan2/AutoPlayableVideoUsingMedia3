package com.example.videoplayerusingmedia3.player.util;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.upstream.BandwidthMeter;

/**
 * A utility used for bandwidth metering, as well as observation of the data transfer-related events.
 */
@UnstableApi
public final class BaseMeter<T extends BandwidthMeter, S extends TransferListener> implements BandwidthMeter, TransferListener {

    @NonNull
    final T bandwidthMeter;

    @NonNull
    final S transferListener;

    @SuppressWarnings("WeakerAccess")
    public BaseMeter(@NonNull T bandwidthMeter, @NonNull S transferListener) {
        this.bandwidthMeter = bandwidthMeter;
        this.transferListener = transferListener;
    }

    @OptIn(markerClass = UnstableApi.class) @Override
    public final void addEventListener(Handler eventHandler, EventListener eventListener) {
        this.bandwidthMeter.addEventListener(eventHandler, eventListener);
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public final void removeEventListener(EventListener eventListener) {
        this.bandwidthMeter.removeEventListener(eventListener);
    }

    @OptIn(markerClass = UnstableApi.class)
    @Nullable
    @Override
    public final TransferListener getTransferListener() {
        return this.bandwidthMeter.getTransferListener();
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public final long getBitrateEstimate() {
        return this.bandwidthMeter.getBitrateEstimate();
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public final void onTransferInitializing(DataSource dataSource,
                                             DataSpec dataSpec,
                                             boolean isNetwork) {
        this.transferListener.onTransferInitializing(
                dataSource,
                dataSpec,
                isNetwork
        );
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public final void onTransferStart(DataSource dataSource,
                                      DataSpec dataSpec,
                                      boolean isNetwork) {
        this.transferListener.onTransferStart(
                dataSource,
                dataSpec,
                isNetwork
        );
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public final void onBytesTransferred(DataSource dataSource,
                                         DataSpec dataSpec,
                                         boolean isNetwork,
                                         int bytesTransferred) {
        this.transferListener.onBytesTransferred(
                dataSource,
                dataSpec,
                isNetwork,
                bytesTransferred
        );
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public final void onTransferEnd(DataSource dataSource,
                                    DataSpec dataSpec,
                                    boolean isNetwork) {
        this.transferListener.onTransferEnd(
                dataSource,
                dataSpec,
                isNetwork
        );
    }

}

