package cc.hisens.hardboiled.patient.ui.activity.healthrecord;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;

import butterknife.OnClick;
import cc.hisens.hardboiled.patient.PatientConstants;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.utils.ToastUtils;



public class SetNameActivity extends BaseActivity {

    @BindView(R.id.et_input_name)
    EditText mEtInputName;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_back)
    TextView tvBack;  //返回
    @BindView(R.id.tv_record)
    TextView tvSure; //保存

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("设置姓名");

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_name;
    }



    @OnClick({R.id.tv_back,R.id.tv_record})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.tv_back: //返回
                saveName(mEtInputName.getText().toString().trim());
                break;





        }
    }


    private void saveName(String name) {
        if (!TextUtils.isEmpty(name.trim())) {

            Intent intent = new Intent();
            intent.putExtra(PatientConstants.KEY_NAME, name);
            setResult(RESULT_OK, intent);

        } else {
            ToastUtils.show(this,"请输入姓名");
        }
        finish();
    }


    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
