package pri.guanhua.todayreminders.model.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ThingsEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo
    public String remTime = "";

    @ColumnInfo
    public String remThings = "";

}
