package cc.hisens.hardboiled.patient.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.db.bean.Conversation;
import cc.hisens.hardboiled.patient.db.bean.Doctor;
import cc.hisens.hardboiled.patient.ui.activity.searchdoctor.model.SearchDoctorModel;

public class SearchDoctorAdapter extends BaseAdapter {
    public List<Doctor>list;
    public Context mContext;



    public SearchDoctorAdapter(Context context, List<Doctor> list) {
        this.list=list;
        this.mContext=context;
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
        SearchViewHolder vh=null;
         if (convertView==null){
             convertView=View.inflate(mContext,R.layout.mydoctor_item,null);
             vh=new SearchViewHolder(convertView);
             convertView.setTag(vh);
         }else{
             vh= (SearchViewHolder) convertView.getTag();
         }
        Doctor bean=list.get(position);
         //todo  这里需要赋值，目前咱们没有内容，稍后再弄
         vh.tvStar.setVisibility(View.VISIBLE);
         if (!TextUtils.isEmpty(bean.getName())) {
             vh.tvName.setText(bean.getName());
         }

        for (int i=0;i<bean.getWorkplaces().size();i++){
             if (bean.getWorkplaces().get(i).getIndex()==1){
                 vh.tvPosition.setText(bean.getWorkplaces().get(i).getName());
             }
        }


       if (bean.getLevel()==1){
             vh.tvZhiwei.setText("主任医师");
       }else if (bean.getLevel()==2){
           vh.tvZhiwei.setText("副主任医师");
       }else if (bean.getLevel()==3){
           vh.tvZhiwei.setText("主治医师");
       }else if (bean.getLevel()==4){
           vh.tvZhiwei.setText("住院医师");
       }else if (bean.getLevel()==5){
           vh.tvZhiwei.setText("助理医师");
       }

       vh.tvStar.setText(bean.getAve_score()+"");

       Glide.with(mContext).load(bean.getHead_url()).placeholder(R.drawable.doctor_head_100).into(vh.ivAvator);

        return convertView;
    }


     class SearchViewHolder{
         public ImageView ivAvator; //头像
         public TextView tvName,tvZhiwei,tvPosition,tvStar;

         public SearchViewHolder(View view){
             ivAvator=view.findViewById(R.id.iv_avator);
             tvName=view.findViewById(R.id.tv_username);
             tvZhiwei=view.findViewById(R.id.tv_zhiwei);
             tvPosition=view.findViewById(R.id.tv_position);
             tvStar=view.findViewById(R.id.tv_star);
         }


     }

}
