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


public interface MainView {
    void actionShowVoice(String voiceMessage);

    void showVoiceLayout();

    void hideVoiceLayout();

    void updateStateResult(boolean isSuccess);
}
