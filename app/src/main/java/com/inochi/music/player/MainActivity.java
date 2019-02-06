package com.inochi.music.player;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.inochi.music.player.adapter.SongAdapter;
import com.inochi.music.player.helper.BundleSetting;
import com.inochi.music.player.helper.Constants;
import com.inochi.music.player.item.SongItem;
import com.inochi.music.player.listener.PlayerListener;
import com.inochi.music.player.listener.SongListener;
import com.inochi.music.player.service.PlayerService;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends BaseActivity implements PlayerListener, SongListener {
    private boolean mShouldUnbind;
    private PlayerService playerService;
    private ActionBar actionBar;
    private Toolbar toolbarTop;
    private Toolbar toolbarBottom;
    private ActionMenuView amvMenuTop;
    private ActionMenuView amvMenuBottom;
    private SeekBar seekProgress;

    private RecyclerView rvwList;

    private MenuItem action_play;
    private MenuItem action_pause;
    private MenuItem action_next;
    private MenuItem action_previous;

    private MenuItem action_add;
    private MenuItem action_delete;

    private BundleSetting bundleSetting;
    private ArrayList<SongItem> songItems;
    private SongAdapter songAdapter;

    private int songDuration;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setPermission();

        actionBar = getSupportActionBar();

        bundleSetting = new BundleSetting(this);

        toolbarTop = findViewById(R.id.toolbarTop);
        toolbarBottom = findViewById(R.id.toolbarBottom);

        amvMenuTop = toolbarTop.findViewById(R.id.amvMenuTop);
        amvMenuBottom = toolbarBottom.findViewById(R.id.amvMenuBottom);

        rvwList = findViewById(R.id.rvwList);
        seekProgress = findViewById(R.id.seekProgress);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        songItems = bundleSetting.getPlayListSelected();

        if (songItems == null){
            songItems = new ArrayList<>();

            SongItem songItem = new SongItem();
            songItem.setSongArtist("Unknown");
            songItem.setSongTitle(String.valueOf(R.raw.sample));
            songItem.setSongName(String.valueOf(R.raw.sample));
            songItem.setSongPath(String.valueOf(R.raw.sample));
            songItems.add(songItem);

            songItem = new SongItem();
            songItem.setSongArtist("Unknown");
            songItem.setSongTitle("Despacito");
            songItem.setSongName(String.valueOf(R.raw.despacito));
            songItem.setSongPath(String.valueOf(R.raw.despacito));
            songItems.add(songItem);

            bundleSetting.setPlayListSelected(songItems);
        }

        songAdapter = new SongAdapter(songItems, this);

        if (rvwList != null){
            rvwList.setHasFixedSize(true);
            rvwList.setLayoutManager(layoutManager);
            rvwList.setAdapter(songAdapter);
        }

        seekProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    int progress = seekBar.getProgress();
                    int intNewPosition = progressToTimer(progress, songDuration);

                    Intent service = new Intent(MainActivity.this, PlayerService.class);
                    Bundle extras = new Bundle();
                    extras.putInt(Constants.Player.Setting.SEEK_VALUE, intNewPosition);
                    service.putExtras(extras);
                    service.setAction(Constants.Player.Action.SEEK);
                    startService(service);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }
        });

        MenuBuilder menuBuilderTop = (MenuBuilder) amvMenuTop.getMenu();
        getMenuInflater().inflate(R.menu.menu_top, menuBuilderTop);

        MenuBuilder menuBuilderBottom = (MenuBuilder) amvMenuBottom.getMenu();
        getMenuInflater().inflate(R.menu.menu_bottom, menuBuilderBottom);

        action_play = menuBuilderBottom.findItem(R.id.action_play);
        action_pause = menuBuilderBottom.findItem(R.id.action_pause);
        action_next = menuBuilderBottom.findItem(R.id.action_next);
        action_previous = menuBuilderBottom.findItem(R.id.action_previous);

        action_add = menuBuilderBottom.findItem(R.id.action_add);
        action_delete = menuBuilderBottom.findItem(R.id.action_delete);

        menuBuilderTop.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }

            @Override public void onMenuModeChange(MenuBuilder menuBuilder) {}
        });

        menuBuilderBottom.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }

            @Override public void onMenuModeChange(MenuBuilder menuBuilder) {}
        });

        doBindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent service = new Intent(this, PlayerService.class);
        service.setAction(Constants.Player.Action.STATUS);
        startService(service);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    @SuppressLint("InlinedApi")
    private void setPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.Permission.READ_STORAGE);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.Permission.WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        try {
            setPermission();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playerService = ((PlayerService.PlayerBinder) service).getPlayerService();
            playerService.setPlayerListener(MainActivity.this);
            playerService.getStatus();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            playerService = null;
        }
    };

    private void doBindService() {
        if (this.bindService(new Intent(this, PlayerService.class),
                serviceConnection, Context.BIND_AUTO_CREATE)) {
            mShouldUnbind = true;
        }
    }

    private void doUnbindService() {
        if (mShouldUnbind) {
            this.unbindService(serviceConnection);
            mShouldUnbind = false;
        }
    }

    private int progressToTimer(int progress, int totalDuration) {
        try {
            int currentDuration;
            totalDuration = (totalDuration / 1000);
            currentDuration = (int) ((((double)progress) / 100) * totalDuration);

            return currentDuration * 1000;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent playService;

        switch (id){
            case R.id.action_play:
                playService = new Intent(this, PlayerService.class);
                playService.setAction(Constants.Player.Action.PLAY);
                startService(playService);
                break;
            case R.id.action_pause:
                playService = new Intent(this, PlayerService.class);
                playService.setAction(Constants.Player.Action.PLAY);
                startService(playService);
                break;
            case R.id.action_previous:
                playService = new Intent(this, PlayerService.class);
                playService.setAction(Constants.Player.Action.PREV);
                startService(playService);
                break;
            case R.id.action_next:
                playService = new Intent(this, PlayerService.class);
                playService.setAction(Constants.Player.Action.NEXT);
                startService(playService);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPlayerCompletion() {

    }

    @Override
    public void onBeginPlay(SongItem songItem) {

    }

    @Override
    public void onBeforePlay(SongItem songItem) {

    }

    @Override
    public void onPlayerPlay(SongItem songItem) {
        if (action_play != null)
            action_play.setVisible(false);

        if (action_pause != null)
            action_pause.setVisible(true);

        if (actionBar != null)
            actionBar.setSubtitle(songItem.getSongArtist() + " - " + songItem.getSongTitle());

    }

    @Override
    public void onPlayerPause(SongItem songItem) {
        if (action_play != null)
            action_play.setVisible(true);

        if (action_pause != null)
            action_pause.setVisible(false);
    }

    @Override
    public void onBeforePause(SongItem songItem) {

    }

    @Override
    public void onPlayerPrepare(Map<String, Object> info) {
        if (info != null){
            songDuration = (int) info.get(Constants.Player.Info.DURATION);
        }
    }

    @Override
    public void onPlayerPlaying(Map<String, Object> info) {
        if (info != null){
            songDuration = (int) info.get(Constants.Player.Info.DURATION);
            int progress = (int) info.get(Constants.Player.Info.PROGRESS);

            String timeDecrease = (String) info.get(Constants.Player.Info.DECREASE);
            String timeIncrease = (String) info.get(Constants.Player.Info.INCREASE);

            if (seekProgress != null)
                seekProgress.setProgress(progress);

            /*
            if (tvwIncrease != null)
                tvwIncrease.setText(timeDecrease);

            if (tvwDecrease != null)
                tvwDecrease.setText(timeIncrease);
            */
        }
    }

    @Override
    public void onPlayerStatus(Map<String, Object> info) {

    }

    @Override
    public void onShowProgress(String message) {
        showProgressLoading(message);
    }

    @Override
    public void onHideProgress() {
        hideProgressLoading();
    }

    @Override
    public void onPlayerExit() {
        finish();
    }

    @Override
    public void onPlayerNext() {

    }

    @Override
    public void onSongItemClick(View view, SongItem songItem) {
        bundleSetting.setSongItemSelected(songItem);
        int songIndex = 0;

        if (songAdapter != null){
            ArrayList<SongItem> songItems = songAdapter.getItems();

            if (songItems != null){
                bundleSetting.setLastPostion(0);
                bundleSetting.setPlayListSelected(songItems);

                for (SongItem item : songItems){
                    if (item.getSongPath().equals(songItem.getSongPath())){
                        break;
                    }
                    songIndex++;
                }

                bundleSetting.setPlayingIndex(songIndex);

                Intent playService = new Intent(this, PlayerService.class);
                playService.setAction(Constants.Player.Action.STOP);
                startService(playService);

                playService = new Intent(this, PlayerService.class);
                playService.setAction(Constants.Player.Action.REFRESH);
                startService(playService);

                playService = new Intent(this, PlayerService.class);
                playService.setAction(Constants.Player.Action.PLAY);
                startService(playService);
            }
        }
    }
}
