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
    Paint redPaint;

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


    /**
     * This method is used to adjust the position of the ball when the tick method is called
     */
    public void adjustBallPosition()
    {
        Random rand = new Random(); //generate random speed changes as the ball bounces
        int temp = 0;
        temp = rand.nextInt(6)-10; //range of [-5,5]


        ballX += speedX;
        ballY += speedY;

        //check if the ball has hit the bottom or top wall
        if(vertWallCollision())
        {
            speedY = -speedY + temp;
            if(speedY > 25)
            {
                speedY = 20;
            }
        }
        else if(horzWallCollision())
        {
            speedX = -speedX + temp;
            if(speedX > 25)
            {
                speedX = 20;
            }
        }
    }

    /**
     *
     * @return true if ball collided with top or bottom wall
     */
    private boolean vertWallCollision()
    {
        //todo fix ball going way over the top wall, bounces a little after the bottom wall

        //check if there was a collision with the top wall
        if(ballY-ballRad < wallWidth)
        {
            return true;
        }
        //check bottom wall
        else if(ballY+ballRad > height-wallWidth)
        {
            return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
    private boolean horzWallCollision()
    {
        if(ballX+ballRad >= width-wallWidth)
        {
            return true;
        }
        return false;
    }

    private boolean gameOver()
    {
        return false;
    }

    public void draw(Canvas g)
    {
        g.drawCircle(ballX, ballY, 60, redPaint);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
