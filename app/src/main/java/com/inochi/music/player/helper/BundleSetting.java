package com.inochi.music.player.helper;

import android.content.Context;

import com.inochi.music.player.item.ArtistItem;
import com.inochi.music.player.item.PlaylistItem;
import com.inochi.music.player.item.SongItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BundleSetting {
    private Setting setting;

    public BundleSetting(Context context){
        this.setting = new Setting(context);
    }

    private String dateFormat(int year, int month, int day){
        return year +
                "-" + String.format(Locale.getDefault(), "%02d", month) +
                "-" + String.format(Locale.getDefault(), "%02d", day);
    }

    public String getTodayDate(){
        Calendar todayMasehi = Calendar.getInstance();
        int month = todayMasehi.get(Calendar.MONTH) + 1;
        int year = todayMasehi.get(Calendar.YEAR);
        int day = todayMasehi.get(Calendar.DAY_OF_MONTH);
        return dateFormat(year, month, day);
    }

    public void setSongList(ArrayList<SongItem> listSong, String artist, int page){
        if (listSong != null){
            if (listSong.size() > 0){
                JSONArray jsonBooks = new JSONArray();
                for (SongItem songItem : listSong){
                    JSONObject jsonProperties = new JSONObject();
                    try {
                        jsonProperties.put("songName", songItem.getSongName());
                        jsonProperties.put("songTitle", songItem.getSongTitle());
                        jsonProperties.put("songPath", songItem.getSongPath());
                        String itemArtist = songItem.getSongArtist();
                        if (itemArtist == null) itemArtist = "";
                        if (itemArtist.isEmpty()) itemArtist = artist;
                        jsonProperties.put("songArtist", itemArtist);
                        jsonProperties.put("songFav", songItem.isSongFav());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonBooks.put(jsonProperties);
                }
                String content = jsonBooks.toString();
                setting.setSetting(Constants.Setting.Key.SONG_LIST + "_" + artist + "_" + page, content);
            }
        }
    }

    public ArrayList<SongItem> getSongList(String artist, int page){
        ArrayList<SongItem> result = null;
        String content = setting.getSetting(Constants.Setting.Key.SONG_LIST + "_" + artist + "_" + page, "");
        if (!content.isEmpty()){
            try {
                JSONArray jsonBooks = new JSONArray(content);
                if (jsonBooks.length() > 0){
                    result = new ArrayList<>();
                    for (int i = 0; i < jsonBooks.length(); i++) {
                        JSONObject jsonProp = jsonBooks.getJSONObject(i);
                        SongItem songItem = new SongItem();

                        if (jsonProp.has("songName")){
                            songItem.setSongName(jsonProp.getString("songName"));
                        }
                        if (jsonProp.has("songTitle")){
                            songItem.setSongTitle(jsonProp.getString("songTitle"));
                        }
                        if (jsonProp.has("songArtist")){
                            songItem.setSongArtist(jsonProp.getString("songArtist"));
                        }
                        if (jsonProp.has("songPath")){
                            songItem.setSongPath(jsonProp.getString("songPath"));
                        }
                        if (jsonProp.has("songFav")){
                            songItem.setSongFav(jsonProp.getBoolean("songFav"));
                        }
                        result.add(songItem);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public SongItem getSongItemSelected() {
        String strSongItem = setting.getSetting(Constants.Setting.Key.SELECTED_SONG, "");
        SongItem songItem = null;

        if (!strSongItem.isEmpty()){
            songItem = new SongItem();
            try {
                JSONObject jsonObject = new JSONObject(strSongItem);
                String songName = "";
                String songTitle = "";
                String songArtist = "";
                if (jsonObject.has("songName")){
                    songName = jsonObject.getString("songName");
                }
                if (jsonObject.has("songTitle")){
                    songTitle = jsonObject.getString("songTitle");
                }
                if (jsonObject.has("songArtist")){
                    songArtist = jsonObject.getString("songArtist");
                }
                songItem.setSongArtist(songArtist);
                songItem.setSongTitle(songTitle);
                songItem.setSongName(songName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return songItem;
    }

    public void setSongItemSelected(SongItem songItem) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("songName", songItem.getSongName());
            jsonObject.put("songTitle", songItem.getSongTitle());
            jsonObject.put("songArtist", songItem.getSongArtist());
            jsonObject.put("songPath", songItem.getSongPath());
            jsonObject.put("songFav", songItem.isSongFav());

            String strSongItem = jsonObject.toString();
            setting.setSetting(Constants.Setting.Key.SELECTED_SONG, strSongItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public SongItem getSongItemPlaying() {
        String strSongItem = setting.getSetting(Constants.Setting.Key.PLAYING_SONG, "");
        SongItem songItem = null;

        if (!strSongItem.isEmpty()){
            songItem = new SongItem();
            try {
                JSONObject jsonObject = new JSONObject(strSongItem);
                String songName = "";
                String songTitle = "";
                String songArtist = "";
                if (jsonObject.has("songName")){
                    songName = jsonObject.getString("songName");
                }
                if (jsonObject.has("songTitle")){
                    songTitle = jsonObject.getString("songTitle");
                }
                if (jsonObject.has("songArtist")){
                    songArtist = jsonObject.getString("songArtist");
                }
                songItem.setSongArtist(songArtist);
                songItem.setSongTitle(songTitle);
                songItem.setSongName(songName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return songItem;
    }

    public void setSongItemPlaying(SongItem songItem) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("songName", songItem.getSongName());
            jsonObject.put("songTitle", songItem.getSongTitle());
            jsonObject.put("songArtist", songItem.getSongArtist());
            jsonObject.put("songPath", songItem.getSongPath());
            jsonObject.put("songFav", songItem.isSongFav());

            String strSongItem = jsonObject.toString();
            setting.setSetting(Constants.Setting.Key.PLAYING_SONG, strSongItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArtistItem getArtistItemSelected() {
        String strArtistItem = setting.getSetting(Constants.Setting.Key.SELECTED_ARTIST, "");
        ArtistItem artistItem = new ArtistItem();

        if (!strArtistItem.isEmpty()){
            try {
                JSONObject jsonObject = new JSONObject(strArtistItem);
                String artisTitle = "";
                String artisName = "";

                if (jsonObject.has("artisTitle")){
                    artisTitle = jsonObject.getString("artisTitle");
                }
                if (jsonObject.has("artisName")){
                    artisName = jsonObject.getString("artisName");
                }
                artistItem.setArtistName(artisName);
                artistItem.setArtistTitle(artisTitle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return artistItem;
    }

    public void setArtistItemSelected(ArtistItem artistItem) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("artisTitle", artistItem.getArtistTitle());
            jsonObject.put("artisName", artistItem.getArtistName());

            String strUserItem = jsonObject.toString();
            setting.setSetting(Constants.Setting.Key.SELECTED_ARTIST, strUserItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public PlaylistItem getPlaylistItemSelected() {
        String strItem = setting.getSetting(Constants.Setting.Key.SELECTED_PLAYLIST_ITEM, "");
        PlaylistItem playlistItem = new PlaylistItem();

        if (!strItem.isEmpty()){
            try {
                JSONObject jsonObject = new JSONObject(strItem);
                String playlistTitle = "";
                String playlistName = "";

                if (jsonObject.has("playlistName")){
                    playlistName = jsonObject.getString("playlistName");
                }
                if (jsonObject.has("playlistTitle")){
                    playlistTitle = jsonObject.getString("playlistTitle");
                }
                playlistItem.setPlaylistName(playlistName);
                playlistItem.setPlaylistTitle(playlistTitle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return playlistItem;
    }

    public void setPlaylistItemSelected(PlaylistItem playlistItem) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("playlistTitle", playlistItem.getPlaylistTitle());
            jsonObject.put("playlistName", playlistItem.getPlaylistName());

            String strUserItem = jsonObject.toString();
            setting.setSetting(Constants.Setting.Key.SELECTED_PLAYLIST_ITEM, strUserItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setPlayList(String strName, ArrayList<SongItem> listSong){
        if (listSong != null){
            String content = "";
            if (listSong.size() > 0){
                JSONArray jsonBooks = new JSONArray();
                for (SongItem songItem : listSong){
                    JSONObject jsonProperties = new JSONObject();
                    try {
                        jsonProperties.put("songName", songItem.getSongName());
                        jsonProperties.put("songTitle", songItem.getSongTitle());
                        jsonProperties.put("songPath", songItem.getSongPath());
                        jsonProperties.put("songArtist", songItem.getSongArtist());
                        jsonProperties.put("songFav", songItem.isSongFav());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonBooks.put(jsonProperties);
                }
                content = jsonBooks.toString();
            }
            setting.setSetting(Constants.Setting.Key.PLAY_LIST + "_" + strName, content);
        }
    }

    public ArrayList<SongItem> getPlayList(String strName){
        ArrayList<SongItem> result = null;
        String content = setting.getSetting(Constants.Setting.Key.PLAY_LIST + "_" + strName, "");
        if (!content.isEmpty()){
            try {
                JSONArray jsonBooks = new JSONArray(content);
                if (jsonBooks.length() > 0){
                    result = new ArrayList<>();
                    for (int i = 0; i < jsonBooks.length(); i++) {
                        JSONObject jsonProp = jsonBooks.getJSONObject(i);
                        SongItem songItem = new SongItem();

                        if (jsonProp.has("songName")){
                            songItem.setSongName(jsonProp.getString("songName"));
                        }
                        if (jsonProp.has("songTitle")){
                            songItem.setSongTitle(jsonProp.getString("songTitle"));
                        }
                        if (jsonProp.has("songPath")){
                            songItem.setSongPath(jsonProp.getString("songPath"));
                        }
                        if (jsonProp.has("songArtist")){
                            songItem.setSongArtist(jsonProp.getString("songArtist"));
                        }
                        if (jsonProp.has("songFav")){
                            songItem.setSongFav(jsonProp.getBoolean("songFav"));
                        }
                        result.add(songItem);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void setArtistList(ArrayList<ArtistItem> artistItems){
        if (artistItems != null){
            if (artistItems.size() > 0){
                JSONArray jsonBooks = new JSONArray();
                for (ArtistItem artistItem : artistItems){
                    JSONObject jsonProperties = new JSONObject();
                    try {
                        jsonProperties.put("artistName", artistItem.getArtistName());
                        jsonProperties.put("artistTitle", artistItem.getArtistTitle());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonBooks.put(jsonProperties);
                }
                String content = jsonBooks.toString();
                setting.setSetting(Constants.Setting.Key.ARTIST_LIST, content);
            }
        }
    }

    public ArrayList<ArtistItem> getArtistList(){
        ArrayList<ArtistItem> result = null;
        String content = setting.getSetting(Constants.Setting.Key.ARTIST_LIST, "");
        if (!content.isEmpty()){
            try {
                JSONArray jsonBooks = new JSONArray(content);
                if (jsonBooks.length() > 0){
                    result = new ArrayList<>();
                    for (int i = 0; i < jsonBooks.length(); i++) {
                        JSONObject jsonProp = jsonBooks.getJSONObject(i);
                        ArtistItem artistItem = new ArtistItem();

                        if (jsonProp.has("artistName")){
                            artistItem.setArtistName(jsonProp.getString("artistName"));
                        }
                        if (jsonProp.has("artistTitle")){
                            artistItem.setArtistTitle(jsonProp.getString("artistTitle"));
                        }
                        result.add(artistItem);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void setPlayListSelected(ArrayList<SongItem> listSong){
        if (listSong != null){
            String content = "";
            if (listSong.size() > 0){
                JSONArray jsonBooks = new JSONArray();
                for (SongItem songItem : listSong){
                    JSONObject jsonProperties = new JSONObject();
                    try {
                        jsonProperties.put("songName", songItem.getSongName());
                        jsonProperties.put("songTitle", songItem.getSongTitle());
                        jsonProperties.put("songPath", songItem.getSongPath());
                        jsonProperties.put("songArtist", songItem.getSongArtist());
                        jsonProperties.put("songFav", songItem.isSongFav());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonBooks.put(jsonProperties);
                }
                content = jsonBooks.toString();
            }
            setting.setSetting(Constants.Setting.Key.SELECTED_PLAYLIST, content);
        }
    }

    public ArrayList<SongItem> getPlayListSelected(){
        ArrayList<SongItem> result = null;
        String content = setting.getSetting(Constants.Setting.Key.SELECTED_PLAYLIST, "");
        if (!content.isEmpty()){
            try {
                JSONArray jsonBooks = new JSONArray(content);
                if (jsonBooks.length() > 0){
                    result = new ArrayList<>();
                    for (int i = 0; i < jsonBooks.length(); i++) {
                        JSONObject jsonProp = jsonBooks.getJSONObject(i);
                        SongItem songItem = new SongItem();

                        if (jsonProp.has("songName")){
                            songItem.setSongName(jsonProp.getString("songName"));
                        }
                        if (jsonProp.has("songTitle")){
                            songItem.setSongTitle(jsonProp.getString("songTitle"));
                        }
                        if (jsonProp.has("songArtist")){
                            songItem.setSongArtist(jsonProp.getString("songArtist"));
                        }
                        if (jsonProp.has("songPath")){
                            songItem.setSongPath(jsonProp.getString("songPath"));
                        }
                        if (jsonProp.has("songFav")){
                            songItem.setSongFav(jsonProp.getBoolean("songFav"));
                        }
                        result.add(songItem);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void setPlaylistList(ArrayList<PlaylistItem> playlistItems){
        if (playlistItems != null){
            if (playlistItems.size() > 0){
                JSONArray jsonPlaylists = new JSONArray();
                for (PlaylistItem playlistItem : playlistItems){
                    JSONObject jsonPlaylist = new JSONObject();
                    try {
                        jsonPlaylist.put("playlistName", playlistItem.getPlaylistName());
                        jsonPlaylist.put("playlistTitle", playlistItem.getPlaylistTitle());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonPlaylists.put(jsonPlaylist);

                }
                String content = jsonPlaylists.toString();
                setting.setSetting(Constants.Setting.Key.PLAYLIST_LIST, content);
            }
        }
    }

    public ArrayList<PlaylistItem> getPlaylistList(){
        ArrayList<PlaylistItem> result = null;
        String content = setting.getSetting(Constants.Setting.Key.PLAYLIST_LIST, "");
        if (!content.isEmpty()){
            try {
                JSONArray jsonPlaylists = new JSONArray(content);
                if (jsonPlaylists.length() > 0){
                    result = new ArrayList<>();
                    for (int i = 0; i < jsonPlaylists.length(); i++) {
                        JSONObject jsonPlaylist = jsonPlaylists.getJSONObject(i);
                        PlaylistItem playlistItem = new PlaylistItem();

                        if (jsonPlaylist.has("playlistName")){
                            playlistItem.setPlaylistName(jsonPlaylist.getString("playlistName"));
                        }
                        if (jsonPlaylist.has("playlistTitle")){
                            playlistItem.setPlaylistTitle(jsonPlaylist.getString("playlistTitle"));
                        }

                        result.add(playlistItem);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public int getPlayingIndex() {
        return setting.getIntSetting(Constants.Setting.Key.PLAYING_INDEX, -1);
    }

    public void setPlayingIndex(int value) {
        this.setting.setIntSetting(Constants.Setting.Key.PLAYING_INDEX, value);
    }

    public int getPlayerSession() {
        return setting.getIntSetting(Constants.Setting.Key.PLAYER_SESSION, 0);
    }

    public void setPlayerSession(int value) {
        this.setting.setIntSetting(Constants.Setting.Key.PLAYER_SESSION, value);
    }

    public int getPlayerState() {
        return setting.getIntSetting(Constants.Setting.Key.PLAYER_STATE, 0);
    }

    public void setPlayerState(int value) {
        this.setting.setIntSetting(Constants.Setting.Key.PLAYER_STATE, value);
    }

    public int getLastPostion() {
        return setting.getIntSetting(Constants.Setting.Key.LAST_POSITION, 0);
    }

    public void setLastPostion(int value) {
        this.setting.setIntSetting(Constants.Setting.Key.LAST_POSITION, value);
    }

    public short getEqValue(short band) {
        return setting.getShortSetting(Constants.Setting.Key.EQ_VALUE + "_" + String.valueOf(band), (short) -1);
    }

    public short getEqAverage(short band) {
        return setting.getShortSetting(Constants.Setting.Key.EQ_AVERAGE + String.valueOf(band), (short) 0);
    }

    public void setEqAverage(short value) {
        setting.setShortSetting(Constants.Setting.Key.EQ_AVERAGE, value);
    }

    public short getEqMin() {
        return setting.getShortSetting(Constants.Setting.Key.EQ_LEVEL_MIN, (short) 0);
    }

    public void setEqMin(short value) {
        setting.setShortSetting(Constants.Setting.Key.EQ_LEVEL_MIN, value);
    }

    public short getEqMax() {
        return setting.getShortSetting(Constants.Setting.Key.EQ_LEVEL_MAX, (short) 0);
    }

    public void setEqMax(short value) {
        setting.setShortSetting(Constants.Setting.Key.EQ_LEVEL_MAX, value);
    }

    public void setEqValue(short band, short value) {
        setting.setShortSetting(Constants.Setting.Key.EQ_VALUE + "_" + String.valueOf(band), value);
    }

    public float getFloBalanceLeft() {
        String strBalanceLeft = setting.getSetting(Constants.Setting.Key.BALANCE_LEFT, "1");
        float floBalanceLeft;

        if (strBalanceLeft.equals("")) {
            floBalanceLeft = 1;
        } else {
            floBalanceLeft = Float.parseFloat(strBalanceLeft);
        }
        return floBalanceLeft;
    }

    public void setFloBalanceLeft(float floBalanceLeft) {
        setting.setSetting(Constants.Setting.Key.BALANCE_LEFT, String.valueOf(floBalanceLeft));
    }

    public float getFloBalanceRight() {
        String strBalanceLeft = setting.getSetting(Constants.Setting.Key.BALANCE_RIGHT, "1");
        float floBalanceRight;
        if (strBalanceLeft.equals("")) {
            floBalanceRight = 1;
        } else {
            floBalanceRight = Float.parseFloat(strBalanceLeft);
        }
        return floBalanceRight;
    }

    public void setFloBalanceRight(float floBalanceRight) {
        setting.setSetting(Constants.Setting.Key.BALANCE_RIGHT, String.valueOf(floBalanceRight));
    }
}
