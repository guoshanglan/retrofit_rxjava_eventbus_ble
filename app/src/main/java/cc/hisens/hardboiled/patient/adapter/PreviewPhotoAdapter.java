package cc.hisens.hardboiled.patient.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.bumptech.glide.Glide;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.ui.activity.preview_photo.PreviewModel;

public class PreviewPhotoAdapter extends RecyclerView.Adapter<PreviewPhotoAdapter.PreviewViewHolder> {
    public List<PreviewModel> list;
    public Context mContext;
    public CallBack callBack;


    public PreviewPhotoAdapter(List<PreviewModel> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    @Override
    public PreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.previewpicture_item, parent, false);
        PreviewViewHolder holder = new PreviewViewHolder(view);
        return holder;


    }

    public void  setOnItemClick(CallBack cb){
        this.callBack=cb;
    }

    @Override
    public void onBindViewHolder(PreviewViewHolder holder, int position) {
        Glide.with(mContext).load(list.get(position).getImagePath()).into(holder.imageView);
        if (list.get(position).isClick()){
            holder.rl.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else{
            holder.rl.setBackgroundColor(Color.parseColor("#000000"));
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onItemClick(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class PreviewViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public RelativeLayout rl;

        public PreviewViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_preview);
            rl=itemView.findViewById(R.id.rl_preview);
        }
    }

    public interface  CallBack{
        void onItemClick(int position);
    }


}
