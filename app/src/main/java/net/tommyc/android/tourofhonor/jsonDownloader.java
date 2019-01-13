package net.tommyc.android.tourofhonor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.Parser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * -----------------------ROLES-------------------------
 * 1.CONNECT TO NETWORK
 * 2.DOWNLOAD DATA IN BACKGROUND THREAD
 * 3.SEND THE DATA TO PARSER TO BE PARSED
 */

public class jsonDownloader extends AsyncTask<Void,Void,String> {

    Context c;
    String jsonURL;
    ListView lv;

    ProgressDialog pd;

    public jsonDownloader(Context c, String jsonURL, ListView lv) {
        this.c = c;
        this.jsonURL = jsonURL;
        this.lv = lv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd=new ProgressDialog(c);
        pd.setTitle("Updating Bonus Data");
        pd.setMessage("Updating...Please wait");
        pd.show();
        Log.e("jsonDownloader","Entered jsonDownloader class");

    }

    @Override
    protected String doInBackground(Void... voids) {
        return download();
    }

    @Override
    protected void onPostExecute(String jsonData) {
        super.onPostExecute(jsonData);

        pd.dismiss();

        if(jsonData.startsWith("Error"))
        {
            String error=jsonData;
            Toast.makeText(c, error, Toast.LENGTH_SHORT).show();
        }else
        {
            //PARSER
            Log.e("jsonDownloader",jsonData);
            // new JSONParser(c,jsonData, lv).execute();
        }

    }

    //DOWNLOAD
    private String download()
    {
        Object connection=Connector.connect(jsonURL);
        if(connection.toString().startsWith("Error"))
        {
            Log.e("jsonDownloader","Connection String Started with Error");
            return connection.toString();
        }

        try
        {
            HttpURLConnection con= (HttpURLConnection) connection;
            if(con.getResponseCode()==con.HTTP_OK)
            {
                //GET INPUT FROM STREAM
                Log.e("jsonDownloader","Entering Stream");
                InputStream is=new BufferedInputStream(con.getInputStream());
                BufferedReader br=new BufferedReader(new InputStreamReader(is));

                String line;
                StringBuffer jsonData=new StringBuffer();

                //READ
                while ((line=br.readLine()) != null)
                {
                    jsonData.append(line+"n");
                }

                //CLOSE RESOURCES
                br.close();
                is.close();

                //RETURN JSON
                Log.e("jsonDownloader","JSON Retrieved");
                return jsonData.toString();

            }else
            {
                Log.e("jsonDownloader","Error Returned");
                return "Error "+con.getResponseMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("jsonDownloader","I/O Error Returned");
            return "Error "+e.getMessage();

        }

    }

}
