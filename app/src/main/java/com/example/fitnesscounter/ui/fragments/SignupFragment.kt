package com.example.fitnesscounter.ui.fragments

import android.app.AlertDialog
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
import com.example.fitnesscounter.databinding.ProgressDialogBinding
import com.example.fitnesscounter.ui.viewmodels.UserViewModel
import com.example.fitnesscounter.utils.ApiStates
import com.example.fitnesscounter.utils.Constants
import com.example.fitnesscounter.utils.LoadingDialog
import com.example.fitnesscounter.utils.showMsg
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel: UserViewModel by viewModels()

    @Inject
    lateinit var loadingDialog: LoadingDialog
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
        if (!loadingDialog.isShowing())
            loadingDialog.create(requireContext())

        binding.apply {
            lifecycleScope.launchWhenStarted {
                if (!TextUtils.isEmpty(username.text) && !TextUtils.isEmpty(email.text) && !TextUtils.isEmpty(
                        password.text
                    )
                ) {
                    viewModel.createUser(
                        username.text.toString().trim(),
                        email.text.toString().trim(),
                        password.text.toString().trim()
                    ).collect {
                        when (it) {
                            is ApiStates.Success -> {
                                requireActivity().showMsg("User created")
                                viewModel.setPref(Constants.userId, it.data.data.id.toString())
                                viewModel.setPref(Constants.token, it.data.data.token)
                                viewModel.setPref(Constants.email, it.data.data.email)
                                viewModel.setPref(Constants.username, it.data.data.username)
                                Constants.passToken = it.data.data.token
                                if (loadingDialog.isShowing())
                                    loadingDialog.dismiss()
                                view?.findNavController()
                                    ?.navigate(R.id.action_signupFragment2_to_profileFragment)
                            }

                            is ApiStates.Failure -> {
                                Log.d("main", it.msg)
                                requireActivity().showMsg("something went wrong, please try again!!")
                                if (loadingDialog.isShowing())
                                    loadingDialog.dismiss()
                            }

                            ApiStates.Loading -> {
                                if (!loadingDialog.isShowing())
                                    loadingDialog.create(requireContext())
                            }
                        }
                    }
                } else {
                    requireActivity().showMsg("please fill all the fields..")
                    loadingDialog.dismiss()
                }
            }
        }
    }

}