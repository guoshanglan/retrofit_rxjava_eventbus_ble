package cc.hisens.hardboiled.patient.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.model.ConSultionModel;
import cc.hisens.hardboiled.patient.utils.TimeUtils;

//评价adapter
public class EvaluationAdapter extends BaseAdapter {
    public Context mContext;
    public List<ConSultionModel.DatasBean.ListBean>list;
    public int type;
    private SimpleDateFormat daySdf;

    public EvaluationAdapter(Context mContext, List<ConSultionModel.DatasBean.ListBean> list,int type) {
        this.mContext = mContext;
        this.list = list;
        this.type=type;

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
        EvaluationViewHolder vh=null;
        if (convertView==null){
            if (type==1) {
                convertView = View.inflate(mContext, R.layout.pinjia_item, null);
            }else{
                convertView = View.inflate(mContext, R.layout.pinjia_item2, null);
            }
            convertView = View.inflate(mContext, R.layout.pinjia_item, null);
            vh=new EvaluationViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh= (EvaluationViewHolder) convertView.getTag();
        }
        ConSultionModel.DatasBean.ListBean bean=list.get(position);
           vh.tvDesc.setText(bean.getContent());
            vh.tvDate.setText(TimeUtils.NowYear(bean.getCreated_time()));
            vh.tvPhone.setText(bean.getPhone()+"");
            if (bean.getScore()==100){
                vh.ivStar1.setBackgroundResource(R.drawable.doctor_star_small_sel);
                vh.ivStar2.setBackgroundResource(R.drawable.doctor_star_small_sel);
                vh.ivStar3.setBackgroundResource(R.drawable.doctor_star_small_sel);
                vh.ivStar4.setBackgroundResource(R.drawable.doctor_star_small_sel);
                vh.ivStar5.setBackgroundResource(R.drawable.doctor_star_small_sel);
            }else if (bean.getScore()>=80){
                vh.ivStar1.setBackgroundResource(R.drawable.doctor_star_small_sel);
                vh.ivStar2.setBackgroundResource(R.drawable.doctor_star_small_sel);
                vh.ivStar3.setBackgroundResource(R.drawable.doctor_star_small_sel);
                vh.ivStar4.setBackgroundResource(R.drawable.doctor_star_small_sel);
                vh.ivStar5.setBackgroundResource(R.drawable.doctor_star_small);
            }else if (bean.getScore()>=60){
                vh.ivStar1.setBackgroundResource(R.drawable.doctor_star_small_sel);
                vh.ivStar2.setBackgroundResource(R.drawable.doctor_star_small_sel);
                vh.ivStar3.setBackgroundResource(R.drawable.doctor_star_small_sel);
                vh.ivStar4.setBackgroundResource(R.drawable.doctor_star_small);
                vh.ivStar5.setBackgroundResource(R.drawable.doctor_star_small);
            }else if (bean.getScore()>=40){
                vh.ivStar1.setBackgroundResource(R.drawable.doctor_star_small_sel);
                vh.ivStar2.setBackgroundResource(R.drawable.doctor_star_small_sel);
                vh.ivStar3.setBackgroundResource(R.drawable.doctor_star_small);
                vh.ivStar4.setBackgroundResource(R.drawable.doctor_star_small);
                vh.ivStar5.setBackgroundResource(R.drawable.doctor_star_small);
            }else {
                vh.ivStar1.setBackgroundResource(R.drawable.doctor_star_small_sel);
                vh.ivStar2.setBackgroundResource(R.drawable.doctor_star_small);
                vh.ivStar3.setBackgroundResource(R.drawable.doctor_star_small);
                vh.ivStar4.setBackgroundResource(R.drawable.doctor_star_small);
                vh.ivStar5.setBackgroundResource(R.drawable.doctor_star_small);
            }

        return convertView;
    }


    class EvaluationViewHolder{
      public TextView tvPhone,tvDesc,tvDate;
      public ImageView ivStar1,ivStar2,ivStar3,ivStar4,ivStar5;

      public EvaluationViewHolder(View view){
          tvPhone=view.findViewById(R.id.tv_userphone);
          tvDate=view.findViewById(R.id.tv_date);
          tvDesc=view.findViewById(R.id.tv_desc);
          ivStar1=view.findViewById(R.id.iv_star1);
          ivStar2=view.findViewById(R.id.iv_star2);
          ivStar3=view.findViewById(R.id.iv_star3);
          ivStar4=view.findViewById(R.id.iv_star4);
          ivStar5=view.findViewById(R.id.iv_star5);
      }

    }
}
