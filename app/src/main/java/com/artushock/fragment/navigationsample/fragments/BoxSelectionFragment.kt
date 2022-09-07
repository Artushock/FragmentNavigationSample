package com.artushock.fragment.navigationsample.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.artushock.fragment.navigationsample.R
import com.artushock.fragment.navigationsample.contract.HasCustomTitle
import com.artushock.fragment.navigationsample.contract.navigator
import com.artushock.fragment.navigationsample.databinding.BoxItemBinding
import com.artushock.fragment.navigationsample.databinding.FragmentBoxSelectionBinding
import com.artushock.fragment.navigationsample.model.Options
import kotlin.math.max
import kotlin.properties.Delegates.notNull
import kotlin.random.Random

class BoxSelectionFragment : Fragment(), HasCustomTitle {

    private var _binding: FragmentBoxSelectionBinding? = null
    private val binding: FragmentBoxSelectionBinding get() = _binding!!

    private lateinit var options: Options
    private var boxItemIndex by notNull<Int>()

    private lateinit var timer: CountDownTimer
    private var timerStartTimeStamp by notNull<Long>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        options = arguments?.getParcelable(OPTIONS_KEY)
            ?: throw IllegalArgumentException("Can't launch fragment without options argument")

        boxItemIndex = savedInstanceState?.getInt(STATE_BOX_ITEM_INDEX_KEY)
            ?: Random.nextInt(options.boxAmount)

        timerStartTimeStamp = savedInstanceState?.getLong(STATE_TIMER_START_TIMESTAMP_KEY)
            ?: System.currentTimeMillis()

        _binding = FragmentBoxSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (options.isTimerEnabled) {
            setupTimer()
        }
        createBoxes()
    }

    override fun onStart() {
        super.onStart()
        if (options.isTimerEnabled) {
            timer.start()
        }
    }

    override fun onStop() {
        super.onStop()
        if (options.isTimerEnabled) {
            timer.cancel()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_BOX_ITEM_INDEX_KEY, boxItemIndex)
        outState.putLong(STATE_TIMER_START_TIMESTAMP_KEY, timerStartTimeStamp)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setupTimer() {
        timer = object : CountDownTimer(getRemainingSeconds() * 1000, 1000) {
            override fun onTick(p0: Long) {
                updateUi()
            }

            override fun onFinish() {
                updateUi()
                showFinishDialog()
            }
        }
    }

    private fun updateUi() {
        if (getRemainingSeconds() >= 0) {
            binding.timer.visibility = View.VISIBLE
            binding.timer.text =
                getString(R.string.box_selection_activity_timer, getRemainingSeconds().toString())
        } else {
            binding.timer.visibility = View.GONE
        }
    }

    private fun showFinishDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.box_selection_activity_the_end)
            .setMessage(R.string.box_selection_activity_timer_end_finish)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                navigator().goMainMenu()
            }
            .create()
            .show()
    }

    private fun getRemainingSeconds(): Long {
        val finishedAt = timerStartTimeStamp + TIMER_MILLIS
        return max(0, (finishedAt - System.currentTimeMillis()) / 1000)
    }

    private fun createBoxes() {
        val boxes = (0 until options.boxAmount).map { index ->
            val box = BoxItemBinding.inflate(layoutInflater)
            box.root.id = View.generateViewId()
            box.boxItemTitle.text =
                getString(R.string.box_selection_activity_item, (index + 1).toString())
            box.root.tag = index
            box.root.setOnClickListener { onBoxClickListener(it) }
            binding.root.addView(box.root)
            box
        }
        binding.flow.referencedIds = boxes.map { it.root.id }.toIntArray()
    }

    private fun onBoxClickListener(view: View) {
        if (view.tag == boxItemIndex) {
            navigator().openSuccessFragment()
        } else {
            Toast.makeText(
                requireContext(),
                "Opps! Unfortunately no, try again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {

        fun newInstance(options: Options): BoxSelectionFragment {
            val fragment = BoxSelectionFragment()
            fragment.arguments = bundleOf(OPTIONS_KEY to options)
            return fragment
        }

        private const val OPTIONS_KEY = "OPTIONS_KEY"

        private const val STATE_BOX_ITEM_INDEX_KEY = "STATE_BOX_ITEM_INDEX_KEY"
        private const val STATE_TIMER_START_TIMESTAMP_KEY = "STATE_TIMER_START_TIMESTAMP_KEY"

        private const val TIMER_MILLIS = 10000L

    }

    override fun getTitleRes(): Int {
        return R.string.box_selection_window_title
    }
}