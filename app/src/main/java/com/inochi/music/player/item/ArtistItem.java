package com.inochi.music.player.item;

import java.io.Serializable;

public class ArtistItem implements Serializable {
	private String artistName;
	private String artistTitle;

    public ArtistItem(){

    }

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getArtistTitle() {
		return artistTitle;
	}

	public void setArtistTitle(String artistTitle) {
		this.artistTitle = artistTitle;
	}
}
