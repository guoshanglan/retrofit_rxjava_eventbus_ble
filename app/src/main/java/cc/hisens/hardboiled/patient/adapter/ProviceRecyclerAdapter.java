package cc.hisens.hardboiled.patient.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.ui.activity.selectcity.model.SelectCityModel;


//选择省份
public class ProviceRecyclerAdapter extends RecyclerView.Adapter<ProviceRecyclerAdapter.ProviceViewHolder> {
    public List<SelectCityModel.DatasBean>list;
    public onItemClick onItemClick;


    public ProviceRecyclerAdapter(List<SelectCityModel.DatasBean> list) {
        this.list = list;
    }

    @Override
    public ProviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.select_city_item1,parent,false);
        ProviceViewHolder holder=new ProviceViewHolder(view);
        return holder;



    }

    @Override
    public void onBindViewHolder(ProviceViewHolder holder, int position) {

        SelectCityModel.DatasBean provice=list.get(position);
        holder.tvProvice.setText(provice.getProvince());
        if (provice.isClick()){
            holder.tvProvice.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.tvProvice.setTextColor(Color.parseColor("#000000"));
        }else{
            holder.tvProvice.setBackgroundColor(Color.parseColor("#f3f3f3"));
            holder.tvProvice.setTextColor(Color.parseColor("#999999"));
        }

        holder.tvProvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onProviceItemClick(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }


    public void OnProItemClick(onItemClick onItemClick){
        this.onItemClick=onItemClick;
    }

    class  ProviceViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProvice;

        public ProviceViewHolder(View itemView) {
            super(itemView);
            tvProvice=itemView.findViewById(R.id.tv_provice);
        }
    }

    public interface onItemClick{
         void  onProviceItemClick(int positon);

    }


}
