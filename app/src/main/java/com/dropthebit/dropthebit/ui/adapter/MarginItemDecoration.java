package com.dropthebit.dropthebit.ui.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mason-hong on 2018. 1. 7..
 * LinearLayoutManager 전용
 */
public class MarginItemDecoration extends RecyclerView.ItemDecoration {
    private int margin = 0;

    public MarginItemDecoration(int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int last = parent.getAdapter() != null ? parent.getAdapter().getItemCount() - 1 : -1;
        int position = parent.getChildLayoutPosition(view);
        if (position != 0) {
            outRect.top = margin;
        }
        if (position != last) {
            outRect.bottom = margin;
        }
    }
}
