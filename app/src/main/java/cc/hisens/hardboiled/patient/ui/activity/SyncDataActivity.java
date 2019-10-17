package cc.hisens.hardboiled.patient.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.SyncFragmentAdapter;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.eventbus.OnSyncMessage;
import cc.hisens.hardboiled.patient.ui.fragment.monitor.AvssFragment;
import cc.hisens.hardboiled.patient.ui.fragment.monitor.NptFragment;
import cc.hisens.hardboiled.patient.wideview.ContentViewPager;


public class SyncDataActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.viewpager_syncdata)
    public ContentViewPager viewPager; //左右滑动的控件
    @BindView(R.id.sync_tv1)
    public TextView tv1;
    @BindView(R.id.sync_tv2)
    public TextView tv2;
    public List<Fragment>fragmentList;
    public NptFragment nptFragment;
    public AvssFragment avssFragment;
    public SyncFragmentAdapter adapter;
    public View view;


    //订阅的evnetbus回调蓝牙事件
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDialogMessage(OnSyncMessage event) {
        if (event != null) {
             finish();
        }

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
    }

    //初始化数据
    private void initView() {
        view=View.inflate(this,R.layout.synctype_dialog,null);
        nptFragment=new NptFragment(viewPager,0);
        avssFragment=new AvssFragment(viewPager,1);
        fragmentList=new ArrayList<>();
        fragmentList.add(nptFragment);
        fragmentList.add(avssFragment);
        adapter=new SyncFragmentAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(this);

        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.resetHeight(0);

            }
        });



    }

    @Override
    protected int getLayoutId() {
        return R.layout.synctype_dialog;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        viewPager.resetHeight(position);
    }

    @Override
    public void onPageSelected(int position) {
        if (position==0){
            tv1.setBackgroundResource(R.drawable.syncdialog_selected_shape);
            tv2.setBackgroundResource(R.drawable.syncdialog_unselected_shape);
        }else if (position==1){
            tv2.setBackgroundResource(R.drawable.syncdialog_selected_shape);
            tv1.setBackgroundResource(R.drawable.syncdialog_unselected_shape);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
