package pri.guanhua.todayreminders.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import pri.guanhua.todayreminders.R;
import pri.guanhua.todayreminders.model.bean.ThingsBean;
import pri.guanhua.todayreminders.model.bean.ThingsEntity;
import pri.guanhua.todayreminders.model.database.AppDatabase;

public class ListViewAdapter extends BaseAdapter {

    private List<ThingsBean> mList = null;
    private Context mContext = null;
    private Handler mHandler;

    public ListViewAdapter(List<ThingsBean> mList, Context mContext, Handler mHandler) {
        this.mList = mList;
        this.mContext = mContext;
        this.mHandler = mHandler;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        ThingsBean thingsBean = mList.get(position);
        return thingsBean.id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_thing, parent, false);
            Holder holder = new Holder();
            holder.radioButton = convertView.findViewById(R.id.radio_button);
            holder.remTime = convertView.findViewById(R.id.rem_time);
            holder.remThing = convertView.findViewById(R.id.rem_content);

            ThingsBean bean = mList.get(position);
            holder.radioButton.setTag(bean);
            holder.radioButton.setChecked(false);
            holder.remTime.setText(bean.remTime);
            holder.remThing.setText(bean.remThings);

            setRadioButtonListener(holder);

            convertView.setTag(holder);

        }else {
            Holder holder = (Holder) convertView.getTag();
            ThingsBean bean = mList.get(position);
            holder.radioButton.setTag(bean);
            holder.radioButton.setChecked(false);
            holder.remTime.setText(bean.remTime);
            holder.remThing.setText(bean.remThings);

            setRadioButtonListener(holder);
        }
        return convertView;
    }

    private void setRadioButtonListener(Holder holder){
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase instance = AppDatabase.getInstance(mContext);
                        ThingsBean thingsBean = (ThingsBean) v.getTag();
                        long id = thingsBean.id;
                        instance.thingsDao().deleteById(Integer.parseInt(Long.toString(id)));
                        mList.remove(thingsBean);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(800);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                ListViewAdapter.this.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    static class Holder{
        RadioButton radioButton;
        TextView remTime;
        TextView remThing;
    }

}
