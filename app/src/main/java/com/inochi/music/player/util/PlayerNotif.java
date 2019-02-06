package com.inochi.music.player.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.inochi.music.player.MainActivity;
import com.inochi.music.player.R;
import com.inochi.music.player.helper.Constants;
import com.inochi.music.player.item.SongItem;
import com.inochi.music.player.service.PlayerService;

public class PlayerNotif {
    public static void notify(final Context context, SongItem songItem, int status) {
        showBarNotif(context, songItem, status);
    }

    private static void showBarNotif(final Context context, SongItem songItem, int status){
        //BundleSetting bundleSetting = new BundleSetting(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.notif_custom);

        int icon = R.mipmap.ic_launcher;
        int picture = R.mipmap.ic_launcher;

        final Resources res = context.getResources();
        final Bitmap bitmap = BitmapFactory.decodeResource(res, picture);

        NotificationManager notificationManager = createNotificationChannel(context);

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

        final NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(context, Constants.Default.APP_SLUG)
                .setDefaults(NotificationCompat.FLAG_NO_CLEAR)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(songTitle)
                .setContentText(fullTitle)
                .setSmallIcon(icon)
                .setLargeIcon(bitmap)
                .setContent(views)
                .setVibrate(null)
                .setOngoing(true)
                .setTicker(songItem.getSongArtist() + " - " + songItem.getSongTitle())
                .setAutoCancel(false)
        ;

        notificationManager.notify(Constants.Default.APP_ID, builder.build());
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

    private static NotificationManager createNotificationChannel(Context context) {
        NotificationManager notificationManager;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = context.getSystemService(NotificationManager.class);
        } else {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);
            String description = context.getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.Default.APP_SLUG, name, importance);
            channel.enableVibration(true);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        return notificationManager;
    }

    public static void cancel(final Context context) {
        try {
            final NotificationManager nm = createNotificationChannel(context);
            nm.cancel(Constants.Default.APP_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
