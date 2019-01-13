package net.tommyc.android.tourofhonor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static net.tommyc.android.tourofhonor.splashScreen.pillionNum;
import static net.tommyc.android.tourofhonor.splashScreen.riderNum;
import static net.tommyc.android.tourofhonor.splashScreen.tohPreferences;

public class captureBonus extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    //int riderNumToH = 479;
    //int pillionNumToH = 000;
    String riderNumToH;
    String pillionNumToH;
    String submissionEmailAddress = "me@tommyc.net";

    /**
     * Opens an already installed Camera application
     */
    static final int REQUEST_TAKE_PHOTO = 1;
    Button btnTakeMainPic;
    Button btnSubmitBonus;
    ImageView imageViewMain;
    ImageView imageViewSecondary;
    int tappedImageView = 3;
    File mainPhotoUri = null;
    File secondaryPhotoUri = null;

    /**
     * Saves the full size image to the public photo directory (similar to the Camera Roll on iOS)
     * * saveImage(imageName: "2018_\(riderNumToH)_\(bonusCodeLabel.text!)_1.jpg")
     */

    String mainPhotoPath;
    String secondaryPhotoPath;
    String imageFolder = "Tour of Honor";
    String imagePath = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES + "/" + imageFolder).toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_bonus);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        // Grab the rider numbers from SharedPreferences
        sharedpreferences = getSharedPreferences(tohPreferences,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(riderNum)) {
            riderNumToH = sharedpreferences.getString(riderNum,"000");
            Log.e("captureBonus","riderNum set to " + riderNum);
        } else {
            Log.e("captureBonus","riderNum Failed");
        }
        if (sharedpreferences.contains(pillionNum)) {
            pillionNumToH = sharedpreferences.getString(pillionNum,"000");
            Log.e("captureBonus","pillionNum set to " + pillionNum);
        } else {
            Log.e("captureBonus","pillionNum Failed");
        }

        btnSubmitBonus = findViewById(R.id.btnSubmitBonus);
        btnSubmitBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchSubmitBonusIntent();
            }
        });
        imageViewMain = findViewById(R.id.bonusMainImage);
        imageViewMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tappedImageView = 0;
                dispatchTakeMainPictureIntent();
                Log.v("User Action", "Main Image Tapped");
            }
        });
        imageViewSecondary = findViewById(R.id.bonusSecondaryImage);
        imageViewSecondary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tappedImageView = 1;
                dispatchTakeMainPictureIntent();
                Log.v("User Action", "Main Image Tapped");
            }
        });
        File f = new File(imagePath);
        if (!f.exists()) {
            Log.e("App Setup","Tour of Honor folder has been created");
            f.mkdirs();
        } else {
            Log.e("App Setup","Tour of Honor folder already existed");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                if (tappedImageView == 0) {
                    Bitmap bitmap = BitmapFactory.decodeFile(mainPhotoPath);
                    imageViewMain.setImageBitmap(bitmap);
                } else if (tappedImageView == 1) {
                    Bitmap bitmap = BitmapFactory.decodeFile(secondaryPhotoPath);
                    imageViewSecondary.setImageBitmap(bitmap);
                } else {
                    Log.w("ERROR", "onActivityResult: valid view ID not found (" + tappedImageView + ")");
                }

            }
        }
    }

    private void dispatchTakeMainPictureIntent() {
        Intent takeMainPictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takeMainPictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                if (tappedImageView == 0) {
                    mainPhotoUri = createImageFile();
                } else if (tappedImageView == 1) {
                    secondaryPhotoUri = createImageFile();
                }
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("fileCreationError", "An error occurred while creating the image file.");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(captureBonus.this, "net.tommyc.android.tourofhonor", photoFile);
                takeMainPictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takeMainPictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        //String imagePath = Environment.getExternalStoragePublicDirectory(
        //        Environment.DIRECTORY_PICTURES).toString();
        String mainImageFileName = "2019_" + riderNumToH + "_BonusCode_1.jpg";
        String secondaryImageFileName = "2019_" + riderNumToH + "_BonusCode_2.jpg";
        if (tappedImageView == 0) {
            File capturedImage = new File(imagePath, mainImageFileName);
            mainPhotoPath = capturedImage.getAbsolutePath();
            return capturedImage;
        } else if (tappedImageView == 1) {
            File capturedImage = new File(imagePath, secondaryImageFileName);
            secondaryPhotoPath = capturedImage.getAbsolutePath();
            return capturedImage;
        } else {
            Log.w("ERROR", "createImageFile: valid view ID not found (" + tappedImageView + ")");
        }
        return null;
    }

    /**
     * Submits the bonus images via email.
     */
    private void dispatchSubmitBonusIntent() {
        Intent sendEmailIntent = new Intent(Intent.ACTION_SEND);
        sendEmailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendEmailIntent.setType("text/plain");
        sendEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{submissionEmailAddress});
        sendEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "2019_" + riderNumToH + "_BonusCode");
        sendEmailIntent.putExtra(Intent.EXTRA_TEXT, "Sent from TOH App\nAndroid Version 0.3.076");
        if (mainPhotoPath != null) {
            sendEmailIntent.putExtra(android.content.Intent.EXTRA_STREAM, FileProvider.getUriForFile(captureBonus.this, "net.tommyc.android.tourofhonor", mainPhotoUri));
            Log.v("MainImageFound", mainPhotoPath + "|" + mainPhotoUri);
            if (secondaryPhotoPath != null) {
                sendEmailIntent.putExtra(android.content.Intent.EXTRA_STREAM, FileProvider.getUriForFile(captureBonus.this, "net.tommyc.android.tourofhonor", secondaryPhotoUri));
                Log.v("SecondaryImageFound", secondaryPhotoPath + "|" + secondaryPhotoUri);
            } else {
                Log.e("NoImageFound", "Image Not Found");
            }
        }
        this.startActivity(Intent.createChooser(sendEmailIntent, "Sending email..."));
    }
}