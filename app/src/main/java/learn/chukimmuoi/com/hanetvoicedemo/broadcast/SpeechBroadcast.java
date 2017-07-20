package learn.chukimmuoi.com.hanetvoicedemo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import learn.chukimmuoi.com.hanetvoicedemo.service.SpeechService;
import learn.chukimmuoi.com.hanetvoicedemo.speech.SpeechManager;

/**
 * @author:Hanet Electronics
 * @Skype: chukimmuoi
 * @Mobile : +84 167 367 2505
 * @Email: muoick@hanet.com
 * @Website: http://hanet.com/
 * @Project: VoidListenerDemo
 * Created by CHUKIMMUOI on 1/14/2017.
 */


public class SpeechBroadcast extends BroadcastReceiver {

    private static final String TAG = SpeechBroadcast.class.getSimpleName();

    private SpeechManager mSpeechManager;

    public SpeechBroadcast(SpeechManager speechManager) {
        this.mSpeechManager = speechManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(SpeechService.ACTION_START)) {
            //TODO: Start.
            handleActionStart();
        } else if (action.equals(SpeechService.ACTION_STOP)) {
            //TODO: Stop.
            handleActionStop();
        }
    }

    public void handleActionStart() {
        if (mSpeechManager != null) {
            mSpeechManager.onStart();
        }
    }

    public void handleActionStop() {
        if (mSpeechManager != null) {
            mSpeechManager.onStop();
        }
    }
}
