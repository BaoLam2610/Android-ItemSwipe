package com.lambao.itemswipe.ui

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children

class CustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    override fun onSaveInstanceState(): Parcelable? {
        return saveInstanceState(super.onSaveInstanceState())
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(restoreInstanceState(state))
    }

//    override fun onSaveInstanceState(): Parcelable {
//        return Bundle().apply {
//            putParcelable("superState", super.onSaveInstanceState())
//            putBoolean("isCustomView", true)
//        }
//    }
//
//    override fun onRestoreInstanceState(state: Parcelable?) {
//        super.onRestoreInstanceState(state)
//        state?.let {
//            if (it is Bundle && it.getBoolean("isCustomView", false)) {
//                // Xử lý trạng thái của CustomView tại đây
//            }
//        }
//    }
}

fun ViewGroup.saveChildViewStates(): SparseArray<Parcelable> {
    val childViewStates = SparseArray<Parcelable>()
    children.forEach { child -> child.saveHierarchyState(childViewStates) }
    return childViewStates
}

fun ViewGroup.restoreChildViewStates(childViewStates: SparseArray<Parcelable>) {
    children.forEach { child -> child.restoreHierarchyState(childViewStates) }
}

fun ViewGroup.saveInstanceState(state: Parcelable?): Parcelable? {
    return Bundle().apply {
        putParcelable("SUPER_STATE_KEY", state)
        putSparseParcelableArray("SPARSE_STATE_KEY", saveChildViewStates())
    }
}

fun ViewGroup.restoreInstanceState(state: Parcelable?): Parcelable? {
    var newState = state
    if (newState is Bundle) {
        val childrenState = newState.getSparseParcelableArray<Parcelable>("SPARSE_STATE_KEY")
        childrenState?.let { restoreChildViewStates(it) }
        newState = newState.getParcelable("SUPER_STATE_KEY")
    }
    return newState
}