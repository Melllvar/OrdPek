package se.henkan.ordpek;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;


public class ChooseImageActivity extends Activity {
    private static boolean isQuestionCapitalized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);


        // Set up the images...
        ImageButton btn21 = (ImageButton) findViewById(R.id.chooseImageButton21);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;


        File[] files = this.getFilesDir().listFiles();
        for (File f : files) {
            //Uri uri = Uri.parse(f.toString());
            //btn22.setImageURI(uri);


            Bitmap bitmap = BitmapFactory.decodeFile(f.toString(), options);

            //Bitmap bm = Bitmap.createBitmap(bitmap);
            btn21.setImageBitmap(scaleBitmap(bitmap, 350, 350));






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

    // Toggle between CAPITAL/lower letters in the question...
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





    //TODO: Move to helper class??!!
    /**
     * Scales the provided bitmap to have the height and width provided.
     * (Alternative method for scaling bitmaps
     * since Bitmap.createScaledBitmap(...) produces bad (blocky) quality bitmaps.)
     *
     * @param bitmap is the bitmap to scale.
     * @param newWidth is the desired width of the scaled bitmap.
     * @param newHeight is the desired height of the scaled bitmap.
     * @return the scaled bitmap.
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }


}
