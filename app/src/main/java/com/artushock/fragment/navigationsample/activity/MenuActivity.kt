package com.artushock.fragment.navigationsample.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.artushock.fragment.navigationsample.databinding.ActivityMenuBinding
import com.artushock.fragment.navigationsample.model.Options

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    private lateinit var options: Options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        options = savedInstanceState?.getParcelable("OPTIONS_KEY") ?: Options.DEFAULT

        binding.openBoxButton.setOnClickListener { onOpenBoxButtonPressed() }
        binding.optionsButton.setOnClickListener { onOptionButtonClick() }
        binding.aboutButton.setOnClickListener { onAboutButtonClick() }
        binding.exitButton.setOnClickListener { onExitButtonClick() }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(OPTIONS_KEY, options)
    }


    private fun onOpenBoxButtonPressed() {
        val intent = Intent(this, BoxSelectionActivity::class.java)
        intent.putExtra(BoxSelectionActivity.EXTRA_OPTIONS, options)
        startActivity(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent: Intent? = result.data
                options = intent?.getParcelableExtra(OptionActivity.EXTRA_OPTIONS)
                    ?: throw IllegalArgumentException("Can't get the updated data from options activity")

                Log.d(
                    "MenuActivity",
                    "option = { boxAmount = ${options.boxAmount}, isTimerEnabled = ${options.isTimerEnabled}}"
                )
            }
        }

    private fun onOptionButtonClick() {
        val intent = Intent(this, OptionActivity::class.java)
        intent.putExtra(OptionActivity.EXTRA_OPTIONS, options)
        resultLauncher.launch(intent)
    }

    private fun onAboutButtonClick() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun onExitButtonClick() {
        finish()
    }

    companion object {
        const val OPTIONS_KEY = "OPTIONS_KEY"
    }
}