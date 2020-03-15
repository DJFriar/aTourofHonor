package net.tommyc.android.tourofhonor;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
}