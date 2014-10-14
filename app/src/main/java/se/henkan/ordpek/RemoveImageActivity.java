package se.henkan.ordpek;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import se.henkan.util.DatabaseHandler;


public class RemoveImageActivity extends Activity {
/** ToDo: Update listview like this: (in thread...)
 *  http://developer.android.com/guide/topics/ui/layout/listview.html#Example
*/
    // The list of images
    ListView mRemoveList;

    // Handler to the database
    private DatabaseHandler db = new DatabaseHandler(this);





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_image);

        mRemoveList = (ListView) findViewById(R.id.remove_list);

        // Get all the image names from the database
        ArrayList<String> imageNames = (ArrayList<String>) db.getAllNames();

        // Create adapter...
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, imageNames);

        // Assign adapter to ListView
        mRemoveList.setAdapter(adapter);

        removeFromInternal();
    }



    private void removeFromInternal() {

    }
}