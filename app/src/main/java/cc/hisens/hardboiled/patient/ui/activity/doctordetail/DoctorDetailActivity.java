package cc.hisens.hardboiled.patient.ui.activity.doctordetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.EvaluationAdapter;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.Doctor;
import cc.hisens.hardboiled.patient.eventbus.ChatBackEvent;
import cc.hisens.hardboiled.patient.eventbus.OnDoctorEvent;
import cc.hisens.hardboiled.patient.model.ProduceInfoModel;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.PreviewHeadActivity;
import cc.hisens.hardboiled.patient.ui.activity.chat.ChatActivity;
import cc.hisens.hardboiled.patient.ui.activity.doctor_introduce.DoctorInduceModel;
import cc.hisens.hardboiled.patient.ui.activity.doctor_introduce.DoctorIntroduceActivity;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.model.ConSultionModel;
import cc.hisens.hardboiled.patient.model.OrderInfoModel;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.model.FollowState;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.presenter.DoctorDetailPresenter;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.view.DoctorDetailView;
import cc.hisens.hardboiled.patient.ui.activity.patient_evaluation.Patient_evaluationActivity;
import cc.hisens.hardboiled.patient.utils.Consultiondialog;
import cc.hisens.hardboiled.patient.utils.ErrorDialog;
import cc.hisens.hardboiled.patient.utils.ToastUtils;
import cc.hisens.hardboiled.patient.wideview.CircleImageView;
import cc.hisens.hardboiled.patient.wideview.LoadingPointView;


//医生详情界面
public class DoctorDetailActivity extends BaseActivity implements View.OnClickListener,DoctorDetailView {
    @BindView(R.id.listview_doctor_pingjia)
    public ListView mListview;  //列表
    @BindView(R.id.tv_back)
    public TextView tvback;  //返回键
    @BindView(R.id.tv_title)
    public TextView tvTitle; //标题
    @BindView(R.id.tv_toConsultion)
    public TextView tvConsultion; //咨询
    @BindView(R.id.id_loading_point_view)
    public LoadingPointView loadingPointView; //等待控件
    @BindView(R.id.ly_doctor_detail)
    public LinearLayout lyDoctorDetail;   //医生详情主要界面
    public View headView,footView; //listview的头布局
    public TextView tvDoctorName,tvZhiwei,tvWorkPlace,tvIntroduce,tvScore,tvCount,tvFollow,tvAll;
    public CircleImageView circleImageView;//头像
    public LinearLayout lyType;
    public EvaluationAdapter adapter;
    public List<ConSultionModel.DatasBean.ListBean>list;
    public int PageIndex=0;
    public Consultiondialog consultiondialog;
    public int doctorId,score,ConsultCount,level; //医生id，医生获得分数，医生咨询次数，医生等级
    public String doctorName,workPlace,headUrl; //医生姓名，医生工作地点,医生头像
    public DoctorDetailPresenter presenter;
    public TextView tvNoData,tvPinjiaCount,tvOnePackPrice,tvMorePackPrice;
    public boolean isFollowed=false;  //是否关注医生
    public DoctorInduceModel model;  //医生基本信息model类
    public Gson gson=new Gson();
    public   OrderInfoModel orderInfoModel; //用户账单信息类
    public float onePackagePrice, morePackagePrice; //一个资费包的价格，多个资费包的价格
    public ErrorDialog errorDialog;


    //订阅的是否需要finish这个activity,从聊天页面返回
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onPay(ChatBackEvent event) {
        if (event!=null&&event.isChatBack){
            finish();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        Intent intent=getIntent();
        doctorId=intent.getIntExtra("id",0);
        list=new ArrayList<>();
       errorDialog=new ErrorDialog(this);
        adapter=new EvaluationAdapter(this,list,1);
        headView=View.inflate(this,R.layout.doctor_detail_head,null);
        footView=View.inflate(this,R.layout.doctor_detail_foot,null);
        tvDoctorName=headView.findViewById(R.id.doctor_name);
        tvZhiwei=headView.findViewById(R.id.doctor_zhiwei);
        lyType=headView.findViewById(R.id.ly_consolution_type);
        tvWorkPlace=headView.findViewById(R.id.doctor_workplace);
        tvScore=headView.findViewById(R.id.doctor_socre);
        tvIntroduce=headView.findViewById(R.id.doctor_introduce);
        tvFollow=headView.findViewById(R.id.tv_followed);
        tvCount=headView.findViewById(R.id.tv_count);
        circleImageView=headView.findViewById(R.id.doctor_head_image);
        tvAll=footView.findViewById(R.id.tv_all);
        tvNoData=footView.findViewById(R.id.tv_nomessage);
        tvPinjiaCount=headView.findViewById(R.id.pinjia_count);
        tvOnePackPrice=headView.findViewById(R.id.tv_onePackagePrice);
        tvMorePackPrice=headView.findViewById(R.id.tv_morePackagePrice);

        getData(doctorId); //获取医生个人信息

        tvIntroduce.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        tvFollow.setOnClickListener(this);
        tvback.setOnClickListener(this);
        tvConsultion.setOnClickListener(this);
        lyType.setOnClickListener(this);
        tvAll.setOnClickListener(this);
        mListview.addHeaderView(headView);
        mListview.addFooterView(footView);

        mListview.setAdapter(adapter);


    }



    //查询医生信息

    public void getData( int doctorId){

        List<Integer>params=new ArrayList<>();
        params.add(doctorId);
        RequestUtils.get3(this, Url.DoctorInfo, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result.result==0){
                    model=gson.fromJson(gson.toJson(result),DoctorInduceModel.class);
                    if (model.getDatas().size()!=0) {
                        Doctor databean = model.getDatas().get(0);
                        appLication.doctor = databean;  //讲医生赋值给全局变量，方便后面存储
                        tvDoctorName.setText(databean.getName());

                        for (int i = 0; i < databean.getWorkplaces().size(); i++) {
                            if (databean.getWorkplaces().get(i).getIndex() == 1) {
                                tvWorkPlace.setText(databean.getWorkplaces().get(i).getName());
                                workPlace = databean.getWorkplaces().get(i).getName();
                            }
                        }
                        Glide.with(DoctorDetailActivity.this).load(databean.getHead_url()).placeholder(R.drawable.doctor_head_100).into(circleImageView);
                        if (databean.getLevel() == 1) {
                            tvZhiwei.setText("主任医师");
                        } else if (databean.getLevel() == 2) {
                            tvZhiwei.setText("副主任医师");
                        } else if (databean.getLevel() == 3) {
                            tvZhiwei.setText("主治医师");
                        } else if (databean.getLevel() == 4) {
                            tvZhiwei.setText("住院医师");
                        } else if (databean.getLevel() == 5) {
                            tvZhiwei.setText("助理医师");
                        }

                        tvCount.setText(databean.getNumber() + "");
//                        if (databean.getScore() >= 80) {
//                            tvScore.setText("5.0");
//                        } else if (databean.getScore() >= 60) {
//                            tvScore.setText("4.0");
//                        } else if (databean.getScore() >= 40) {
//                            tvScore.setText("3.0");
//                        } else if (databean.getScore() >= 20) {
//                            tvScore.setText("2.0");
//                        } else if (databean.getScore() >= 0) {
//                            tvScore.setText("1.0");
//                        }
                        tvScore.setText(databean.getAve_score()+"");
                        doctorName = databean.getName();
                        level = databean.getLevel();
                        headUrl = databean.getHead_url();
                        tvConsultion.setText("向" + databean.getName() + "咨询");

                    }
                    getFollowed();  //获取关注状态

                }
            }
            @Override
            public void onFailure(Throwable e, String errorMsg) {
                dismissProgressDialog();
               ShowToast(errorMsg);


                if(!isFinishing())
                    lyDoctorDetail.post(new Runnable() {
                        @Override
                        public void run() {
                            errorDialog.initErrorDialog("你所请求的数据出现网络异常，请稍后重试。。",lyDoctorDetail);
                        }
                    });

            }
        });
    }



    //获取关注状态
  public void getFollowed(){
      Map<String,Object>params=new HashMap<>();
      params.put("doctor_id",doctorId);
        RequestUtils.get(this, Url.FolledState, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result.result==0){
                    FollowState model=gson.fromJson(gson.toJson(result),FollowState.class);
                    if (model.getDatas().isIs_follow()){
                        isFollowed=true;
                        tvFollow.setText("已关注");
                        tvFollow.setBackgroundResource(R.drawable.next_unclickable);
                        EventBus.getDefault().post(new OnDoctorEvent(true));
                    }else{
                        isFollowed=false;
                        tvFollow.setText("+ 关注");
                        tvFollow.setBackgroundResource(R.drawable.next_clickable);
                        EventBus.getDefault().post(new OnDoctorEvent(false));
                    }

                    presenter.getScoreList();   //加载评价列表
                }

            }
            @Override
            public void onFailure(Throwable e, String errorMsg) {
                dismissProgressDialog();
                ShowToast(errorMsg);
                if(!isFinishing())
                    lyDoctorDetail.post(new Runnable() {
                        @Override
                        public void run() {
                            errorDialog.initErrorDialog("你所请求的数据出现网络异常，请稍后重试。。",lyDoctorDetail);
                        }
                    });
            }
        });
  }



  //获取用户对这个医生的订单信息
  public void  getUserOrderInfo(){
      Map<String,Object>params=new HashMap<>();
      params.put("doctor_id",doctorId);
       RequestUtils.get(this, Url.getUserOrderInfo, params, new MyObserver<BaseResponse>() {
           @Override
           public void onSuccess(BaseResponse result) {
               lyDoctorDetail.setVisibility(View.VISIBLE);
               loadingPointView.setVisibility(View.GONE);

               if (result.result==0){
                   orderInfoModel=gson.fromJson(gson.toJson(result),OrderInfoModel.class);
               }
                if (appLication.doctor!=null&& !TextUtils.isEmpty(appLication.doctor.getOnce_product_no())||!TextUtils.isEmpty(appLication.doctor.getPack_product_no())) {
                    getPackInfo(appLication.doctor.getOnce_product_no(), appLication.doctor.getPack_product_no());  //获取资费包信息
                }else{

                    tvOnePackPrice.setText(onePackagePrice+"元");
                    tvMorePackPrice.setText(morePackagePrice+"元");
                    consultiondialog=new Consultiondialog(DoctorDetailActivity.this,doctorName,headUrl,doctorId,level,onePackagePrice,morePackagePrice,orderInfoModel,1);
                }
           }
           @Override
           public void onFailure(Throwable e, String errorMsg) {
               dismissProgressDialog();
               if(!isFinishing())
                   lyDoctorDetail.post(new Runnable() {
                       @Override
                       public void run() {
                           errorDialog.initErrorDialog("你所请求的数据出现网络异常，请稍后重试。。",lyDoctorDetail);
                       }
                   });
           }
       });


  }





    //调用关注医生
     public void  SetFollowed(TextView textView){
         Map<String,Object>params=new HashMap<>();
         params.put("doctor_id",doctorId);
         params.put("is_follow",isFollowed);

         RequestUtils.post(this, Url.FollowedDocotor, params, new HashMap<>(), new MyObserver<BaseResponse>() {
             @Override
             public void onSuccess(BaseResponse result) {
                 dismissProgressDialog();
                 if (result.result==0){
                     if (isFollowed){
//                         SaveDoctor(); //将医生信息存入到数据库中
                         textView.setBackgroundResource(R.drawable.next_unclickable);
                         textView.setText("已关注");
                         EventBus.getDefault().post(new OnDoctorEvent(true));
                     }else{
//                        boolean a= doctorRepo.Delete(doctorId); //从数据库中删除医生
//                         Log.e("shanchu",a+"");
                         textView.setBackgroundResource(R.drawable.next_clickable);
                         textView.setText("+ 关注");
                         EventBus.getDefault().post(new OnDoctorEvent(true));
                     }

                 }

             }

             @Override
             public void onFailure(Throwable e, String errorMsg) {
                 dismissProgressDialog();
                 if(!isFinishing())
                     lyDoctorDetail.post(new Runnable() {
                         @Override
                         public void run() {
                             errorDialog.initErrorDialog("你所请求的数据出现网络异常，请稍后重试。。",lyDoctorDetail);
                         }
                     });
             }
         });

     }


//     //获取资费包信息

public void  getPackInfo(String once_product_no,String pack_product_no){

        List<String>params=new ArrayList<>();
        if (!TextUtils.isEmpty(once_product_no))
        params.add(once_product_no);
    if (!TextUtils.isEmpty(pack_product_no))
        params.add(pack_product_no);
    RequestUtils.get2(this, Url.getProduct_Info, params, new MyObserver<BaseResponse>() {
        @Override
        public void onSuccess(BaseResponse result) {
            dismissProgressDialog();
            if (result.result==0){
                ProduceInfoModel model=gson.fromJson(gson.toJson(result),ProduceInfoModel.class);
                for (int i=0;i<model.getDatas().size();i++){
                    if (model.getDatas().get(i).getProduct_no().equals(once_product_no)){
                        onePackagePrice= (float) (model.getDatas().get(i).getFee()*0.01);
                    }else if(model.getDatas().get(i).getProduct_no().equals(pack_product_no)){
                        morePackagePrice= (float) (model.getDatas().get(i).getFee()*0.01);
                    }
                }
                 tvOnePackPrice.setText(String.format("%.2f", onePackagePrice)+"元");
                tvMorePackPrice.setText(String.format("%.2f", morePackagePrice)+"元");
                consultiondialog=new Consultiondialog(DoctorDetailActivity.this,doctorName,headUrl,doctorId,level,onePackagePrice,morePackagePrice,orderInfoModel,1);

            }
        }

        @Override
        public void onFailure(Throwable e, String errorMsg) {

            if(!isFinishing())
                lyDoctorDetail.post(new Runnable() {
                    @Override
                    public void run() {
                        errorDialog.initErrorDialog("你所请求的数据出现网络异常，请稍后重试。。",lyDoctorDetail);
                    }
                });
        }
    });



}



    @Override
    protected int getLayoutId() {
        return R.layout.doctor_detail;
    }

    @Override
    public BasePresenter getPresenter() {
        if (presenter==null){
            presenter=new DoctorDetailPresenter();
        }
        return presenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.doctor_introduce:  //跳转到医生履历界面
                Intent intent=new Intent(this, DoctorIntroduceActivity.class);
                intent.putExtra("id",doctorId);
                 startActivity(intent);
                break;
            case R.id.doctor_head_image:  //查看医生头像
                Intent intent2=new Intent(this, PreviewHeadActivity.class);
                intent2.putExtra("headurl",headUrl);
                startActivity(intent2);
                break;

            case R.id.tv_followed:  //关注,这边需要存储到数据库和删除数据库数据等操作
                  isFollowed=!isFollowed;
                  initProgressDialog("");
                  SetFollowed(tvFollow);
                break;
            case R.id.ly_consolution_type:
                if (orderInfoModel.getDatas().getUse_count()==0) {
                    if (onePackagePrice != 0 || morePackagePrice != 0) {
                        consultiondialog.inintDialog();
                    }
                }else{
                    Intent intent5 = new Intent(this, ChatActivity.class);
                    intent5.putExtra("id", doctorId+"");
                    startActivity(intent5);
                }
                break;

            case R.id.tv_toConsultion:
                if (orderInfoModel.getDatas().getUse_count()==0) {
                    if (onePackagePrice != 0 || morePackagePrice != 0) {
                        consultiondialog.inintDialog();
                    }
                }else{
                    Intent intent5 = new Intent(this, ChatActivity.class);
                    intent5.putExtra("id", doctorId+"");
                    startActivity(intent5);
                }
                break;

            case R.id.tv_all:
                Intent intent3=new Intent(this, Patient_evaluationActivity.class);
                intent3.putExtra("id",doctorId);
                startActivity(intent3);
                break;


        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public int PageIndex() {
        return PageIndex;
    }

    @Override
    public int DoctorId() {
        return doctorId;
    }


    //回调成功
    @Override
    public void Success(ConSultionModel conSultionModel) {

        if (conSultionModel!=null){
            getListData(conSultionModel);
        }
    }



    //获取评价列表数据
    private void getListData(ConSultionModel conSultionModel) {

        if (conSultionModel.getDatas()!=null&&conSultionModel.getDatas().getList()!=null){
            tvPinjiaCount.setText("("+conSultionModel.getDatas().getList().size()+")");

            if (conSultionModel.getDatas().getList().size()<=5){


                if (conSultionModel.getDatas().getList().size()==0){
                    tvAll.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                    tvPinjiaCount.setVisibility(View.GONE);

                }else{
                    list.addAll(conSultionModel.getDatas().getList());
                    tvAll.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.GONE);
                    tvPinjiaCount.setVisibility(View.VISIBLE);
                }


            }else{
                list.addAll(conSultionModel.getDatas().getList().subList(0,5));
                tvAll.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
                tvPinjiaCount.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
            getUserOrderInfo();
        }
    }


    @Override
    public void NoMoreData(String str) {
        dismissProgressDialog();
        ToastUtils.show(this,"没有更多数据了");

    }

    @Override
    public void Fail(String str) {
       dismissProgressDialog();

        if(!isFinishing())
            lyDoctorDetail.post(new Runnable() {
                @Override
                public void run() {
                    errorDialog.initErrorDialog("你所请求的数据出现网络异常，请稍后重试。。",lyDoctorDetail);
                }
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
