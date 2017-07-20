package learn.chukimmuoi.com.hanetvoicedemo.listener;

import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import learn.chukimmuoi.com.hanetvoicedemo.MainView;
import learn.chukimmuoi.com.hanetvoicedemo.speech.SpeechManager;
import learn.chukimmuoi.com.hanetvoicedemo.ui.SpeechProgressView;

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

    public static final int TIME_DELAY_HIDE_LAYOUT_VOICE = 3 * 1000;

    private SpeechProgressView mProgress;

    private TextView mTextVoice;

    private SpeechManager mSpeechManager;

    private MainView mMainView;

    private Handler mHandler;

    public SpeechListener(Handler handler, MainView mainView, SpeechProgressView progress,
                          TextView textVoice, SpeechManager speechManager) {
        mHandler       = handler;
        mMainView      = mainView;
        mProgress      = progress;
        mTextVoice     = textVoice;
        mSpeechManager = speechManager;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.e(TAG, "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.e(TAG, "onBeginningOfSpeech");
        if (mProgress != null)
            mProgress.onBeginningOfSpeech();

        mSpeechManager.setListening(true);
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

        if (mProgress != null)
            mProgress.onResultOrOnError();

        hideVoiceLayout();
    }

    @Override
    public void onResults(Bundle results) {
        Log.e(TAG, "onResults");
        mSpeechManager.setListening(false);

        String str = new String();
        ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (int i = 0; i < data.size(); i++) {
            Log.e(TAG, "result: " + data.get(i));
            str += ((i != 0) ? ",\n" : "") + data.get(i);
        }
        mTextVoice.setText(str);

        if (mProgress != null)
            mProgress.onResultOrOnError();

        hideVoiceLayout();
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.e(TAG, "onPartialResults");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.e(TAG, "onEvent");
    }

    private void hideVoiceLayout(){
        if(mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMainView.hideVoiceLayout();
                }
            }, TIME_DELAY_HIDE_LAYOUT_VOICE);
        }
    }
}
