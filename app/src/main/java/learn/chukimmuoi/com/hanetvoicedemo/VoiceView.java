package learn.chukimmuoi.com.hanetvoicedemo;

import edu.cmu.pocketsphinx.SpeechRecognizer;

/**
 * @author:Hanet Electronics
 * @Skype: chukimmuoi
 * @Mobile : +84 167 367 2505
 * @Email: muoick@hanet.com
 * @Website: http://hanet.com/
 * @Project: HanetVoiceDemo
 * Created by CHUKIMMUOI on 7/20/2017.
 */


public interface VoiceView {
    SpeechRecognizer setupRecognizer();

    void start(SpeechRecognizer recognizer);

    void stop(SpeechRecognizer recognizer);

    void refresh(SpeechRecognizer recognizer);
}
