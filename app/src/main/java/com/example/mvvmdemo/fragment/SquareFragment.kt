package com.example.mvvmdemo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mvvmdemo.RewardAdActivity
import com.example.mvvmdemo.PointsRedemptionActivity
import com.example.mvvmdemo.base.BaseFragment
import com.example.mvvmdemo.databinding.FragmentSquareBinding
import com.example.mvvmdemo.vm.SquareFragmentViewModel

/**
 * 广场Fragment
 */
class SquareFragment : BaseFragment<FragmentSquareBinding, SquareFragmentViewModel>() {
    override val viewModel: SquareFragmentViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSquareBinding =
        FragmentSquareBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnPointsRedemption.setOnClickListener {
            startActivity(Intent(requireContext(), PointsRedemptionActivity::class.java))
        }

        binding.btnAdRevenue.setOnClickListener {
            startActivity(Intent(requireContext(), RewardAdActivity::class.java))
        }
    }

    override fun observeViewModel() {
    }

    override fun showEmptyView() {
    }
}