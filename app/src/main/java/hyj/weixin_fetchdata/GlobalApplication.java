package hyj.weixin_fetchdata;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;


import hyj.weixin_fetchdata.handler.CrashHandler;



public class GlobalApplication extends Application {
    private static Context context;
    private static ContentResolver resolver;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        resolver = getContentResolver();
        //程序崩溃错误捕捉
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(context);
    }

    public static Context getContext(){
        return context;
    }
    public static ContentResolver getResolver(){
        return resolver;
    }
}
