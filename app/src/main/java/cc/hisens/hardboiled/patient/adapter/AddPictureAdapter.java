package cc.hisens.hardboiled.patient.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cc.hisens.hardboiled.patient.R;

public class AddPictureAdapter extends BaseAdapter {
    public Context mContext;
    public List<String>list;
    public CallBack callBack;


    public AddPictureAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.list = list;
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
        AddPictureViewHolder vh=null;
        if (convertView==null){
            convertView=View.inflate(mContext,R.layout.addpic_item,null);
            vh=new AddPictureViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh= (AddPictureViewHolder) convertView.getTag();
        }

        if (list.get(position).equals("add")){
            vh.ivPicture.setImageResource(R.drawable.doctor_btn_addpic);
             vh.ivDelete.setVisibility(View.GONE);
        }else{
            vh.ivDelete.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(list.get(position)).into(vh.ivPicture);
        }
       vh.ivDelete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               callBack.DeletePic(position);
           }
       });

        vh.ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.PreviewPic(position);
            }
        });


        return convertView;
    }

    class AddPictureViewHolder{
        public ImageView ivPicture,ivDelete;
        public AddPictureViewHolder(View view){
            ivPicture=view.findViewById(R.id.iv_picture);
            ivDelete=view.findViewById(R.id.iv_delete);
        }


    }


    public void setOnCallBack(CallBack callBack){
        this.callBack=callBack;
    }

    public interface CallBack{
        void DeletePic(int position);
        void PreviewPic(int position);
    }



}
