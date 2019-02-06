package com.inochi.music.player.item;

import java.io.Serializable;
import java.util.ArrayList;

public class SongResult implements Serializable {
	private int page;
	private int limit;
	private int count;
	private String artist;
	private ArrayList<SongItem> songItems;

    public SongResult(){

    }

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public ArrayList<SongItem> getSongItems() {
		return songItems;
	}

	public void setSongItems(ArrayList<SongItem> songItems) {
		this.songItems = songItems;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
