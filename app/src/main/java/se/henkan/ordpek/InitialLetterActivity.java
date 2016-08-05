package se.henkan.ordpek;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import se.henkan.util.DatabaseHandler;
import se.henkan.util.ScalingUtilities;


public class InitialLetterActivity extends Activity {
    private static boolean isQuestionCapitalized = true;
    private ImageButton correctButton;
    private DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_letter);

        // Setup the game board
        randomizeBoard();
    }

    // Set random images in place...
    // ToDo: Add errorhandling (when not enough images...)
    // ToDo: Move this functionality to util?
    private void randomizeBoard(){
        // Get the buttons in a list
        ArrayList<ImageButton> buttons = new ArrayList<ImageButton>();
        buttons.add((ImageButton) findViewById(R.id.initialLetterButton11));
        buttons.add((ImageButton) findViewById(R.id.initialLetterButton12));
        buttons.add((ImageButton) findViewById(R.id.initialLetterButton21));
        buttons.add((ImageButton) findViewById(R.id.initialLetterButton22));

        Random random = new Random();

        List<String> imagePaths = new ArrayList<String>();
        List<Integer> ids= db.getAllIDs();

        // Set the "True" answer in a random button
        int randButton = random.nextInt(buttons.size());
        correctButton = buttons.get(randButton);

        int randId = random.nextInt(ids.size());
        ImageEntry trueImageEntry =  db.getImageEntry(ids.get(randId));

        Bitmap unscaledBitmap = ScalingUtilities.decodeFile(trueImageEntry.get_filePath(),
                350, 350, ScalingUtilities.ScalingLogic.CROP);
        Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, 350, 350,
                ScalingUtilities.ScalingLogic.CROP);
        unscaledBitmap.recycle();
        buttons.get(randButton).setImageBitmap(scaledBitmap);

        String trueLetter = trueImageEntry.get_firstLetter();

        buttons.remove(randButton);
        ids.remove(randId);

        // Set the remaining buttons...
        while (!buttons.isEmpty()){
            randButton = random.nextInt(buttons.size());
            randId = random.nextInt(ids.size());
            ImageEntry randImageEntry = db.getImageEntry(ids.get(randId));

            if (randImageEntry.get_firstLetter().compareToIgnoreCase(trueLetter) != 0) {
                unscaledBitmap = ScalingUtilities.decodeFile(randImageEntry.get_filePath(),
                        350, 350, ScalingUtilities.ScalingLogic.CROP);
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, 350, 350,
                        ScalingUtilities.ScalingLogic.CROP);
                unscaledBitmap.recycle();
                buttons.get(randButton).setImageBitmap(scaledBitmap);

                buttons.remove(randButton);
                ids.remove(randId);
            } else {
                ids.remove(randId);
            }
        }

        // Set the question Capital/small letter...
        setQuestionText(trueLetter);
    }

    // Check the answer
    public void checkLetterAnswer(View view){
        final TextView textViewAnswer = (TextView) findViewById(R.id.textViewAnswerLetter);

        if (view.getId() == correctButton.getId()){
            textViewAnswer.setBackgroundColor(Color.GREEN);

        } else {
            textViewAnswer.setBackgroundColor(Color.RED);
        }

        // Wait a while then set up the game again...
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                textViewAnswer.setBackgroundColor(Color.TRANSPARENT);
                randomizeBoard();
                findViewById(R.id.initialLetterButton11).setClickable(true);
                findViewById(R.id.initialLetterButton12).setClickable(true);
                findViewById(R.id.initialLetterButton21).setClickable(true);
                findViewById(R.id.initialLetterButton22).setClickable(true);
            }
        }, 1000);
    }

    // Toggle between capital/small letters..
    public void toggleCapsLetter(View view){
        TextView questionView = (TextView) view;
        String questionString = questionView.getText().toString();

        if (isQuestionCapitalized){
            questionString = questionString.toLowerCase();
        } else {
            questionString = questionString.toUpperCase();
        }
        isQuestionCapitalized = !isQuestionCapitalized;
        questionView.setText(questionString);
    }

    // Set the question string
    private void setQuestionText(String question){
        TextView questionView = (TextView) findViewById(R.id.TextViewLetterQuestion);
        question = question.toUpperCase();

        if (!isQuestionCapitalized){
            question = question.toLowerCase();
        }
        questionView.setText(question);
    }
}
