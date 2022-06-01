package com.setiadev.githubuserapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.setiadev.githubuserapp.models.ActionResult
import com.setiadev.githubuserapp.models.DetailViewModel
import com.setiadev.githubuserapp.ListUserAdapter
import com.setiadev.githubuserapp.apis.UserResponse
import com.setiadev.githubuserapp.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    private var binding: FragmentFollowBinding? = null
    private val  userAdapter by lazy {
        ListUserAdapter {

        }
    }

    private val viewModel by activityViewModels<DetailViewModel>()
    var type = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(layoutInflater)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvFollow?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowFragment.userAdapter
        }

        when (type) {
            FOLLOWERS -> {
                viewModel.actionFollowers.observe(viewLifecycleOwner, this::actionFollowManager)
            }
            FOLLOWING -> {
                viewModel.actionFollowing.observe(viewLifecycleOwner, this::actionFollowManager)
            }
        }
    }

    private fun actionFollowManager(status: ActionResult) {
        when (status) {
            is ActionResult.Success<*> -> {
                userAdapter.setData(status.data as MutableList<UserResponse.Item>)
            }
            is ActionResult.Error -> {
                Toast.makeText(requireActivity(), status.exception.message.toString(), Toast.LENGTH_SHORT).show()
            }
            is ActionResult.Loading -> {
                binding?.progressBar?.isVisible = status.isLoading
            }
        }
    }

    companion object {
        const val FOLLOWERS = 1
        const val FOLLOWING = 10

        fun newInstance(type: Int) = FollowFragment().apply {
            this.type = type
        }
    }
}