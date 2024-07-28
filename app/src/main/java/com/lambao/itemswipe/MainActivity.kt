package com.lambao.itemswipe

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.lambao.itemswipe.base.OnSwipeButtonClickListener
import com.lambao.itemswipe.base.SwipeButton
import com.lambao.itemswipe.base.SwipeHelper
import com.lambao.itemswipe.databinding.ActivityMainBinding
import com.lambao.itemswipe.model.users
import com.lambao.itemswipe.ui.UserAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val userAdapter by lazy { UserAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        userAdapter.submitData(users)
        binding.rvUsers.adapter = userAdapter

        val swipeHelper = object : SwipeHelper(this, binding.rvUsers, 80f.dpToPx(this).toInt()) {
            override fun instantiateSwipeButtons(
                viewHolder: RecyclerView.ViewHolder?,
                buffer: MutableList<SwipeButton>
            ) {
                buffer.add(SwipeButton(
                    this@MainActivity,
                    R.drawable.deleteicon,
                    Color.parseColor("#F24C05"),
                    "Delete",
                    12f.spToPx(this@MainActivity),
                    Color.WHITE,
                    R.font.muli_italic,
                    8f.dpToPx(this@MainActivity),
                    object : OnSwipeButtonClickListener {
                        override fun onClick(pos: Int) {
                            Toast.makeText(this@MainActivity, "Delete", Toast.LENGTH_SHORT).show()
//                            adapter.callDeleteFunction(pos)
                        }
                    }
                ))

                buffer.add(SwipeButton(
                    this@MainActivity,
                    R.drawable.editicon,
                    Color.parseColor("#B9D40B"),
                    "Edit",
                    12f.spToPx(this@MainActivity),
                    Color.WHITE,
                    R.font.muli_bold,
                    8f.dpToPx(this@MainActivity),
                    object : OnSwipeButtonClickListener {
                        override fun onClick(pos: Int) {
                            Toast.makeText(this@MainActivity, "Edit", Toast.LENGTH_SHORT).show()
//                            adapter.callEditFunction(pos)
                        }
                    }
                ))
            }

        }
    }

    fun Float.spToPx(context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            context.resources.displayMetrics
        )
    }

    fun Float.dpToPx(context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            context.resources.displayMetrics
        )
    }
}