package com.sajad.mytrainingtracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sajad.mytrainingtracker.data.entities.Routine
import com.sajad.mytrainingtracker.databinding.ItemRoutineBinding

class RoutineAdapter(
    private val onBtnEditClick: (Routine) -> Unit,
    private val onBtnViewRoutineClick: (Routine) -> Unit,
): RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {
    class RoutineViewHolder(val routineBinding: ItemRoutineBinding) :
        RecyclerView.ViewHolder(routineBinding.root) {}

    private val differCallBack = object : DiffUtil.ItemCallback<Routine>() {
        override fun areItemsTheSame(oldItem: Routine, newItem: Routine): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Routine, newItem: Routine): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        return RoutineViewHolder(
            ItemRoutineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val currentRoutine = differ.currentList[position]
        holder.routineBinding.routineTitle.text = currentRoutine.name
        holder.routineBinding.note.text = currentRoutine.note


        holder.routineBinding.btnEdit.setOnClickListener {
            onBtnEditClick(currentRoutine)
        }
        holder.routineBinding.btnViewRoutine.setOnClickListener {
            onBtnViewRoutineClick(currentRoutine)
        }
    }
}