package com.lambao.itemswipe.base

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<BINDING : ViewDataBinding, T> :
    RecyclerView.Adapter<BaseViewHolder<BINDING>>() {

    lateinit var context: Context

    var data = arrayListOf<T>()

    @get:LayoutRes
    abstract val layoutId: Int

    abstract fun bind(binding: BINDING, item: T, position: Int)

    @SuppressLint("NotifyDataSetChanged")
    open fun submitData(list: ArrayList<T> = arrayListOf()) {
        this.data = list
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun appendData(list: MutableList<T>) {
        val oldSize = data.size
        data.addAll(list)
        val newSize = data.size
        notifyItemRangeChanged(oldSize, newSize)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BINDING> {
        context = parent.context
        return BaseViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BINDING>, position: Int) {
        bind(holder.binding, data[position], position)
    }

    override fun getItemCount(): Int = data.size

    var onItemClick: ((Int?, T, Int) -> Unit)? = null
}