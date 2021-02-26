package com.tiket.github.userfinder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tiket.github.userfinder.databinding.ItemRecyclerUserBinding
import com.tiket.github.userfinder.models.UserResponse

class UserRecyclerAdapter(
    val context: Context,
    private val userList: MutableList<UserResponse>
) :
    RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder>() {

    private lateinit var binding: ItemRecyclerUserBinding
    var onItemClick: ((
        Int, UserResponse
    ) -> Unit)? =
        null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserRecyclerAdapter.UserViewHolder {
        binding = ItemRecyclerUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.count() ?: 0
    }

    override fun onBindViewHolder(
        holder: UserRecyclerAdapter.UserViewHolder,
        position: Int
    ) {
        holder.bind(position, userList[position])
    }

    inner class UserViewHolder(private val binding: ItemRecyclerUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, response: UserResponse) {
            with(binding) {
                tvUsername.text = response.login
                Glide.with(context).load(response.avatarUrl).into(ivAvatar)
                ivDetail.setOnClickListener {
                    onItemClick?.invoke(position, response)
                }
            }
        }
    }

}