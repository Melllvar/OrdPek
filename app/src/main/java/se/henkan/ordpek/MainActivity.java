package se.henkan.ordpek;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


//ToDo: Add settings

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

}

