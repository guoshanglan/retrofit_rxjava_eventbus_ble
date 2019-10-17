package cc.hisens.hardboiled.patient.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.db.bean.Ed;
import cc.hisens.hardboiled.patient.model.HeadTitleModel;
import cc.hisens.hardboiled.patient.utils.TimeUtils;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

//监测记录的适配器
public class MonitorRecordAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    public Context context;
    public List<Ed>list;
    public List<HeadTitleModel>headTitleModels;


    public MonitorRecordAdapter(Context context, List<Ed> edList, List<HeadTitleModel> titleModels){
        this.context=context;
        this.list=edList;
        this.headTitleModels=titleModels;

    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderHolder vh=null;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.mydoctor_head_item,null);
            vh=new HeaderHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh= (HeaderHolder) convertView.getTag();
        }
        String head=headTitleModels.get(position).getTitle();
        vh.tvTitle.setText(head);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return Long.parseLong(headTitleModels.get(position).getTitleId());
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MonitorRecordHold recordHold;
        if (convertView==null){
            convertView=View.inflate(context,R.layout.monitorrecord_item,null);
            recordHold=new MonitorRecordHold(convertView);
            convertView.setTag(recordHold);
        }else{
            recordHold= (MonitorRecordHold) convertView.getTag();
        }
        Ed ed=list.get(position);
        if (ed.isInterferential()){
            recordHold.ivIcon.setBackgroundResource(R.drawable.home_icon_avss);
            recordHold.tvMonitorType.setText("AVSS模式");
        }else{
            recordHold.ivIcon.setBackgroundResource(R.drawable.home_icon_npt);
            recordHold.tvMonitorType.setText("NPT模式");
        }

        recordHold.tvDate.setText(TimeUtils.RecordDate(ed.getStartTimestamp())+" "+TimeUtils.RecordTime(ed.getStartTimestamp())+"-"+TimeUtils.RecordTime(ed.getEndTimestamp()));



        return convertView;
    }



    class HeaderHolder{
        public TextView tvTitle;
        public HeaderHolder(View view){
            tvTitle=view.findViewById(R.id.tv_header);
        }

    }


    class MonitorRecordHold{
        TextView tvMonitorType; //监测模式
        TextView tvDate;  //监测日期
        ImageView ivIcon;
        public MonitorRecordHold(View view){
            tvMonitorType=view.findViewById(R.id.tv_monitorecord_type);
            tvDate=view.findViewById(R.id.tv_monitorecord_date);
            ivIcon=view.findViewById(R.id.iv_monitorrecord);
        }
    }

}
