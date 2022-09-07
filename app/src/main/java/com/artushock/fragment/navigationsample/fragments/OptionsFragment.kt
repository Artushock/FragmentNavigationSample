package com.artushock.fragment.navigationsample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.artushock.fragment.navigationsample.R
import com.artushock.fragment.navigationsample.contract.HasCustomTitle
import com.artushock.fragment.navigationsample.contract.navigator
import com.artushock.fragment.navigationsample.databinding.FragmentOptionsBinding
import com.artushock.fragment.navigationsample.model.Options

class OptionsFragment : Fragment(), HasCustomTitle {

    private var _binding: FragmentOptionsBinding? = null
    private val binding: FragmentOptionsBinding get() = _binding!!

    private lateinit var options: Options
    private lateinit var boxItems: List<BoxItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        options = savedInstanceState?.getParcelable(STATE_OPTIONS_KEY)
            ?: arguments?.getParcelable(OPTIONS_KEY)
                    ?: throw IllegalArgumentException("Can't launch fragment without options argument")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STATE_OPTIONS_KEY, options)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()
        setupCheckBox()

        binding.cancelButton.setOnClickListener { navigator().goBack() }
        binding.confirmButton.setOnClickListener {
            navigator().publishResult(options)
            navigator().goBack()
        }

        updateUi()
    }

    private fun setupSpinner() {
        boxItems = (1..6).map { BoxItem(it, "$it boxes") }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            boxItems
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
                    val box = boxItems[position]
                    options = options.copy(boxAmount = box.count)
                }
            }
    }

    private fun setupCheckBox() {
        binding.timerSwitch.setOnCheckedChangeListener { _, b ->
            options = options.copy(isTimerEnabled = b)
        }
    }

    private fun updateUi() {
        binding.timerSwitch.isChecked = options.isTimerEnabled

        val boxIndex = boxItems.indexOfFirst { it.count == options.boxAmount }
        binding.boxesAmountSpinner.setSelection(boxIndex)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val STATE_OPTIONS_KEY = "OPTIONS_KEY"
        private const val OPTIONS_KEY = "OPTIONS_KEY"

        fun newInstance(options: Options): OptionsFragment {
            val fragment = OptionsFragment()
            fragment.arguments = bundleOf(OPTIONS_KEY to options)
            return fragment
        }
    }

    data class BoxItem(
        val count: Int,
        val title: String
    ) {
        override fun toString(): String {
            return title
        }
    }

    override fun getTitleRes(): Int {
        return R.string.options_activity_window_title
    }
}