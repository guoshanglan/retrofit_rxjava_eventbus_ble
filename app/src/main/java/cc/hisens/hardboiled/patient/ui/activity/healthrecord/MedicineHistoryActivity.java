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
import cc.hisens.hardboiled.patient.Appconfig;
import cc.hisens.hardboiled.patient.PatientConstants;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BasePresenter;


public  class MedicineHistoryActivity extends BaseOthersTagActivity {
    @BindView(R.id.tv_back)
    public TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tf_antihypertensive_tag)
    TagFlowLayout mAntihypertensiveTag;
    @BindView(R.id.tf_antiandrogen_tag)
    TagFlowLayout mAntiandrogenTag;
    @BindView(R.id.tf_antidepressants_tag)
    TagFlowLayout mAntidepressantsTag;
    @BindView(R.id.tf_recreational_tag)
    TagFlowLayout mRecreationalTag;

    private String[] mAntihypertensive;
    private String[] mAntiandrogen;
    private String[] mAntidepressants;
    private String[] mRecreational;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_medicine_history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRecordData(RecordType.MEDICINE);
        tvTitle.setText("服药史");
        mAntihypertensive = getResources().getStringArray(R.array.a_antihypertensive_drugs);
        mAntiandrogen = getResources().getStringArray(R.array.a_antiandrogen);
        mAntidepressants = getResources().getStringArray(R.array.a_antidepressants);
        mRecreational = getResources().getStringArray(R.array.a_recreational_drug);

        setAntihypertensiveTag();
        setAntiandrogenTag();
        setAntidepressantsTag();
        setRecreationalTag();

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

    private void setAntihypertensiveTag() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        TagAdapter<String> adapter = new TagAdapter<String>(mAntihypertensive) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) inflater.inflate(R.layout.small_tv,
                        mAntihypertensiveTag, false);
                tv.setText(s);
                return tv;
            }
        };
        adapter.setSelectedList(getSelectedIndex(mAntihypertensive));
        mAntihypertensiveTag.setAdapter(adapter);
    }

    private void setAntiandrogenTag() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        TagAdapter<String> adapter = new TagAdapter<String>(mAntiandrogen) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) inflater.inflate(R.layout.small_tv,
                        mAntiandrogenTag, false);
                tv.setText(s);
                return tv;
            }
        };
        adapter.setSelectedList(getSelectedIndex(mAntiandrogen));
        mAntiandrogenTag.setAdapter(adapter);
    }

    private void setAntidepressantsTag() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        TagAdapter<String> adapter = new TagAdapter<String>(mAntidepressants) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) inflater.inflate(R.layout.small_tv,
                        mAntidepressantsTag, false);
                tv.setText(s);
                return tv;
            }
        };
        adapter.setSelectedList(getSelectedIndex(mAntidepressants));
        mAntidepressantsTag.setAdapter(adapter);
    }

    private void setRecreationalTag() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        TagAdapter<String> adapter = new TagAdapter<String>(mRecreational) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) inflater.inflate(R.layout.small_tv,
                        mRecreationalTag, false);
                tv.setText(s);
                return tv;
            }
        };
        adapter.setSelectedList(getSelectedIndex(mRecreational));
        mRecreationalTag.setAdapter(adapter);
    }

    private void getSelectedAntihypertensive() {
        Set<Integer> indexes = mAntihypertensiveTag.getSelectedList();
        for (Integer i : indexes) {
            mNameBuilder.append(mAntihypertensive[i]).append(Appconfig.COMMA);
        }
    }

    private void getSelectedAntiandrogen() {
        Set<Integer> indexes = mAntiandrogenTag.getSelectedList();
        for (Integer i : indexes) {
            mNameBuilder.append(mAntiandrogen[i]).append(Appconfig.COMMA);
        }
    }

    private void getSelectedAntidepressants() {
        Set<Integer> indexes = mAntidepressantsTag.getSelectedList();
        for (Integer i : indexes) {
            mNameBuilder.append(mAntidepressants[i]).append(Appconfig.COMMA);
        }
    }

    private void getSelectedRecreational() {
        Set<Integer> indexes = mRecreationalTag.getSelectedList();
        for (Integer i : indexes) {
            mNameBuilder.append(mRecreational[i]).append(Appconfig.COMMA);
        }
    }

    protected void saveSelectedTag() {
        getSelectedAntihypertensive();
        getSelectedAntiandrogen();
        getSelectedAntidepressants();
        getSelectedRecreational();
        getSelectedOthers();

        if (mNameBuilder.length()>0) {
            Intent intent = new Intent();
            intent.putExtra(PatientConstants.KEY_MEDICINE_NAMES,
                    mNameBuilder.subSequence(0, mNameBuilder.length() - 1));
            setResult(Activity.RESULT_OK, intent);
        } else {
            Intent intent = new Intent();
            intent.putExtra(PatientConstants.KEY_MEDICINE_NAMES,
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
