package com.artushock.fragment.navigationsample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.artushock.fragment.navigationsample.contract.navigator
import com.artushock.fragment.navigationsample.databinding.FragmentMenuBinding
import com.artushock.fragment.navigationsample.model.Options

class MainMenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding: FragmentMenuBinding get() = _binding!!

    private lateinit var options: Options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        options = savedInstanceState?.getParcelable(STATE_OPTIONS_KEY) ?: Options.DEFAULT
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        navigator().listenResult(Options::class.java, viewLifecycleOwner) {
            options = it
        }

        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STATE_OPTIONS_KEY, options)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.openBoxButton.setOnClickListener { navigator().openBoxFragment(options) }
        binding.optionsButton.setOnClickListener { navigator().openOptionsFragment(options) }
        binding.aboutButton.setOnClickListener { navigator().openAboutFragment() }
        binding.exitButton.setOnClickListener { navigator().goBack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val STATE_OPTIONS_KEY = "STATE_OPTIONS_KEY"
    }

}


