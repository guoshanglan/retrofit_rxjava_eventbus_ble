package cc.hisens.hardboiled.patient.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.ui.activity.searchdevice.SearchModel;

public class DeviceAdapter extends BaseAdapter {
    public List<SearchModel>deviceList;
    public Context mContext;



    public DeviceAdapter(Context context, List<SearchModel> list) {
        this.deviceList=list;
        this.mContext=context;
    }

    @Override
    public int getCount() {
        return deviceList==null?0:deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceViewHolder vh=null;
        if (convertView==null){
            convertView=View.inflate(mContext,R.layout.search_device_item,null);
            vh=new DeviceViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh= (DeviceViewHolder) convertView.getTag();
        }

        SearchModel searchMoel=deviceList.get(position);
        // vh.tvName.setText(searchMoel.bleDevice.getName());
        if (searchMoel.isConnected) {
            vh.tvState.setVisibility(View.VISIBLE);
            vh.tvName.setTextColor(Color.parseColor("#35d5db"));
        }else{
            vh.tvState.setVisibility(View.GONE);
            vh.tvName.setTextColor(Color.parseColor("#000000"));
        }


        return convertView;
    }


    class DeviceViewHolder{
        public TextView tvName;  //蓝牙名称
        public TextView tvState;  //连接状态

        public DeviceViewHolder(View view){
            tvName=view.findViewById(R.id.device_name);
            tvState=view.findViewById(R.id.device_state);
        }

    }
}
