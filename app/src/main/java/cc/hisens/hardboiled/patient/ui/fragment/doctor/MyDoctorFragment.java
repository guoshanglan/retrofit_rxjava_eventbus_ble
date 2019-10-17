package cc.hisens.hardboiled.patient.ui.fragment.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.MyDoctorAdapter;
import cc.hisens.hardboiled.patient.base.BaseFragment;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.Doctor;
import cc.hisens.hardboiled.patient.eventbus.OnDoctorEvent;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.chat.ChatActivity;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.DoctorDetailActivity;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.model.FollowModel;
import cc.hisens.hardboiled.patient.utils.ErrorDialog;
import cc.hisens.hardboiled.patient.wideview.LoadingPointView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

//我的医生
public class MyDoctorFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.stickList)
    public StickyListHeadersListView stickyListHeadersListView;
    @BindView(R.id.ly_nomessage)
    public LinearLayout lyNomessage;
    @BindView(R.id.id_loading_point_view)
    public LoadingPointView loadingPointView;
    @BindView(R.id.rl_mydoctor)
    public RelativeLayout rlMyDoctor; //我的医生
    public List<Doctor> doctorList;
    public MyDoctorAdapter myDoctorAdapter;
    public View headerCustomer; //客服header
    public ErrorDialog errorDialog;


    //订阅的evnetbus回调是否有消息到来,需要重新查找数据库，排序
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onMessage(OnDoctorEvent message) {
        if (message != null) {
            getFollowed();

        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }


    //初始化数据
    private void initData() {
        headerCustomer = View.inflate(getActivity(), R.layout.mydoctor_header, null);
        doctorList = new ArrayList<>();
        errorDialog = new ErrorDialog(getActivity());

        myDoctorAdapter = new MyDoctorAdapter(doctorList, getActivity());
        stickyListHeadersListView.setAdapter(myDoctorAdapter);
        stickyListHeadersListView.addHeaderView(headerCustomer);
        stickyListHeadersListView.setOnItemClickListener(this);
        getFollowed();


    }


    //调用网络请求查询我关注的医生接口
    public void getFollowed() {
        RequestUtils.get(getActivity(), Url.FolledList, new HashMap<>(), new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result.result == 0) {
                    loadingPointView.setVisibility(View.GONE);
                    rlMyDoctor.setVisibility(View.VISIBLE);
                    Gson gson = new Gson();
                    FollowModel model = gson.fromJson(gson.toJson(result), FollowModel.class);
                    if (model.getDatas() != null) {
                        doctorList.clear();

                        for (int i = 0; i < model.getDatas().size(); i++) {
                            Doctor doctor = model.getDatas().get(i);
                            doctor.setTitle("我的关注");
                            doctor.setTitleId("1");
                            doctorList.add(doctor);

                        }

                        myDoctorAdapter.notifyDataSetChanged();
                        UpdateUI();
                    }

                }

            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                errorDialog.initErrorDialog("你所请求的数据出现网络异常，请稍后重试。。", rlMyDoctor);
                ShowToast(errorMsg);

                UpdateUI();
            }
        });
    }


    public void UpdateUI() {
        if (doctorList.size() == 0) {
            lyNomessage.setVisibility(View.VISIBLE);
            stickyListHeadersListView.setVisibility(View.GONE);

        } else {
            lyNomessage.setVisibility(View.GONE);
            stickyListHeadersListView.setVisibility(View.VISIBLE);
            myDoctorAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.mydoctor_fragment;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    //项点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
//            Intent intent=new Intent(getActivity(), ChatActivity.class);
//            intent.putExtra("id","511");
//            intent.putExtra("headurl","123");
//            intent.putExtra("doctorname","客服");
//            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), DoctorDetailActivity.class);
            intent.putExtra("id", doctorList.get(position - 1).getDoctor_id());
            startActivity(intent);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
