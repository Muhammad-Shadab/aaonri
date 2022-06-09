package com.aaonri.app.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/* set spacing for grid view */
class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val spacing_top: Int
) :
    RecyclerView.ItemDecoration() {
    private val includeEdge = true
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item phases_position
        val column = position % spanCount // item column
        if (includeEdge) {
            outRect.left =
                spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right =
                (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
            if (position < spanCount) { // top edge
                outRect.top = spacing_top
            }
            outRect.bottom = spacing_top // item bottom
        } else {
            outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right =
                spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing_top // item top
            }
        }
    }
}

