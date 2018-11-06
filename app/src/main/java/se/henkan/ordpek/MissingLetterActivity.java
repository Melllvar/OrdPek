package se.henkan.ordpek;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import se.henkan.util.DatabaseHandler;
import se.henkan.util.ScalingUtilities;

/**
 * A random letter in the word is missing...
 */


//ToDo: Add check to verify that there are enough images in the DB (and enough unique names/letters)

public class MissingLetterActivity extends Activity {
    private static boolean isButtonsCapitalized = false;
    private Button correctAnswer;
    private DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_word);

        // Setup the game board
        randomizeBoard();
    }

    // Setup the buttons and image...
    private void randomizeBoard(){
        // Get the buttons in an array...
        Button[] buttons = { (Button) findViewById(R.id.Button1),
                (Button) findViewById(R.id.Button2),
                (Button) findViewById(R.id.Button3),
                (Button) findViewById(R.id.Button4)};


        Random random = new Random();
        List<String> imagePaths = new ArrayList<String>();

        List<Integer> ids= db.getAllIDs();
        for (Button button : buttons){
            int randInt = random.nextInt(ids.size());

            button.setText(getToggledText(db.getImageEntry(ids.get(randInt)).get_name()));
            imagePaths.add(db.getImageEntry(ids.get(randInt)).get_filePath());

            ids.remove(randInt);
        }

        // Choose a random button to be "correct"
        int randInt = random.nextInt(4);
        correctAnswer = buttons[randInt];
        ImageView image = (ImageView) findViewById(R.id.Image1);

        // ToDo: Remove hardcoded sizes
        Bitmap unscaledBitmap = ScalingUtilities.decodeFile(imagePaths.get(randInt),
                350, 350, ScalingUtilities.ScalingLogic.CROP);
        Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, 350, 350,
                ScalingUtilities.ScalingLogic.CROP);
        unscaledBitmap.recycle();
        image.setImageBitmap(scaledBitmap);
    }

    // Check the answer...
    public void checkButtonAnswer(View view) {
        final TextView answerView = (TextView) findViewById(R.id.textViewAnswerButtons);

        // Set the buttons to not be clickable to avoid interruptions
        findViewById(R.id.Button1).setClickable(false);
        findViewById(R.id.Button2).setClickable(false);
        findViewById(R.id.Button3).setClickable(false);
        findViewById(R.id.Button4).setClickable(false);

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
                randomizeBoard();
                findViewById(R.id.Button1).setClickable(true);
                findViewById(R.id.Button2).setClickable(true);
                findViewById(R.id.Button3).setClickable(true);
                findViewById(R.id.Button4).setClickable(true);
            }
        }, 1000);
    }

    // Set the question string
    private String getToggledText(String question){
        question = question.toUpperCase();
        if (!isButtonsCapitalized){
            question = question.substring(0, 1) + question.substring(1).toLowerCase();
        }
        return question;
    }

    // Toggle between CAPITAL/lower letters in the buttons
    public void toggleCapsButtons(View view) {
        String questionString;
        Button[] buttons = { (Button) findViewById(R.id.Button1),
                (Button) findViewById(R.id.Button2),
                (Button) findViewById(R.id.Button3),
                (Button) findViewById(R.id.Button4)};

        for (Button button : buttons){
            questionString = button.getText().toString();
            if (isButtonsCapitalized) {
                questionString = questionString.substring(0,1).toUpperCase() +
                        questionString.substring(1).toLowerCase();
                button.setText(questionString);
            } else {
                questionString = questionString.toUpperCase();
                button.setText(questionString);
            }
        }
        isButtonsCapitalized = ! isButtonsCapitalized;
    }


}
