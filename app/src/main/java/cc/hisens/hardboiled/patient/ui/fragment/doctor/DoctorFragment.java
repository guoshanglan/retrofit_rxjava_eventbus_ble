package cc.hisens.hardboiled.patient.ui.fragment.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.FragmentAdapter;
import cc.hisens.hardboiled.patient.base.BaseFragment;
import cc.hisens.hardboiled.patient.base.BasePresenter;

import cc.hisens.hardboiled.patient.eventbus.OnCurrentConsultion;
import cc.hisens.hardboiled.patient.eventbus.OnMessageCome;
import cc.hisens.hardboiled.patient.ui.activity.searchdoctor.SearchDoctorActivity;
import cc.hisens.hardboiled.patient.wideview.PagerSlidingTabStrip;

public class DoctorFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    @BindView(R.id.pageSide)
    public PagerSlidingTabStrip pagerSlidingTabStrip;  //滑动控件
    @BindView(R.id.iv_doctor_add)
    public ImageView ivAdd;   //添加医生
    @BindView(R.id.iv_red)
    public ImageView ivRed; //红点
    @BindView(R.id.viewpager_doctor)
    public ViewPager viewPager;
    public List<Fragment>fragmentList;
    public List<String>title;  //标题
    public CurrentconsultationFragment currentconsultationFragment=new CurrentconsultationFragment(); //当前咨询
    public MyDoctorFragment myDoctorFragment=new MyDoctorFragment(); //我的医生
    public FragmentAdapter fragmentAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }




    //订阅的evnetbus回调是否有消息到来
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDeviceMessage(OnMessageCome message) {
        if (message!=null){
            if (message.isMessageCome) {
                ivRed.setVisibility(View.VISIBLE);
            }else{
                ivRed.setVisibility(View.GONE);
            }
        }

    }

    //订阅的evnetbus回调是否在会话界面
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onMessageVisible(OnCurrentConsultion message) {
        if (message!=null){
            ivRed.setVisibility(View.GONE);
        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentList=new ArrayList<>();
        title=new ArrayList<>();
        fragmentAdapter=new FragmentAdapter(getChildFragmentManager(),fragmentList,title);
        fragmentList.add(currentconsultationFragment);
        fragmentList.add(myDoctorFragment);
        title.add("当前咨询");
        title.add("我的医生");
        viewPager.setAdapter(fragmentAdapter);
        pagerSlidingTabStrip.setViewPager(viewPager);
        fragmentAdapter.notifyDataSetChanged();
        pagerSlidingTabStrip.setOnPageChangeListener(this);

    }


    @OnClick(R.id.iv_doctor_add)
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.iv_doctor_add:
                   startActivity(new Intent(getActivity(), SearchDoctorActivity.class));
                break;

        }
    }



    @Override
    protected int getLayoutId() {

        return R.layout.doctor;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position==0){
            ivRed.setVisibility(View.GONE);
        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
