package com.example.fitnesscounter.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fitnesscounter.R
import com.example.fitnesscounter.databinding.FragmentProfileBinding
import com.example.fitnesscounter.databinding.ProgressDialogBinding
import com.example.fitnesscounter.ui.HomeActivity
import com.example.fitnesscounter.ui.viewmodels.ProfileViewModel
import com.example.fitnesscounter.ui.viewmodels.UserViewModel
import com.example.fitnesscounter.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private var currentDob = ""
    private val userViewModel: UserViewModel by viewModels()

    @Inject
    lateinit var loadingDialog: LoadingDialog

    companion object {
        var tokensData = ""
    }

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        binding.datepicker.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                getDatePicker(requireContext()).collect { date ->
                    currentDob = "${date[0]}/${date[1]}/${date[2]}"
                    binding.dob.setText(currentDob)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            userViewModel.getPref(Constants.token).collect {
                tokensData = it
            }
        }

        binding.submit.setOnClickListener {
            createProfile()
        }

        binding.skip.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    HomeActivity::class.java
                )
            )
        }

        return binding.root

    }

    @ExperimentalCoroutinesApi
    private fun createProfile() {
        if (!loadingDialog.isShowing())
            loadingDialog.create(requireContext())

        var gender1 = ""
        binding.apply {
            if (!TextUtils.isEmpty(goal.text.toString())
                && !TextUtils.isEmpty(active.text.toString())
                && !TextUtils.isEmpty(dob.text.toString())
                && !TextUtils.isEmpty(height.text.toString())
                && !TextUtils.isEmpty(weight.text.toString())
                && !TextUtils.isEmpty(goalWeight.text.toString())
                && !TextUtils.isEmpty(weeklyGoal.text.toString())
            ) {

                radiogroup.setOnCheckedChangeListener { _, i ->
                    gender1 = when (i) {
                        R.id.male -> "male"
                        R.id.female -> "female"
                        else -> ""
                    }
                }
                lifecycleScope.launchWhenStarted {
                    userViewModel.getPref(Constants.userId).collect { userId ->
                        viewModel.createProfile(
                            userId,
                            goal.text.toString(),
                            active.text.toString(),
                            gender1,
                            currentDob,
                            height.text.toString().trim(),
                            weight.text.toString().trim(),
                            goalWeight.text.toString().trim(),
                            weeklyGoal.text.toString()
                        ).collect {
                            when (it) {
                                is ApiStates.Success -> {
                                    if (loadingDialog.isShowing())
                                        loadingDialog.dismiss()
                                }
                                is ApiStates.Failure -> {
                                    Log.d("main", "createProfile: ${it.msg}")
                                    requireActivity().showMsg("Profile Added")
                                    if (loadingDialog.isShowing())
                                        loadingDialog.dismiss()
                                    startActivity(
                                        Intent(
                                            requireActivity(),
                                            HomeActivity::class.java
                                        )
                                    )
                                }
                                is ApiStates.Loading -> {
                                    if (!loadingDialog.isShowing())
                                        loadingDialog.create(requireContext())
                                }
                            }
                        }
                    }
                }
            } else {
                requireActivity().showMsg("please fill all the fields!!")
                loadingDialog.dismiss()
            }
        }
    }

}