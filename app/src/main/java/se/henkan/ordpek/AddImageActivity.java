package se.henkan.ordpek;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;

import se.henkan.util.ScalingUtilities;


public class AddImageActivity extends Activity {

    // Code for our image picker select action.
    private static final int SELECT_IMAGE = 999;

    // Code for pick and crop...
    private static final int SELECT_AND_CROP_IMAGE = 888;

    // Temporary image file
    private static final String TEMP_IMAGE_FILE = "op_temp.jpg";

    // Reference to the image view
    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        mImageView = (ImageView) findViewById(R.id.add_image_view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ToDo: Delete this...?
        if (requestCode == SELECT_IMAGE  && resultCode == RESULT_OK) {
            Bitmap bitmap = getBitmapFromCameraData(data);
            if (bitmap == null) {
                mImageView.setImageResource(R.drawable.op_hund);
            } else {
                Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(bitmap, 350, 350,
                        ScalingUtilities.ScalingLogic.CROP);
                mImageView.setImageBitmap(scaledBitmap);
            }

        } else if (requestCode == SELECT_AND_CROP_IMAGE  && resultCode == RESULT_OK) {
            Log.d("INFO:::", "URI to cropped file: " + data.getData());
            Log.d("INFO:::", "Cropping image... in dir: " + this.getFilesDir().getPath());

            File path = new File(getFilesDir().getPath());
            File files[] = path.listFiles();
            for (File f : files) {
                Log.d("INFO:::", "File:   " + f.toString());
            }



            Bitmap selectedImage =  BitmapFactory.decodeFile(getFilesDir().getPath() +
                    "/" + TEMP_IMAGE_FILE);

            //Bitmap unscaledImage = ScalingUtilities.decodeFile(this.getFilesDir().getPath() +
            //        "/" + TEMP_IMAGE_FILE, 350, 350, ScalingUtilities.ScalingLogic.CROP);

            Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(selectedImage, 350, 350,
                    ScalingUtilities.ScalingLogic.CROP);
            mImageView.setImageBitmap(scaledBitmap);
        }

    }

    public void addFromGallery(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_IMAGE);
    }

    public void addAndCropFromGallery(View view) {

        Intent intent = new Intent("com.android.camera.action.CROP");


        File tempFile = new File(getFilesDir(), TEMP_IMAGE_FILE);
        Log.d("INFO:::", "Trying to create file:  " + tempFile.toString());
        Uri tempUri = Uri.fromFile(tempFile);
        Log.d("INFO:::", "With URI             :  " + tempUri.toString());
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        //i.setData(tempUri);
        i.putExtra("crop", "true");
        i.putExtra("aspectX", 1);
        i.putExtra("aspectY", 1);
        //i.putExtra("scale", "true");
        i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, tempUri);
        i.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(i, SELECT_AND_CROP_IMAGE);
    }


    public void addFromCamera(View view) {
        // Handle a return from the camera...
    }

    /**
     * Use for decoding camera response data.
     *
     * @param data
     * @return
     */
    public Bitmap getBitmapFromCameraData(Intent data){
        Uri selectedImage = data.getData();

        try {
            return decodeAndScaleUri(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return BitmapFactory.decodeResource(getResources(),R.drawable.op_katt);
        }
    }

    /**
     * Use for decoding camera response data.
     *
     * @param selectedImage Uri of the selected image
     * @return
     */
    private Bitmap decodeAndScaleUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        // ToDo: [HARDCODED]
        final int REQUIRED_SIZE = 500;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

    }

    //ToDo: Delete Me!!!
    public void listAllImages(View view) {
        TextView textView = (TextView) findViewById(R.id.add_text_view);
        textView.setText("");

        File path = new File(this.getFilesDir().getPath());
        File files[] = path.listFiles();
        for (File f : files) {
            textView.append(f.toString() + "\n");
        }
    }


}
