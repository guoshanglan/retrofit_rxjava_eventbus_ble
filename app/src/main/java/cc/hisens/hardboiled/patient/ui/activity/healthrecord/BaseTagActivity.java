package cc.hisens.hardboiled.patient.ui.activity.healthrecord;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

import cc.hisens.hardboiled.patient.Appconfig;
import cc.hisens.hardboiled.patient.PatientConstants;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;

import cc.hisens.hardboiled.patient.db.bean.HealthRecords;



public abstract class BaseTagActivity extends BaseActivity {


    @BindView(R.id.rl_input_disease)
    protected RelativeLayout mRlInputDisease;
    @BindView(R.id.et_input_disease)
    protected EditText mEtInputDisease;
    @BindView(R.id.ib_add_other_disease)
    protected ImageButton mAddOtherDisease;

    protected HealthRecords.DatasBean mHealthRecord;
    protected String mAllSelected = "";
    protected StringBuilder mNameBuilder;
    protected RecordType mRecordType;
    protected String mNoChoice = "无";



    public enum RecordType {
        DISEASE, MEDICINE, OPERATION
    }


    @OnClick(R.id.ib_add_other_disease)
    public void onClickAddOtherDisease() {
        mRlInputDisease.setVisibility(View.VISIBLE);
        mAddOtherDisease.setVisibility(View.GONE);
    }



    protected void getRecordData(RecordType type) {
        Intent intent = getIntent();
        mHealthRecord = (HealthRecords.DatasBean) intent.getSerializableExtra(PatientConstants.KEY_HEALTH_RECORD);
        switch (type) {
            case DISEASE:
                if (mHealthRecord.getDisease()!=null&&mHealthRecord.getDisease().size()>0){

                    for (String name:mHealthRecord.getDisease()){
                        mAllSelected += name + Appconfig.COMMA;
                    }
                }

                break;
            case MEDICINE:
                if (mHealthRecord.getMedicine()!=null&&mHealthRecord.getMedicine().size()>0){

                    for (String name:mHealthRecord.getMedicine()){
                        mAllSelected += name + Appconfig.COMMA;
                    }
                }

                break;
            case OPERATION:
                if (mHealthRecord.getTrauma()!=null&&mHealthRecord.getTrauma().size()>0){

                    for (String name:mHealthRecord.getTrauma()){
                        mAllSelected += name + Appconfig.COMMA;
                    }
                }

                break;
        }

        mNameBuilder = new StringBuilder();
    }

    protected Set<Integer> getSelectedIndex(String[] arrays) {
        Set<Integer> set = new HashSet<>();
        if (mAllSelected.length() > 1) {
            for (int i = 0; i < arrays.length; i++) {
                if (mAllSelected.contains(arrays[i])) {
                    mAllSelected = mAllSelected.replace(arrays[i] + Appconfig.COMMA, "");
                    set.add(i);
                }
            }
        }
        return set;
    }

    protected List<String> getOthersOptions() {
        List<String> options = new ArrayList<>();
        if (mAllSelected.length() > 1) {
            String[] temp = mAllSelected.substring(0, mAllSelected.length() - 1).split(Appconfig.COMMA);
            options.addAll(Arrays.asList(temp));
        }
        return options;
    }

    protected boolean isNameLegal(String name) {
        return !TextUtils.isEmpty(name.trim());
    }

    protected void saveSelectedTag() {

    }
}
