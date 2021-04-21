package edu.neu.madcourse.welink.utility;

import android.content.Context;
import android.util.AttributeSet;

public class MyImageView extends androidx.appcompat.widget.AppCompatImageView {
    public MyImageView(Context context) {
        super(context);
    }
    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
