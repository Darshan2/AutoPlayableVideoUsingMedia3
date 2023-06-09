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

package com.example.videoplayerusingmedia3.demo

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.example.videoplayerusingmedia3.PlayerProviderImpl

class ArviApplication : MultiDexApplication() {

    companion object {

        @JvmStatic
        lateinit var INSTANCE: ArviApplication
            private set

    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        if (level >= TRIM_MEMORY_BACKGROUND) {
            PlayerProviderImpl.getInstance(this).release()
        }
    }

}
