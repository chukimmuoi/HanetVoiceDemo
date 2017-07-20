package learn.chukimmuoi.com.hanetvoicedemo;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;
import learn.chukimmuoi.com.hanetvoicedemo.broadcast.SpeechBroadcast;
import learn.chukimmuoi.com.hanetvoicedemo.speech.SpeechManager;
import learn.chukimmuoi.com.hanetvoicedemo.ui.SpeechProgressView;

import static learn.chukimmuoi.com.hanetvoicedemo.service.SpeechService.ACTION_START;
import static learn.chukimmuoi.com.hanetvoicedemo.service.SpeechService.ACTION_STOP;

public class MainActivity extends AppCompatActivity implements MainView, VoiceView, RecognitionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String KWS_SEARCH = "wakeup";

    private static final String VALUE_ACOUSTIC_MODEL = "en-us-ptm";
    private static final String VALUE_DICTIONARY = "cmudict-en-us.dict";
    private static final String VALUE_GRAM = "hanet.gram";

    private static final Float VALUE_KEYWORD_THRESHOLD = 1e-36f;

    private static final String VALUE_CHECK_RESULT_01 = "ha net";
    private static final String VALUE_CHECK_RESULT_02 = "net ha";
    private static final String VALUE_CHECK_RESULT_03 = "ok ha net";

    private LinearLayout mVoiceLayout;

    private SpeechProgressView mProgress;

    private TextView mVoice;

    private MainPresenter mPresenter;

    private SpeechRecognizer mRecognizer;

    private SpeechManager mSpeechManager;

    private SpeechBroadcast mSpeechBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this, this);

        initUI();
        initVoice();
    }

    private void initUI() {
        mVoiceLayout = (LinearLayout) findViewById(R.id.voice);
        mProgress = (SpeechProgressView) findViewById(R.id.progress);
        mVoice = (TextView) findViewById(R.id.message);

        hideVoiceLayout();
    }

    private void initVoice() {
        mRecognizer = setupRecognizer();
        start(mRecognizer);

        mSpeechManager = SpeechManager.getInstance().onCreate(MainActivity.this, mProgress, mVoice);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mSpeechBroadcast = new SpeechBroadcast(mSpeechManager);

        registerReceiver(mSpeechBroadcast, new IntentFilter(ACTION_START));
        registerReceiver(mSpeechBroadcast, new IntentFilter(ACTION_STOP));
    }

    @Override
    public void actionShowVoice(String voiceMessage) {
        if (voiceMessage.contains(VALUE_CHECK_RESULT_01)
                || voiceMessage.contains(VALUE_CHECK_RESULT_02)
                || voiceMessage.contains(VALUE_CHECK_RESULT_03)) {
            showVoiceLayout();
        }
    }

    @Override
    public void showVoiceLayout() {
        if (mVoiceLayout != null) {
            stop(mRecognizer);
            mVoice.setText(getString(R.string.main_message_help));
            mVoiceLayout.setVisibility(View.VISIBLE);

            if (mSpeechBroadcast != null) {
                mSpeechBroadcast.handleActionStart();
            }
        }
    }

    @Override
    public void hideVoiceLayout() {
        if (mVoiceLayout != null) {
            start(mRecognizer);
            mVoiceLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public SpeechRecognizer setupRecognizer() {
        try {
            Assets assets = new Assets(MainActivity.this);
            File assetDir = assets.syncAssets();

            SpeechRecognizer recognizer = SpeechRecognizerSetup.defaultSetup()
                    .setAcousticModel(new File(assetDir, VALUE_ACOUSTIC_MODEL))
                    .setDictionary(new File(assetDir, VALUE_DICTIONARY))
                    .setKeywordThreshold(VALUE_KEYWORD_THRESHOLD)
                    .setRawLogDir(assetDir).getRecognizer();

            recognizer.addListener(this);

            recognizer.addKeywordSearch(KWS_SEARCH, new File(assetDir, VALUE_GRAM));

            return recognizer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void start(SpeechRecognizer recognizer) {
        if (recognizer != null) {
            recognizer.startListening(KWS_SEARCH);
        }
    }

    @Override
    public void stop(SpeechRecognizer recognizer) {
        if (recognizer != null) {
            recognizer.stop();
        }
    }

    @Override
    public void refresh(SpeechRecognizer recognizer) {
        if (recognizer != null) {
            stop(recognizer);
            start(recognizer);
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.e(TAG, "1) onBeginningOfSpeech");

    }

    @Override
    public void onEndOfSpeech() {
        Log.e(TAG, "2) onEndOfSpeech");
        refresh(mRecognizer);
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        Log.e(TAG, "3) onPartialResult: hypothesis = " + (hypothesis == null ? "null" : "NOT null"));

    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        Log.e(TAG, "4) onResult: hypothesis = " + (hypothesis == null ? "null" : "NOT null"));

        if (hypothesis != null) {
            String voiceMessage = hypothesis.getHypstr();

            if (!TextUtils.isEmpty(voiceMessage))
                actionShowVoice(voiceMessage);
        }
    }

    @Override
    public void onError(Exception e) {
        Log.e(TAG, "5) onError: " + e.getMessage());

    }

    @Override
    public void onTimeout() {
        Log.e(TAG, "6) onTimeout");

    }

    @Override
    protected void onStop() {
        unregisterReceiver(mSpeechBroadcast);

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mSpeechManager != null) {
            mSpeechManager.onDestroy();
        }

        super.onDestroy();
    }
}
