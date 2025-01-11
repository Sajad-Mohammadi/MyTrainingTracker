package com.sajad.mytrainingtracker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sajad.mytrainingtracker.data.entities.Exercise
import com.sajad.mytrainingtracker.databinding.ItemExerciseBinding

class ExerciseAdapter(
    private val onBtnEditClick: (Exercise) -> Unit,
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {
    class ExerciseViewHolder(val exerciseBinding: ItemExerciseBinding) :
        RecyclerView.ViewHolder(exerciseBinding.root) {}

    private val differCallBack = object : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(
            ItemExerciseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val currentExercise = differ.currentList[position]
        holder.exerciseBinding.exerciseTitle.text = currentExercise.name
        holder.exerciseBinding.tvSet.text = currentExercise.sets.toString()
        holder.exerciseBinding.tvKg.text = currentExercise.reps.toString()
        holder.exerciseBinding.tvReps.text = currentExercise.reps.toString()
    }
}