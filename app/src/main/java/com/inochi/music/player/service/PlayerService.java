package com.inochi.music.player.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.inochi.music.player.helper.BundleSetting;
import com.inochi.music.player.helper.Constants;
import com.inochi.music.player.helper.SongPlayer;
import com.inochi.music.player.item.SongItem;
import com.inochi.music.player.listener.PlayerListener;
import com.inochi.music.player.util.PlayerNotif;
import com.inochi.music.player.util.PlayerWidget;

import java.util.Map;

public class PlayerService extends Service
        implements PlayerListener, MediaPlayer.OnCompletionListener {
    private SongPlayer songPlayer;
    private PlayerListener playerListener;
    private final IBinder iBinder = new PlayerBinder();

    public PlayerService() {

    }

    public class PlayerBinder extends Binder {
        public PlayerService getPlayerService() {
            return PlayerService.this;
        }
    }

    public void setPlayerListener(PlayerListener playerListener) {
        this.playerListener = playerListener;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            final String action = intent.getAction();
            if (action != null){
                switch (action){
                    case Constants.Player.Action.PLAY:
                        initPlayer();
                        songPlayer.playAudio();
                        break;
                    case Constants.Player.Action.NEXT:
                        initPlayer();
                        songPlayer.nextAudio(Constants.Player.NEXT);
                        break;
                    case Constants.Player.Action.PREV:
                        initPlayer();
                        songPlayer.nextAudio(Constants.Player.PREVIOUS);
                        break;
                    case Constants.Player.Action.STOP:
                        initPlayer();
                        songPlayer.stopAudio();
                        break;
                    case Constants.Player.Action.STATUS:
                        initPlayer();
                        songPlayer.getStatus();
                        break;
                    case Constants.Player.Action.SEEK:
                        initPlayer();
                        Bundle args = intent.getExtras();
                        if (args != null){
                            int seekValue = args.getInt(Constants.Player.Setting.SEEK_VALUE, 0);
                            songPlayer.seekAudio(seekValue);
                        }
                        break;
                    case Constants.Player.Action.REFRESH:
                        initPlayer();
                        songPlayer.refreshList();
                        break;
                    case Constants.Player.Action.EXIT:
                        initPlayer();
                        songPlayer.exitPlayer();
                        break;
                    case Constants.Player.Action.BALANCE:
                        initPlayer();
                        songPlayer.refreshBalance();
                        break;
                    case Constants.Player.Action.EQUALIZER:
                        initPlayer();
                        songPlayer.refreshEqualizer();
                        break;
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initPlayer(){
        if (songPlayer == null){
            songPlayer = new SongPlayer(this, this);
        }
    }

    private void updateSelectedWidget(Context context, Class <?> cls, int status){
        try {
            BundleSetting bundleSetting = new BundleSetting(context);
            bundleSetting.setPlayerState(status);
            Intent update = new Intent(context, cls);
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            ComponentName widgetComponent = new ComponentName(context, cls);

            int[] widgetIds = widgetManager.getAppWidgetIds(widgetComponent);

            update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
            update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            context.sendBroadcast(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getStatus(){
        initPlayer();
        songPlayer.getStatus();
    }

    @Override
    public void onPlayerCompletion() {
        if (playerListener != null)
            playerListener.onPlayerCompletion();
    }

    @Override
    public void onBeginPlay(SongItem songItem) {

    }

    @Override
    public void onBeforePlay(SongItem songItem) {
        if (playerListener != null)
            playerListener.onBeforePlay(songItem);
    }

    @Override
    public void onPlayerPlay(SongItem songItem) {
        PlayerNotif.notify(this, songItem, 1);
        updateSelectedWidget(this, PlayerWidget.class, 1);
        if (playerListener != null)
            playerListener.onPlayerPlay(songItem);
    }

    @Override
    public void onPlayerPause(SongItem songItem) {
        PlayerNotif.notify(this, songItem, 0);
        updateSelectedWidget(this, PlayerWidget.class, 0);
        if (playerListener != null)
            playerListener.onPlayerPause(songItem);
    }

    @Override
    public void onBeforePause(SongItem songItem) {
        if (playerListener != null)
            playerListener.onBeforePause(songItem);
    }

    @Override
    public void onPlayerPrepare(Map<String, Object> info) {
        if (playerListener != null)
            playerListener.onPlayerPrepare(info);

    }

    @Override
    public void onPlayerPlaying(Map<String, Object> info) {
        if (playerListener != null)
            playerListener.onPlayerPlaying(info);
    }

    @Override
    public void onPlayerStatus(Map<String, Object> info) {
        if (playerListener != null)
            playerListener.onPlayerStatus(info);
    }

    @Override
    public void onShowProgress(String message) {
        if (playerListener != null)
        playerListener.onShowProgress(message);
    }

    @Override
    public void onHideProgress() {
        if (playerListener != null)
        playerListener.onHideProgress();
    }

    @Override
    public void onPlayerExit() {
        if (playerListener != null)
            playerListener.onPlayerExit();

        PlayerNotif.cancel(this);
        stopSelf();
    }

    @Override
    public void onPlayerNext() {
        if (playerListener != null)
            playerListener.onPlayerNext();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (playerListener != null)
            playerListener.onPlayerCompletion();
    }
}
