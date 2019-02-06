package com.inochi.music.player.listener;

import com.inochi.music.player.item.SongItem;

import java.util.Map;

public interface PlayerListener {
    void onPlayerCompletion();
    void onBeginPlay(SongItem songItem);
    void onBeforePlay(SongItem songItem);
    void onPlayerPlay(SongItem songItem);
    void onPlayerPause(SongItem songItem);
    void onBeforePause(SongItem songItem);
    void onPlayerPrepare(Map<String, Object> info);
    void onPlayerPlaying(Map<String, Object> info);
    void onPlayerStatus(Map<String, Object> info);
    void onShowProgress(String message);
    void onHideProgress();
    void onPlayerExit();
    void onPlayerNext();
}
