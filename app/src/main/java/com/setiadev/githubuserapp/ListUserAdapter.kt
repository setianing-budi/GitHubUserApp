package com.setiadev.githubuserapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.setiadev.githubuserapp.apis.UserResponse
import com.setiadev.githubuserapp.databinding.ItemUserBinding

class ListUserAdapter(private val data: MutableList<UserResponse.Item> = mutableListOf(),
private val listener:(UserResponse.Item) -> Unit):RecyclerView.Adapter<ListUserAdapter.ListUserViewHolder>() {

    fun setData(data: MutableList<UserResponse.Item>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class ListUserViewHolder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: UserResponse.Item) {
            binding.imgItemPhoto.load(item.avatar_url)
            binding.tvItemUsername.text = item.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserViewHolder =
        ListUserViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ListUserViewHolder, position: Int) {
        val item = data[position]
        holder.onBind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size
}