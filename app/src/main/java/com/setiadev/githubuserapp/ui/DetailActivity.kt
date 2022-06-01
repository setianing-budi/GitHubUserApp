package com.setiadev.githubuserapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.setiadev.githubuserapp.models.ActionResult
import com.setiadev.githubuserapp.R
import com.setiadev.githubuserapp.apis.DetailUserResponse
import com.setiadev.githubuserapp.databinding.ActivityDetailBinding
import com.setiadev.githubuserapp.models.DetailViewModel
import com.setiadev.githubuserapp.TabAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "User Detail"

        val username = intent.getStringExtra("username") ?: ""
        val id = intent.getIntExtra("id", 0)
        val avatar = intent.getStringExtra("avatar") ?: ""

        viewModel.actionDetail.observe(this) {
            when (it) {
                is ActionResult.Success<*> -> {
                    val userData = it.data as DetailUserResponse
                    binding.detailImage.load((userData.avatar_url))
                    binding.fullName.text = userData.name as CharSequence?
                    binding.username.text = userData.login
                    binding.company.text = userData.company as CharSequence?
                    binding.detailLocation.text = userData.location as CharSequence?
                    binding.detailFollowers.text = userData.followers.toString()
                    binding.detailFollowing.text = userData.following.toString()
                    binding.repository.text = userData.public_repos.toString()
                }
                is ActionResult.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is ActionResult.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }
        viewModel.getDetailUser(username)

        var isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.userChecking(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.favToggle.isChecked = true
                        isChecked = true
                    } else {
                        binding.favToggle.isChecked = false
                        isChecked = false
                    }
                }
            }
        }

        binding.favToggle.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                viewModel.addToFav(username, id, avatar)
            } else {
                viewModel.removeFromFav(id)
            }
            binding.favToggle.isChecked = isChecked
        }

        val fragment = mutableListOf<Fragment>(
            FollowFragment.newInstance(FollowFragment.FOLLOWERS),
            FollowFragment.newInstance(FollowFragment.FOLLOWING)
        )
        val fragmentTitle = mutableListOf(
            getString(R.string.followers),
            getString(R.string.following)
        )
        val tabAdapter = TabAdapter(this, fragment)
        binding.viewPager.adapter = tabAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tabLayout, position ->
            tabLayout.text = fragmentTitle[position]
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    viewModel.getUserFollowers(username)
                } else {
                    viewModel.getUserFollowing(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        viewModel.getUserFollowers(username)
    }
}