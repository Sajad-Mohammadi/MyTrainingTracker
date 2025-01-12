package com.sajad.mytrainingtracker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.sajad.mytrainingtracker.data.entities.Exercise
import com.sajad.mytrainingtracker.databinding.ItemExerciseBinding

class ExerciseAdapter(
    private val onBtnEditClick: (Exercise) -> Unit,
    private val onExerciseReordered: (List<Exercise>) -> Unit
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

        holder.exerciseBinding.root.setOnClickListener {
            onBtnEditClick(currentExercise)
        }
    }

    fun onMove(fromPosition: Int, toPosition: Int) {
        val list = differ.currentList.toMutableList()
        val movedItem = list.removeAt(fromPosition)
        list.add(toPosition, movedItem)
        onExerciseReordered(list)
        differ.submitList(list)
    }

    val itemTouchHelperCallback = object : ItemTouchHelper.Callback() {
        override fun isLongPressDragEnabled() = true
        override fun isItemViewSwipeEnabled() = false

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int = makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            onMove(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
    }
}