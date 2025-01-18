package com.sajad.mytrainingtracker.adapter

import android.annotation.SuppressLint
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sajad.mytrainingtracker.R
import com.sajad.mytrainingtracker.data.entities.Goal
import com.sajad.mytrainingtracker.databinding.ItemGoalBinding

class GoalAdapter(
    private val onBtnEditClick: (Goal) -> Unit,
    private val onBtnPriorityClick: (Goal) -> Unit,
    private val onBtnStatusClick: (Goal) -> Unit
) :RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {
    class GoalViewHolder(val goalBinding: ItemGoalBinding) :
        RecyclerView.ViewHolder(goalBinding.root) {}

    private val differCallBack = object : DiffUtil.ItemCallback<Goal>() {
        override fun areItemsTheSame(oldItem: Goal, newItem: Goal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Goal, newItem: Goal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        return GoalViewHolder(
            ItemGoalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val currentGoal = differ.currentList[position]
        holder.goalBinding.goalDescription.text = currentGoal.description
        val priorityArray = holder.itemView.context.resources.getStringArray(R.array.goal_priority)
        when (currentGoal.priority) {
            1 -> {
                holder.goalBinding.btnPriority.text = priorityArray[1]
                holder.goalBinding.btnPriority.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.low_priority))
            }
            2 -> {
                holder.goalBinding.btnPriority.text = priorityArray[2]
                holder.goalBinding.btnPriority.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.medium_priority))
            }
            else -> {
                holder.goalBinding.btnPriority.text = priorityArray[3]
                holder.goalBinding.btnPriority.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.high_priority))
            }
        }
        val statusArray = holder.itemView.context.resources.getStringArray(R.array.goal_status)
        when (currentGoal.isCompleted) {
            1 -> {
                holder.goalBinding.btnStatus.text = statusArray[1]
                holder.goalBinding.btnStatus.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.planned_status))
            }
            2 -> {
                holder.goalBinding.btnStatus.text = statusArray[2]
                holder.goalBinding.btnStatus.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.in_progress_status))
            }
            else -> {
                holder.goalBinding.btnStatus.text = statusArray[3]
                holder.goalBinding.btnStatus.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.completed_status))
            }
        }

        holder.goalBinding.btnPriority.setOnClickListener {
            onBtnPriorityClick(currentGoal)
        }

        holder.goalBinding.btnStatus.setOnClickListener {
            onBtnStatusClick(currentGoal)
        }

        holder.goalBinding.btnEdit.setOnClickListener {
            onBtnEditClick(currentGoal)
        }
    }
}