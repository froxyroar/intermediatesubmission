package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.pref.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding
import com.google.gson.Gson

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyJson = intent.getStringExtra("STORY")
        val story = Gson().fromJson(storyJson, ListStoryItem::class.java)

        binding.loadingProgressBar.visibility = View.VISIBLE
        story?.let {
            binding.tvDetailName.text = it.name
            binding.tvDetailDescription.text = it.description
            Glide.with(this).load(it.photoUrl).into(binding.ivDetailPhoto)
        }
        binding.loadingProgressBar.visibility = View.GONE
    }
}