package learn.chukimmuoi.com.hanetvoicedemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Random;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;
import learn.chukimmuoi.com.hanetvoicedemo.speech.SpeechManager;
import learn.chukimmuoi.com.hanetvoicedemo.ui.SpeechProgressView;

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
    private static final String VALUE_CHECK_RESULT_04 = "net";

    private LinearLayout mVoiceLayout;

    private SpeechProgressView mProgress;

    private TextView mSay;

    private TextView mVoice;

    private ImageView mResultState;

    private MainPresenter mPresenter;

    private SpeechRecognizer mRecognizer;

    private SpeechManager mSpeechManager;

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
        mSay = (TextView) findViewById(R.id.say);
        mResultState = (ImageView) findViewById(R.id.img_result_state);

        hideVoiceLayout();
    }

    private void initVoice() {
        mRecognizer = setupRecognizer();
        start(mRecognizer);

        mSpeechManager = SpeechManager.getInstance().onCreate(MainActivity.this, this, mProgress, mVoice);
    }

    @Override
    public void actionShowVoice(String voiceMessage) {
        Log.e(TAG, "actionShowVoice: voiceMessage = " + voiceMessage);

        if (voiceMessage.contains(VALUE_CHECK_RESULT_01)
                || voiceMessage.contains(VALUE_CHECK_RESULT_02)
                || voiceMessage.contains(VALUE_CHECK_RESULT_03)
                || voiceMessage.contains(VALUE_CHECK_RESULT_04)) {
            showVoiceLayout();
        }
    }

    @Override
    public void showVoiceLayout() {
        if (mVoiceLayout != null) {
            stop(mRecognizer);

            mVoice.setText(getHintMessage(this));
            mSay.setVisibility(View.GONE);
            mVoiceLayout.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.VISIBLE);
            mResultState.setVisibility(View.GONE);

            if (mSpeechManager != null) {
                mSpeechManager.onStart();
            }
        }
    }

    @Override
    public void hideVoiceLayout() {
        if (mVoiceLayout != null) {
            start(mRecognizer);
            mSay.setVisibility(View.VISIBLE);
            mVoiceLayout.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            mResultState.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateStateResult(boolean isSuccess) {
        mProgress.setVisibility(View.GONE);
        if (isSuccess) {
            mResultState.setImageResource(R.drawable.ic_done);
        } else {
            mResultState.setImageResource(R.drawable.ic_error);
            mVoice.setText(getString(R.string.main_message_error));
        }
        mResultState.setVisibility(View.VISIBLE);

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
    protected void onDestroy() {
        if (mSpeechManager != null) {
            mSpeechManager.onDestroy();
        }

        super.onDestroy();
    }

    private String getHintMessage(Context context) {
        String output = "";
        String[] arrayHint = context.getResources().getStringArray(R.array.hanet_hint);
        if (arrayHint != null) {
            int size = arrayHint.length;
            Random random = new Random();
            int indexRandom = random.nextInt(size);
            output = arrayHint[indexRandom];
        }

        if (TextUtils.isEmpty(output)) {
            output = context.getString(R.string.main_message_help);
        }

        return output;
    }
}
