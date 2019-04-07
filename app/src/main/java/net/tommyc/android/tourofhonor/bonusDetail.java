package net.tommyc.android.tourofhonor;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

public class bonusDetail extends AppCompatActivity {

    AppDataDBHelper appDBHelper;
    TextView bonusName;
    TextView bonusCategory;
    TextView bonusCode;
    TextView bonusAddress;
    TextView bonusGPS;
    String tappedBonusID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_detail);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Handle the appData DB.
        appDBHelper = new AppDataDBHelper(this);
        tappedBonusID = getIntent().getStringExtra("codeTapped");

        // Fetch Bonus Data from DB
        Cursor data = appDBHelper.getAppDataSelectedBonus(tappedBonusID);
        data.moveToFirst();
        String sCode = data.getString(data.getColumnIndex("sCode"));
        String sName = data.getString(data.getColumnIndex("sName"));
        String sCategory = data.getString(data.getColumnIndex("sCategory"));
        String sAddress = data.getString(data.getColumnIndex("sAddress"));
        String sGPS = data.getString(data.getColumnIndex("sGPS"));

        // Populate the view data values
        bonusCode = findViewById(R.id.bonusCode);
        bonusCode.setText(sCode);
        bonusName = findViewById(R.id.bonusName);
        bonusName.setText(sName);
        bonusCategory = findViewById(R.id.bonusCategory);
        bonusCategory.setText(sCategory);
        bonusAddress = findViewById(R.id.bonusAddress);
        bonusAddress.setText(sAddress);
        bonusGPS = findViewById(R.id.bonusCoordinates);
        bonusGPS.setText(sGPS);

        /*
        // Get Associated Sample Image
        ImageView imageView = findViewById(R.id.bonusSampleImage);
        imageView = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        imageView.setImageBitmap(bitmap);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    /*
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
    */

}