package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.addstory.AddStoryActivity
import com.dicoding.picodiploma.loginwithanimation.view.main.paging.StoryPagingAdapter
import com.dicoding.picodiploma.loginwithanimation.view.map.MapsActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyPagingAdapter: StoryPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyPagingAdapter = StoryPagingAdapter()

        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = storyPagingAdapter

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_add_story -> {
                    startActivity(Intent(this, AddStoryActivity::class.java))
                    true
                }
                R.id.action_logout -> {
                    binding.loadingProgressBar.visibility = View.VISIBLE
                    viewModel.logout()
                    binding.loadingProgressBar.visibility = View.GONE
                    startActivity(Intent(this, WelcomeActivity::class.java))
                    finish()
                    true
                }
                R.id.action_maps -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                lifecycleScope.launch {
                    viewModel.getPagingStories("Bearer ${user.token}").collectLatest { pagingData ->
                        storyPagingAdapter.submitData(pagingData)
                    }
                }
            }
        }

        setupView()

        storyPagingAdapter.addLoadStateListener { loadState ->
            binding.loadingProgressBar.visibility = if (loadState.source.refresh is LoadState.Loading) View.VISIBLE else View.GONE
            if (loadState.source.refresh is LoadState.Error) {
                Toast.makeText(this, (loadState.source.refresh as LoadState.Error).error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}
