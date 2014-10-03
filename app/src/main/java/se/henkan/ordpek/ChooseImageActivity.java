package se.henkan.ordpek;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import se.henkan.util.DatabaseHandler;
import se.henkan.util.ScalingUtilities;
import se.henkan.util.ScalingUtilities.ScalingLogic;


public class ChooseImageActivity extends Activity {
    private static boolean isQuestionCapitalized = false;
    private ImageButton correctAnswer;
    private DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);

        // Set up the images...
        setUpImagesFromDB();
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
        final TextView answerView = (TextView) findViewById(R.id.textViewAnswer);

        // Set the buttons to not be clickable to avoid interruptions
        findViewById(R.id.chooseImageButton11).setClickable(false);
        findViewById(R.id.chooseImageButton12).setClickable(false);
        findViewById(R.id.chooseImageButton21).setClickable(false);
        findViewById(R.id.chooseImageButton22).setClickable(false);

        if (view.getId() == correctAnswer.getId()){
            answerView.setBackgroundColor(Color.GREEN);

        } else {
            answerView.setBackgroundColor(Color.RED);
        }

        // Wait a while then set up the game again...
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                answerView.setBackgroundColor(Color.TRANSPARENT);
                setUpImagesFromDB();
                findViewById(R.id.chooseImageButton11).setClickable(true);
                findViewById(R.id.chooseImageButton12).setClickable(true);
                findViewById(R.id.chooseImageButton21).setClickable(true);
                findViewById(R.id.chooseImageButton22).setClickable(true);
            }
        }, 1500);
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

    @Deprecated
    private void setUpImages(){
        // Set up the images...
        ImageButton[] buttons = { (ImageButton) findViewById(R.id.chooseImageButton11),
                (ImageButton) findViewById(R.id.chooseImageButton12),
                (ImageButton) findViewById(R.id.chooseImageButton21),
                (ImageButton) findViewById(R.id.chooseImageButton22)};

        // List of all the images...(Use DB?)
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(this.getFilesDir().listFiles()));
        Random random = new Random();
        List<String> fileNames = new ArrayList<String>();

        // Set up the images...
        for (ImageButton button : buttons) {
            int randInt = random.nextInt(files.size());

            // ToDo: Remove hardcoded sizes
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(files.get(randInt).toString(), 350, 350, ScalingLogic.CROP);
            Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, 350, 350, ScalingLogic.CROP);
            unscaledBitmap.recycle();
            button.setImageBitmap(scaledBitmap);

            fileNames.add(files.get(randInt).getName().substring(3, files.get(randInt).getName().length() - 4));
            files.remove(randInt);
        }

        // Choose a random image to be "correct"
        int randInt = random.nextInt(4);
        correctAnswer = buttons[randInt];
        String question = fileNames.get(randInt);

        // Set the question text
        setQuestionText(question);
    }

    // Set up from database
    private void setUpImagesFromDB(){
        // Get the buttons in an array...
        ImageButton[] buttons = { (ImageButton) findViewById(R.id.chooseImageButton11),
                (ImageButton) findViewById(R.id.chooseImageButton12),
                (ImageButton) findViewById(R.id.chooseImageButton21),
                (ImageButton) findViewById(R.id.chooseImageButton22)};

        // List of all the images... (better to just get a random number and use as KEY?
        // Does this take long if database is large?
        ArrayList<ImageEntry> images = (ArrayList<ImageEntry>) db.getAllImageEntries();

        Random random = new Random();
        List<String> fileNames = new ArrayList<String>();

        // Set up the images...
        for (ImageButton button : buttons) {
            int randInt = random.nextInt(images.size());

            // ToDo: Remove hardcoded sizes
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(images.get(randInt).get_filePath(),
                    350, 350, ScalingLogic.CROP);
            Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, 350, 350,
                    ScalingLogic.CROP);
            unscaledBitmap.recycle();
            button.setImageBitmap(scaledBitmap);

            fileNames.add(images.get(randInt).get_name());
            images.remove(randInt);
        }

        // Choose a random image to be "correct"
        int randInt = random.nextInt(4);
        correctAnswer = buttons[randInt];
        String question = fileNames.get(randInt);

        // Set the question text
        setQuestionText(question);
    }

    // Set the question string
    private void setQuestionText(String question){
        TextView questionView = (TextView) findViewById(R.id.textViewImageQuestion);
        question = question.toUpperCase();

        if (!isQuestionCapitalized){
            question = question.substring(0, 1) + question.substring(1).toLowerCase();
        }
        questionView.setText(question);
    }
}
