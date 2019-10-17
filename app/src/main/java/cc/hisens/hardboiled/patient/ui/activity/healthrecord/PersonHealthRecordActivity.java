package cc.hisens.hardboiled.patient.ui.activity.healthrecord;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.PatientConstants;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.HealthRecords;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.eventbus.HealthMessage;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.utils.DateUtils;
import cc.hisens.hardboiled.patient.utils.ErrorDialog;
import cc.hisens.hardboiled.patient.utils.TimeUtils;
import cc.hisens.hardboiled.patient.wideview.LoadingPointView;
import cc.hisens.hardboiled.patient.wideview.pickerview.OptionsPickerView;
import cc.hisens.hardboiled.patient.wideview.pickerview.TimePickerView;

public class PersonHealthRecordActivity extends BaseActivity {
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_record)
    TextView tvSure;  //保存
    @BindView(R.id.et_name)
    TextView mEtName;
    @BindView(R.id.tv_age)
    TextView mTvAge;
    @BindView(R.id.tv_height)
    TextView mTvHeight;
    @BindView(R.id.tv_weight)
    TextView mTvWeight;
    @BindView(R.id.tv_marriage)
    TextView tvMarrage;  //婚姻
    @BindView(R.id.tv_shengyu)
    TextView tvShengyu; //生育情况
    @BindView(R.id.tv_sexLove)
    TextView tvSexLove; //性生活状况
    @BindView(R.id.tv_chenbo)
    TextView tvChenbo;  //晨勃状况
    @BindView(R.id.tv_boqi)
    TextView tvBoqi;  //勃起功能障碍
    @BindView(R.id.tv_sick_time)
    TextView tvSickTime; //病程持续时间
    @BindView(R.id.tv_concomitant_disease)
    TextView mTvConcomitantDisease;  //伴随疾病
    @BindView(R.id.tv_operation_history)
    TextView mTvOperationHistory;   //手术
    @BindView(R.id.tv_medicine_history)
    TextView mTvMedicineHistory;   //服药
    @BindView(R.id.tv_smoke_history)
    TextView tvSomkeHistory;  //吸烟史
    @BindView(R.id.tv_drunk)
    TextView tvDrunk;  //饮酒
    @BindView(R.id.id_loading_point_view)
    LoadingPointView loadingPointView;
    @BindView(R.id.ly_info)
    LinearLayout lyInfo;

    private TimePickerView mAgePickerView;
    private TimePickerView mSickTimePickerView;
    private OptionsPickerView<String> option;
    public ErrorDialog errorDialog;

    private List<String> mData = new ArrayList<>();



    private long dayOfBirth;
    private long sickTime;
    private HealthRecords.DatasBean mHealthRecords=new HealthRecords.DatasBean();
    private boolean mInitialSetup;

    public static final int REQUEST_CODE_NAME = 1;
    public static final int REQUEST_CODE_CONCOMITANT_DISEASE = 2;
    public static final int REQUEST_CODE_OPERATION_HISTORY = 3;
    public static final int REQUEST_CODE_MEDICINE_HISTORY = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("个人档案");
        option = new OptionsPickerView<>(this);
        errorDialog=new ErrorDialog(this);
        getHealthRecord();
    }



    @OnClick({R.id.tv_back,R.id.tv_record,R.id.et_name,R.id.tv_height,R.id.tv_age,R.id.tv_weight,R.id.tv_marriage,R.id.tv_shengyu,R.id.tv_sexLove,R.id.tv_chenbo,R.id.tv_boqi,R.id.tv_sick_time,R.id.tv_smoke_history,R.id.tv_drunk,R.id.tv_operation_history,R.id.tv_concomitant_disease,R.id.tv_medicine_history})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.tv_back:  //返回
                  updateHealthRecord();
                break;


            case R.id.et_name:
                Intent intent = new Intent(this, SetNameActivity.class);
                intent.putExtra(PatientConstants.KEY_HEALTH_RECORD, (Serializable) mHealthRecords);
                startActivityForResult(intent, REQUEST_CODE_NAME);
                break;
            case R.id.tv_age:  //年龄
                initAgePicker();
                break;
            case R.id.tv_height:  //身高
               mData.clear();
               for (int i=0;i<300;i++){
                   mData.add(String.valueOf(i));
               }
                InitOption(mData,174,"身高",mTvHeight,"cm");
                break;
            case R.id.tv_weight:   //体重
                mData.clear();
                for (int i=0;i<300;i++){
                    mData.add(i+"");
                }
                InitOption(mData,65,"体重",mTvWeight,"kg");
                break;
            case R.id.tv_marriage:  //是否婚姻
                mData.clear();
                 mData.add("未婚");
                mData.add("已婚");
                InitOption(mData,0," ",tvMarrage," ");
                break;

            case R.id.tv_shengyu:  //是否生育
                mData.clear();
                mData.add("有子女");
                mData.add("无子女");
                InitOption(mData,0," ",tvShengyu," ");

                break;

            case R.id.tv_sexLove:  //性生活状况
                mData.clear();
                mData.add("无规律");
                mData.add("有规律");
                InitOption(mData,0," ",tvSexLove," ");
                break;
            case R.id.tv_chenbo:  //是否晨勃
                mData.clear();
                mData.add("无晨勃");
                mData.add("有晨勃");
                InitOption(mData,0," ",tvChenbo," ");

                break;
            case R.id.tv_boqi:  //勃起障碍
                mData.clear();
                mData.add("无勃起");
                mData.add("有勃起");
                InitOption(mData,0," ",tvBoqi," ");
                break;
            case R.id.tv_sick_time:  //病例持续时间
                initSickTimePicker();
                break;

            case R.id.tv_smoke_history:  //吸烟史
                mData.clear();
                mData.add("从不");
                mData.add("偶尔");
                mData.add("经常");
                mData.add("每天");
                InitOption(mData,0," ",tvSomkeHistory," ");
                break;
            case R.id.tv_drunk:  //饮酒史
                mData.clear();
                mData.add("从不");
                mData.add("偶尔");
                mData.add("经常");
                mData.add("每天");
                InitOption(mData,0," ",tvDrunk," ");
                break;

            case R.id.tv_operation_history:  //手术历史
                Intent intent3 = new Intent(this, OperationHistoryActivity.class);
                intent3.putExtra(PatientConstants.KEY_HEALTH_RECORD, mHealthRecords);
                startActivityForResult(intent3, REQUEST_CODE_OPERATION_HISTORY);

                break;

            case R.id.tv_medicine_history:  //服药历史
                Intent intent4 = new Intent(this, MedicineHistoryActivity.class);
                intent4.putExtra(PatientConstants.KEY_HEALTH_RECORD, mHealthRecords);
                startActivityForResult(intent4, REQUEST_CODE_MEDICINE_HISTORY);
                break;

            case R.id.tv_concomitant_disease: //伴随疾病
                Intent intent2 = new Intent(this, ConcomitantDiseaseActivity.class);
                intent2.putExtra(PatientConstants.KEY_HEALTH_RECORD, mHealthRecords);
                startActivityForResult(intent2, REQUEST_CODE_CONCOMITANT_DISEASE);
                break;


        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_NAME:
                    String name = data.getStringExtra(PatientConstants.KEY_NAME);
                    mHealthRecords.setName(name);
                    mEtName.setText(name);
                    break;
                case REQUEST_CODE_CONCOMITANT_DISEASE:
                    String disease = data.getStringExtra(PatientConstants.KEY_DISEASE_NAMES);
                    mHealthRecords.setDisease(Arrays.asList(disease.split(",")));
                    mTvConcomitantDisease.setText(disease);
                    break;
                case REQUEST_CODE_OPERATION_HISTORY:
                    String operation = data.getStringExtra(PatientConstants.KEY_OPERATION_NAMES);

                    mHealthRecords.setTrauma(Arrays.asList(operation.split(",")));
                    mTvOperationHistory.setText(operation);
                    break;
                case REQUEST_CODE_MEDICINE_HISTORY:
                    String medicine = data.getStringExtra(PatientConstants.KEY_MEDICINE_NAMES);
                    mHealthRecords.setMedicine(Arrays.asList(medicine.split(",")));
                    mTvMedicineHistory.setText(medicine);
                    break;
            }
        }
    }



    //
    private void InitOption(List<String>list,int selected,String title,TextView textView,String end) {

        option.setTitle(title);
        option.setPicker(list);
        option.setCyclic(false);
        option.setLabels(end);
        option.setSelectOptions(selected);
        option.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                String weight = list.get(options1);
                textView.setText(weight+end);


                if (option.isShowing()){
                    option.dismiss();
                }
            }
        });
        option.show();
    }




    //病例时间
    private void initSickTimePicker() {
        //时间选择器
        mSickTimePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        try {
            mSickTimePickerView.setRange(calendar.get(Calendar.YEAR) - DateUtils.getAge(sdf.parse("1970-01")), calendar.get(Calendar.YEAR));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSickTimePickerView.setTime(calendar.getTime());
        mSickTimePickerView.setCyclic(false);
        mSickTimePickerView.setCancelable(true);
        try {
            mSickTimePickerView.setTime(new SimpleDateFormat("yyyy-MM").parse("2016-01"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //时间选择后回调
        mSickTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                sickTime = TimeUtils.toSecs(date.getTime());
                try {
                    //此处是获得的患病年月日
                    int months = DateUtils.betweenMonths(new Date(), date);
                    tvSickTime.setText(getSickTimeText(months));

                    mHealthRecords.setDuration(sickTime);
                    if (mSickTimePickerView.isShowing()){
                        mSickTimePickerView.dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mSickTimePickerView.show();
    }


    private void initAgePicker() {


        //时间选择器
        mAgePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        mAgePickerView.setRange(calendar.get(Calendar.YEAR) - 100, calendar.get(Calendar.YEAR));
        mAgePickerView.setTime(calendar.getTime());
        mAgePickerView.setCyclic(false);
        mAgePickerView.setCancelable(true);

        try {
            mAgePickerView.setTime(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //时间选择后回调
        mAgePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                dayOfBirth = TimeUtils.toSecs(date.getTime());
                if (mAgePickerView.isShowing()){
                    mAgePickerView.dismiss();
                }
                try {
                    //此处是获得的年龄
                    int age = DateUtils.getAge(date);           //由出生日期获得年龄***
                    mTvAge.setText(String.valueOf(age));

                    mHealthRecords.setBirthday(dayOfBirth);

                    KLog.i("-->> age = " + age);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        mAgePickerView.show();
    }

    private String getSickTimeText(int months) {
        StringBuilder sb = new StringBuilder();
        int years = months / 12;
        if (years > 0) {
            sb.append(years)
                    .append(getString(R.string.year));
        }
        sb.append(Math.max(1, months % 12))
                .append(getString(R.string.month_unit));
        return sb.toString();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateHealthRecord();
    }

    //上传个人档案
    private void updateHealthRecord() {
         initProgressDialog("正在上传个人信息");
        Map<String,Object>params=new HashMap<>();
        params.put("name",mEtName.getText().toString().trim());
        params.put("birthday",mHealthRecords.getBirthday());
        if (!TextUtils.isEmpty(mTvHeight.getText().toString().trim()))
        params.put("height",Float.parseFloat(mTvHeight.getText().toString().trim().substring(0,mTvHeight.length()-2)));

        if (!TextUtils.isEmpty(mTvWeight.getText().toString().trim()))
            params.put("weight",Float.parseFloat(mTvWeight.getText().toString().trim().substring(0,mTvWeight.length()-2)));

        if (!TextUtils.isEmpty(tvMarrage.getText().toString().trim())){
            if (tvMarrage.getText().toString().trim().equals("未婚")){
                params.put("marriage",0);
            }else{
                params.put("marriage",1);
            }

        }
        if (!TextUtils.isEmpty(tvShengyu.getText().toString().trim())){
            if (tvShengyu.getText().toString().trim().equals("无子女")){
                params.put("breeding",0);
            }else{
                params.put("breeding",1);
            }
        }
        if (!TextUtils.isEmpty(tvSexLove.getText().toString().trim())){
            if (tvSexLove.getText().toString().trim().equals("无规律")){
                params.put("sexual_life",0);
            }else{
                params.put("sexual_life",1);
            }

        }

        if (!TextUtils.isEmpty(tvChenbo.getText().toString().trim())){
            if (tvChenbo.getText().toString().trim().equals("无晨勃")){
                params.put("morning_wood",0);
            }else{
                params.put("morning_wood",1);
            }
        }

        if (!TextUtils.isEmpty(tvBoqi.getText().toString().trim())){
            if (tvBoqi.getText().toString().trim().equals("无勃起")){
                params.put("sexual_stimulus",0);
            }else{
                params.put("sexual_stimulus",1);
            }

        }
        if (!TextUtils.isEmpty(tvSickTime.getText().toString().trim())){
            params.put("duration",mHealthRecords.getDuration());
        }


        if (!TextUtils.isEmpty(tvSomkeHistory.getText().toString().trim())){
            if (tvSomkeHistory.getText().toString().trim().equals("从不")){
                params.put("smoke",0);
            }else if (tvSomkeHistory.getText().toString().trim().equals("偶尔")){
                params.put("smoke",1);
            }else if (tvSomkeHistory.getText().toString().trim().equals("经常")){
                params.put("smoke",2);
            }else if (tvSomkeHistory.getText().toString().trim().equals("每天")){
                params.put("smoke",3);
            }

        }
        if (!TextUtils.isEmpty(tvDrunk.getText().toString().trim())){
            if (tvDrunk.getText().toString().trim().equals("从不")){
                params.put("drink",0);
            }else if (tvDrunk.getText().toString().trim().equals("偶尔")){
                params.put("drink",1);
            }else if (tvDrunk.getText().toString().trim().equals("经常")){
                params.put("drink",2);
            }else if (tvDrunk.getText().toString().trim().equals("每天")){
                params.put("drink",3);
            }
        }
        params.put("disease",mHealthRecords.getDisease());
        params.put("trauma",mHealthRecords.getTrauma());
        params.put("medicine",mHealthRecords.getMedicine());

        RequestUtils.put(this, Url.UpLoadUserInfo, params, new HashMap<>(), new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                dismissProgressDialog();
                if (result.result==0){
                    finish();
                    EventBus.getDefault().post(new HealthMessage(true));
                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                dismissProgressDialog();
                ShowToast(errorMsg);
                finish();
            }
        });




    }


    @SuppressLint("CheckResult")
    private void getHealthRecord() {
//
     Map<String,Object>params=new HashMap<>();
     params.put("user_id", UserConfig.UserInfo.getUid());
     RequestUtils.get(this, Url.UpLoadUserInfo, params, new MyObserver<BaseResponse>() {
         @Override
         public void onSuccess(BaseResponse result) {
             if (result.result==0){
                 loadingPointView.setVisibility(View.GONE);
                 lyInfo.setVisibility(View.VISIBLE);
                 Gson gson=new Gson();
               HealthRecords  mHealthRecord=gson.fromJson(gson.toJson(result),HealthRecords.class);
                 mHealthRecords=mHealthRecord.getDatas();

                 initView(mHealthRecords);
             }
         }

         @Override
         public void onFailure(Throwable e, String errorMsg) {
             ShowToast(errorMsg);
             if(!isFinishing()) {
                 lyInfo.post(new Runnable() {
                     @Override
                     public void run() {
                         errorDialog.initErrorDialog("你所请求的数据出现网络异常，请稍后重试。。", lyInfo);
                     }
                 });

             }
         }
     });




    }

    //初始化所有界面
    private void initView(HealthRecords.DatasBean healthRecords) {
        mEtName.setText(healthRecords.getName());
        if (healthRecords.getBirthday() > 0) {
            mTvAge.setText(String.valueOf(DateUtils.getAge(
                    new Date(TimeUtils.toMillis(healthRecords.getBirthday())))));
        }

        if (healthRecords.getHeight() != 0) {
            mTvHeight.setText(String.format(Locale.getDefault(), "%d%s", healthRecords.getHeight(), getString(R.string.height_union)));
        }

        if (healthRecords.getWeight() != 0) {
            mTvWeight.setText(String.format(Locale.getDefault(), "%d%s", healthRecords.getWeight(), getString(R.string.weight_union)));
        }

        switch (healthRecords.getMarriage()) {
            case 0:
                tvMarrage.setText("未婚");
                break;
            case 1:
                tvMarrage.setText("已婚");
                break;
        }

        switch (healthRecords.getBreeding()) {
            case 0:
                tvShengyu.setText("有子女");
                break;
            case 1:
                tvShengyu.setText("无子女");
                break;
        }

        switch (healthRecords.getSexual_life()) {
            case 0:
              tvSexLove.setText("有规律");
                break;
            case 1:
               tvSexLove.setText("无规律");
                break;

        }

        switch (healthRecords.getMorning_wood()) {
            case 0:
               tvChenbo.setText("无晨勃");
                break;
            case 1:
                tvChenbo.setText("有晨勃");
                break;
        }

        switch (healthRecords.getSexual_stimulus()) {
            case 0:
                tvBoqi.setText("无勃起");
                break;
            case 1:
                tvBoqi.setText("有勃起");
                break;
        }
        if (healthRecords.getDuration() > 0) {
            int months = DateUtils.betweenMonths(new Date(),
                    new Date(TimeUtils.toMillis(healthRecords.getDuration())));
            tvSickTime.setText(getSickTimeText(months));
        }

        switch (healthRecords.getSmoke()) {
            case 0:
                tvSomkeHistory.setText("从不");
                break;
            case 1:
                tvSomkeHistory.setText("有时");
                break;
            case 2:
                tvSomkeHistory.setText("经常");
                break;
            case 3:
                tvSomkeHistory.setText("每天");
                break;
        }

        switch (healthRecords.getDrink()) {
            case 0:
                tvDrunk.setText("从不");
                break;
            case 1:
                tvDrunk.setText("有时");
                break;
            case 2:
                tvDrunk.setText("经常");
                break;
            case 3:
                tvDrunk.setText("每天");
                break;
        }

        String medicine="";
        String disease="";
        String trauma="";

         if (mHealthRecords.getMedicine()!=null) {
             for (int i = 0; i < mHealthRecords.getMedicine().size(); i++) {
                 if (i == mHealthRecords.getMedicine().size() - 1) {
                     medicine += mHealthRecords.getMedicine().get(i);
                 } else {
                     medicine += mHealthRecords.getMedicine().get(i) + ",";
                 }
             }

             mTvMedicineHistory.setText(medicine);
         }
         if (mHealthRecords.getDisease()!=null) {
             for (int i = 0; i < mHealthRecords.getDisease().size(); i++) {
                 if (i == mHealthRecords.getDisease().size() - 1) {
                     disease += mHealthRecords.getDisease().get(i);
                 } else {
                     disease += mHealthRecords.getDisease().get(i) + ",";
                 }
             }

             mTvConcomitantDisease.setText(disease);
         }
         if (mHealthRecords.getTrauma()!=null) {
             for (int i = 0; i < mHealthRecords.getTrauma().size(); i++) {
                 if (i == mHealthRecords.getTrauma().size() - 1) {
                     trauma += mHealthRecords.getTrauma().get(i);
                 } else {
                     trauma += mHealthRecords.getTrauma().get(i) + ",";
                 }
             }
             mTvOperationHistory.setText(trauma);
         }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_health_records;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
