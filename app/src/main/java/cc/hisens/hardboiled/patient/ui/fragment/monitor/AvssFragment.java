package cc.hisens.hardboiled.patient.ui.fragment.monitor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.Appconfig;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.SyncFragmentAdapter;
import cc.hisens.hardboiled.patient.base.BaseFragment;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.eventbus.OnSyncMessage;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.utils.SharedUtils;
import cc.hisens.hardboiled.patient.wideview.ContentViewPager;
import cc.hisens.hardboiled.patient.wideview.DefaultTagLayout;

public class AvssFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.tv_sure)
    TextView tvSure;  //确认提交
    @BindView(R.id.checkbox1)
    CheckBox cb1;
    @BindView(R.id.checkbox2)
    CheckBox cb2;
    @BindView(R.id.checkbox3)
    CheckBox cb3;
    @BindView(R.id.checkbox4)
    CheckBox cb4;
    @BindView(R.id.checkbox5)
    CheckBox cb5;
    @BindView(R.id.checkbox6)
    CheckBox cb6;
    public ContentViewPager viewPager;
    public int position;
    public StringBuilder builder;  //选择的tag
    public List<String>taglist;

    public AvssFragment(ContentViewPager viewPager, int i) {
        this.viewPager=viewPager;
        this.position=i;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=View.inflate(getActivity(),R.layout.sync_avss_type,null);

        ButterKnife.bind(this, view);

        viewPager.setObjectForPosition(view,position);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

          initView();

    }

    private void initView() {
        builder=new StringBuilder();
        taglist=new ArrayList<>();
        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
        cb3.setOnCheckedChangeListener(this);
        cb4.setOnCheckedChangeListener(this);
        cb5.setOnCheckedChangeListener(this);
        cb6.setOnCheckedChangeListener(this);

    }


    @OnClick(R.id.tv_sure)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_sure:
                if (taglist.size()>0) {
                    UserConfig.UserInfo.setMonitorType("AVSS");
                    String tag="";
                    for (int i=0;i<taglist.size();i++){
                        if (i!=taglist.size()-1){
                            tag+=taglist.get(i)+"、";
                        }else{
                            tag+=taglist.get(i);
                        }
                    }
                    UserConfig.UserInfo.setMonitorTag(tag);
                    EventBus.getDefault().post(new OnSyncMessage(true));
                }
                break;



        }

    }






    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.checkbox1:
                getTag(isChecked,cb1.getText().toString());
                break;
            case R.id.checkbox2:
                getTag(isChecked,cb2.getText().toString());
                break;
            case R.id.checkbox3:
                getTag(isChecked,cb3.getText().toString());
                break;
            case R.id.checkbox4:
                getTag(isChecked,cb4.getText().toString());
                break;
            case R.id.checkbox5:
                getTag(isChecked,cb5.getText().toString());
                break;
            case R.id.checkbox6:
                getTag(isChecked,cb6.getText().toString());
                break;




        }
    }


    //获取标签tag
    public void getTag(boolean isCheck,String tag){
        if (isCheck){
            if (!taglist.contains(tag))
            taglist.add(tag);
        }else{
            if (taglist.contains(tag))
                taglist.remove(tag);
        }

        if (taglist.size()>0){
            tvSure.setBackgroundResource(R.drawable.btn_getverification_code_input_shape);
        }else{
            tvSure.setBackgroundResource(R.drawable.btn_getverification_code_uninput_shape);
        }



    }

}
