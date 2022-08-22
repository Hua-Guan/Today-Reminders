package pri.guanhua.todayreminders.model.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class ThingsEntity implements Comparable<ThingsEntity>{

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

    @Override
    public int compareTo(ThingsEntity o) {
        if (this.year != o.year){
            return this.year - o.year;
        }
        if (this.month != o.month){
            return this.month - o.month;
        }
        if (this.day != o.day){
            return this.day - o.day;
        }
        if (this.hour != o.hour){
            return this.hour - o.hour;
        }
        if (this.min != o.min){
            return this.min - o.min;
        }
        return 0;
    }
}
