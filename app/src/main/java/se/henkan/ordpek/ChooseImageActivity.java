package se.henkan.ordpek;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;

import se.henkan.util.ScalingUtilities;
import se.henkan.util.ScalingUtilities.ScalingLogic;


public class ChooseImageActivity extends Activity {
    private static boolean isQuestionCapitalized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);







        // Set up the images...


        ImageButton btn21 = (ImageButton) findViewById(R.id.chooseImageButton21);


        File[] files = this.getFilesDir().listFiles();
        for (File f : files) {

            Bitmap unscaledBitmap = ScalingUtilities.decodeResource(getResources(), R.drawable.op_agnes, 350, 350, ScalingLogic.CROP);
            Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, 350, 350, ScalingLogic.CROP);
            unscaledBitmap.recycle();

            btn21.setImageBitmap(scaledBitmap);


            // Set the question
            TextView question = (TextView) findViewById(R.id.textViewImageQuestion);
            question.setText(f.getName().substring(0, f.getName().length()-4));
        }
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

    // Toggle between CAPITAL/lower letters in the question when pressed...
    public void toggleCaps(View view) {
        TextView textView = (TextView) view;
        String questionString = textView.getText().toString();

        if (isQuestionCapitalized) {
            questionString = questionString.substring(0,1).toUpperCase() +
                    questionString.substring(1).toLowerCase();
            textView.setText(questionString);
            isQuestionCapitalized = false;
        } else {
            questionString = questionString.toUpperCase();
            textView.setText(questionString);
            isQuestionCapitalized = true;
        }
    }
}
