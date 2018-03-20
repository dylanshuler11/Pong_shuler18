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

    private int paddleY = 250;
    private int ballRad = 30; //radius of ball
    private int wallWidth = 40;
    private ball b;
    private int height, width; //useable dimmensions of the board
    boolean dimmensionsSet = true;
    @Override
    public int interval() {
        return 30;
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

        b.adjustBallPosition();

        //draw 3 walls
        Paint white = new Paint();
        white.setColor(Color.WHITE);
        g.drawRect(0,0,g.getWidth(),40,white);
        g.drawRect(g.getWidth()-40,0,g.getWidth(),g.getHeight(),white);
        g.drawRect(0,g.getHeight()-40,g.getWidth(),g.getHeight(),white);

        //draw the paddle
        Paint black = new Paint();
        black.setColor(Color.BLACK);
        g.drawRect(0, paddleY, 60,paddleY-200,black);

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


    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
