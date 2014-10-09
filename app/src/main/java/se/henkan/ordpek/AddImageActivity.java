package se.henkan.ordpek;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;

import se.henkan.util.ScalingUtilities;


public class AddImageActivity extends Activity {

    // Code for our image picker select action.
    private static final int SELECT_IMAGE = 999;

    // Code for pick and crop...
    private static final int SELECT_AND_CROP_IMAGE = 888;

    // Temporary image file
    private static final String TEMP_IMAGE_FILE = "temporary_holder.jpg";

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

        if (requestCode == SELECT_IMAGE  && resultCode == Activity.RESULT_OK) {

            Bitmap bitmap = getBitmapFromCameraData(data);

            if (bitmap == null) {
                mImageView.setImageResource(R.drawable.op_hund);

            } else {
                Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(bitmap, 350, 350,
                        ScalingUtilities.ScalingLogic.CROP);
                mImageView.setImageBitmap(scaledBitmap);
            }

        } else if (requestCode == SELECT_AND_CROP_IMAGE  && resultCode == Activity.RESULT_OK) {
            Log.d("INFO:::", "Cropping image... in dir: " + this.getFilesDir().getPath());

            Bitmap selectedImage = ScalingUtilities.decodeFile(this.getFilesDir().getPath() +
                    "/" + TEMP_IMAGE_FILE, 350, 350, ScalingUtilities.ScalingLogic.CROP);

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
        File tempFile = new File(this.getFilesDir().getPath(), TEMP_IMAGE_FILE);
        Uri tempUri = Uri.fromFile(tempFile);
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        i.putExtra("crop", "true");
        i.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
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
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
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

}
