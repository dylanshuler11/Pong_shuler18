package shuler18.pong_001803990;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.Random;

/**
 * Created by Dylan Shuler on 3/18/2018.
 */

public class pongAnimator implements Animator {


    private int paddleHeight = 300;
    private int paddleY = 1512/2 - paddleHeight/2;//middle position of paddle
    private int ballRad = 30; //radius of ball
    private int wallWidth = 40;
    private ball b;
    private int height, width; //useable dimmensions of the board
    boolean dimmensionsSet = true;
    @Override
    public int interval() {
        return 1;
    }

    @Override
    public int backgroundColor() {
        return Color.rgb(100, 100, 100);
    }

    @Override
    public boolean doPause() {
        return false;
    }

    @Override
    public boolean doQuit() {
        return false;
    }

    @Override
    public void tick(Canvas g)
    {
        //check if game is paused maybe?
        //on first run, pass dimmensions to ball
        if(dimmensionsSet)
        {
            b.setHeight(g.getHeight());
            b.setWidth(g.getWidth());
            dimmensionsSet = false;
        }

        adjustBallPosition(b);

        //draw 3 walls
        Paint white = new Paint();
        white.setColor(Color.WHITE);
        g.drawRect(0,0,g.getWidth(),wallWidth,white);
        g.drawRect(g.getWidth()-wallWidth,0,g.getWidth(),g.getHeight(),white);
        g.drawRect(0,g.getHeight()-wallWidth,g.getWidth(),g.getHeight(),white);

        //draw the paddle
        Paint black = new Paint();
        black.setColor(Color.BLACK);
        g.drawRect(0, paddleY+paddleHeight/2, 60,paddleY-paddleHeight/2,black);

        // Draw the ball in the correct position.
        b.draw(g);
    }




    @Override
    public void onTouch(MotionEvent event) {
        //todo implement a way to prevent the paddle going past the edge of the wall
        paddleY = (int)event.getY();
    }



    /**
     * This method is called at the start of a game to randomly set the motion of the ball
     */
    public void pongInit()
    {
        //todo replace holder values with real values
        height = 1512;
        width = 2560;
        Random r = new Random();
        b = new ball(r.nextInt(width), r.nextInt(height),r.nextInt(20),r.nextInt(20));


        //todo figure out a way to get height and width parameters

    }

    /**
     * This method is used to adjust the position of the ball when the tick method is called
     */
    public void adjustBallPosition(ball b)
    {
        Random rand = new Random(); //generate random speed changes as the ball bounces
        int temp = 0;
        int reduction = rand.nextInt(1)-4;
        temp = rand.nextInt(6)-10; //range of [-5,5]
        boolean decrement = rand.nextInt(1) == 0 ? true : false;

        //update position of ball
        //todo fix setting of height and width in initial loop! 
        if(b.getBallX() + b.getSpeedX() > width-wallWidth)
        {
            b.setBallX(width-wallWidth);
        }
        else
        {
            b.setBallX(b.getBallX() + b.getSpeedX());
        }
        b.setBallY(b.getBallY() + b.getSpeedY());

        //check if the ball has hit the bottom or top wall
        if(paddleHit(b))
        {
            b.setSpeedX(-b.getSpeedX() + reduction);

        }
        else if(vertWallCollision(b))
        {
            b.setSpeedY(-b.getSpeedY() + temp);
            if(b.getSpeedY() > 25)
            {
                b.setSpeedY(20);
            }
        }
        else if(horzWallCollision(b))
        {
            b.setSpeedX(-b.getSpeedX() + temp);
            if(b.getSpeedX() > 25)
            {
                b.setSpeedX(20);
            }
        }
    }

    private boolean paddleHit(ball b)
    {

        if(b.getBallY() >= paddleY-paddleHeight/2 &&
                b.getBallY() <= paddleY+paddleHeight/2 &&
                b.getBallX() <= 60+b.getBallRad() &&
                b.getSpeedX() < 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     *
     * @return true if ball collided with top or bottom wall
     */
    private boolean vertWallCollision(ball b)
    {
        //todo fix ball going way over the top wall, bounces a little after the bottom wall

        //check if there was a collision with the top wall
        if(b.getBallY()-b.getBallRad() < wallWidth && b.getSpeedY() < 0)
        {
            return true;
        }
        //check bottom wall
        else if(b.getBallY()+b.getBallRad() > height-wallWidth && b.getSpeedY() > 0)
        {
            return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
    private boolean horzWallCollision(ball b)
    {
        if(b.getBallX()+ballRad >= width-wallWidth && b.getSpeedX() > 0)
        {
            return true;
        }
        return false;
    }


    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
