package pri.guanhua.todayreminders.view;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import pri.guanhua.todayreminders.R;
import pri.guanhua.todayreminders.model.ListViewAdapter;
import pri.guanhua.todayreminders.model.bean.ThingsBean;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        setList();
    }

    private void initView(){
        mToolbar = findViewById(R.id.toolbar);
        mList = findViewById(R.id.list);
        mFab = findViewById(R.id.fab);
    }

    private void setToolbar(){
        mToolbar.setTitle("Today Reminders");
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
                List<ThingsBean> list = new ArrayList<>();
                for (ThingsEntity entity : all){
                    ThingsBean bean = new ThingsBean();
                    bean.id = entity.id;
                    bean.remThings = entity.remThings;
                    bean.remTime = entity.remTime;
                    list.add(bean);
                }
                ListViewAdapter adapter = new ListViewAdapter(list, MainActivity.this, mHandler);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mList.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

    private void setOnClickThingsItem(){
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

}