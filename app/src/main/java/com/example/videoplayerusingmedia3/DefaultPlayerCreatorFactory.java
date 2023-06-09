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

package com.example.videoplayerusingmedia3;

import androidx.annotation.NonNull;

import com.example.videoplayerusingmedia3.player.creator.DefaultPlayerCreator;
import com.example.videoplayerusingmedia3.player.creator.PlayerCreator;
import com.example.videoplayerusingmedia3.util.misc.Preconditions;

/**
 * A default implementation of the {@link PlayerCreatorFactory}.
 */
public final class DefaultPlayerCreatorFactory implements PlayerCreatorFactory {

    @NonNull
    @Override
    public final PlayerCreator create(@NonNull PlayerProvider playerProvider, @NonNull Config config) {
        Preconditions.checkNonNull(playerProvider);
        Preconditions.checkNonNull(config);

        return new DefaultPlayerCreator(playerProvider, config);
    }

}
