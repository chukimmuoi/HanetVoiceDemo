package learn.chukimmuoi.com.hanetvoicedemo;

/**
 * @author:Hanet Electronics
 * @Skype: chukimmuoi
 * @Mobile : +84 167 367 2505
 * @Email: muoick@hanet.com
 * @Website: http://hanet.com/
 * @Project: HanetVoiceDemo
 * Created by CHUKIMMUOI on 7/20/2017.
 */


public class MainPresenter {
    private static final String TAG = MainPresenter.class.getSimpleName();

    private MainView mMainView;

    private VoiceView mVoiceView;

    public MainPresenter(MainView mMainView, VoiceView mVoiceView) {
        this.mMainView = mMainView;
        this.mVoiceView = mVoiceView;
    }
}
