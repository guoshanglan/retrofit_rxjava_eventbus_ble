package cc.hisens.hardboiled.patient.ui.activity.healthrecord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.PatientConstants;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BasePresenter;

import static cc.hisens.hardboiled.patient.Appconfig.COMMA;


public class ConcomitantDiseaseActivity extends BaseOthersTagActivity {

    @BindView(R.id.tv_back)
    public TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tf_global_disease_tag)
    TagFlowLayout mGlobalDiseaseTag;
    @BindView(R.id.tf_genital_system_disease_tag)
    TagFlowLayout mGenitalSystemDisease;
    @BindView(R.id.tf_internal_secretion_disease_tag)
    TagFlowLayout mInternalSecretionTag;
    @BindView(R.id.tf_psychological_disease_tag)
    TagFlowLayout mPsychologicalDiseaseTag;

    String[] mGlobalDisease;
    String[] mGenitalDisease;
    String[] mInternalDisease;
    String[] mPsychologicalDisease;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_concomitant_disease;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRecordData(RecordType.DISEASE);
        tvTitle.setText("伴随疾病");
        mGlobalDisease = getResources().getStringArray(R.array.a_global_disease);
        mGenitalDisease = getResources().getStringArray(R.array.a_genital_system_disease);
        mInternalDisease = getResources().getStringArray(R.array.a_internal_secretion_disease);
        mPsychologicalDisease = getResources().getStringArray(R.array.a_psychological_disease);

        setGlobalDiseaseTag();
        setGenitalSystemDiseaseTag();
        setInternalSecretionTag();
        setPsychologicalDiseaseTag();

        mOthersList = getOthersOptions();
        setOtherDiseasesTag(mOthersList);
    }


    @OnClick(R.id.tv_back)
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.tv_back:
                saveSelectedTag();
                break;

        }

    }


    private void setGlobalDiseaseTag() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        TagAdapter<String> adapter = new TagAdapter<String>(mGlobalDisease) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) inflater.inflate(R.layout.small_tv,
                        mGlobalDiseaseTag, false);
                tv.setText(s);
                return tv;
            }
        };
        adapter.setSelectedList(getSelectedIndex(mGlobalDisease));
        mGlobalDiseaseTag.setAdapter(adapter);

    }

    private void setGenitalSystemDiseaseTag() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        TagAdapter<String> adapter = new TagAdapter<String>(mGenitalDisease) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) inflater.inflate(R.layout.small_tv,
                        mGenitalSystemDisease, false);
                tv.setText(s);
                return tv;
            }
        };
        adapter.setSelectedList(getSelectedIndex(mGenitalDisease));
        mGenitalSystemDisease.setAdapter(adapter);
    }

    private void setInternalSecretionTag() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        TagAdapter<String> adapter = new TagAdapter<String>(mInternalDisease) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) inflater.inflate(R.layout.small_tv,
                        mInternalSecretionTag, false);
                tv.setText(s);
                return tv;
            }
        };
        adapter.setSelectedList(getSelectedIndex(mInternalDisease));
        mInternalSecretionTag.setAdapter(adapter);

    }

    private void setPsychologicalDiseaseTag() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        TagAdapter<String> adapter = new TagAdapter<String>(mPsychologicalDisease) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) inflater.inflate(R.layout.small_tv,
                        mPsychologicalDiseaseTag, false);
                tv.setText(s);
                return tv;
            }
        };
        adapter.setSelectedList(getSelectedIndex(mPsychologicalDisease));
        mPsychologicalDiseaseTag.setAdapter(adapter);

    }



    private void getSelectedGlobal() {
        Set<Integer> selected = mGlobalDiseaseTag.getSelectedList();
        for (Integer i : selected) {
            mNameBuilder.append(mGlobalDisease[i]).append(COMMA);
        }
    }

    private void getSelectedGenital() {
        Set<Integer> selected = mGenitalSystemDisease.getSelectedList();
        for (Integer i : selected) {
            mNameBuilder.append(mGenitalDisease[i]).append(COMMA);
        }
    }

    private void getSelectedInternal() {
        Set<Integer> selected = mInternalSecretionTag.getSelectedList();
        for (Integer i : selected) {
            mNameBuilder.append(mInternalDisease[i]).append(COMMA);
        }
    }

    private void getSelectedPsy() {
        Set<Integer> selected = mPsychologicalDiseaseTag.getSelectedList();
        for (Integer i : selected) {
            mNameBuilder.append(mPsychologicalDisease[i]).append(COMMA);
        }
    }

    @Override
    protected void saveSelectedTag() {
        getSelectedGlobal();
        getSelectedGenital();
        getSelectedInternal();
        getSelectedPsy();
        getSelectedOthers();

        if (mNameBuilder.length() > 0) {
            Intent intent = new Intent();
            intent.putExtra(PatientConstants.KEY_DISEASE_NAMES,
                    mNameBuilder.subSequence(0, mNameBuilder.length() - 1));
            setResult(Activity.RESULT_OK, intent);
        } else {
            Intent intent = new Intent();
            intent.putExtra(PatientConstants.KEY_DISEASE_NAMES,
                    mNoChoice);
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
