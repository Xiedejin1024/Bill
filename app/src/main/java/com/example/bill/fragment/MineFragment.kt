package com.example.bill.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.bill.databinding.FragmentMineBinding
import com.example.bill.utils.setOnClickListener
import com.example.bill.utils.showToast


class MineFragment : Fragment() {
    private var _binding: FragmentMineBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MineFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMineBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        setOnClickListener(
            binding.llSwitchTopic, binding.llShare, binding.llPrivacyPolicy,
            binding.llUserAgreement, binding.llFeedback, binding.llAbout
        ) {
            when (this) {
                binding.llSwitchTopic -> {
                    "切换主题".showToast()
                }

                binding.llShare -> {
                    "分享软件".showToast()
                }

                binding.llPrivacyPolicy -> {
                    "隐私政策".showToast()
                }

                binding.llUserAgreement -> {
                    "用户协议".showToast()
                }

                binding.llFeedback -> {
                    "意见反馈".showToast()
                }

                binding.llAbout -> {
                    "关于软件".showToast()
                }
            }
        }
    }

}