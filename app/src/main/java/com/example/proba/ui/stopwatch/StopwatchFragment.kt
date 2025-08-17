package com.example.proba.ui.stopwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.proba.databinding.FragmentStopwatchBinding

class StopwatchFragment : Fragment() {

    private var _binding: FragmentStopwatchBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: StopwatchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[StopwatchViewModel::class.java]
        
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.timeText.observe(viewLifecycleOwner) { timeText ->
            binding.textTime.text = timeText
        }
        
        viewModel.buttonText.observe(viewLifecycleOwner) { buttonText ->
            binding.buttonStartPause.text = buttonText
        }
    }

    private fun setupClickListeners() {
        binding.buttonStartPause.setOnClickListener {
            viewModel.toggleTimer()
        }
        
        binding.buttonReset.setOnClickListener {
            viewModel.resetTimer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
