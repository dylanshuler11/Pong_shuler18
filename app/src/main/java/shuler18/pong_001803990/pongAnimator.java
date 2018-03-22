package shuler18.pong_001803990;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dylan Shuler on 3/18/2018.
 */

public class pongAnimator implements Animator {


    private ArrayList<ball> balls = new ArrayList<>(); //stores list of all balls
    private ArrayList<ball> toBeDeleted = new ArrayList<>(); //list of balls to be removed
    private int paddleHeight = 300; //total height of paddle
    private int paddleY = 1512/2 - paddleHeight/2;//middle position of paddle, starting at midpoint
    private int ballRad = 30; //radius of ball
    private int wallWidth = 40; //width of walls drawn on edges
    private int height, width; //useable dimmensions of the board

    boolean dimmensionsSet = true; //used to get dimmensions on first run after the board is drawn

    @Override
    public int interval()
    {
        return 5;
    }

    @Override
    public int backgroundColor()
    {
        return Color.rgb(100, 100, 100);
    }

    @Override
    public boolean doPause()
    {
        return false;
    }

    @Override
    public boolean doQuit()
    {
        return false;
    }

    @Override
    public void tick(Canvas g)
    {

            //on first run, set board dimmension for use in hit detection
            if (dimmensionsSet) {
                height = g.getHeight();
                width = g.getWidth();
                dimmensionsSet = false; //prevent reentering loop
            }

            drawWalls(g);

            //iterate through each ball in list and update its position and speed
            for (ball b : balls) {
                if (b.inPlay) {
                    b.draw(g); // Draw the ball in the correct position.
                    adjustBallPosition(b); //move ball

                }
            }

        /*
            External Citation:
            3/21/2018

            Couldn't remove balls from arrayList without causing a concurrentModificationException
            Resource:
            https://stackoverflow.com/questions/10431981/remove-elements-from-collection-while-iterating

            solution:
            used removeAll method as seen in top answer

         */
            balls.removeAll(toBeDeleted); //remove out of play balls
            toBeDeleted.clear(); //empty list now that balls are removed




    }

    /**
     * Helper method to draw the walls and paddle
     * @param g animationsurface that is being drawn on
     */
    public void drawWalls(Canvas g)
    {
        //draw 3 walls
        Paint walls = new Paint();
        walls.setColor(Color.BLUE);
        g.drawRect(100,0,g.getWidth(),wallWidth,walls); //top wall
        g.drawRect(g.getWidth()-wallWidth,0,g.getWidth(),g.getHeight(),walls); //left wall
        g.drawRect(100,g.getHeight()-wallWidth,g.getWidth(),g.getHeight(),walls); //bottom wall

        //draw the paddle
        Paint paddle = new Paint();
        paddle.setColor(Color.GREEN);
        g.drawRect(0, paddleY+paddleHeight/2, 60,paddleY-paddleHeight/2,paddle);


    }

    @Override
    public void onTouch(MotionEvent event) {
        //todo implement a way to prevent the paddle going past the edge of the wall

        //paddleY = (int)event.getY(); turns out this isn't needed in part a
    }



    /**
     * This method is called at the start of a game to randomly set the motion of the ball
     */
    public void pongInit()
    {
        height = 1512;
        width = 2560;
        Random r = new Random();
        balls.add(new ball(r.nextInt(width), r.nextInt(height),r.nextInt(20),r.nextInt(20)));
    }

    /**
     * This method is used to adjust the position of the ball when the tick method is called
     * @param b ball to be moved
     */
    public void adjustBallPosition(ball b)
    {
        /* probably crap but maybe i'll use it later somehow
        Random rand = new Random(); //generate random speed changes as the ball bounces
        int temp = 0;
        int reduction = rand.nextInt(1)-2; //range of [-4,0]
        temp = rand.nextInt(6)-10; //range of [-5,5]

        boolean slow = rand.nextBoolean(); //randomly decide whether to slow down after wall contact
        */


        if(playerLost(b))
        {
            b.inPlay = false; //prevent the ball from being drawn while it awaits deletion
            toBeDeleted.add(b); //add to list for removal
        }
        else if(paddleHit(b))
        {
            b.setSpeedX(-b.getSpeedX());

        }
        else if(vertWallCollision(b))
        {
            b.setSpeedY(-b.getSpeedY());

        }
        else if(horzWallCollision(b))
        {
            b.setSpeedX(-b.getSpeedX());

        }

        b.setBallX(b.getBallX() + b.getSpeedX());
        b.setBallY(b.getBallY() + b.getSpeedY());

    }

    /**
     * check if a ball went out of play
     * @param b ball to check
     * @return true if ball reached far left side
     */
    private boolean playerLost(ball b)
    {
        if(b.getBallX() - b.getBallRad() <= 0)
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
     * @param b ball to check
     * @return
     */
    private boolean paddleHit(ball b)
    {
        //checking if ball's y and x coordinates are contained in paddle,
        if(b.getBallY() >= paddleY-paddleHeight/2 &&
                b.getBallY() <= paddleY+paddleHeight/2 &&
                b.getBallX() <= 40+b.getBallRad() &&
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
     * Used to check if a ball hit the top or bottom wall
     * @param b ball that is being checked
     * @return true if ball collided with top or bottom wall
     */
    private boolean vertWallCollision(ball b)
    {


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
     * Used to check if the ball hit the right side wall
     * @return true if there was a collision
     */
    private boolean horzWallCollision(ball b)
    {
        if(b.getBallX()+ballRad >= width-wallWidth && b.getSpeedX() > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * Called by addButton listener. Adds ball with random starting properties to arrayList
     */
    public void addBall()
    {
        Random r = new Random();
        balls.add(new ball(r.nextInt(width), r.nextInt(height),r.nextInt(20)+10,r.nextInt(20)+10));
    }

    /**
     * Remove most recently added ball from arraylist. called by removeButton listener
     */
    public void removeBall()
    {
        try
        {
            balls.remove(balls.size()-1);
        }
        catch(Exception e)
        {
            //don't do anything, but prevent crash from trying to remove when there are no balls
        }

    }

    /**
     * adjust the size of the player's paddle
     * @param height size of the paddle
     */
    public void setPaddleHeight(int height)
    {
        this.paddleHeight = height;
    }

}
