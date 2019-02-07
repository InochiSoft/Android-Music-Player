package com.inochi.music.player.task;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;

import com.inochi.music.player.R;
import com.inochi.music.player.helper.Constants;
import com.inochi.music.player.helper.FileManager;
import com.inochi.music.player.item.FileItem;
import com.inochi.music.player.listener.ExploreListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AsyncExplorer extends AsyncTask<String, String, ArrayList<FileItem>> {
    private ExploreListener exploreListener;

    @Override
    protected ArrayList<FileItem> doInBackground(String... params) {
        ArrayList<FileItem> listItems = new ArrayList<>();

        String strPath = params[0];
        File selectedDir = new File(strPath);

        if (selectedDir.exists()){
            File parentDir = selectedDir.getParentFile();

            if (parentDir != null){
                FileItem parentItem = new FileItem();
                parentItem.setTitle(selectedDir.getName());
                parentItem.setPath(parentDir.getPath());
                parentItem.setGroupType(Constants.GroupType.FILE_ITEM);
                parentItem.setIcon(R.mipmap.ic_folder_white);
                parentItem.setType(Constants.GroupType.TYPE_PARENT);
                parentItem.setColor(0);
                parentItem.setExtension("");
                listItems.add(parentItem);
            }
        }

        FileManager fileManager1 = new FileManager(strPath, new String[]{"mp3"});
        List<String> arrFolders = fileManager1.getArrFolders();
        List<String> arrFiles = fileManager1.getArrFiles();

        if (arrFolders != null){
            if (arrFolders.size() > 0){
                for (int i = 0; i < arrFolders.size(); i++){
                    //Menambahkan daftar folder pada obyek listItems
                    String strPathFolder = arrFolders.get(i);
                    String[] arrPath = strPathFolder.split("\\/");
                    String strTitle = "";

                    if (arrPath.length > 0){
                        strTitle = arrPath[arrPath.length - 1];
                    }

                    FileItem item = new FileItem();
                    item.setTitle(strTitle);
                    item.setPath(strPathFolder);
                    item.setGroupType(Constants.GroupType.FILE_ITEM);
                    item.setIcon(R.mipmap.ic_folder_white);
                    item.setColor(0);
                    item.setType(Constants.GroupType.TYPE_FOLDER);
                    item.setExtension("");
                    listItems.add(item);
                }
            }
        }

        if (arrFiles != null){
            if (arrFiles.size() > 0){
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                for (int i = 0; i < arrFiles.size(); i++){
                    String strPathFile = arrFiles.get(i);
                    String[] arrPath = strPathFile.split("\\/");
                    String strTitle;

                    String[] arrTitle;
                    String fileExt = "";

                    String strTitleFile = "";

                    mediaMetadataRetriever.setDataSource(strPathFile);
                    strTitle = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

                    if (arrPath.length > 0){
                        strTitleFile = arrPath[arrPath.length - 1];
                        arrTitle = strTitleFile.split("\\.");
                        if (arrTitle.length > 0){
                            if (arrTitle[0].length() > 0){
                                fileExt = arrTitle[arrTitle.length - 1];
                            } else {
                                strTitleFile = arrPath[arrPath.length - 1];
                                fileExt = "";
                            }
                        }
                    }

                    if (strTitle == null){
                        strTitle = strTitleFile;
                    }

                    String[] arrPathFile = strPathFile.split("\\/");
                    String strDirPath = "";

                    if (arrPathFile.length > 0){
                        for (int p = 0; p < arrPathFile.length - 1; p++){
                            String part = arrPathFile[p];
                            strDirPath += part + "/";
                        }
                    }

                    FileItem item = new FileItem();
                    item.setTitle(strTitle);
                    item.setPath(strPathFile);
                    item.setGroupType(Constants.GroupType.FILE_ITEM);
                    item.setIcon(R.mipmap.ic_music_box_white);
                    item.setColor(0);
                    item.setType(Constants.GroupType.TYPE_FILE);
                    item.setExtension(fileExt);
                    item.setDirPath(strDirPath);
                    listItems.add(item);
                }
            }
        }

        return listItems;
    }

    @Override
    protected void onPostExecute(ArrayList<FileItem> result) {
        super.onPostExecute(result);
        try {
            if (exploreListener != null){
                exploreListener.ExploreResult(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ExploreListener getExploreListener() {
        return exploreListener;
    }

    public void setExploreListener(ExploreListener exploreListener) {
        this.exploreListener = exploreListener;
    }
}
