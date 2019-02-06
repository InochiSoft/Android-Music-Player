package com.inochi.music.player.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.inochi.music.player.helper.Constants;

public class PlayerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null){
            final String action = intent.getAction();

            if (action != null) {
                switch (action) {
                    case Intent.ACTION_MEDIA_BUTTON:
                        KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            if (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == event.getKeyCode()) {
                                intentPlay(context);
                            } else if (KeyEvent.KEYCODE_MEDIA_PLAY == event.getKeyCode()) {
                                intentPlay(context);
                            } else if (KeyEvent.KEYCODE_MEDIA_PAUSE == event.getKeyCode()) {
                                intentPlay(context);
                            } else if (KeyEvent.KEYCODE_MEDIA_NEXT == event.getKeyCode()) {
                                intentNext(context);
                            } else if (KeyEvent.KEYCODE_MEDIA_PREVIOUS == event.getKeyCode()) {
                                intentPrev(context);
                            } else if (KeyEvent.KEYCODE_MEDIA_STOP == event.getKeyCode()) {
                                intentStop(context);
                            }
                        }
                }
            }
        }
    }

    private void intentPlay(Context context){
        Intent service = new Intent(context, PlayerService.class);
        service.setAction(Constants.Player.Action.PLAY);
        context.startService(service);
    }

    private void intentNext(Context context){
        Intent service = new Intent(context, PlayerService.class);
        service.setAction(Constants.Player.Action.NEXT);
        context.startService(service);
    }

    private void intentPrev(Context context){
        Intent service = new Intent(context, PlayerService.class);
        service.setAction(Constants.Player.Action.PREV);
        context.startService(service);
    }

    private void intentStop(Context context){
        Intent service = new Intent(context, PlayerService.class);
        service.setAction(Constants.Player.Action.STOP);
        context.startService(service);
    }

    private void intentExit(Context context){
        Intent service = new Intent(context, PlayerService.class);
        service.setAction(Constants.Player.Action.EXIT);
        context.startService(service);
    }
}
