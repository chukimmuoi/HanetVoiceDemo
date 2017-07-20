package learn.chukimmuoi.com.hanetvoicedemo.listener;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import learn.chukimmuoi.com.hanetvoicedemo.service.SpeechService;
import learn.chukimmuoi.com.hanetvoicedemo.speech.SpeechManager;
import learn.chukimmuoi.com.hanetvoicedemo.ui.SpeechProgressView;

import static learn.chukimmuoi.com.hanetvoicedemo.service.SpeechService.ACTION_START;

/**
 * @author:Hanet Electronics
 * @Skype: chukimmuoi
 * @Mobile : +84 167 367 2505
 * @Email: muoick@hanet.com
 * @Website: http://hanet.com/
 * @Project: VoidListenerDemo
 * Created by CHUKIMMUOI on 1/13/2017.
 */

public class SpeechListener implements RecognitionListener {

    private static final String TAG = SpeechListener.class.getSimpleName();

    private static final long MILLIS_IN_FUTURE = 10000;

    private static final long COUNT_DOWN_INTERVAL = 1000;

    private static final long MIN_MILLIS_UNTIL_FINISHED = 3000;

    private Context mContext;

    private AudioManager mAudioManager;

    private SpeechProgressView mProgress;

    private TextView mTextVoice;

    private TextView mTextMessage;

    private int mStreamVolume;

    private SpeechManager mSpeechManager;

    public SpeechListener(Context context, AudioManager audioManager, int streamVolume, SpeechProgressView progress,
                          TextView textVoice, TextView textMessage, SpeechManager speechManager) {
        mContext       = context;
        mAudioManager  = audioManager;
        mStreamVolume  = streamVolume;
        mProgress      = progress;
        mTextVoice     = textVoice;
        mTextMessage   = textMessage;
        mSpeechManager = speechManager;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.e(TAG, "onReadyForSpeech");
        mCountDownTimer.start();

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mStreamVolume, 0);

        mTextMessage.setText("Listening...");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.e(TAG, "onBeginningOfSpeech");
        if (mProgress != null)
            mProgress.onBeginningOfSpeech();

        mSpeechManager.setListening(true);

        mTextMessage.setText("Listening...");
    }

    @Override
    public void onRmsChanged(float rmsDB) {
        Log.e(TAG, "onRmsChanged");
        if (mProgress != null)
            mProgress.onRmsChanged(rmsDB);

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.e(TAG, "onBufferReceived");

    }

    @Override
    public void onEndOfSpeech() {
        Log.e(TAG, "onEndOfSpeech");

        if (mProgress != null)
            mProgress.onEndOfSpeech();
    }

    @Override
    public void onError(int error) {
        Log.e(TAG, "onError");
        mSpeechManager.setListening(false);

        mCountDownTimer.cancel();

        if (mProgress != null)
            mProgress.onResultOrOnError();

        actionStart(mContext);
    }

    @Override
    public void onResults(Bundle results) {
        Log.e(TAG, "onResults");
        mSpeechManager.setListening(false);

        mCountDownTimer.cancel();

        String str = new String();
        ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (int i = 0; i < data.size(); i++) {
            Log.e(TAG, "result: " + data.get(i));
            str += ((i != 0) ? ",\n" : "") + data.get(i);
        }
        mTextVoice.setText(str);
        mTextMessage.setText("Success");

        if (mProgress != null)
            mProgress.onResultOrOnError();

        actionStart(mContext);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.e(TAG, "onPartialResults");

    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.e(TAG, "onEvent");

    }

    private void actionStop(Context context) {
        Intent intent = new Intent(context, SpeechService.class);
        intent.setAction(SpeechService.ACTION_STOP);
        context.startService(intent);
    }

    private void actionStart(Context context) {
        Intent intent = new Intent(context, SpeechService.class);
        intent.setAction(ACTION_START);
        context.startService(intent);
    }

    private CountDownTimer mCountDownTimer
            = new CountDownTimer(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished < MIN_MILLIS_UNTIL_FINISHED) {
                actionStop(mContext);
            }
        }

        @Override
        public void onFinish() {
            actionStart(mContext);
        }
    };
}
