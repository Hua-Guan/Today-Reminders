package pri.guanhua.todayreminders.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import pri.guanhua.todayreminders.GlobalValues;
import pri.guanhua.todayreminders.R;
import pri.guanhua.todayreminders.model.bean.ThingsEntity;
import pri.guanhua.todayreminders.model.database.AppDatabase;

public class AddThingsActivity extends BaseActivity{

    private androidx.appcompat.widget.Toolbar mToolbar = null;
    private Button mAddThings = null;
    private EditText mTodayThings = null;
    private TimePicker mTimerPicker = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_things);
        initView();
        setToolbar();
        setAddThings();
    }

    private void initView(){
        mToolbar = findViewById(R.id.toolbar);
        mAddThings = findViewById(R.id.add_things);
        mTodayThings = findViewById(R.id.today_things);
        mTimerPicker = findViewById(R.id.time_picker);
    }

    private void setToolbar(){
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setAddThings(){
        mAddThings.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.S)
                    @Override
                    public void run() {
                        AppDatabase instance = AppDatabase.getInstance(AddThingsActivity.this);
                        ThingsEntity entity = new ThingsEntity();
                        entity.remThings = mTodayThings.getText().toString();
                        entity.remTime = "" + mTimerPicker.getHour() + mTimerPicker.getMinute();
                        instance.thingsDao().insert(entity);
                        //从数据库中获取事项的id
                        List<ThingsEntity> all = instance.thingsDao().getAll();
                        long id = all.get(all.size() - 1).id;
                        //设定闹钟，时间到了即可发送通知
                        String num = String.valueOf(id);
                        setAlarm(Integer.parseInt(num), mTimerPicker.getHour(), mTimerPicker.getMinute(), entity.remThings);
                        finish();
                    }
                }).start();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void setAlarm(int id, int hour, int min, String things){
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent();
        ComponentName name = new ComponentName(GlobalValues.PACKAGE_NAME, GlobalValues.ALARM_CLASS_PATH);
        intent.setAction(GlobalValues.TIME_ACTION);
        intent.putExtra("content", things);
        intent.setComponent(name);
        PendingIntent sender = PendingIntent.getBroadcast(AddThingsActivity.this,
                id, intent, PendingIntent.FLAG_MUTABLE);

        Calendar calen = Calendar.getInstance();
        calen.setTimeInMillis(System.currentTimeMillis());
        calen.set(Calendar.HOUR_OF_DAY, hour);
        calen.set(Calendar.MINUTE, min);
        calen.set(Calendar.SECOND, 0);
        //开启高精度闹钟，在手机休眠仍然起作用
        alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calen.getTimeInMillis(), sender);
        //测试取消闹钟,测试成功
        PendingIntent test = PendingIntent.getBroadcast(AddThingsActivity.this,
                id, intent, PendingIntent.FLAG_MUTABLE);
        //alarm.cancel(test);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
