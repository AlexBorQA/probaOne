package com.example.proba.ui.stopwatch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proba.databinding.ItemLapTimeBinding
import com.example.proba.domain.LapTime

class LapTimeAdapter : RecyclerView.Adapter<LapTimeAdapter.LapTimeViewHolder>() {

    private var lapTimes: List<LapTime> = emptyList()

    fun updateLapTimes(newLapTimes: List<LapTime>) {
        val oldSize = lapTimes.size
        val newSize = newLapTimes.size
        
        lapTimes = newLapTimes
        
        // Оптимизация: используем DiffUtil для эффективного обновления списка
        if (oldSize == newSize) {
            notifyItemRangeChanged(0, newSize)
        } else if (newSize > oldSize) {
            notifyItemRangeChanged(0, oldSize)
            notifyItemRangeInserted(oldSize, newSize - oldSize)
        } else {
            notifyItemRangeChanged(0, newSize)
            notifyItemRangeRemoved(newSize, oldSize - newSize)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapTimeViewHolder {
        val binding = ItemLapTimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LapTimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LapTimeViewHolder, position: Int) {
        holder.bind(lapTimes[position])
    }

    override fun getItemCount(): Int = lapTimes.size

    class LapTimeViewHolder(private val binding: ItemLapTimeBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lapTime: LapTime) {
            binding.textLapNumber.text = lapTime.lapNumber.toString()
            binding.textLapTime.text = lapTime.formattedLapTime
            binding.textTotalTime.text = lapTime.formattedTotalTime
        }
    }
}
