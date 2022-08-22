package pri.guanhua.todayreminders.model.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class ThingsEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public String remThings = "";

    @ColumnInfo
    public int hour = 0;

    @ColumnInfo
    public int min = 0;

    @ColumnInfo
    public int year = 0;

    @ColumnInfo
    public int month = 0;

    @ColumnInfo
    public int day = 0;

    @ColumnInfo
    public long currentTimeMillis = 0;

}
