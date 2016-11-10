package br.com.marblestudio.a2048android;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by eric on 10/11/16.
 */

public class Game2048 extends View
{
    private int[][] board = new int[4][4];

    public void setBoardPos(int h, int v, int val)
    {
        this.board[h][v] = val;
    }

    public int getBoardPos(int h, int v)
    {
        return this.board[h][v];
    }

    public Game2048(Context context) {
        super(context);
    }

    public Game2048(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Game2048(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Game2048(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
