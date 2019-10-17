package cc.hisens.hardboiled.patient.ui.fragment.monitor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.Appconfig;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseFragment;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.eventbus.OnSyncMessage;
import cc.hisens.hardboiled.patient.utils.SharedUtils;
import cc.hisens.hardboiled.patient.wideview.ContentViewPager;




public class NptFragment extends Fragment {
    @BindView(R.id.tv_sure_npt)
    public TextView tvSure; //确认
    public ContentViewPager viewPager;
    public int position;


    public NptFragment(ContentViewPager viewPager, int i) {
        this.viewPager=viewPager;
        this.position=i;
    }


    @OnClick(R.id.tv_sure_npt)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_sure_npt:  //确认
                UserConfig.UserInfo.setMonitorType("NPT");
                UserConfig.UserInfo.setMonitorTag("");
                EventBus.getDefault().post(new OnSyncMessage(true));
                break;

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=View.inflate(getActivity(),R.layout.sync_npt_type,null);
        ButterKnife.bind(this, view);
        viewPager.setObjectForPosition(view,position);


        return view;
    }


}
