package com.example.videoplayerusingmedia3.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.videoplayerusingmedia3.R
import com.example.videoplayerusingmedia3.widget.PlayableItemsContainer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val videoFragment = BasicVideosFragment.newInstance(PlayableItemsContainer.AutoplayMode.ONE_AT_A_TIME)
        supportFragmentManager.beginTransaction().replace(R.id.container, videoFragment).commit()
    }
}