package net.tommyc.android.tourofhonor;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class RetrieveData extends AsyncTask<String, String, String> {
    public String JsonData;
    public BonusDatabaseHelper dbHelper;

    public RetrieveData(BonusDatabaseHelper bonusDatabaseHelper) {
        dbHelper = bonusDatabaseHelper;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                //Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }
            //Log.d("FETCH", buffer.toString());
            this.JsonData = buffer.toString();
            try {
                // create the json array from String
                JSONArray json = new JSONArray(this.JsonData);
                // iterate over the bonuses
                for (int i=0; i<json.length();i++){
                    JSONObject obj = (JSONObject) json.get(i);
                    //System.out.println("====obj===="+obj);

                    Bonus newBonus = new Bonus();
                    newBonus.sCode = obj.getString("bonusCode");
                    newBonus.sName = obj.getString("bonusName");
                    newBonus.sCategory = obj.getString("bonusCategory");
                    newBonus.sAddress = obj.getString("address");
                    newBonus.sCity = obj.getString("city");
                    newBonus.sState = obj.getString("state");
                    newBonus.sGPS = obj.getString("GPS");
                    newBonus.sImageName = obj.getString("sampleImage");

                    dbHelper.addBonus(newBonus);

                }
            } catch (JSONException e){
                e.printStackTrace();
            }



            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
