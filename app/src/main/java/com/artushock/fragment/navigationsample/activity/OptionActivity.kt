package com.artushock.fragment.navigationsample.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.artushock.fragment.navigationsample.databinding.ActivityOptionsBinding
import com.artushock.fragment.navigationsample.model.Options

class OptionActivity : BaseActivity() {

    private lateinit var options: Options
    private lateinit var boxesItemCount: List<BoxItemCount>

    private lateinit var binding: ActivityOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        options = savedInstanceState?.getParcelable(OPTIONS_KEY)
            ?: intent.getParcelableExtra(EXTRA_OPTIONS)
                    ?: throw  IllegalArgumentException("Parameter options doesn't exists")

        setupSpinner()
        updateUi()

        binding.cancelButton.setOnClickListener { onCancelButtonClick() }
        binding.confirmButton.setOnClickListener { onConfirmButtonClick() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(OPTIONS_KEY, options)
    }

    private fun setupSpinner() {
        boxesItemCount = (1..6).map { BoxItemCount(it, "$it boxes") }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            boxesItemCount
        )
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        binding.boxesAmountSpinner.adapter = adapter
        binding.boxesAmountSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}

                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    val box: BoxItemCount = boxesItemCount[position]
                    options = options.copy(boxAmount = box.count)
                }
            }
    }

    private fun updateUi() {
        binding.timerSwitch.isChecked = options.isTimerEnabled

        val currentIndex = boxesItemCount.indexOfFirst { it.count == options.boxAmount }
        binding.boxesAmountSpinner.setSelection(currentIndex)
    }

    private fun onCancelButtonClick() {
        finish()
    }

    private fun onConfirmButtonClick() {
        options = options.copy(isTimerEnabled = binding.timerSwitch.isChecked)
        val intent = Intent()
        intent.putExtra(EXTRA_OPTIONS, options)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        const val EXTRA_OPTIONS = "EXTRA_OPTIONS"
        private const val OPTIONS_KEY = "OPTIONS_KEY"
    }

    class BoxItemCount(
        val count: Int,
        val itemTitle: String,
    ) {
        override fun toString(): String {
            return itemTitle
        }
    }
}