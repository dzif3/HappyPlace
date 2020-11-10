package com.example.happyplaces.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.R

abstract class SwipeToEditCallBack(context: Context): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
    private val editIcon=ContextCompat.getDrawable(context, R.drawable.ic_baseline_edit_24)
    private val intrinsicWidht = editIcon!!.intrinsicWidth
    private val intrinsicHeight = editIcon!!.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundcolor = Color.parseColor("#24AE05")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder.adapterPosition == 10 ) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
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
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCancelled = dX == 0f && !isCurrentlyActive

        if (isCancelled) {
            clearCanvas(c,itemView.left + dX, itemView.top.toFloat(), itemView.left.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        //draw edit background menjadi hijau
        background.color = backgroundcolor
        background.setBounds(itemView.left + dX.toInt(), itemView.top, itemView.left, itemView.bottom)
        background.draw(c)

        //menghitung posisi edit icon
        val editIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val editIconMargin = (itemHeight - intrinsicHeight)
        val editIconLeft = itemView.left + editIconMargin - intrinsicWidht
        val editIconRight = itemView.left + editIconMargin
        val editIconBottom = editIconTop + intrinsicHeight

        //draw delete of icon
        editIcon!!.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom)
        editIcon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(
        c: Canvas?,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ) {
        c?.drawRect(left, top, right, bottom, clearPaint)

    }

}