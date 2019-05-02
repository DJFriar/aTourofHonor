package net.tommyc.android.tourofhonor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;

import static net.tommyc.android.tourofhonor.splashScreen.pillionNum;
import static net.tommyc.android.tourofhonor.splashScreen.riderNum;
import static net.tommyc.android.tourofhonor.splashScreen.tohPreferences;

public class captureBonus extends AppCompatActivity {

    private static String TAG = "captureBonus"; // Tag just for the LogCat window

    SharedPreferences sharedpreferences;
    String riderNumToH;
    String pillionNumToH;
    String submissionEmailAddress = "photos@tourofhonor.com";

    AppDataDBHelper appDBHelper;
    TextView bonusName;
    TextView bonusCategory;
    TextView bonusCode;
    TextView bonusGPSCoordinates;
    TextView bonusAddress;
    TextView bonusCity;
    TextView bonusState;
    TextView bonusFlavorContent;
    ImageView bonusSampleImage;
    String tappedBonusID;

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
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        // Handle the appData DB.
        appDBHelper = new AppDataDBHelper(this);
        tappedBonusID = getIntent().getStringExtra("codeTapped");

        // Fetch Bonus Data from DB
        Cursor data = appDBHelper.getAppDataSelectedBonus(tappedBonusID);
        data.moveToFirst();
        String sCode = data.getString(data.getColumnIndex("sCode"));
        String sName = data.getString(data.getColumnIndex("sName"));
        String sCategory = data.getString(data.getColumnIndex("sCategory"));
        String sGPS = data.getString(data.getColumnIndex("sGPS"));
        String sAddress = data.getString(data.getColumnIndex("sAddress"));
        String sCity = data.getString(data.getColumnIndex("sCity"));
        String sState = data.getString(data.getColumnIndex("sState"));
        String sFlavor = data.getString(data.getColumnIndex("sFlavor"));

        // Populate the view data values
        bonusCode = findViewById(R.id.bonusCode);
        bonusCode.setText(sCode);
        bonusName = findViewById(R.id.bonusName);
        bonusName.setText(sName);
        bonusCategory = findViewById(R.id.bonusCategory);
        bonusCategory.setText(sCategory);
        bonusGPSCoordinates = findViewById(R.id.bonusGPSCoordinates);
        bonusGPSCoordinates.setText(sGPS);
        bonusAddress = findViewById(R.id.bonusAddress);
        bonusAddress.setText(sAddress);
        bonusCity = findViewById(R.id.bonusCity);
        bonusCity.setText(sCity);
        bonusState = findViewById(R.id.bonusState);
        bonusState.setText(sState);
        bonusFlavorContent = findViewById(R.id.bonusFlavorContent);
        bonusFlavorContent.setText(sFlavor);


        // Adjust submission email if category is National Parks
        if (bonusCategory.getText().toString().equals("National Parks")) {
            submissionEmailAddress = "nps_photos@tourofhonor.com";
        }

        // Grab the rider numbers from SharedPreferences
        sharedpreferences = getSharedPreferences(tohPreferences,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(riderNum)) {
            riderNumToH = sharedpreferences.getString(riderNum,"000");
            Log.e(TAG,"riderNum set to " + riderNum);
        } else {
            Log.e("captureBonus","riderNum Failed");
        }
        if (sharedpreferences.contains(pillionNum)) {
            pillionNumToH = sharedpreferences.getString(pillionNum,"000");
            Log.e("captureBonus","pillionNum set to " + pillionNum);
        } else {
            Log.e("captureBonus","pillionNum Failed");
        }

        // Process the Sample Image
        bonusSampleImage = findViewById(R.id.bonusSampleImage);
        Glide
                .with(this)
                .load("https://www.tourofhonor.com/appimages/2019" + bonusCode.getText().toString().toLowerCase() + ".jpg")
                .placeholder(R.drawable.sample_image_missing)
                .into(bonusSampleImage);

        // Populate Image Wells with Captured Images
        replacePrimaryImageWithCapturedImage();

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
                Log.v("User Action", "Optional Image Tapped");
            }
        });

        bonusGPSCoordinates = findViewById(R.id.bonusGPSCoordinates);
        bonusGPSCoordinates.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openGoogleMaps();
                Log.v("User Action", "Going to Google Maps");
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

    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = view.findViewById(R.id.bonusName);
        name.setText(cursor.getString(cursor.getColumnIndex("sName")));

        TextView code = view.findViewById(R.id.bonusCode);
        code.setText(cursor.getString(cursor.getColumnIndex("sCode")));
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

    private void replacePrimaryImageWithCapturedImage() {
        imageViewMain = findViewById(R.id.bonusMainImage);
        String mainImageFileName = "2019_" + riderNumToH + "_" + bonusCode.getText() + "_1.jpg";
        File primaryImage = new File
                (imagePath + "/" + mainImageFileName);
        Glide
                .with(this)
                .load(primaryImage)
                .placeholder(R.drawable.no_image_taken)
                .into(imageViewMain);

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
        String mainImageFileName = "2019_" + riderNumToH + "_" + bonusCode.getText() + "_1.jpg";
        String secondaryImageFileName = "2019_" + riderNumToH + "_" + bonusCode.getText() + "_2.jpg";
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
        sendEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "2019_" + riderNumToH + "_" + bonusCategory.getText() +"_" + bonusState.getText() + "_" + bonusCity.getText() + "_" + bonusCode.getText());
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

    public void openGoogleMaps() {
        // Example URL is https://www.google.com/maps/search/?api=1&query=47.5951518,-122.3316393
        // String baseMapsURL = "https://www.google.com/maps/search/?api=1&query=";
        Log.e(TAG,"geo:" + bonusGPSCoordinates.getText());
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + bonusGPSCoordinates.getText() + "&mode=d");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}