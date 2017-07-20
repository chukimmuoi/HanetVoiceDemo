package learn.chukimmuoi.com.hanetvoicedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import learn.chukimmuoi.com.hanetvoicedemo.ui.SpeechProgressView;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final String TAG = MainActivity.class.getSimpleName();

    private LinearLayout mVoiceLayout;

    private SpeechProgressView mProgress;

    private TextView mMessage;

    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter();

        initUI();
    }

    private void initUI() {
        mVoiceLayout = (LinearLayout) findViewById(R.id.voice);
        mProgress = (SpeechProgressView) findViewById(R.id.progress);
        mMessage = (TextView) findViewById(R.id.message);
    }

    @Override
    public void showVoiceLayout() {
        if (mVoiceLayout != null)
            mVoiceLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideVoiceLayout() {
        if (mVoiceLayout != null)
            mVoiceLayout.setVisibility(View.GONE);
    }
}
