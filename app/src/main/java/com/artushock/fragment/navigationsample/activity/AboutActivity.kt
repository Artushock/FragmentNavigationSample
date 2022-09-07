package com.artushock.fragment.navigationsample.activity

import android.os.Bundle
import com.artushock.fragment.navigationsample.BuildConfig
import com.artushock.fragment.navigationsample.R
import com.artushock.fragment.navigationsample.databinding.ActivityAboutBinding

class AboutActivity : BaseActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appName.text = getString(R.string.app_name)
        binding.appVersion.text = BuildConfig.VERSION_NAME
        binding.appVersionCode.text = BuildConfig.VERSION_CODE.toString()

        binding.okButton.setOnClickListener {
            okButtonClick()
        }
    }

    private fun okButtonClick() {
        finish()
    }
}