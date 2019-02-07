package com.inochi.music.player;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.inochi.music.player.adapter.ExploreAdapter;
import com.inochi.music.player.helper.BundleSetting;
import com.inochi.music.player.helper.Constants;
import com.inochi.music.player.item.FileItem;
import com.inochi.music.player.item.SongItem;
import com.inochi.music.player.listener.ExploreListener;
import com.inochi.music.player.listener.FileListener;
import com.inochi.music.player.task.AsyncExplorer;

import java.io.File;
import java.util.ArrayList;

public class FileActivity extends AppCompatActivity implements ExploreListener, FileListener {
    private RecyclerView rvwList;
    private ArrayList<FileItem> fileItems;
    private ExploreAdapter exploreAdapter;
    private BundleSetting bundleSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this;

        setContentView(R.layout.activity_file);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bundleSetting = new BundleSetting(context);

        rvwList = findViewById(R.id.rvwList);
        rvwList.setHasFixedSize(true);
        rvwList.setLayoutManager(new LinearLayoutManager(context));

        String strPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath();

        AsyncExplorer asyncExplorer = new AsyncExplorer();
        asyncExplorer.setExploreListener(this);
        asyncExplorer.execute(strPath);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_explorer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_done:
                ArrayList<SongItem> songItems = bundleSetting.getPlayListSelected();
                if (songItems == null){
                    songItems = new ArrayList<>();
                }

                fileItems = exploreAdapter.getListItems();
                for (FileItem fileItem : fileItems){
                    boolean select = fileItem.isSelect();
                    if (select){
                        int type = fileItem.getType();
                        String file = fileItem.getPath();

                        if (type == Constants.GroupType.TYPE_FILE){
                            SongItem songItem = new SongItem();
                            String[] arrFilePath = file.split("/");
                            String endPath = arrFilePath[arrFilePath.length - 1];

                            File mp3File = new File(file);
                            String artist = "";
                            String title = "";

                            if (mp3File.exists()){
                                MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
                                metadataRetriever.setDataSource(file);
                                artist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                                title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                            }

                            if (title == null) title = "";

                            if (title.isEmpty()){
                                title = endPath;
                                if (title.length() > 4) {
                                    if (title.substring(title.length() - 3).toLowerCase().equals("mp3")){
                                        title = title.substring(0, title.length() - 4);
                                    }
                                }
                            }

                            if (artist == null) artist = "";

                            songItem.setSongTitle(title.trim());
                            songItem.setSongName(title.trim());
                            songItem.setSongArtist(artist.trim());
                            songItem.setSongPath(file);

                            songItems.add(songItem);
                        }
                    }
                }

                bundleSetting.setPlayListSelected(songItems);

                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ExploreResult(ArrayList<FileItem> result) {
        if (result != null){
            fileItems = result;
            exploreAdapter = new ExploreAdapter(fileItems, this);
            rvwList.setAdapter(exploreAdapter);
            rvwList.invalidate();
        }
    }

    @Override
    public void setOnFileItemClick(View view, FileItem fileItem) {
        if (fileItem.getType() != Constants.GroupType.TYPE_FILE){
            String strPath = fileItem.getPath();
            AsyncExplorer asyncExplorer = new AsyncExplorer();
            asyncExplorer.setExploreListener(this);
            asyncExplorer.execute(strPath);
        }
    }
}
