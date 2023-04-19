package com.example.videoplayerusingmedia3.player.creator;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;

import com.example.videoplayerusingmedia3.player.Player;

/**
 * A base contract to be implemented by the concrete implementations
 * of the {@link PlayerCreator}s.
 * Should be able to create the {@link Player}s and related {@link MediaItem}s.
 */
public interface PlayerCreator {

    /**
     * Creates a brand-new {@link Player} instance.
     *
     * @return the created {@link Player} instance
     */
    @NonNull
    Player createPlayer();

    /**
     * Creates a {@link MediaItem} for the specified {@link Uri}.
     *
     * @param uri the media uri
     * @return the created {@link MediaItem}
     */
    @NonNull
    MediaItem createMediaItem(@NonNull Uri uri);

}
