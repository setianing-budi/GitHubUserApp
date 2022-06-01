 package com.setiadev.githubuserapp.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.setiadev.githubuserapp.models.ActionResult
import com.setiadev.githubuserapp.ListUserAdapter
import com.setiadev.githubuserapp.models.MainViewModel
import com.setiadev.githubuserapp.R
import com.setiadev.githubuserapp.apis.UserResponse
import com.setiadev.githubuserapp.databinding.ActivityMainBinding
import com.setiadev.githubuserapp.settings.SettingsActivity

 class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userAdapter by lazy {
        ListUserAdapter {
            Intent(this, DetailActivity::class.java).apply {
                putExtra("username", it.login)
                putExtra("id", it.id)
                putExtra("avatar", it.avatar_url)
                startActivity(this)
            }
        }
    }

     private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.adapter = userAdapter
        showRecyclerList()

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getUser(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        viewModel.actionUser.observe(this) {
            when (it) {
                is ActionResult.Success<*> -> {
                    userAdapter.setData(it.data as MutableList<UserResponse.Item>)
                }
                is ActionResult.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is ActionResult.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }
        viewModel.getUser()
    }

     private fun showRecyclerList() {
         if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
             binding.rvUsers.layoutManager = GridLayoutManager(this, 2)
         } else {
             binding.rvUsers.layoutManager = LinearLayoutManager(this)
         }
     }

     override fun onCreateOptionsMenu(menu: Menu): Boolean {
         menuInflater.inflate(R.menu.option_menu, menu)
         return super.onCreateOptionsMenu(menu)
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
             R.id.fav_appbar -> {
                 Intent(this, FavoriteActivity::class.java).also {
                     startActivity(it)
                 }
             }
             R.id.settings -> {
                 Intent(this, SettingsActivity::class.java).also {
                     startActivity(it)
                 }
             }
         }
         return super.onOptionsItemSelected(item)
     }
}