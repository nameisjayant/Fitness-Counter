package com.example.fitnesscounter.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.fitnesscounter.R
import com.example.fitnesscounter.databinding.FragmentSignupBinding
import com.example.fitnesscounter.ui.viewmodels.UserViewModel
import com.example.fitnesscounter.utils.ApiStates
import com.example.fitnesscounter.utils.Constants
import com.example.fitnesscounter.utils.showMsg
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel: UserViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(layoutInflater, container, false)
        binding.login.setOnClickListener {
            view?.findNavController()
                ?.navigate(R.id.action_signupFragment2_to_loginFragment2)
        }

        binding.signup.setOnClickListener {
            createUser()
        }
        return binding.root
    }

    private fun createUser() {
        binding.apply {
            lifecycleScope.launchWhenStarted {
                if (!TextUtils.isEmpty(username.text) && !TextUtils.isEmpty(email.text) && !TextUtils.isEmpty(
                        password.text
                    )
                ) {
                    viewModel.createUser(
                        username.text.toString(),
                        email.text.toString(),
                        password.text.toString()
                    ).collect {
                        when (it) {
                            is ApiStates.Success -> {
                                requireActivity().showMsg("User created")
                                viewModel.setPref(Constants.userId, it.data.data.id.toString())
                                viewModel.setPref(Constants.token, it.data.data.token)
                                viewModel.setPref(Constants.email, it.data.data.email)
                                viewModel.setPref(Constants.username, it.data.data.username)

                            }

                            is ApiStates.Failure -> {
                                Log.d("main", it.msg)
                            }

                            ApiStates.Loading -> {
                                requireActivity().showMsg("loading..")
                            }
                        }
                    }
                } else {
                    requireActivity().showMsg("please fill all the fields..")
                }
            }
        }
    }
}