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
import com.example.fitnesscounter.databinding.FragmentLoginBinding
import com.example.fitnesscounter.ui.viewmodels.UserViewModel
import com.example.fitnesscounter.utils.ApiStates
import com.example.fitnesscounter.utils.Constants
import com.example.fitnesscounter.utils.showMsg
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: UserViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        binding.signup.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_loginFragment2_to_signupFragment2)
        }

        binding.login.setOnClickListener {
            login()
        }

        return binding.root
    }

    private fun login() {
        binding.apply {
            lifecycleScope.launchWhenStarted {
                if (!TextUtils.isEmpty(username.text) && !TextUtils.isEmpty(password.text)) {
                    viewModel.login(username.text.toString(), password.text.toString()).collect {
                        when (it) {
                            is ApiStates.Success -> {
                                requireActivity().showMsg("login successfully!!")
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
                    requireActivity().showMsg("please fill all the fields")
                }
            }
        }
    }
}