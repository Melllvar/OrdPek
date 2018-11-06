package se.henkan.ordpek;

/**
 * Created by Henkan on 2014-10-03.
 */

// ToDo: Move to utils?
public class ImageEntry {

    // Private variables
    int _id;
    String _filePath;
    String _name;
    String _firstLetter;


    // Empty constructor
    public ImageEntry(){

    }

    // Constructor
    public ImageEntry(int id, String filePath, String name){
        this._id = id;
        this._filePath = filePath;
        this._name = name;
        this._firstLetter = name.substring(0,1);
    }

    // Constructor
    public ImageEntry(String filePath, String name){
        this._filePath = filePath;
        this._name = name;
        this._firstLetter = name.substring(0,1);
    }

    // Get ID
    public int get_id() {
        return _id;
    }
    // Set ID
    public void set_id(int id) {
        this._id = id;
    }
    // Get FILE_PATH
    public String get_filePath() {
        return _filePath;
    }
    // Set FILE_PATH
    public void set_filePath(String filePath) {
        this._filePath = filePath;
    }
    // get NAME
    public String get_name() {
        return _name;
    }
    // set NAME
    public void set_name(String name) {
        this._name = name;
        this._firstLetter = name.substring(0,1);
    }
    // get FIRST_LETTER
    public String get_firstLetter() {
        return _firstLetter;
    }
    // set FIRST_LETTER  ToDo:<========================REMOVE??? or take first letter from _name?
    public void set_firstLetter(String firstLetter) {
        this._firstLetter = firstLetter;
    }
}
