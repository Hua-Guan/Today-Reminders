package pri.guanhua.todayreminders.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationManagerCompat;

public class Utils {
    //是否有通知权限
    public static boolean isOpenNotify(Context context) {
        boolean isOpened = false;
        try {
            NotificationManagerCompat from = NotificationManagerCompat.from(context);
            isOpened = from.areNotificationsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOpened;
    }

    //前往设置
    public static void goToSetNotify(Context context) {
        if(Build.VERSION.SDK_INT >=26) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE,context.getPackageName());
            context.startActivity(intent);
        }else {
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package",context.getApplicationContext().getPackageName());
            intent.putExtra("app_uid",context.getApplicationInfo().uid);
            context.startActivity(intent);
        }
    }
}
