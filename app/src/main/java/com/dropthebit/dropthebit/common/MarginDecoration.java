package com.dropthebit.dropthebit.common;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by HTJ_Home_PC on 2018-02-01.
 */

public class MarginDecoration extends RecyclerView.ItemDecoration {
    private int top;
    private int gap;
    private int bottom;

    public MarginDecoration(int top, int gap, int bottom) {
        this.top = top;
        this.gap = gap;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildLayoutPosition(view);
        int total = parent.getAdapter() != null ? parent.getAdapter().getItemCount() : 0;
        if (position == 0) {
            outRect.top = top;
        }
        if (position < total - 1) {
            outRect.bottom = gap;
        }
        if (position == total - 1) {
            outRect.bottom = bottom;
        }
    }
}
