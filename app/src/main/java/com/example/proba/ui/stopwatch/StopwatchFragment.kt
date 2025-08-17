package com.example.proba.ui.stopwatch

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proba.R
import com.example.proba.databinding.FragmentStopwatchBinding

class StopwatchFragment : Fragment() {

    private var _binding: FragmentStopwatchBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding is null")
    
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
            animateTimeUpdate(timeText)
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
            animateButtonClick(binding.buttonStartPause)
            viewModel.toggleTimer()
        }
        
        binding.buttonLap?.setOnClickListener {
            animateButtonClick(binding.buttonLap)
            viewModel.addLap()
        }
        
        binding.buttonReset?.setOnClickListener {
            animateButtonClick(binding.buttonReset)
            viewModel.resetTimer()
        }
    }
    
    private fun animateButtonClick(button: View?) {
        button?.let {
            val scaleDown = ObjectAnimator.ofFloat(it, "scaleX", 1.0f, 0.95f)
            val scaleUp = ObjectAnimator.ofFloat(it, "scaleX", 0.95f, 1.0f)
            val scaleDownY = ObjectAnimator.ofFloat(it, "scaleY", 1.0f, 0.95f)
            val scaleUpY = ObjectAnimator.ofFloat(it, "scaleY", 0.95f, 1.0f)
            
            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(
                AnimatorSet().apply { playTogether(scaleDown, scaleDownY) },
                AnimatorSet().apply { playTogether(scaleUp, scaleUpY) }
            )
            animatorSet.duration = 100
            animatorSet.interpolator = AccelerateDecelerateInterpolator()
            animatorSet.start()
        }
    }
    
    private fun animateTimeUpdate(newTime: String) {
        binding.textTime?.let { textView ->
            // Отменяем предыдущую анимацию если она еще идет
            textView.animate().cancel()
            
            val fadeOut = ObjectAnimator.ofFloat(textView, "alpha", 1.0f, 0.7f)
            val fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0.7f, 1.0f)
            
            fadeOut.duration = 50
            fadeIn.duration = 50
            
            fadeOut.addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    textView.text = newTime
                    fadeIn.start()
                }
            })
            
            fadeOut.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
