package com.artushock.fragment.navigationsample.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.artushock.fragment.navigationsample.R
import com.artushock.fragment.navigationsample.databinding.ActivityBoxSelectionBinding
import com.artushock.fragment.navigationsample.databinding.BoxItemBinding
import com.artushock.fragment.navigationsample.model.Options
import kotlin.math.max
import kotlin.properties.Delegates.notNull
import kotlin.random.Random

class BoxSelectionActivity : BaseActivity() {

    private lateinit var binding: ActivityBoxSelectionBinding

    private lateinit var timer: CountDownTimer
    private var timerTimeStamp by notNull<Long>()

    private lateinit var options: Options
    private var boxIndex by notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoxSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        options = savedInstanceState?.getParcelable(OPTIONS_STATE_SAVE_KEY)
            ?: intent.getParcelableExtra(EXTRA_OPTIONS)
                    ?: throw IllegalArgumentException("There isn't option argument")

        boxIndex = savedInstanceState?.getInt(BOX_INDEX_STATE_SAVE_KEY)
            ?: Random.nextInt(options.boxAmount)

        if (options.isTimerEnabled) {
            timerTimeStamp = savedInstanceState?.getLong(TIMER_TIMESTAMP_STATE_SAVE_KEY)
                ?: System.currentTimeMillis()

            setupTimer()
            updateTimerUi()
        }


        createBoxes()
    }

    private fun updateTimerUi() {
        if (getRemainingSeconds() >= 0) {
            binding.timer.visibility = View.VISIBLE
            binding.timer.text =
                getString(R.string.box_selection_activity_timer, getRemainingSeconds().toString())
        } else {
            binding.timer.visibility = View.GONE
        }
    }


    private fun setupTimer() {
        timer = object : CountDownTimer(getRemainingSeconds() * 1000, 1000) {
            override fun onTick(p0: Long) {
                updateTimerUi()
            }

            override fun onFinish() {
                updateTimerUi()
                showTimerEndDialog()
            }
        }
    }

    private fun showTimerEndDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.box_selection_activity_the_end))
            .setMessage(getString(R.string.box_selection_activity_timer_end_finish))
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { _, _ -> finish() }
            .create()
        dialog.show()
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
        outState.putParcelable(OPTIONS_STATE_SAVE_KEY, options)
        outState.putInt(BOX_INDEX_STATE_SAVE_KEY, boxIndex)
        if (options.isTimerEnabled) {
            outState.putLong(TIMER_TIMESTAMP_STATE_SAVE_KEY, timerTimeStamp)
        }
    }

    private fun createBoxes() {
        val boxBindings = (0 until options.boxAmount).map { index ->
            val boxItem = BoxItemBinding.inflate(layoutInflater)
            boxItem.root.id = View.generateViewId()
            boxItem.boxItemTitle.text =
                getString(R.string.box_selection_activity_item, (index + 1).toString())
            boxItem.root.setOnClickListener { view -> onItemBoxSelected(view) }
            boxItem.root.tag = index
            binding.root.addView(boxItem.root)
            boxItem
        }

        binding.flow.referencedIds = boxBindings.map { it.root.id }.toIntArray()
    }

    private fun onItemBoxSelected(view: View) {
        if (view.tag == boxIndex) {
            val intent = Intent(this, SuccessActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "You didn't guess. Try again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRemainingSeconds(): Long {
        val finishedAt = timerTimeStamp + TIMER_DURATION
        return max(0, (finishedAt - System.currentTimeMillis()) / 1000)
    }


    companion object {
        const val EXTRA_OPTIONS = "EXTRA_OPTIONS"
        private const val OPTIONS_STATE_SAVE_KEY = "STATE_SAVE_KEY"
        private const val BOX_INDEX_STATE_SAVE_KEY = "BOX_INDEX_STATE_SAVE_KEY"
        private const val TIMER_TIMESTAMP_STATE_SAVE_KEY = "BOX_INDEX_STATE_SAVE_KEY"

        private const val TIMER_DURATION = 10000L
    }

}