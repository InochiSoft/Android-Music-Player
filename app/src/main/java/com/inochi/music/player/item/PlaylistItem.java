package com.inochi.music.player.item;

import java.io.Serializable;

public class PlaylistItem implements Serializable {
	private String playlistName;
	private String playlistTitle;

    public PlaylistItem(){

    }

	public String getPlaylistName() {
		return playlistName;
	}

	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}

	public String getPlaylistTitle() {
		return playlistTitle;
	}

	public void setPlaylistTitle(String playlistTitle) {
		this.playlistTitle = playlistTitle;
	}
}
