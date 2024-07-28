package com.lambao.itemswipe.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.LinkedList
import java.util.Queue

abstract class SwipeHelper @SuppressLint("ClickableViewAccessibility") constructor(
    context: Context?,
    private val recyclerView: RecyclerView,
    private val buttonWidth: Int
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private var swipePosition = -1
    private var swipeThreshold = 0.5f
    private var buttonBuffer: MutableMap<Int, MutableList<SwipeButton>>
    private lateinit var activeButtons: MutableList<SwipeButton>
    private lateinit var gestureDetector: GestureDetector
    private lateinit var removeQueue: Queue<Int>

    private val gestureListener: SimpleOnGestureListener = object : SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            for (button in activeButtons) {
                if (button.onClick(e.x, e.y)) {
                    break
                }
            }
            return true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = OnTouchListener { _, motionEvent ->
        if (swipePosition < 0) {
            return@OnTouchListener false
        }
        val point = Point(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())

        val swipeViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition)

        if (swipeViewHolder != null) {
            val swipedItem = swipeViewHolder.itemView
            val rect = Rect()
            swipedItem.getGlobalVisibleRect(rect)

            if (motionEvent.action == MotionEvent.ACTION_DOWN || motionEvent.action == MotionEvent.ACTION_UP || motionEvent.action == MotionEvent.ACTION_MOVE) {
                if (rect.top < point.y && rect.bottom > point.y) {
                    gestureDetector.onTouchEvent(motionEvent)
                } else {
                    removeQueue.add(swipePosition)
                    swipePosition = -1
                    recoverSwipedItem()
                }
            }
            return@OnTouchListener false
        }
        return@OnTouchListener true
    }

    init {
        this.gestureDetector = GestureDetector(context, gestureListener)
        recyclerView.setOnTouchListener(onTouchListener)
        this.buttonBuffer = HashMap()

        removeQueue = object : LinkedList<Int>() {
            override fun add(element: Int): Boolean {
                return if (contains(element)) false
                else super.add(element)
            }
        }

        attachSwipe()
    }

    private fun attachSwipe() {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    @Synchronized
    private fun recoverSwipedItem() {
        while (!removeQueue.isEmpty()) {
            val pos = removeQueue.poll()
            if (pos != null) {
                if (pos > -1) recyclerView.adapter?.notifyItemChanged(pos)
            }
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if (swipePosition != pos) removeQueue.add(swipePosition)
        swipePosition = pos
        if (buttonBuffer.containsKey(swipePosition)) activeButtons =
            buttonBuffer[swipePosition] ?: mutableListOf()
        else activeButtons.clear()
        buttonBuffer.clear()
        swipeThreshold = 0.5f * activeButtons.size * buttonWidth
        recoverSwipedItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f * defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f * defaultValue
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos = viewHolder.adapterPosition
        var translationX = dX
        val itemView = viewHolder.itemView
        if (pos < 0) {
            swipePosition = pos
            return
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                var buffer = mutableListOf<SwipeButton>()
                if (!buttonBuffer.containsKey(pos)) {
                    instantiateSwipeButtons(viewHolder, buffer)
                    buttonBuffer[pos] = buffer
                } else {
                    buffer = buttonBuffer[pos] ?: mutableListOf()
                }
                translationX = dX * buffer.size * buttonWidth / itemView.width
                drawButton(c, itemView, buffer, pos, translationX)
            }
        }
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            translationX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    private fun drawButton(
        c: Canvas,
        itemView: View,
        buffer: List<SwipeButton>,
        pos: Int,
        translationX: Float
    ) {
        var right = itemView.right.toFloat()
        val dButtonWidth = -1 * translationX / buffer.size
        for (button in buffer) {
            val left = right - dButtonWidth
            button.onDraw(
                c,
                RectF(left, itemView.top.toFloat(), right, itemView.bottom.toFloat()),
                pos
            )
            right = left
        }
    }

    abstract fun instantiateSwipeButtons(
        viewHolder: RecyclerView.ViewHolder?,
        buffer: MutableList<SwipeButton>
    )
}
