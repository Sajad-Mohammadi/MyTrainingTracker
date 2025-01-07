package com.sajad.mytrainingtracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sajad.mytrainingtracker.data.entities.User
import com.sajad.mytrainingtracker.databinding.UserInfoBinding

class UserAdapter(
    private val onBtnEditClick: (User) -> Unit,
    private val onBtnLogoutClick: (User) -> Unit,
    private val onBtnDeleteClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    class UserViewHolder(val userBinding: UserInfoBinding) :
        RecyclerView.ViewHolder(userBinding.root) {}

    private val differCallBack = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            UserInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = differ.currentList[position]
        holder.userBinding.userFirstnameTextView.text = currentUser.firstname
        holder.userBinding.userLastnameTextView.text = currentUser.lastname
        holder.userBinding.userEmailTextView.text = currentUser.email

        holder.userBinding.btnEdit.setOnClickListener {
            onBtnEditClick(currentUser)
        }

        holder.userBinding.btnLogout.setOnClickListener {
            onBtnLogoutClick(currentUser)
        }

        holder.userBinding.btnDelete.setOnClickListener {
            onBtnDeleteClick(currentUser)
        }
    }
}