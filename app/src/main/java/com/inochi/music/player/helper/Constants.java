package com.inochi.music.player.helper;

public final class Constants {
    public static final class Default {
        public static final int APP_ID = 9249;
        public static final String APP_SLUG = "com.inochi.music.player";
    }

    public static final class Permission {
        public static final int READ_STORAGE = 1002;
        public static final int WRITE_STORAGE = 1003;
    }

    public static final class Player {
        public static final int PREVIOUS = 0;
        public static final int NEXT = 1;

        public static final class Info {
            public static final String DURATION = "DURATION";
            public static final String POSITION = "POSITION";
            public static final String PROGRESS = "PROGRESS";
            public static final String DECREASE = "DECREASE";
            public static final String INCREASE = "INCREASE";
            public static final String INDEX = "INDEX";
            public static final String SONGITEM = "SONGITEM";
            public static final String STATUS = "STATUS";
            public static final String TIME = "TIME";
        }

        public static final class Setting {
            public static final String SEEK_VALUE = "SEEK_VALUE";
        }

        public static final class Action {
            public static final String PLAY = Default.APP_SLUG + ".PLAY";
            public static final String NEXT = Default.APP_SLUG + ".NEXT";
            public static final String PREV = Default.APP_SLUG + ".PREV";
            public static final String SEEK = Default.APP_SLUG + ".SEEK";
            public static final String STOP = Default.APP_SLUG + ".STOP";
            public static final String STATUS = Default.APP_SLUG + ".STATUS";
            public static final String REFRESH = Default.APP_SLUG + ".REFRESH";
            public static final String EXIT = Default.APP_SLUG + ".EXIT";
            public static final String BALANCE = Default.APP_SLUG + ".BALANCE";
            public static final String EQUALIZER = Default.APP_SLUG + ".EQUALIZER";
        }
    }

    public static final class Setting {
        public static final class Key {
            public static final String FRAGMENT_TYPE = "FRAGMENT_TYPE";
            public static final String LAST_FRAGMENT_TYPE = "LAST_FRAGMENT_TYPE";
            public static final String LAST_USER = "LAST_USER";
            public static final String SECTION_TYPE = "SECTION_TYPE";
            public static final String SONG_LIST = "SONG_LIST";
            public static final String ARTIST_LIST = "ARTIST_LIST";
            public static final String FAVOURITE_LIST = "FAVOURITE_LIST";
            public static final String PLAYLIST_LIST = "PLAYLIST_LIST";
            public static final String PLAYING_SONG = "PLAYING_SONG";
            public static final String SELECTED_SONG = "SELECTED_SONG";
            public static final String SELECTED_ARTIST = "SELECTED_ARTIST";
            public static final String SELECTED_PLAYLIST = "SELECTED_PLAYLIST";
            public static final String SELECTED_PLAYLIST_ITEM = "SELECTED_PLAYLIST_ITEM";

            public static final String PLAY_LIST = "PLAY_LIST";
            public static final String PLAYING_INDEX = "PLAYING_INDEX";
            public static final String PLAYER_STATE = "PLAYER_STATE";
            public static final String PLAYER_SESSION = "PLAYER_SESSION";
            public static final String BG_INDEX = "BG_INDEX";
            public static final String EQ_VALUE = "EQ_VALUE";
            public static final String EQ_AVERAGE = "EQ_AVERAGE";
            public static final String EQ_LEVEL_MIN = "EQ_LEVEL_MIN";
            public static final String EQ_LEVEL_MAX = "EQ_LEVEL_MAX";
            public static final String BALANCE_LEFT = "BALANCE_LEFT";
            public static final String BALANCE_RIGHT = "BALANCE_RIGHT";
            public static final String LAST_POSITION = "LAST_POSITION";

            public static final String LYRIC = "LYRIC";

            public static final String CHECK_SONG_LIST = "CHECK_SONG_LIST";
            public static final String CHECK_ARTIST_LIST = "CHECK_ARTIST_LIST";
            public static final String CHECK_LYRIC_LIST = "CHECK_LYRIC_LIST";
            public static final String CHECK_PLAYLIST_LIST = "CHECK_PLAYLIST_LIST";

            public static final String PLAYLIST_CURRENT = "PLAYLIST_CURRENT";
            public static final String ADMOB_APP_ID = "ADMOB_APP_ID";
            public static final String ADMOB_BANNER_UNIT_ID = "ADMOB_BANNER_UNIT_ID";
            public static final String ADMOB_INTER_UNIT_ID = "ADMOB_INTER_UNIT_ID";
            public static final String INTER_SHOW = "ADMOB_INTER_UNIT_ID";
        }
    }
}
