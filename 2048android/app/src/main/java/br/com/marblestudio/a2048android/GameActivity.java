package br.com.marblestudio.a2048android;

import android.app.Activity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GameActivity extends AppCompatActivity{

    private static String DEBUG_TAG = "--2048--debug:";
    private int xstart, ystart, x, y;
    private int threshold = 200;
    private boolean touchStarted = false, thresholdPassed = false;
    private Game2048 gameBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameBoard = (Game2048) findViewById(R.id.board);
    }

    //@Usar essa função para definir o threshold de swipe para um valor que tenha algo a ver com o tamanho da tela.
    public void setThreshold(int threshold)
    {
        this.threshold = threshold;
    }

    private int pointDistance(int x1, int y1, int x2, int y2)
    {
        double xx = ((double)x2-(double)x1);
        double yy = ((double)y2-(double)y1);
        return (int) Math.sqrt(xx*xx + yy*yy);
    }

    private int pointDirection(int x1, int y1, int x2, int y2)
    {
        float angle = (float) Math.toDegrees(Math.atan2(y2 - y1, x1 - x2));

        if(angle < 0)
        {
            angle += 360;
        }

        return (int) angle;
    }

    private void swipe(int angle)
    {
        if (angle<45 && angle>=0 || angle>=315 && angle<=360)
        {
            //RIGHT
            Log.d(DEBUG_TAG, "swipe right");
        }
        else if (angle>=45 && angle<135)
        {
            //UP
            Log.d(DEBUG_TAG, "swipe up");
        }
        else if (angle>=135 && angle<225)
        {
            //LEFT
            Log.d(DEBUG_TAG, "swipe left");
        }
        else
        {
            //DOWN
            Log.d(DEBUG_TAG, "swipe down");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = MotionEventCompat.getActionMasked(event);

        switch(action)
        {
            case (MotionEvent.ACTION_DOWN) :
                touchStarted = true;
                thresholdPassed = false;
                xstart = (int)event.getX();
                ystart = (int)event.getY();
                return true;
            case (MotionEvent.ACTION_MOVE) :
                x = (int)event.getX();
                y = (int)event.getY();
                if (!thresholdPassed)
                {
                    if (pointDistance(xstart, ystart, x, y)>threshold)
                    {
                        thresholdPassed = true;
                    }
                }

                return true;
            case (MotionEvent.ACTION_UP) :
                x = (int)event.getX();
                y = (int)event.getY();

                if (thresholdPassed)
                {
                    int angle = pointDirection(x, y, xstart, ystart);
                    swipe(angle);
                }

                touchStarted = false;
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }
}
