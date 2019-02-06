package com.inochi.music.player.util;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.inochi.music.player.MainActivity;
import com.inochi.music.player.R;
import com.inochi.music.player.helper.BundleSetting;
import com.inochi.music.player.helper.Constants;
import com.inochi.music.player.item.SongItem;
import com.inochi.music.player.service.PlayerService;

import java.util.ArrayList;

public class PlayerWidget extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.player_widget);
        BundleSetting bundleSetting = new BundleSetting(context);

        ArrayList<SongItem> songItems = bundleSetting.getPlayListSelected();
        int indexPlay = bundleSetting.getPlayingIndex();
        SongItem songItem = songItems.get(indexPlay);

        //int icon = R.mipmap.ic_launcher;
        //int picture = R.mipmap.ic_music_box;
        int status = bundleSetting.getPlayerState();

        //final Resources res = context.getResources();
        //final Bitmap bitmap = BitmapFactory.decodeResource(res, picture);

        views.setInt(R.id.linPlayer, "setVisibility", View.VISIBLE);

        if (status == 1){
            views.setInt(R.id.btnPause, "setVisibility", View.VISIBLE);
            views.setInt(R.id.btnPlay, "setVisibility", View.GONE);
        } else {
            views.setInt(R.id.btnPause, "setVisibility", View.GONE);
            views.setInt(R.id.btnPlay, "setVisibility", View.VISIBLE);
        }

        views.setOnClickPendingIntent(R.id.btnPlay, intentPlay(context));
        views.setOnClickPendingIntent(R.id.btnPause, intentPlay(context));
        views.setOnClickPendingIntent(R.id.btnNext, intentNext(context));
        views.setOnClickPendingIntent(R.id.btnPrevious, intentPrev(context));
        views.setOnClickPendingIntent(R.id.btnCloseNotif, intentExit(context));
        views.setOnClickPendingIntent(R.id.btnLogoNotif, intentActivity(context));

        String songTitle = songItem.getSongTitle();
        String songArtist = songItem.getSongArtist();

        String fullTitle = songArtist + " - " + songTitle;
        views.setTextViewText(R.id.txtTitleNotif, fullTitle);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static PendingIntent intentPlay(Context context){
        Intent service = new Intent(context, PlayerService.class);
        service.setAction(Constants.Player.Action.PLAY);
        return PendingIntent.getService(context, 0, service, 0);
    }

    private static PendingIntent intentNext(Context context){
        Intent service = new Intent(context, PlayerService.class);
        service.setAction(Constants.Player.Action.NEXT);
        return PendingIntent.getService(context, 0, service, 0);
    }

    private static PendingIntent intentPrev(Context context){
        Intent service = new Intent(context, PlayerService.class);
        service.setAction(Constants.Player.Action.PREV);
        return PendingIntent.getService(context, 0, service, 0);
    }

    private static PendingIntent intentExit(Context context){
        Intent service = new Intent(context, PlayerService.class);
        service.setAction(Constants.Player.Action.EXIT);
        return PendingIntent.getService(context, 0, service, 0);
    }

    private static PendingIntent intentActivity(Context context){
        Intent service = new Intent(context, MainActivity.class);
        service.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, 0, service, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

