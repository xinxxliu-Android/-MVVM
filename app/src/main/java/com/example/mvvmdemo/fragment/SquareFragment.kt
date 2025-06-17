package com.example.mvvmdemo.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mvvmdemo.base.BaseFragment
import com.example.mvvmdemo.databinding.FragmentSquareBinding
import com.example.mvvmdemo.vm.HomeFragmentViewModel
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


    override fun observeViewModel() {
    }

    override fun showEmptyView() {
    }
}