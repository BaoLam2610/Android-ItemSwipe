package com.lambao.itemswipe.ui

import android.widget.Toast
import com.lambao.itemswipe.R
import com.lambao.itemswipe.base.BaseRecyclerViewAdapter
import com.lambao.itemswipe.databinding.ItemUserBinding
import com.lambao.itemswipe.model.User

class UserAdapter : BaseRecyclerViewAdapter<ItemUserBinding, User>() {
    override val layoutId: Int
        get() = R.layout.item_user

    override fun bind(binding: ItemUserBinding, item: User, position: Int) {
        binding.tvName.text = item.name
        binding.cbActive.setOnClickListener {
            Toast.makeText(context, "Click Checked", Toast.LENGTH_SHORT).show()
        }
        binding.root.setOnClickListener {
            Toast.makeText(context, "Click Item", Toast.LENGTH_SHORT).show()

        }
    }
}