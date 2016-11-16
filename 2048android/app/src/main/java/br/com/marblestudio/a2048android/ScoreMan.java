package br.com.marblestudio.a2048android;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by eric on 15/11/16.
 */

public class ScoreMan extends TextView {

    public ScoreMan(Context context) {
        super(context);
    }

    public ScoreMan(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScoreMan(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ScoreMan(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int measure(int Spec)
    {
        int retVal = 0;

        int specMode = MeasureSpec.getMode(Spec);
        int specSize = MeasureSpec.getSize(Spec);

        if (specMode == MeasureSpec.UNSPECIFIED)
            retVal = 200;
        else
            retVal = specSize;

        return retVal;
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec)
    {
        int measuredWidth = measure(widthSpec);
        int measuredHeight = measure(heightSpec);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public void onDraw(Canvas canvas)
    {

        super.onDraw(canvas);
    }
}
