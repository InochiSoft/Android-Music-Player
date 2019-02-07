package com.inochi.music.player.listener;

import android.view.View;

import com.inochi.music.player.item.SongItem;

public interface SongListener {
    void onSongItemClick(View view, SongItem songItem);
    void onDeleteButtonClick(View view, SongItem songItem, int position);
}
