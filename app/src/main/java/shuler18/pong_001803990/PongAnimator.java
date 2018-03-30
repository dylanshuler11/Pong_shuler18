package shuler18.pong_001803990;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dylan Shuler on 3/18/2018.
 */

public class PongAnimator implements Animator {


    private ArrayList<Ball> Balls = new ArrayList<>(); //stores list of all Balls
    private ArrayList<Ball> toBeDeleted = new ArrayList<>(); //list of Balls to be removed
    private int paddleHeight = 300; //total height of paddle
    private int playerPaddleY = 1512/2 - paddleHeight/2;//middle position of paddle, starting at midpoint
    private int compPaddleY = 1512/2 - 150; //comp's paddle size is fixed
    private int ballRad = 30; //radius of Ball
    private int wallWidth = 40; //width of walls drawn on edges
    private int height, width; //useable dimmensions of the board
    private int p1Score=0, p2Score=0; //keep track of the game's score
    private int aiSpeed;
    private int aiDefaultSpeed = 5;
    boolean dimmensionsSet = true; //used to get dimmensions on first run after the board is drawn

    MediaPlayer playerMp;
    MediaPlayer compMp;

    @Override
    public int interval()

    {
        return 3;
    }

    @Override
    public int backgroundColor()
    {
        return Color.rgb(0, 0, 0);
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

    /**
     * The tick method is called to redraw the canvas at the start of a new frame
     * @param g canvas being drawn on
     */
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

            //iterate through each Ball in list and update its position and speed
            for (Ball b : Balls) {
                if (b.inPlay) {
                    b.draw(g); // Draw the Ball in the correct position.
                    adjustBallPosition(b); //move Ball

                }
                //There is a smart ai used if there is only one ball in play
                if(Balls.size() == 1)
                {
                    aiSpeed = (int)(b.getSpeedY() * .8); //scale speed to allow ai to miss
                    compPaddleY += (aiSpeed); //increment location of paddle

                    if(compPaddleY+150 >= height)
                    {
                        compPaddleY = height-150;
                    }
                    else if(compPaddleY-150 <= 0)
                    {
                        compPaddleY = 150;
                    }
                }
                else if(Balls.size() != 0 && Balls.size() > 1) //ai simply moves up and down
                {
                    compPaddleY += aiDefaultSpeed;
                    if(compPaddleY+150 >= height)
                    {
                        compPaddleY = height-150;
                        aiDefaultSpeed = -aiDefaultSpeed;
                    }
                    else if(compPaddleY-150 <= 0)
                    {
                        compPaddleY = 150;
                        aiDefaultSpeed = -aiDefaultSpeed;
                    }
                }
            }

        /*
            External Citation:
            3/21/2018

            Couldn't remove Balls from arrayList without causing a concurrentModificationException
            Resource:
            https://stackoverflow.com/questions/10431981/remove-elements-from-collection-while-iterating

            solution:
            used removeAll method as seen in top answer

         */
            Balls.removeAll(toBeDeleted); //remove out of play Balls
            toBeDeleted.clear(); //empty list now that Balls are removed




    }



    /**
     * Helper method to draw the board and paddles
     * @param g animationsurface that is being drawn on
     */
    public void drawWalls(Canvas g)
    {

        Paint white = new Paint();
        white.setColor(Color.WHITE);

        //draw the center line
        int temp = wallWidth;
        int h = height/40;
        for(int i = 0; i < 10; i++)
        {

            g.drawRect(width/2-15,temp,width/2+15,temp+2*h,white);
            temp += 4*h;

        }


        //draw player's paddle
        g.drawRect(0, playerPaddleY +paddleHeight/2, 60, playerPaddleY -paddleHeight/2,white);

        //draw computer's paddle
        g.drawRect(width-60, compPaddleY +150, width, compPaddleY-150,white);

        white.setTextSize(50); //make it so the scores can actually be seen

        //draw player's scores
        g.drawText(""+p1Score,width/4,height/8,white); //p1 score
        g.drawText(""+p2Score,(3*width)/4,height/8,white); //p2 score

    }

    /**
     * Called when the user wants to move their paddle location
     * @param event a MotionEvent describing the touch
     */
    @Override
    public void onTouch(MotionEvent event)
    {
        int userClick = (int)event.getY();
        if(userClick+paddleHeight/2 > height) //user clicked below the playing field
        {
            playerPaddleY = height-paddleHeight/2;
        }
        else if(userClick-paddleHeight/2 < 0) //user clicked above playing field
        {
            playerPaddleY = 0+paddleHeight/2;
        }
        else //valid click
        {
            playerPaddleY = userClick;
        }
    }



    /**
     * This method is called at the start of a game to randomly set the motion of the Ball
     */
    public void pongInit()
    {
        height = 1512;
        width = 2560;
        addBall();
    }

    /**
     * This method is used to adjust the position of the Ball when the tick method is called
     * @param b Ball to be moved
     */
    public void adjustBallPosition(Ball b)
    {
        if(playerLost(b))
        {
            b.inPlay = false; //prevent the Ball from being drawn while it awaits deletion
            toBeDeleted.add(b); //add to list for removal
            p2Score += 1; //add one to the second player's score
        }
        else if(compLost(b))
        {
            b.inPlay = false;
            toBeDeleted.add(b);
            p1Score += 1;
        }
        else if(paddleHit(b))
        {
            b.setSpeedX(-b.getSpeedX());
            try {playerMp.start(); } catch(Exception e) {}; //prevent a crash if the MP hits a snag

        }
        else if(compPaddleHit(b))
        {
            b.setSpeedX(-b.getSpeedX());
            try {compMp.start(); } catch(Exception e) {};
        }
        else if(vertWallCollision(b))
        {
            b.setSpeedY(-b.getSpeedY());

        }


        b.setBallX(b.getBallX() + b.getSpeedX());
        b.setBallY(b.getBallY() + b.getSpeedY());

    }

    /**
     * check if a Ball went out of play
     * @param b Ball to check
     * @return true if Ball reached far left side
     */
    private boolean playerLost(Ball b)
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
     * @param b Ball to check
     * @return
     */
    private boolean paddleHit(Ball b)
    {
        //checking if Ball's y and x coordinates are contained in paddle,
        if(b.getBallY() >= playerPaddleY -paddleHeight/2 &&
                b.getBallY() <= playerPaddleY +paddleHeight/2 &&
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
     * Used to check if a Ball hit the top or bottom wall
     * @param b Ball that is being checked
     * @return true if Ball collided with top or bottom wall
     */
    private boolean vertWallCollision(Ball b)
    {


        //check if there was a collision with the top wall
        if(b.getBallY()-b.getBallRad() < 0 && b.getSpeedY() < 0)
        {
            return true;
        }
        //check bottom wall
        else if(b.getBallY()+b.getBallRad() > height && b.getSpeedY() > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * Used to check if the Ball hit the right side wall
     * @return true if there was a collision
     */
    private boolean compPaddleHit(Ball b)
    {
        //checking if Ball's y and x coordinates are contained in paddle,
        if(b.getBallY() >= compPaddleY - 150 &&
                b.getBallY() <= compPaddleY + 150 &&
                b.getBallX()+b.getBallRad()+60 >= width &&
                b.getSpeedX() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    /**
     * check if the human player scored
     * @param b ball being checked
     * @return true if player scored, false otherwise
     */
    private boolean compLost(Ball b)
    {
        if(b.getBallX() + b.getBallRad() >= width)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Called by addButton listener. Adds Ball with random starting properties to arrayList
     */
    public void addBall()
    {
        Random r = new Random();
        int sx, sy, xMod, yMod;
        xMod = yMod = 1;
        if(r.nextInt(2) == 1)
        {
            xMod = -1;

        }
        if(r.nextInt(2) == 1)
        {
            yMod = -1;
        }
        sx = (r.nextInt(20)+10) * xMod;
        sy = (r.nextInt(20)+10) * yMod;

        Balls.add(new Ball(r.nextInt(600)+1000, r.nextInt(550)+400,sx,sy));
    }

    /**
     * Remove most recently added Ball from arraylist. called by removeButton listener
     */
    public void removeBall()
    {
        try
        {
            Balls.remove(Balls.size()-1);
        }
        catch(Exception e)
        {
            //don't do anything, but prevent crash from trying to remove when there are no Balls
        }

    }

    public void resetScores()
    {
        p1Score = 0;
        p2Score = 0;
    }

    /**
     * adjust the size of the player's paddle
     * @param height size of the paddle
     */
    public void setPaddleHeight(int height)
    {
        this.paddleHeight = height;
    }

    /**
     * Configures the media player used to play noise when the player's paddle hits a ball
     * @param m mediaplayer using the player_paddle_effect audio file
     */
    public void addPlayerFx(MediaPlayer m)
    {
        playerMp = m;
    }

    public void addCompFx(MediaPlayer m)
    {
        compMp = m;
    }



}
