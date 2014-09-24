package se.henkan.ordpek;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class ChooseImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);

        ImageButton btn11 = (ImageButton) findViewById(R.id.chooseImageButton11);
        btn11.setImageResource(R.drawable.agnes);


        TextView question = (TextView) findViewById(R.id.textViewQuestion);


        question.setText("Tage");




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.choose_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Check the answer...
    public void checkImageAnswer(View view) {
        TextView answerView = (TextView) findViewById(R.id.textViewAnswer);

        if (view.getId() == R.id.chooseImageButton22){
            answerView.setBackgroundColor(Color.GREEN);


        } else {
            answerView.setBackgroundColor(Color.RED);
        }

    }


}
