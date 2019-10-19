package com.wk.imageloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = mutableListOf<String>()
        for (i in 0 until 1000) {
            list.add("https://static.mojieai.com/sb/ng/0527/banner/4%20spin.png")
        }
        listImg.adapter = ImgAdapter(this, list)
    }
}
