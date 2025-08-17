package com.example.proba.ui.stopwatch

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.proba.R
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
    
    override fun onResume() {
        super.onResume()
        // Обеспечиваем корректное отображение состояния при возврате к фрагменту
        viewModel.refreshState()
    }

    private fun setupObservers() {
        viewModel.timeText.observe(viewLifecycleOwner) { timeText ->
            binding.textTime.text = timeText
        }
        
        viewModel.buttonText.observe(viewLifecycleOwner) { buttonText ->
            binding.buttonStartPause.text = buttonText
        }
        
        viewModel.buttonColor.observe(viewLifecycleOwner) { buttonState ->
            updateButtonColor(buttonState)
        }
    }
    
    private fun updateButtonColor(buttonState: StopwatchViewModel.ButtonState) {
        val color = when (buttonState) {
            StopwatchViewModel.ButtonState.START -> R.color.stopwatch_start_button
            StopwatchViewModel.ButtonState.PAUSE -> R.color.stopwatch_pause_button
        }
        
        binding.buttonStartPause.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), color)
        )
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
