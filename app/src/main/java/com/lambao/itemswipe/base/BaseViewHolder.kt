package com.lambao.itemswipe.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder<BINDING : ViewDataBinding>(
    val binding: BINDING,
) : RecyclerView.ViewHolder(binding.root)