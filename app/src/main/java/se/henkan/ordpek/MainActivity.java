package se.henkan.ordpek;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import se.henkan.util.DatabaseHandler;


//ToDo: Add settings "VERSALER/Gemener"
//ToDo: Add types of game: {"Välj rätt bild", "Välj rätt ord", "Begynnelsebokstav"}
//ToDo: Add private handlers to the different resources and layouts...

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Save some (default) drawables to internal storage...
        if (this.getFilesDir().listFiles().length < 9) {
            final ProgressDialog pd = ProgressDialog.show(MainActivity.this,
                    getResources().getString(R.string.progress_title),
                    getResources().getString(R.string.progress_text));
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    pd.dismiss();
                }
            };

            //start a new thread to process job
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //heavy job here
                    //send message to main thread
                    copyAddToDBDefaultImages();
                    handler.sendEmptyMessage(0);
                }
            }).start();
        }

        Log.d("INFO::: ", "Reading all images..");
        DatabaseHandler db = new DatabaseHandler(this);
        List<ImageEntry> images = db.getAllImageEntries();
        for (ImageEntry image : images) {
            String log = "Id: "+image.get_id()+" ,Filepath: " + image.get_filePath() +
                    " ,Name: " + image.get_name() + " ,First letter: " + image.get_firstLetter();
            // Writing Contacts to log
            Log.d("INFO::: ", log);
        }


        // Add to database instead?

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

    // Copy some default images from drawables to internal storage...
    // ToDo: Move drawables to assets?
    private void copyAddToDBDefaultImages(){
        Log.d("INFO:::", "Copy default images from drawables...");

        // Ugly but how to list all files in drawable?
        int[] img_ids = {R.drawable.op_agnes, R.drawable.op_alvar, R.drawable.op_axel,
                R.drawable.op_boat, R.drawable.op_henrik, R.drawable.op_hund, R.drawable.op_katt,
                R.drawable.op_tage, R.drawable.op_traktor};

        for (int id : img_ids) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), id);
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(getResources().getResourceEntryName(id) + ".png",
                        Context.MODE_PRIVATE);
                bm.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.d("INFO:::", "Adding to database...");
        DatabaseHandler db = new DatabaseHandler(this);
        String dirPath = this.getFilesDir().getPath();
        db.addImageEntry(new ImageEntry(dirPath + "/op_agnes.png", "Agnes"));
        db.addImageEntry(new ImageEntry(dirPath + "/op_alvar.png", "Alvar"));
        db.addImageEntry(new ImageEntry(dirPath + "/op_axel.png", "Axel"));
        db.addImageEntry(new ImageEntry(dirPath + "/op_boat.png", "Båt"));
        db.addImageEntry(new ImageEntry(dirPath + "/op_henrik.png", "Henrik"));
        db.addImageEntry(new ImageEntry(dirPath + "/op_hund.png", "Hund"));
        db.addImageEntry(new ImageEntry(dirPath + "/op_katt.png", "Katt"));
        db.addImageEntry(new ImageEntry(dirPath + "/op_tage.png", "Tage"));
        db.addImageEntry(new ImageEntry(dirPath + "/op_traktor.png", "Traktor"));
    }

}

