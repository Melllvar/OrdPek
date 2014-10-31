package se.henkan.ordpek;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import se.henkan.util.DatabaseHandler;


public class EditImageActivity extends Activity {
/** ToDo: Update listview like this: (in thread...)
 *  http://developer.android.com/guide/topics/ui/layout/listview.html#Example
*/
    // The list of images
    ListView mEditList;

    // Handler to the database
    private DatabaseHandler db = new DatabaseHandler(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        mEditList = (ListView) findViewById(R.id.edit_list);

        // Get all the imageEntries
        ArrayList<ImageEntry> imageEntries = (ArrayList<ImageEntry>) db.getAllImageEntries();

        // Get all the names...
        ArrayList<String> imageNames = new ArrayList<String>();
        ArrayList<Integer> imageIds = new ArrayList<Integer>(); // ToDo: Do this better?
        for (ImageEntry ie : imageEntries) {
            imageNames.add(ie.get_name());
            imageIds.add(ie.get_id());
        }

        // Create adapter...
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, imageNames);

        // Sort somehow?
        adapter.sort(String.CASE_INSENSITIVE_ORDER);



        // Assign adapter to ListView
        mEditList.setAdapter(adapter);



        removeFromInternal();

    }



    private void removeFromInternal() {

    }
}