package com.zaen.githubuser.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.zaen.githubuser.util.Constants
import com.zaen.githubuser.util.Resource
import kotlinx.android.synthetic.main.fragment_github_follow.*

class GithubFollowingsFragment : BaseGithubFollowFragment() {

    private val TAG = "GithubFollowingsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAndUpdateFollowingUser()

        arguments?.takeIf { it.containsKey(Constants.FOLLOW_ARG_OBJECT_USERNAME) }?.apply {
            val username = getString(Constants.FOLLOW_ARG_OBJECT_USERNAME)

            username?.let {
                githubUsersViewModel.getFollowingsGithubUser(it)
            }
        }
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun observeAndUpdateFollowingUser() {
        githubUsersViewModel.followingsGithubUser.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { githubUsersResponse ->
                        followAdapter.differ.submitList(githubUsersResponse.toList())
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }
}