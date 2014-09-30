package se.henkan.ordpek;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;


//ToDo: Add settings "VERSALER/Gemener"
//ToDo: Add types of game: {"Välj rätt bild", "Välj rätt ord", "Begynnelsebokstav"}

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Save some (default) drawables to internal storage...
        if (this.getFilesDir().listFiles().length < 4) {

            Log.d("INFO:::", "Copy images from drawables...");
            




            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.agnes2);
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput("ankan.png", Context.MODE_PRIVATE);
                bm.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }







        // List files in internal storage
        // REMOVE ME!
        Log.d("INFO:::", this.getFilesDir().toString());
        File[] files = this.getFilesDir().listFiles();
        for (File f : files) {
            Log.d("INFO:::", f.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    /** Called when the user clicks the "Choose image" button*/
    public void chooseImage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ChooseImageActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the "Choose word" button*/
    public void chooseWord(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ChooseWordActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the "Initial letter" button*/
    public void initialLetter(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, InitialLetterActivity.class);
        startActivity(intent);
    }









    /**
     * Converts drawable to Bitmap
     *
     *
     * ToDo: Should this method reside here?
     * http://stackoverflow.com/questions/12559974/save-images-from-drawable-to-internal-file-storage-in-android
     * http://stackoverflow.com/questions/649154/save-bitmap-to-location
     * http://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap
     * http://stackoverflow.com/questions/10174399/how-can-i-write-a-drawable-resource-to-a-file
     *
     *
     */
    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        // Is this ok or very resource heavy?
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}

