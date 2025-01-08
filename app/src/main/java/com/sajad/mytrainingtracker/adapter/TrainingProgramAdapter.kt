package com.sajad.mytrainingtracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sajad.mytrainingtracker.data.entities.TrainingProgram
import com.sajad.mytrainingtracker.databinding.ItemTrainingProgramBinding


class TrainingProgramAdapter(
    private val onBtnEditClick: (TrainingProgram) -> Unit,
    private val onBtnGoToProgramClick: (TrainingProgram) -> Unit,
) : RecyclerView.Adapter<TrainingProgramAdapter.TrainingProgramViewHolder>() {
    class TrainingProgramViewHolder(val trainingProgramBinding: ItemTrainingProgramBinding) :
        RecyclerView.ViewHolder(trainingProgramBinding.root) {}

    private val differCallBack = object : DiffUtil.ItemCallback<TrainingProgram>() {
        override fun areItemsTheSame(oldItem: TrainingProgram, newItem: TrainingProgram): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TrainingProgram, newItem: TrainingProgram): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingProgramViewHolder {
        return TrainingProgramViewHolder(
            ItemTrainingProgramBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TrainingProgramViewHolder, position: Int) {
        val currentTrainingProgram = differ.currentList[position]
        holder.trainingProgramBinding.programTitle.text = currentTrainingProgram.name
        holder.trainingProgramBinding.duration.text = currentTrainingProgram.description

        holder.trainingProgramBinding.btnEdit.setOnClickListener {
            onBtnEditClick(currentTrainingProgram)
        }

        holder.trainingProgramBinding.btnGoToProgram.setOnClickListener {
            onBtnGoToProgramClick(currentTrainingProgram)
        }
    }
}