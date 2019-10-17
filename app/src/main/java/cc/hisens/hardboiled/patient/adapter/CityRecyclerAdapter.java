package cc.hisens.hardboiled.patient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.ui.activity.selectcity.model.SelectCityModel;


//选择城市
public class CityRecyclerAdapter extends RecyclerView.Adapter<CityRecyclerAdapter.CityViewHolder> {
    public List<String>list;
    public onItemClick onItemClick;



    public CityRecyclerAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.select_city_item2,parent,false);
        CityViewHolder holder=new CityViewHolder(view);
        return holder;



    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {

       String city=list.get(position);
        holder.tvcity.setText(city);
        holder.tvcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onCityItemClick(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }


    public void OnItemClick(onItemClick onItemClick){
        this.onItemClick=onItemClick;
    }

    class  CityViewHolder extends RecyclerView.ViewHolder {
        public TextView tvcity;

        public CityViewHolder(View itemView) {
            super(itemView);
            tvcity=itemView.findViewById(R.id.tv_city);
        }
    }

    public interface onItemClick{
         void  onCityItemClick(int positon);

    }


}
