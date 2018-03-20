package shuler18.pong_001803990;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    /**
     * creates an AnimationSurface containing a TestAnimator.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pong_main);
        //surface that is being drawn on
        pongAnimator pa = new pongAnimator();
        // Connect the animation surface with the animator
        AnimationSurface mySurface = (AnimationSurface) this
                .findViewById(R.id.animationSurface);
        int height = mySurface.getHeight();
        pa.setHeight(mySurface.getHeight());
        pa.setWidth(mySurface.getWidth());
        pa.pongInit();

        mySurface.setAnimator(pa);



    }
}
