package pri.guanhua.todayreminders.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import pri.guanhua.todayreminders.model.bean.ThingsEntity;

@Dao
public interface ThingsDao {

    @Query("select * from ThingsEntity")
    List<ThingsEntity> getAll();

    @Delete
    void delete(ThingsEntity entity);

    @Insert
    void insert(ThingsEntity entity);

    @Query("delete from ThingsEntity where id in (:id)")
    void deleteById(int id);

}
