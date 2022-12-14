package pri.guanhua.todayreminders.view;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

import pri.guanhua.todayreminders.R;
import pri.guanhua.todayreminders.model.ListViewAdapter;
import pri.guanhua.todayreminders.model.bean.ThingsEntity;
import pri.guanhua.todayreminders.model.database.AppDatabase;

public class MainActivity extends BaseActivity {

    private Toolbar mToolbar = null;
    private ListView mList = null;
    private FloatingActionButton mFab = null;

    private final Handler mHandler = new Handler(Looper.myLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setToolbar();
        setAdd();
        createNotificationChannel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setList();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        //设置总是显示图标
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.question:
                Intent intent = new Intent(this, QuestionActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView(){
        mToolbar = findViewById(R.id.toolbar);
        mList = findViewById(R.id.list);
        mFab = findViewById(R.id.fab);
    }

    private void setToolbar(){
        mToolbar.setTitle("Today Reminders");
        setSupportActionBar(mToolbar);
    }

    private void setAdd(){
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddThingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ThingsEntity> all = AppDatabase.getInstance(MainActivity.this).thingsDao().getAll();
                Collections.sort(all);//重新排序
                ListViewAdapter adapter = new ListViewAdapter(all, MainActivity.this, mHandler);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mList.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            String CHANNEL_ID = "1";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.enableVibration(true);//设置可以震动
            channel.setShowBadge(true);//设置悬浮式通知
            //channel.enableLights(true);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}