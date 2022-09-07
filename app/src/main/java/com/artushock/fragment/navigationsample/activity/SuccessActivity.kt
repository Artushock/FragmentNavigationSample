package com.artushock.fragment.navigationsample.activity

import android.content.Intent
import android.os.Bundle
import com.artushock.fragment.navigationsample.databinding.ActivitySuccessBinding

class SuccessActivity : BaseActivity() {

    private lateinit var binding: ActivitySuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener { onGotoMenuButtonPressed() }
    }

    private fun onGotoMenuButtonPressed() {
        val intent = Intent(this, MenuActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

}