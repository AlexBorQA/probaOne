package com.example.proba.ui.stopwatch

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proba.R
import com.example.proba.databinding.FragmentStopwatchBinding

class StopwatchFragment : Fragment() {

    private var _binding: FragmentStopwatchBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: StopwatchViewModel
    private lateinit var lapTimeAdapter: LapTimeAdapter

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
        
        // Инициализируем SoundManager
        viewModel.initializeSoundManager(requireContext())
        
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }
    
    override fun onResume() {
        super.onResume()
        // Обеспечиваем корректное отображение состояния при возврате к фрагменту
        viewModel.refreshState()
    }

    private fun setupRecyclerView() {
        lapTimeAdapter = LapTimeAdapter()
        binding.recyclerLaps?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = lapTimeAdapter
        }
    }

    private fun setupObservers() {
        viewModel.timeText.observe(viewLifecycleOwner) { timeText ->
            binding.textTime?.text = timeText
        }
        
        viewModel.buttonText.observe(viewLifecycleOwner) { buttonText ->
            binding.buttonStartPause?.text = buttonText
        }
        
        viewModel.buttonColor.observe(viewLifecycleOwner) { buttonState ->
            updateButtonColor(buttonState)
        }
        
        viewModel.lapTimes.observe(viewLifecycleOwner) { lapTimes ->
            lapTimeAdapter.updateLapTimes(lapTimes)
        }
    }
    
    private fun updateButtonColor(buttonState: StopwatchViewModel.ButtonState) {
        val color = when (buttonState) {
            StopwatchViewModel.ButtonState.START -> R.color.stopwatch_start_button
            StopwatchViewModel.ButtonState.PAUSE -> R.color.stopwatch_pause_button
        }
        
        binding.buttonStartPause?.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), color)
        )
    }

    private fun setupClickListeners() {
        binding.buttonStartPause?.setOnClickListener {
            viewModel.toggleTimer()
        }
        
        binding.buttonLap?.setOnClickListener {
            viewModel.addLap()
        }
        
        binding.buttonReset?.setOnClickListener {
            viewModel.resetTimer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
