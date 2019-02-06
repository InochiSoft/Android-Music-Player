package com.inochi.music.player.helper;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class Helper {
    private Context context;
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public Helper(Context context){
        this.context = context;
    }

    /*
    Metode untuk menyiapkan dan mendapatkan lokasi penyimpanan file unduhan
    */
    private String getMainPath() {
        String strInochiPath = "";
        File mainDirectory;

        try {
            String sdCardPath = Environment.getExternalStoragePublicDirectory("").getPath();

            mainDirectory = new File(sdCardPath + "/" + Constants.Default.APP_SLUG + "/");

            if (!mainDirectory.exists()){
                mainDirectory.mkdirs();
            }
            strInochiPath = mainDirectory.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strInochiPath;
    }

    /*
    Metode untuk menyiapkan dan mendapatkan lokasi penyimpanan cache
    */
    public String getCachePath() {
        String strCachePath = "";

        File cacheDir;
        try {
            String sdCardPath;
            sdCardPath = context.getCacheDir().getPath();
            cacheDir = new File(sdCardPath + "/" + Constants.Default.APP_SLUG + "/");

            if (!cacheDir.exists()){
                boolean mkdirs = cacheDir.mkdirs();
            }

            strCachePath = cacheDir.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strCachePath;
    }

    public void saveFile(String filePath, String content){
        try {
            File myFile = new File(filePath);
            boolean bCreate = myFile.createNewFile();

            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

            myOutWriter.write(content);
            myOutWriter.close();

            fOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String openFile(String fileNamePath){
        StringBuilder strResult = new StringBuilder();
        try {
            File myFile = new File(fileNamePath);
            if (myFile.exists()){
                String strDataRow;

                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));

                while ((strDataRow = myReader.readLine()) != null) {
                    strResult.append(strDataRow).append("\n");
                }

                strResult = new StringBuilder(strResult.toString().trim());
                myReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strResult.toString();
    }

    /*
    Metode untuk mendapatkan slug dari suatu teks
    */
    public String getSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
