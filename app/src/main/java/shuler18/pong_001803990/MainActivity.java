package shuler18.pong_001803990;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * @author Dylan Shuler
 * Created on 3/18/2018
 *
 * Enhancements added:
 * Spinner to set size of player's paddle
 * Buttons to add and remove balls from play(unlimited range, but it slows down around 50)
 * Game pauses if there are no balls on screen, waits till user adds ball
 */
public class MainActivity extends AppCompatActivity {
//todo don't allow player to delete balls when there are none in play
    pongAnimator pa;
    /**
     * creates an AnimationSurface containing a TestAnimator.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pong_main);
        //surface that is being drawn on
        pa = new pongAnimator();
        // Connect the animation surface with the animator
        AnimationSurface mySurface = (AnimationSurface) this
                .findViewById(R.id.animationSurface);
        //setup listener for add button
        Button add = (Button)findViewById(R.id.addBallButton);
        add.setOnClickListener(new addButtonListener());
        //setup listener for remove button
        Button remove = (Button)findViewById(R.id.removeBallButton);
        remove.setOnClickListener(new removeButtonListener());

        initSizeSpinner();
        Spinner s = (Spinner)findViewById(R.id.spinner);
        s.setOnItemSelectedListener(new addSpinnerListener());
        s.setSelection(1); //start in normal mode
        pa.pongInit(); //configure settings for game to start

        mySurface.setAnimator(pa);



    }

    private void initSizeSpinner (){
        Spinner s = (Spinner)findViewById(R.id.spinner);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerValues,
                android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

    }

    private class addSpinnerListener implements Spinner.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {


            String s = (String)parent.getItemAtPosition(position); //get text from selection


            if(s.equals("Ez"))
            {
                pa.setPaddleHeight(600);
            }
            else if(s.equals("Normal"))
            {
                pa.setPaddleHeight(300);
            }
            else if(s.equals("Gg"))
            {
                pa.setPaddleHeight(100);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class addButtonListener implements View.OnClickListener
    {
        public void onClick(View button) {
            pa.addBall();
        }
    }

    private class removeButtonListener implements View.OnClickListener
    {
        public void onClick(View button) {
            pa.removeBall();
        }
    }
}
