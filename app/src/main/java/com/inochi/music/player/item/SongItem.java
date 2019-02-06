package com.inochi.music.player.item;

import java.io.Serializable;

public class SongItem implements Serializable, Comparable<SongItem> {
	private boolean songFav;
	private String songName;
	private String songTitle;
	private String songArtist;
	private String songPath;
	private boolean selected;
	private int songPlay;

    public SongItem(){

    }

	public boolean isSongFav() {
		return songFav;
	}

	public void setSongFav(boolean songFav) {
		this.songFav = songFav;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getSongTitle() {
		return songTitle;
	}

	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}

	public String getSongArtist() {
		return songArtist;
	}

	public void setSongArtist(String songArtist) {
		this.songArtist = songArtist;
	}

	public String getSongPath() {
		return songPath;
	}

	public void setSongPath(String songPath) {
		this.songPath = songPath;
	}

	@Override
	public int compareTo(SongItem other) {
    	String beginArtistTitle = this.getSongArtist().toLowerCase() + "_" + this.getSongName();
    	String otherArtistTitle = other.getSongArtist().toLowerCase() + "_" + other.getSongName();
		return beginArtistTitle.compareTo(otherArtistTitle);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getSongPlay() {
		return songPlay;
	}

	public void setSongPlay(int songPlay) {
		this.songPlay = songPlay;
	}
}
