package cc.hisens.hardboiled.patient.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.db.bean.Doctor;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


//我的医生adapter

public class MyDoctorAdapter extends BaseAdapter implements StickyListHeadersAdapter{

    public List<Doctor>list;
    public Context mContext;


    public MyDoctorAdapter(List<Doctor> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }



    @Override
    public long getHeaderId(int position) {
        return Long.parseLong(list.get(position).getTitleId());
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
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderHolder vh=null;
        if (convertView==null){
            convertView=View.inflate(mContext,R.layout.mydoctor_head_item,null);
            vh=new HeaderHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh= (HeaderHolder) convertView.getTag();
        }
        String head=list.get(position).getTitle();
        vh.tvTitle.setText(head);
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DoctorViewHolder vh=null;
        if (convertView==null){
            convertView=View.inflate(mContext,R.layout.mydoctor_item,null);
            vh=new DoctorViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh= (DoctorViewHolder) convertView.getTag();
        }
        Doctor doctor=list.get(position);
        vh.tvName.setText(doctor.getName());
        for (int i=0;i<doctor.getWorkplaces().size();i++){
            if (doctor.getWorkplaces().get(i).getIndex()==1){
                vh.tvPosition.setText(doctor.getWorkplaces().get(i).getName());
            }
        }
        Glide.with(mContext).load(doctor.getHead_url()).placeholder(R.drawable.doctor_head_100).into(vh.ivAvator);
        if (doctor.getLevel()==1){
            vh.tvZhiwei.setText("主任医师");
        }else if (doctor.getLevel()==2){
            vh.tvZhiwei.setText("副主任医师");
        }else if (doctor.getLevel()==3){
            vh.tvZhiwei.setText("主治医师");
        }else if (doctor.getLevel()==4){
            vh.tvZhiwei.setText("住院医师");
        }else if (doctor.getLevel()==5){
            vh.tvZhiwei.setText("助理医师");
        }



        return convertView;
    }


    class HeaderHolder{
        public TextView tvTitle;
        public HeaderHolder(View view){
            tvTitle=view.findViewById(R.id.tv_header);
        }

    }

    class DoctorViewHolder{
        public ImageView ivAvator; //头像
        public TextView tvName,tvZhiwei,tvPosition;

        public DoctorViewHolder(View view){
            ivAvator=view.findViewById(R.id.iv_avator);
            tvName=view.findViewById(R.id.tv_username);
            tvZhiwei=view.findViewById(R.id.tv_zhiwei);
            tvPosition=view.findViewById(R.id.tv_position);
        }

    }






}
