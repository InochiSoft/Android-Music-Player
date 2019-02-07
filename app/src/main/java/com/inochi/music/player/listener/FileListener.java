package com.inochi.music.player.listener;

import android.view.View;

import com.inochi.music.player.item.FileItem;

public interface FileListener {
    void setOnFileItemClick(View view, FileItem fileItem);
}
