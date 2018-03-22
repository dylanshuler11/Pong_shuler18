package shuler18.pong_001803990;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by shule on 3/19/2018.
 */

public class ball {

    private int ballX;
    private int ballY;
    private int ballRad = 30;
    private int height;
    private int width;
    private int wallWidth = 50;
    private int speedX;
    private int speedY;
    public boolean inPlay = true;
    Paint redPaint;

    public void setBallX(int ballX) {
        this.ballX = ballX;
    }

    public void setBallY(int ballY) {
        this.ballY = ballY;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public int getBallX() {

        return ballX;
    }

    public int getBallY() {
        return ballY;
    }

    public int getBallRad() {
        return ballRad;
    }

    public int getWallWidth() {
        return wallWidth;
    }

    public int getSpeedX() {
        return speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    /**
     * Constructor to create ball object
     * @param x starting x position of ball
     * @param y starting y position of ball
     * @param sx speed of ball in x direction
     * @param sy speed of ball in y direction
     */
    public ball(int x, int y, int sx, int sy)
    {
        ballX = x;

        ballY = y;
        speedX = sx;
        speedY = sy;
        //initilize the color of the ball
        redPaint = new Paint();
        redPaint.setColor(Color.RED);
    }




    private boolean gameOver()
    {
        return false;
    }

    public void draw(Canvas g)
    {
        if(inPlay)
        {
            g.drawCircle(ballX, ballY, 60, redPaint);
        }
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
