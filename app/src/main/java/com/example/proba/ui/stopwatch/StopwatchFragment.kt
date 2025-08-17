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
        // TODO: Добавить наблюдатели для LiveData
    }

    private fun setupClickListeners() {
        // TODO: Добавить обработчики кнопок
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
