package com.lambao.itemswipe.ui

import android.view.View
import com.lambao.itemswipe.R
import com.lambao.itemswipe.base.BaseRecyclerViewAdapter
import com.lambao.itemswipe.databinding.ItemUserBinding
import com.lambao.itemswipe.model.User

class UserAdapter : BaseRecyclerViewAdapter<ItemUserBinding, User>() {
    override val layoutId: Int
        get() = R.layout.item_user

    override fun bind(binding: ItemUserBinding, item: User, position: Int) {
        binding.tvName.text = item.name
    }

    fun closeSwipeButtons(binding: ItemUserBinding) {
        binding.swipeButtons.visibility = View.GONE
        binding.tvName.translationX = 0f
    }
}