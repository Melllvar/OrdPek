package se.henkan.ordpek;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import se.henkan.util.DatabaseHandler;



public class EditImageActivity extends ListActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
/** ToDo: Update listview like this: (in thread...)
 http://developer.android.com/guide/topics/ui/layout/listview.html#Example
*/
    // The list of images
    ListView mEditList;

    // Adapter used to display the image names
    SimpleCursorAdapter mAdapter;

    // Handler to the database
    private DatabaseHandler db = new DatabaseHandler(this);

    // The image names to retrive

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        mEditList = (ListView) findViewById(R.id.edit_list);

        // Get all the imageEntries
        // ToDo: get cursor instead...
        ArrayList<ImageEntry> imageEntries = (ArrayList<ImageEntry>) db.getAllImageEntries();

        // Get all the names...
        ArrayList<String> imageNames = new ArrayList<>();
        ArrayList<Integer> imageIds = new ArrayList<>(); // ToDo: Do this better?
        for (ImageEntry ie : imageEntries) {
            imageNames.add(ie.get_name());
            imageIds.add(ie.get_id());
        }

        // Create adapter...
        // ToDo: Use a cursor!
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, imageNames);

        //ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,
        //        android.R.layout.simple_list_item_1, android.R.id.text1, imageIds);

        //ArrayAdapter<ImageEntry> adapter = new ArrayAdapter<>(this,
        //        android.R.layout.simple_list_item_1, android.R.id.text1, imageEntries);



        // Sort somehow?
        //adapter.sort(String.CASE_INSENSITIVE_ORDER);

        // Assign adapter to ListView
        mEditList.setAdapter(adapter);


        mEditList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                String selected = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();

                selected = selected + " " + position + " " + id;

                Toast toast = Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT);
                toast.show();

                // ToDo: Choose edit name or remove...

            }
        });




        // Call some method
        //ToDo: Rename!
        removeFromInternal();

    }


    // Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        //return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
        //        PROJECTION, SELECTION, null, null);
        return null;

    }


    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        //mAdapter.swapCursor(data);
    }


    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        //mAdapter.swapCursor(null);
    }



    private void removeFromInternal() {
        // Use db-handler to remove the image from storage....
    }
}
