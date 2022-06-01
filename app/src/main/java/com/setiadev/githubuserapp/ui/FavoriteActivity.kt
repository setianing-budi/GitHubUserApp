package com.setiadev.githubuserapp.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.setiadev.githubuserapp.ListUserAdapter
import com.setiadev.githubuserapp.apis.UserResponse
import com.setiadev.githubuserapp.databinding.ActivityFavoriteBinding
import com.setiadev.githubuserapp.models.FavoriteViewModel
import com.setiadev.githubuserapp.local.FavUser

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val userAdapter by lazy {
        ListUserAdapter{
            Intent(this, DetailActivity::class.java).apply {
                putExtra("username", it.login)
                putExtra("id", it.id)
                putExtra("avatar", it.avatar_url)
                startActivity(this)
            }
        }
    }
    private val viewModel by viewModels<FavoriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favorite"

        binding.rvFollow.setHasFixedSize(true)
        binding.rvFollow.adapter = userAdapter
        userAdapter.notifyDataSetChanged()
        showRecyclerList()
    }

    private fun showRecyclerList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvFollow.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvFollow.layoutManager = LinearLayoutManager(this)
        }

        viewModel.getFavUser()?.observe(this) {
            if (it != null) {
                val updateList = mapList(it)
                userAdapter.setData(updateList)
            }
        }
    }

    private fun mapList(users: List<FavUser>): ArrayList<UserResponse.Item> {
        val listUsers = ArrayList<UserResponse.Item>()
        for (user in users) {
            val userMapped = UserResponse.Item(
                user.avatar,
                user.id,
                user.login
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }
}