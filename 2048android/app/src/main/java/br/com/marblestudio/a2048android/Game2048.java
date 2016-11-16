package br.com.marblestudio.a2048android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by eric on 10/11/16.
 */

public class Game2048 extends View
{
    private int[][] board = new int[4][4];
    private Paint cellPaint, cellBackgroundPaint, textPaint, textBorderPaint;
    private Random randomMan;
    private ScoreMan scoreMan;

    public void setBoardPos(int h, int v, int val)
    {
        this.board[h][v] = val;
    }

    public int getBoardPos(int h, int v)
    {
        return this.board[h][v];
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startGame()
    {
        scoreMan = (ScoreMan) findViewById(R.id.scoreMan);
        randomMan = new Random();

        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j)
                board[i][j] = 0;//(int) Math.pow(2, randomMan.nextInt(11));

        cellBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        cellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cellPaint.setStyle(Paint.Style.STROKE);
        cellPaint.setStrokeWidth(4);
        cellPaint.setColor(getResources().getColor(R.color.cellBackground, null));

        textBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textBorderPaint.setColor(0xff000000);
        textBorderPaint.setTextSize(40);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(getResources().getColor(R.color.white, null));
        textPaint.setTextSize(40);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Game2048(Context context) {
        super(context);
        startGame();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Game2048(Context context, AttributeSet attrs) {
        super(context, attrs);
        startGame();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Game2048(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        startGame();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Game2048(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        startGame();
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec)
    {
        int measuredWidth = measure(widthSpec);
        int measuredHeight = measure(heightSpec);
        int diam = Math.min(measuredWidth, measuredHeight);

        setMeasuredDimension(diam, diam);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onDraw(Canvas canvas)
    {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int xx, yy, sizex, sizey, offx, offy, textWidth, tx, ty;
        String boardPos;

        sizex = (int) Math.floor(measuredWidth*0.2);
        sizey = (int) Math.floor(measuredHeight*0.2);
        offx = (int) Math.floor(measuredWidth*0.025);
        offy = (int) Math.floor(measuredHeight*0.025);

        setBackgroundColor(getResources().getColor(R.color.boardBackground, null));
        for(int i=0;i<4;++i)
        {
            yy = (int) Math.floor(measuredHeight*0.25)*i+offy;
            ty = (int) Math.floor(yy+sizey*0.5)+20;
            for (int j = 0; j < 4; ++j)
            {
                cellBackgroundPaint.setColor(getColorFromNumber(board[i][j]));
                boardPos = String.valueOf(board[i][j]);

                textWidth = (int)textPaint.measureText(boardPos);
                xx = (int) Math.floor(measuredWidth*0.25)*j+offx;
                tx = (int) Math.floor(xx+sizex*0.5-textWidth*0.5);

                canvas.drawRect(xx, yy, xx+sizex, yy+sizey, cellBackgroundPaint);
                canvas.drawRect(xx, yy, xx+sizex, yy+sizey, cellPaint);

                if (board[i][j]>0)
                {
                    canvas.drawText(boardPos, tx - 1, ty - 1, textBorderPaint);
                    canvas.drawText(boardPos, tx - 1, ty + 1, textBorderPaint);
                    canvas.drawText(boardPos, tx + 1, ty - 1, textBorderPaint);
                    canvas.drawText(boardPos, tx + 1, ty + 1, textBorderPaint);
                    canvas.drawText(boardPos, tx, ty, textPaint);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private int getColorFromNumber(int number)
    {
        Resources r = getResources();
        switch(number)
        {
            case 0: return r.getColor(R.color.number0, null);
            case 2: return r.getColor(R.color.number2, null);
            case 4: return r.getColor(R.color.number4, null);
            case 8: return r.getColor(R.color.number8, null);
            case 16: return r.getColor(R.color.number16, null);
            case 32: return r.getColor(R.color.number32, null);
            case 64: return r.getColor(R.color.number64, null);
            case 128: return r.getColor(R.color.number128, null);
            case 256: return r.getColor(R.color.number256, null);
            case 512: return r.getColor(R.color.number512, null);
            case 1024: return r.getColor(R.color.number1024, null);
            case 2048: return r.getColor(R.color.number2048, null);
            default: return r.getColor(R.color.number, null);
        }
    }
}
