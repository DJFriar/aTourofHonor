package net.tommyc.android.tourofhonor;

import android.util.Log;

import java.io.IOException;

public class fileDownloaderOld {

    public static void main(String[] args) {
        String fileURL = "http://www.tourofhonor.com/BonusData.json";
        String saveFileName = "BonusData.json";
        try {
            Log.e("fileDownloaderOld","Attempting to save file from server");
            fileDownloadUtility.downloadFile(fileURL, saveFileName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
