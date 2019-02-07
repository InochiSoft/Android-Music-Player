package com.inochi.music.player.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileManager {
    private List<String> arrFolders;
    private List<String> arrFiles;

    public FileManager(String strPath, String[] extensions){
        arrFolders = new ArrayList<>();
        arrFiles = new ArrayList<>();

        try {
            File selectedDir = new File(strPath);
            File[] arrDir = selectedDir.listFiles();

            if (arrDir != null){
                //Pengulangan sebanyak folder dan/atau berkas
                //yang terdapat pada sebuah folder
                for (File anArrDir : arrDir) {
                    //Jika tipe obyek adalah File
                    if (anArrDir.isFile()) {
                        String strName = anArrDir.getName();
                        String[] arrName = strName.split("\\.");

                        //Menambahkan nama obyek pada array arrFiles
                        if (arrName.length > 0) {
                            String strPathFile = anArrDir.getPath();
                            String[] arrPath = strPathFile.split("\\/");
                            String strTitle;

                            String[] arrTitle;
                            String fileExt = "";

                            if (arrPath.length > 0) {
                                strTitle = arrPath[arrPath.length - 1];
                                arrTitle = strTitle.split("\\.");
                                if (arrTitle.length > 1) {
                                    if (arrTitle[0].length() > 0) {
                                        fileExt = arrTitle[arrTitle.length - 1];
                                    } else {
                                        fileExt = "";
                                    }
                                }
                            }
                            boolean blnAdd = false;

                            if (fileExt.length() > 0) {
                                for (String extension : extensions) {
                                    if (extension.toLowerCase().equals(fileExt.toLowerCase())) {
                                        blnAdd = true;
                                        break;
                                    } else if (extension.toLowerCase().equals("*")) {
                                        blnAdd = true;
                                        break;
                                    }
                                }
                            }

                            if (blnAdd) arrFiles.add(strPathFile);
                        }
                        //Jika tipe obyek adalah Folder
                    } else {
                        //Menambahkan nama obyek pada array arrFolders
                        arrFolders.add(anArrDir.getPath());
                    }
                }

                //Mengurutkan nama folder dan nama berkas
                //String.CASE_INSENSITIVE_ORDER akan mengabaikan
                //perbedaan huruf besar dan huruf kecil
                Collections.sort(arrFolders, String.CASE_INSENSITIVE_ORDER);
                Collections.sort(arrFiles, String.CASE_INSENSITIVE_ORDER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<String> getArrFolders() {
        return arrFolders;
    }

    public List<String> getArrFiles() {
        return arrFiles;
    }

}
