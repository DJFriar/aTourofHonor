package net.tommyc.android.tourofhonor;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.ArrayList;

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
    String primaryCapturedPhotoName;
    String optionalCapturedPhotoName;
    String primaryCapturedPhotoFullPath;
    String optionalCapturedPhotoFullPath;

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
        String sImageName = data.getString(data.getColumnIndex("sImageName"));

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
            riderNumToH = sharedpreferences.getString(riderNum,"0000");
            Log.e(TAG,"riderNum set to " + riderNum);
        } else {
            Log.e("captureBonus","riderNum Failed");
        }
        if (sharedpreferences.contains(pillionNum)) {
            pillionNumToH = sharedpreferences.getString(pillionNum,"0000");
            Log.e("captureBonus","pillionNum set to " + pillionNum);
        } else {
            Log.e("captureBonus","pillionNum Failed");
        }

        // Process the Sample Image
        bonusSampleImage = findViewById(R.id.bonusSampleImage);
        Glide
                .with(this)
                .load("https://www.tourofhonor.com/appimages/" + sImageName)
                .placeholder(R.drawable.sample_image_missing)
                .into(bonusSampleImage);

        // Setup variables for later use
        primaryCapturedPhotoName = "2019_" + riderNumToH + "_" + bonusCode.getText() + "_1.jpg";
        optionalCapturedPhotoName = "2019_" + riderNumToH + "_" + bonusCode.getText() + "_2.jpg";
        primaryCapturedPhotoFullPath = imagePath + "/" + primaryCapturedPhotoName;
        optionalCapturedPhotoFullPath = imagePath + "/" + optionalCapturedPhotoName;

        // Populate Image Wells with Captured Images
        replacePrimaryImageWithCapturedImage();
        replaceOptionalImageWithCapturedImage();

        btnSubmitBonus = findViewById(R.id.btnSubmitBonus);
        btnSubmitBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"Submit Bonus button was tapped.");
                if (isImageExists(optionalCapturedPhotoName)) {
                    dispatchSubmitMultiBonusIntent();
                } else if (isImageExists(primaryCapturedPhotoName)) {
                    dispatchSubmitSingleBonusIntent();
                } else {
                    Log.e(TAG,"No Images Found to Submit");

                    AlertDialog alertDialog = new AlertDialog.Builder(captureBonus.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("No images found to submit.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                    /*
                    Snackbar.make(findViewById(R.id.appSettingsView), R.string.noImagesFound,
                            Snackbar.LENGTH_SHORT)
                            .show();
                    */
                    return;
                }
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
            Log.i("App Setup","Tour of Honor folder has been created");
            f.mkdirs();
        } else {
            Log.v("App Setup","Tour of Honor folder already existed");
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

    public boolean isImageExists(String filename){
        //File dataDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File dataFile = new File(imagePath + "/" + filename);
        String dataFilePath = dataFile.getAbsolutePath();
        Log.e(TAG, "isImageExists, Image Found: " + dataFilePath);
        return dataFile.exists();
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
        File primaryImage = new File
                (imagePath + "/" + primaryCapturedPhotoName);
        Glide
                .with(this)
                .load(primaryImage)
                .placeholder(R.drawable.no_image_taken)
                .into(imageViewMain);
    }

    private void replaceOptionalImageWithCapturedImage() {
        imageViewSecondary = findViewById(R.id.bonusSecondaryImage);
        File optionalImage = new File
                (imagePath + "/" + optionalCapturedPhotoName);
        Glide
                .with(this)
                .load(optionalImage)
                .placeholder(R.drawable.no_image_taken)
                .into(imageViewSecondary);
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
    private void dispatchSubmitSingleBonusIntent() {
        Log.e(TAG, "entering dispatchSubmitSingleBonusIntent");
        Intent sendEmailIntent = new Intent(Intent.ACTION_SEND);
        sendEmailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendEmailIntent.setType("text/plain");
        sendEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{submissionEmailAddress});
        sendEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "2019_" + riderNumToH + "_" + bonusCategory.getText() +"_" + bonusState.getText() + "_" + bonusCity.getText() + "_" + bonusCode.getText());
        sendEmailIntent.putExtra(Intent.EXTRA_TEXT, "Sent via TOH App for Android");
        if (mainPhotoPath != null) {
            sendEmailIntent.putExtra(android.content.Intent.EXTRA_STREAM, FileProvider.getUriForFile(captureBonus.this, "net.tommyc.android.tourofhonor", mainPhotoUri));
            Log.v("MainImageFound", mainPhotoPath + "|" + mainPhotoUri);
        } else {
                Log.e("NoImageFound", "Image Not Found");
            }
        this.startActivity(Intent.createChooser(sendEmailIntent, "Sending email..."));
    }

    public void dispatchSubmitMultiBonusIntent() {
        Log.e(TAG, "entering dispatchSubmitMultiBonusIntent");

        //need to "send multiple" to use more than one attachment
        final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);

        // Set up the email parameters
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[]{submissionEmailAddress});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "2019_" + riderNumToH + "_" + bonusCategory.getText() +"_" + bonusState.getText() + "_" + bonusCity.getText() + "_" + bonusCode.getText());
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Sent via TOH App for Android");

        // Get the attachments
        ArrayList<Uri> uris = new ArrayList<>();
        String[] filePaths = new String[] {primaryCapturedPhotoFullPath,optionalCapturedPhotoFullPath};
        for (String file : filePaths) {
            File fileIn = new File(file);
            Uri u = FileProvider.getUriForFile(captureBonus.this, "net.tommyc.android.tourofhonor", fileIn);
            uris.add(u);
            Log.e(TAG,uris.toString());
        }

        // Add the attachments to the email and trigger the email intent
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        this.startActivity(Intent.createChooser(emailIntent, "Sending email ..."));
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