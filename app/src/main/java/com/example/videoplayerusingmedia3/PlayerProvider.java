package com.example.videoplayerusingmedia3;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.ui.PlayerView;

import com.example.videoplayerusingmedia3.player.Player;

/**
 * Defines a base contract for the concrete {@link PlayerProvider} implementations.
 */
public interface PlayerProvider {

    /**
     * Creates the a non-looping {@link MediaItem}
     *
     * @param uri the uri to create the media source for
     * @return the created media source
     */
    @NonNull
    MediaItem createMediaItem(@NonNull Uri uri);


    /**
     * Creates the a non-looping {@link MediaItem}
     *
     * @param config the player configuration
     * @param uri    the uri to created the media source for
     * @return the created media source
     */
    @NonNull
    MediaItem createMediaItem(@NonNull Config config, @NonNull Uri uri);

    /**
     * Creates the a non-looping {@link MediaItem}
     *
     * @param uri    the uri to created the media source for
     * @return the created media source
     */
    @NonNull
    MediaItem createAdSupportedMediaItem(@NonNull Uri uri, @NonNull Uri adTagUri);

    /**
     * Creates the a non-looping {@link MediaItem}
     *
     * @param config the player configuration
     * @param uri    the uri to created the media source for
     * @return the created media source
     */
    @NonNull
    MediaItem createAdSupportedMediaItem(@NonNull Config config, @NonNull Uri uri, @NonNull Uri adTagUri);


    /**
     * Retrieves the application name.
     *
     * @return the application name
     */
    @NonNull
    String getApplicationName();

    /**
     * Retrieves the application {@link Context}.
     *
     * @return the application context
     */
    @NonNull
    Context getContext();

    /**
     * Retrieves an existing {@link Player} instance for the specified key, if there's any.
     * Uses the default {@link Config}.
     *
     * @param key the key to retrieve the player for
     * @return the retrieved Player, or <strong>null</strong> if no Player was found.
     */
    @Nullable
    Player getPlayer(@NonNull String key);

    /**
     * Retrieves an existing {@link Player} instance for the specified key and Player {@link Config}, if there's any.
     *
     * @param config the player configuration
     * @param key    the key to retrieve the player for
     * @return the retrieved Player, or <strong>null</strong> if no Player was found.
     */
    @Nullable
    Player getPlayer(@NonNull Config config, @NonNull String key);

    /**
     * Retrieves an existing or create a brand-new {@link Player} instance for the specified key.
     * Users the default player {@link Config}.
     *
     * @param key the key to retrieve the player for
     * @return the retrieved or created Player
     */
    @NonNull
    Player getOrInitPlayer(@NonNull String key);

    /**
     * Retrieves an existing or create a brand-new {@link Player} instance for the specified key and Player {@link Config}.
     *
     * @param config the player configuration
     * @param key    the key to retrieve the player for
     * @return the retrieved or created Player
     */
    @NonNull
    Player getOrInitPlayer(@NonNull Config config, @NonNull String key);

    /**
     * Retrieves an existing or create a brand-new {@link Player} instance for the specified key and Player {@link Config}.
     *
     * @param config the player configuration
     * @param key    the key to retrieve the player for
     * @return the retrieved or created Player
     */
    @NonNull
    Player getOrInitAdSupportedPlayer(@NonNull Config config, @NonNull String key, PlayerView playerView);

    /**
     * Checks if there's a {@link Player} available for the specified key.
     * Uses the default Player {@link Config}.
     *
     * @param key the key to check the player presence for
     * @return <strong>true</strong> if the player is available, <strong>false</strong> otherwise
     */
    boolean hasPlayer(@NonNull String key);

    /**
     * Checks if there's a {@link Player} available for the specified key and Player {@link Config}.
     *
     * @param config the player configuration
     * @param key    the key to check the player presence for
     * @return <strong>true</strong> if the player is available, <strong>false</strong> otherwise
     */
    boolean hasPlayer(@NonNull Config config, @NonNull String key);

    /**
     * Unregisters the {@link Player}, thus making it available within the Player Pool as a "free" Player.
     * Uses the default Player {@link Config}.
     *
     * @param key the key to unregister the Player for
     */
    void unregister(@NonNull String key);

    /**
     * Unregisters the {@link Player}, thus making it available within the Player Pool as a "free" Player.
     *
     * @param config the player configuration
     * @param key    the key to unregister the Player for
     */
    void unregister(@NonNull Config config, @NonNull String key);

    /**
     * Releases the {@link Player} for the specified key.
     * Uses the default Player {@link Config}.
     *
     * @param key the key to release the Player for
     */
    void release(@NonNull String key);

    /**
     * Releases all the {@link Player}s that match the specified {@link Config}.
     *
     * @param config the player configuration
     */
    void release(@NonNull Config config);

    /**
     * Releases the {@link Player} for the specified key and {@link Config}.
     *
     * @param config the player configuration
     * @param key    the key to release the Player for
     */
    void release(@NonNull Config config, @NonNull String key);

    /**
     * Releases all the currently available (initialized) {@link Player}s.
     */
    void release();

}

