package com.artushock.fragment.navigationsample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.artushock.fragment.navigationsample.BuildConfig
import com.artushock.fragment.navigationsample.R
import com.artushock.fragment.navigationsample.contract.HasCustomTitle
import com.artushock.fragment.navigationsample.contract.navigator
import com.artushock.fragment.navigationsample.databinding.FragmentAboutBinding

class AboutFragment : Fragment(), HasCustomTitle {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appName.text = getString(R.string.app_name)
        binding.appVersion.text = BuildConfig.VERSION_NAME
        binding.appVersionCode.text = BuildConfig.VERSION_CODE.toString()

        binding.okButton.setOnClickListener { navigator().goBack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTitleRes(): Int {
        return R.string.about_activity_window_title
    }


}