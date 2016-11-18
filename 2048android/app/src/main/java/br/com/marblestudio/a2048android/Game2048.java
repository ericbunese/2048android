package br.com.marblestudio.a2048android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by eric on 10/11/16.
 */

public class Game2048 extends View
{
    public int score;
    private int[][] board = new int[4][4];
    private Paint cellPaint, cellBackgroundPaint, textPaint, textBorderPaint;
    private Random randomMan;
    private boolean play;

    public void setBoardPos(int h, int v, int val)
    {
        this.board[h][v] = val;
    }

    public int getBoardPos(int h, int v)
    {
        return this.board[h][v];
    }

    public boolean getPlay()
    {
        return play;
    }

    public void setPlay(boolean play)
    {
        this.play = play;
    }


    public boolean gameOver()
    {
        boolean ended = true;
        if (countEmpty()>0) return false;
        if (findPairDown()) return false;
        rotateBoard();
        if (findPairDown()) ended = false;
        rotateBoard();
        rotateBoard();
        rotateBoard();
        return ended;
    }

    /*
    private boolean gameOver()
    {
        int free = 0;
        for (int i=0;i<4;++i)
        {
            for(int j=0;j<4;++j)
            {
                if (board[i][j]==0)
                    free++;
            }
        }
        return (free==0);
    }*/

    public boolean spawn()
    {
        if (gameOver())
            return false;
        else
        {
            int rand = randomMan.nextInt(3), val;
            int[] pos;
            ArrayList<int[]> list = new ArrayList<>();

            //Make a list of all free spots in the game board
            for (int i=0;i<4;++i)
            {
                for (int j=0;j<4;++j)
                {
                    if (getBoardPos(i, j)==0)
                    {
                        pos = new int[2];
                        pos[0] = i;
                        pos[1] = j;
                        list.add(pos);
                    }
                }
            }

            //Decide if we're placing a two or four
            if (rand<3)
                val = 2;
            else val = 4;

            //Grab a random position within that list
            Collections.shuffle(list);
            pos = list.get(0);

            //Place the new number at that position
            setBoardPos(pos[0], pos[1], val);
            return true;
        }
    }

    public void rotateBoard()
    {
        int n = 4;
        int tmp;
        for (int i=0; i<n/2; i++)
        {
            for (int j=i; j<n-i-1; j++)
            {
                tmp = board[i][j];
                board[i][j] = board[j][n-i-1];
                board[j][n-i-1] = board[n-i-1][n-j-1];
                board[n-i-1][n-j-1] = board[n-j-1][i];
                board[n-j-1][i] = tmp;
            }
        }
    }

    public int findTarget(int array[],int x,int stop)
    {
        int t;
        // if the position is already on the first, don't evaluate
        if (x==0)
        {
            return x;
        }
        for(t=x-1;t>=0;t--)
        {
            if (array[t]!=0)
            {
                if (array[t]!=array[x])
                {
                    // merge is not possible, take next position
                    return t+1;
                }
                return t;
            }
            else
            {
                // we should not slide further, return this one
                if (t==stop)
                {
                    return t;
                }
            }
        }
        // we did not find a
        return x;
    }

    public boolean slideArray(int array[])
    {
        boolean success = false;
        int x,t,stop=0;

        for (x=0;x<4;x++)
        {
            if (array[x]!=0)
            {
                t = findTarget(array,x,stop);
                // if target is not original position, then move or merge
                if (t!=x)
                {
                    // if target is zero, this is a move
                    if (array[t]==0)
                    {
                        array[t]=array[x];
                    }
                    else if (array[t]==array[x])
                    {
                        // merge (increase power of two)
                        array[t]*=2;
                        // increase score
                        Log.d("SCORETHING", String.valueOf(array[t]));
                        score+=array[t];
                        // set stop to avoid double merge
                        stop = t+1;
                    }
                    array[x]=0;
                    success = true;
                }
            }
        }
        return success;
    }

    public boolean moveUp()
    {
        boolean success = false;
        int x;
        for (x=0;x<4;x++)
        {
            success |= slideArray(board[x]);
        }
        return success;
    }

    public boolean moveLeft()
    {
        boolean success;
        rotateBoard();
        success = moveUp();
        rotateBoard();
        rotateBoard();
        rotateBoard();
        return success;
    }

    public boolean moveDown()
    {
        boolean success;
        rotateBoard();
        rotateBoard();
        success = moveUp();
        rotateBoard();
        rotateBoard();
        return success;
    }

    public boolean moveRight()
    {
        boolean success;
        rotateBoard();
        rotateBoard();
        rotateBoard();
        success = moveUp();
        rotateBoard();
        return success;
    }

    public boolean findPairDown()
    {
        int x,y;
        for (x=0;x<4;x++)
        {
            for (y=0;y<4-1;y++)
            {
                if (board[x][y]==board[x][y+1])
                    return true;
            }
        }
        return false;
    }

    public int countEmpty()
    {
        int x,y;
        int count=0;
        for (x=0;x<4;x++) {
            for (y=0;y<4;y++) {
                if (board[x][y]==0) {
                    count++;
                }
            }
        }
        return count;
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

    public void restartGame()
    {
        score = 0;
        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j)
                board[i][j] = 0;

        play = true;
        spawn();
        this.invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startGame()
    {
        score = 0;
        randomMan = new Random();

        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j)
                board[i][j] = 0;

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

        play = true;
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
