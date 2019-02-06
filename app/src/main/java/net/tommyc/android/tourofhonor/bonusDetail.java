package net.tommyc.android.tourofhonor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class bonusDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_detail);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
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