package learn.chukimmuoi.com.hanetvoicedemo.speech;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.TextView;

import learn.chukimmuoi.com.hanetvoicedemo.MainView;
import learn.chukimmuoi.com.hanetvoicedemo.constanst.IConstanst;
import learn.chukimmuoi.com.hanetvoicedemo.listener.SpeechListener;
import learn.chukimmuoi.com.hanetvoicedemo.ui.SpeechProgressView;

/**
 * @author:Hanet Electronics
 * @Skype: chukimmuoi
 * @Mobile : +84 167 367 2505
 * @Email: muoick@hanet.com
 * @Website: http://hanet.com/
 * @Project: VoidListenerDemo
 * Created by CHUKIMMUOI on 1/14/2017.
 */


public class SpeechManager implements IConstanst {

    private static final String TAG = SpeechManager.class.getSimpleName();

    private static SpeechManager ourInstance;

    private SpeechRecognizer mSpeechRecognizer;

    private Intent mIntent;

    private boolean isListening;

    private Handler mHandler = new Handler();

    public static SpeechManager getInstance() {
        if (ourInstance == null) {
            synchronized (SpeechManager.class) {
                ourInstance = new SpeechManager();
            }
        }
        return ourInstance;
    }

    public SpeechManager onCreate(Context context, MainView mainView, SpeechProgressView progress, TextView textVoice) {
        initialSpeech(context, mainView, progress, textVoice);
        return ourInstance;
    }

    public void onStart() {
        startListener();
    }

    public void onStop() {
        stopListener();
    }

    public void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        if (mSpeechRecognizer != null) {
            stopListener();
            mSpeechRecognizer.destroy();
            mSpeechRecognizer = null;
        }

        if (ourInstance != null) {
            ourInstance = null;
        }
    }

    private void initialSpeech(Context context, MainView mainView, SpeechProgressView progress, TextView textVoice) {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        mSpeechRecognizer.setRecognitionListener(new SpeechListener(mHandler, mainView, progress, textVoice, ourInstance));

        mIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getClass().getPackage().getName());

        mIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, MAX_RESULTS_VOID_SEARCH);
        mIntent.putExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES, EXTRA_CONFIDENCE_SCORES);
        mIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,
                EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS);

        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, LANGUAGES_VI);
        mIntent.putExtra(EXTRA_ADDITIONAL_LANGUAGES, new String[]{LANGUAGES_VI});
    }

    private void startListener() {
        if (mSpeechRecognizer != null) {
            if (!isListening) {
                isListening = true;

                mSpeechRecognizer.startListening(mIntent);
            }
        }
    }

    private void stopListener() {
        if (mSpeechRecognizer != null) {
            isListening = false;

            mSpeechRecognizer.stopListening();
            mSpeechRecognizer.cancel();
        }
    }

    public boolean isListening() {
        return isListening;
    }

    public void setListening(boolean listening) {
        isListening = listening;
    }
}
