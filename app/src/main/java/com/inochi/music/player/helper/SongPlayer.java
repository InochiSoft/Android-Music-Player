package com.inochi.music.player.helper;

import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.os.Handler;

import com.inochi.music.player.item.SongItem;
import com.inochi.music.player.listener.PlayerListener;
import com.inochi.music.player.service.PlayerReceiver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SongPlayer implements MediaPlayer.OnCompletionListener {
    private BundleSetting bundleSetting;
    private MediaPlayer mediaPlayer;
    private ArrayList<SongItem> songItems;
    private int sessionId;
    private int delay = 900;
    private Handler handler;
    private PlayerListener playerListener;
    private Equalizer equalizer;
    private Context context;

    public SongPlayer(Context context, PlayerListener playerListener){
        this.context = context;

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        ComponentName componentName = new ComponentName(context.getPackageName(), PlayerReceiver.class.getName());
        audioManager.registerMediaButtonEventReceiver(componentName);

        this.bundleSetting = new BundleSetting(context);
        this.playerListener = playerListener;
        this.songItems = bundleSetting.getPlayListSelected();
        createMediaPlayer();
    }

    private void createMediaPlayer(){
        this.mediaPlayer = new MediaPlayer();
        this.sessionId = mediaPlayer.getAudioSessionId();
        createEqualizer();
    }

    private void createEqualizer(){
        equalizer = new Equalizer(0, sessionId);
        equalizer.setEnabled(true);

        try {
            BassBoost bassBoost = new BassBoost(0, sessionId);
            bassBoost.setEnabled(true);
            if (bassBoost.getStrengthSupported()){
                bassBoost.setStrength((short) 1000);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        short bands = 5, minEQLevel, maxEQLevel, eqAverage;
        short[] arrEqValue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        minEQLevel = equalizer.getBandLevelRange()[0];
        maxEQLevel = equalizer.getBandLevelRange()[1];

        for (short i = 0; i < bands; i++) {
            arrEqValue[i] = getEqValue(i);
            setEqValue(i, arrEqValue[i]);
        }

        eqAverage = (short) (maxEQLevel - minEQLevel);
        bundleSetting.setEqAverage(eqAverage);
        bundleSetting.setEqMin(minEQLevel);
        bundleSetting.setEqMax(maxEQLevel);
    }

    private short getEqValue(short band) {
        short eqValue = bundleSetting.getEqValue(band);
        if (eqValue <= -1){
            if (equalizer != null){
                short minEQLevel = equalizer.getBandLevelRange()[0];
                eqValue = equalizer.getBandLevelRange()[1];
                setEqValue(band, (short) (eqValue - minEQLevel));
            }
        }

        return eqValue;
    }

    private void setEqValue(short band, short value) {
        bundleSetting.setEqValue(band, value);
        if (equalizer != null) {
            short minEQLevel = equalizer.getBandLevelRange()[0];
            equalizer.setBandLevel(band, (short) (value + minEQLevel));
        }
    }

    private int getPlayingIndex() {
        this.songItems = bundleSetting.getPlayListSelected();
        int playingIndex = bundleSetting.getPlayingIndex();
        if (songItems != null){
            if (playingIndex >= songItems.size()){
                playingIndex = songItems.size() - 1;
            }
        }
        if (playingIndex < 0) playingIndex = 0;
        bundleSetting.setPlayingIndex(playingIndex);
        return playingIndex;
    }

    public void refreshList(){
        this.songItems = bundleSetting.getPlayListSelected();
    }

    public void getStatus(){
        if (mediaPlayer != null) {
            int indexPlay = getPlayingIndex();
            int intDuration = mediaPlayer.getDuration();
            this.songItems = bundleSetting.getPlayListSelected();
            if (songItems != null){
                SongItem songItem = songItems.get(indexPlay);
                if (songItem != null){
                    Map<String, Object> info = new HashMap<>();
                    info.put(Constants.Player.Info.INDEX, indexPlay);
                    info.put(Constants.Player.Info.SONGITEM, songItem);
                    info.put(Constants.Player.Info.DURATION, intDuration);
                    info.put(Constants.Player.Info.POSITION, 0);
                    info.put(Constants.Player.Info.PROGRESS, 0);
                    info.put(Constants.Player.Info.DECREASE, "");
                    info.put(Constants.Player.Info.INCREASE, "");

                    playerListener.onPlayerPrepare(info);
                    boolean isPlaying = mediaPlayer.isPlaying();

                    info.put(Constants.Player.Info.STATUS, isPlaying);
                    playerListener.onPlayerStatus(info);
                }
            }
        }
    }

    public void playAudio(){
        if (mediaPlayer.isPlaying()){
            pauseAudio();
        } else {
            if (sessionId == 0){
                beginPlay();
            } else {
                resumePlay();
            }
        }
    }

    private void pauseAudio(){
        try {
            if (mediaPlayer != null) {
                boolean isPlaying = mediaPlayer.isPlaying();
                if (isPlaying){
                    int indexPlay = getPlayingIndex();
                    final SongItem songItem = songItems.get(indexPlay);
                    playerListener.onBeforePause(songItem);

                    Handler handlerX = new Handler();
                    handlerX.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer.pause();
                            //bundleSetting.setPlayerState(2);
                            playerListener.onPlayerPause(songItem);

                            if (handler != null)
                                handler.removeCallbacks(runnableTask);
                        }
                    }, delay);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopAudio(){
        try {
            int indexPlay = getPlayingIndex();
            this.songItems = bundleSetting.getPlayListSelected();
            SongItem songItem = new SongItem();

            if (songItems != null){
                songItem = songItems.get(indexPlay);
                if (songItem == null) songItem = new SongItem();
            }

            mediaPlayer.stop();
            bundleSetting.setPlayerSession(0);
            playerListener.onPlayerPause(songItem);
            bundleSetting.setLastPostion(0);

            if (handler != null)
                handler.removeCallbacks(runnableTask);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exitPlayer(){
        try {
            mediaPlayer.stop();
            bundleSetting.setPlayerSession(0);
            bundleSetting.setLastPostion(0);

            if (handler != null)
                handler.removeCallbacks(runnableTask);

            playerListener.onPlayerExit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resumePlay(){
        int indexPlay = getPlayingIndex();
        final SongItem songItem = songItems.get(indexPlay);
        String filePath = songItem.getSongPath();

        sessionId = mediaPlayer.getAudioSessionId();
        int intSessionIdSaved = bundleSetting.getPlayerSession();

        if (intSessionIdSaved == sessionId){
            if (filePath != null){
                if (filePath.length() > 0){
                    if (isNumeric(filePath)){
                        try {
                            playerListener.onBeforePlay(songItem);

                            Handler handlerX = new Handler();
                            handlerX.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mediaPlayer.start();
                                    boolean isPlaying = mediaPlayer.isPlaying();

                                    if (isPlaying){
                                        playerListener.onPlayerPlay(songItem);

                                        if (mediaPlayer.isPlaying()){
                                            handler = new Handler();
                                            handler.postDelayed(runnableTask, delay);
                                        }
                                    } else {
                                        playerListener.onPlayerPause(songItem);
                                    }
                                    int sessionid = mediaPlayer.getAudioSessionId();
                                    bundleSetting.setPlayerSession(sessionid);
                                }
                            }, delay);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        File file = new File(filePath);
                        if (file.exists()){
                            try {
                                playerListener.onBeforePlay(songItem);

                                Handler handlerX = new Handler();
                                handlerX.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mediaPlayer.start();
                                        boolean isPlaying = mediaPlayer.isPlaying();

                                        if (isPlaying){
                                            playerListener.onPlayerPlay(songItem);

                                            if (mediaPlayer.isPlaying()){
                                                handler = new Handler();
                                                handler.postDelayed(runnableTask, delay);
                                            }
                                        } else {
                                            playerListener.onPlayerPause(songItem);
                                        }
                                        int sessionid = mediaPlayer.getAudioSessionId();
                                        bundleSetting.setPlayerSession(sessionid);
                                    }
                                }, delay);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } else {
            beginPlay();
        }
    }

    private void beginPlay(){
        final int indexPlay = getPlayingIndex();
        final SongItem songItem = songItems.get(indexPlay);
        final String filePath = songItem.getSongPath();

        playerListener.onPlayerPause(songItem);

        if (isNumeric(filePath)){
            try {
                final int intId = Integer.parseInt(filePath);
                playerListener.onBeforePlay(songItem);

                Handler handlerX = new Handler();
                handlerX.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mediaPlayer.reset();
                            mediaPlayer = MediaPlayer.create(context, intId);
                            mediaPlayer.start();

                            int lastPost = bundleSetting.getLastPostion();
                            if (lastPost > 0) mediaPlayer.seekTo(lastPost);

                            Map<String, Object> info = new HashMap<>();
                            int intDuration = mediaPlayer.getDuration();
                            info.put(Constants.Player.Info.INDEX, indexPlay);
                            info.put(Constants.Player.Info.SONGITEM, songItem);

                            info.put(Constants.Player.Info.DURATION, intDuration);
                            info.put(Constants.Player.Info.POSITION, lastPost);
                            info.put(Constants.Player.Info.PROGRESS, 0);
                            info.put(Constants.Player.Info.DECREASE, "");
                            info.put(Constants.Player.Info.INCREASE, "");

                            playerListener.onBeginPlay(songItem);
                            playerListener.onPlayerPrepare(info);

                            playerListener.onPlayerPlay(songItem);
                            mediaPlayer.setOnCompletionListener(SongPlayer.this);

                            if (mediaPlayer.isPlaying()){
                                handler = new Handler();
                                handler.postDelayed(runnableTask, delay);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, delay);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                sessionId = mediaPlayer.getAudioSessionId();
                bundleSetting.setPlayerSession(sessionId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            File file = new File(filePath);

            if (file.exists()){
                try {
                    playerListener.onBeforePlay(songItem);

                    Handler handlerX = new Handler();
                    handlerX.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mediaPlayer.reset();
                                mediaPlayer.setDataSource(filePath);
                                mediaPlayer.prepare();

                                mediaPlayer.start();

                                int lastPost = bundleSetting.getLastPostion();
                                if (lastPost > 0) mediaPlayer.seekTo(lastPost);

                                Map<String, Object> info = new HashMap<>();
                                int intDuration = mediaPlayer.getDuration();
                                info.put(Constants.Player.Info.INDEX, indexPlay);
                                info.put(Constants.Player.Info.SONGITEM, songItem);

                                info.put(Constants.Player.Info.DURATION, intDuration);
                                info.put(Constants.Player.Info.POSITION, lastPost);
                                info.put(Constants.Player.Info.PROGRESS, 0);
                                info.put(Constants.Player.Info.DECREASE, "");
                                info.put(Constants.Player.Info.INCREASE, "");

                                playerListener.onBeginPlay(songItem);
                                playerListener.onPlayerPrepare(info);

                                playerListener.onPlayerPlay(songItem);
                                mediaPlayer.setOnCompletionListener(SongPlayer.this);

                                if (mediaPlayer.isPlaying()){
                                    handler = new Handler();
                                    handler.postDelayed(runnableTask, delay);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }, delay);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    sessionId = mediaPlayer.getAudioSessionId();
                    bundleSetting.setPlayerSession(sessionId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void nextAudio(final int typePlay){
        this.songItems = bundleSetting.getPlayListSelected();
        int intCountItem;
        int intNextPlay = getPlayingIndex();
        if (songItems != null){
            intCountItem = songItems.size();
            if (intCountItem > 0){
                switch (typePlay){
                    case Constants.Player.PREVIOUS: //previous
                        intNextPlay -= 1;
                        if (intNextPlay < 0){
                            intNextPlay = intCountItem - 1;
                        }
                        break;
                    case Constants.Player.NEXT: //next
                        intNextPlay += 1;
                        if (intNextPlay > intCountItem - 1){
                            intNextPlay = 0;
                        }
                        break;
                }

                SongItem songItem = songItems.get(intNextPlay);
                playerListener.onBeforePause(songItem);

                Handler handlerX = new Handler();
                final int finalIntNextPlay = intNextPlay;
                handlerX.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopAudio();
                        bundleSetting.setPlayingIndex(finalIntNextPlay);
                        SongItem songItemSelected = songItems.get(finalIntNextPlay);
                        bundleSetting.setSongItemSelected(songItemSelected);

                        playAudio();
                    }
                }, delay);

            } else {
                playerListener.onBeforePause(new SongItem());
                Handler handlerX = new Handler();
                handlerX.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopAudio();
                    }
                }, delay);
            }
        } else {
            playerListener.onBeforePause(new SongItem());
            Handler handlerX = new Handler();
            handlerX.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopAudio();
                }
            }, delay);
        }
    }

    public void seekAudio(int value){
        if (mediaPlayer != null)
            mediaPlayer.seekTo(value);
    }

    private int getProgressPercent(long currentDuration, long totalDuration){
        try {
            long currentSeconds = (int) (currentDuration / 1000);
            long totalSeconds = (int) (totalDuration / 1000);

            Double percentage =(((double)currentSeconds)/totalSeconds) * 100;

            return percentage.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String milliSecondsToTimer(long milliseconds){
        try {
            String finalTimerString = "";
            String secondsString;

            int hours = (int)( milliseconds / (1000*60*60));
            int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
            int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);

            if(hours > 0){
                finalTimerString = hours + ":";
            }

            if(seconds < 10){
                secondsString = "0" + seconds;
            } else {
                secondsString = "" + seconds;}

            finalTimerString = finalTimerString + minutes + ":" + secondsString;
            // return timer string
            return finalTimerString;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void refreshBalance() {
        if (this.mediaPlayer != null)
            this.mediaPlayer.setVolume(getBalanceLeft(), getBalanceRight());
    }

    public void refreshEqualizer() {
        short bands = 5;
        short[] arrEqValue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (short i = 0; i < bands; i++) {
            arrEqValue[i] = getEqValue(i);
            setEqValue(i, arrEqValue[i]);
        }
    }

    public float getBalanceLeft(){
        return bundleSetting.getFloBalanceLeft();
    }

    public void setBalanceLeft(float value){
        bundleSetting.setFloBalanceLeft(value);
        if (this.mediaPlayer != null)
            this.mediaPlayer.setVolume(getBalanceLeft(), getBalanceRight());
    }

    public float getBalanceRight(){
        return bundleSetting.getFloBalanceRight();
    }

    public void setBalanceRight(float value){
        bundleSetting.setFloBalanceRight(value);
        if (this.mediaPlayer != null)
            this.mediaPlayer.setVolume(getBalanceLeft(), getBalanceRight());
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(.\\d+)?");
    }

    private Runnable runnableTask = new Runnable() {
        Map<String, Object> info = new HashMap<>();
        public void run() {
            try {
                if (mediaPlayer.isPlaying()){
                    int curPosition = mediaPlayer.getCurrentPosition();
                    int intDuration = mediaPlayer.getDuration();
                    int progress = (getProgressPercent(curPosition, intDuration));

                    String timeDecrease = milliSecondsToTimer(curPosition);
                    String timeIncrease = milliSecondsToTimer(intDuration - curPosition);

                    info.put(Constants.Player.Info.DURATION, intDuration);
                    info.put(Constants.Player.Info.POSITION, curPosition);
                    info.put(Constants.Player.Info.PROGRESS, progress);
                    info.put(Constants.Player.Info.DECREASE, timeDecrease);
                    info.put(Constants.Player.Info.INCREASE, timeIncrease);

                    bundleSetting.setLastPostion(curPosition);

                    playerListener.onPlayerPlaying(info);

                    if (handler != null) handler.postDelayed(this, delay);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onCompletion(MediaPlayer mp) {
        playerListener.onPlayerCompletion();
        playerListener.onPlayerNext();
        nextAudio(Constants.Player.NEXT);
    }
}
