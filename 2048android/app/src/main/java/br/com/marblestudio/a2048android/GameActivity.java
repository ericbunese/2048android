package br.com.marblestudio.a2048android;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GameActivity extends AppCompatActivity{

    private static String DEBUG_TAG = "--2048--debug:";
    private int xstart, ystart, x, y;
    private int threshold = 200;
    private boolean thresholdPassed = false, moveSuccess;
    private Game2048 gameBoard;
    private ScoreMan scoreMan;
    private Button playAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        playAgain = (Button) findViewById(R.id.playAgain);
        gameBoard = (Game2048) findViewById(R.id.board);
        if (gameBoard.spawn())
        {
            Log.d(DEBUG_TAG, "SPAWN");
            gameBoard.invalidate();
        }
        else
        {
            Log.d(DEBUG_TAG, "NOPE");
        }
        scoreMan = (ScoreMan) findViewById(R.id.scoreMan);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            loadGame();
        }
    }

    public void clickHandlerPlayAgain(View view)
    {
        Log.d(DEBUG_TAG, "PLAY AGAIN");
        gameBoard.restartGame();
        saveGame();
        playAgain.setVisibility(View.INVISIBLE);
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

    private boolean swipe(int angle)
    {
        boolean success = false;
        if (angle<45 && angle>=0 || angle>=315 && angle<=360)
        {
            //RIGHT
            success = gameBoard.moveDown();
            Log.d(DEBUG_TAG, "swipe right");
        }
        else if (angle>=45 && angle<135)
        {
            //UP
            success = gameBoard.moveLeft();
            Log.d(DEBUG_TAG, "swipe up");
        }
        else if (angle>=135 && angle<225)
        {
            //LEFT
            success = gameBoard.moveUp();
            Log.d(DEBUG_TAG, "swipe left");
        }
        else
        {
            //DOWN
            success = gameBoard.moveRight();
            Log.d(DEBUG_TAG, "swipe down");
        }
        return success;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = MotionEventCompat.getActionMasked(event);

        if (gameBoard.getPlay())
        {

            switch (action) {
                case (MotionEvent.ACTION_DOWN):
                    thresholdPassed = false;
                    xstart = (int) event.getX();
                    ystart = (int) event.getY();
                    return true;
                case (MotionEvent.ACTION_MOVE):
                    x = (int) event.getX();
                    y = (int) event.getY();
                    if (!thresholdPassed) {
                        if (pointDistance(xstart, ystart, x, y) > threshold) {
                            thresholdPassed = true;
                        }
                    }

                    return true;
                case (MotionEvent.ACTION_UP):
                    x = (int) event.getX();
                    y = (int) event.getY();

                    if (thresholdPassed)
                    {
                        int angle = pointDirection(x, y, xstart, ystart);
                        moveSuccess = swipe(angle);
                        gameBoard.setPlay(false);
                        if (moveSuccess)
                        {
                            scoreMan.setText("SCORE: "+String.valueOf(gameBoard.score));
                            gameBoard.setPlay(false);

                            if (gameBoard.spawn())
                            {
                                gameBoard.invalidate();
                                saveGame();
                                gameBoard.setPlay(true);
                                if (gameBoard.gameOver())
                                {
                                    gameBoard.invalidate();
                                    Toast toast = Toast.makeText(this, "GAME OVER", Toast.LENGTH_SHORT);
                                    toast.show();

                                    playAgain.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        else gameBoard.setPlay(true);
                    }
                    return true;
                default:
                    return false;//super.onTouchEvent(event);
            }
        }
        else
        {
            return false;
        }
    }

    public void loadGame()
    {
        FileServices fs = new FileServices();
        try
        {
            FileInputStream fis = fs.getReader(this, "2048.txt");
            for (int i=0;i<4;++i)
            {
                for (int j=0;j<4;++j)
                {
                    int val = (int) Math.pow(2, fis.read());
                    if (val!=1)
                        gameBoard.setBoardPos(i, j, val);
                }
            }
            fis.close();
            Log.d(DEBUG_TAG, "Game Loaded Succesfully");
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void saveGame()
    {
        FileServices fs = new FileServices();
        try
        {
            FileOutputStream fos = fs.getWriter(this, "2048.txt");
            for (int i=0;i<4;++i)
            {
                for (int j=0;j<4;++j)
                {
                    int val = (int) (Math.log(gameBoard.getBoardPos(i, j)) / Math.log(2));
                    fos.write(val);
                }
            }
            fos.close();
            Log.d(DEBUG_TAG, "Game Saved Succesfully");
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
