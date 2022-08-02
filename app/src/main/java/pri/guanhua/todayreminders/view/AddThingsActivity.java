package pri.guanhua.todayreminders.view;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Objects;

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
                    @Override
                    public void run() {
                        AppDatabase instance = AppDatabase.getInstance(AddThingsActivity.this);
                        ThingsEntity entity = new ThingsEntity();
                        entity.remThings = mTodayThings.getText().toString();
                        entity.remTime = "" + mTimerPicker.getHour() + mTimerPicker.getMinute();
                        instance.thingsDao().insert(entity);
                        finish();
                    }
                }).start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
