package md.fusionworks.android.cardio.application;

import android.app.Application;

/**
 * Created by admin on 12.08.2015.
 */
public class CardioApp extends Application {

    public static CardioApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static CardioApp getInstance() {

        return instance;
    }
}
