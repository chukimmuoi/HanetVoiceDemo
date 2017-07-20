package learn.chukimmuoi.com.hanetvoicedemo.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SpeechService extends IntentService {

    private static final String TAG = SpeechService.class.getSimpleName();

    public static final String ACTION_START
            = "learn.chukimmuoi.com.voidlistenerdemo.service.action.START";
    public static final String ACTION_STOP
            = "learn.chukimmuoi.com.voidlistenerdemo.service.action.STOP";

    public SpeechService() {
        super("SpeechService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(intent.getAction());
        if (intent != null) {
            sendBroadcast(broadcastIntent);
        }
    }
}
