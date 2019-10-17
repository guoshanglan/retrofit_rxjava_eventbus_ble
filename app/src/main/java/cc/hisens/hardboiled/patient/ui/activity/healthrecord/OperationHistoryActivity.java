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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.Appconfig;
import cc.hisens.hardboiled.patient.PatientConstants;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BasePresenter;


public class OperationHistoryActivity extends BaseTagActivity {
    @BindView(R.id.tv_back)
    public TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tf_operation_tag)
    TagFlowLayout mOperationTag;

    private TagAdapter<String> mOperationAdapter;
    private List<String> mOperationList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_operation_history;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRecordData(RecordType.OPERATION);
        tvTitle.setText("手术/外伤史");
        String[] names = getResources().getStringArray(R.array.a_operation_injury_history);
        mOperationList = new ArrayList<>(Arrays.asList(names));

        if (mAllSelected.length() > 1) {
            Set<Integer> set = getSelectedIndex(names);
            List<String> otherOptions = getOthersOptions();
            for (int i = 0; i < otherOptions.size(); i++) {
                mOperationList.add(otherOptions.get(i));
                set.add((mOperationList.size() - 1));
            }
            setOperationTag(mOperationList, set);
        } else {
            setOperationTag(mOperationList, null);
        }
    }

    @OnClick(R.id.tv_back)
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.tv_back:
                saveSelectedTag();
                break;

        }

    }

    @OnClick(R.id.bt_complete)
    public void onClickComplete() {
        String name = mEtInputDisease.getText().toString();
        if (isNameLegal(name)) {
            mRlInputDisease.setVisibility(View.GONE);
            mAddOtherDisease.setVisibility(View.VISIBLE);
            mOperationList.add(name);

            Set<Integer> tmp = mOperationTag.getSelectedList();
            tmp.add(mOperationList.size()-1);

            setOperationTag(mOperationList, tmp);
        } else {

        }
    }

    private void setOperationTag(List operationList, Set<Integer> set) {
        final LayoutInflater inflater = LayoutInflater.from(this);
        mOperationAdapter = new TagAdapter<String>(operationList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv;
                if (s.length() > 7) {
                    tv = (TextView) inflater.inflate(R.layout.medium_tv,
                            mOperationTag, false);
                } else {
                    tv = (TextView) inflater.inflate(R.layout.small_tv,
                            mOperationTag, false);
                }

                tv.setText(s);
                return tv;
            }
        };
        if (set != null) {
            mOperationAdapter.setSelectedList(set);
        }
        mOperationTag.setAdapter(mOperationAdapter);

    }

    private void getSelectedOperation() {
        Set<Integer> indexes = mOperationTag.getSelectedList();
        for (Integer index : indexes) {
            mNameBuilder.append(mOperationList.get(index)).append(Appconfig.COMMA);
        }
    }

    @Override
    protected void saveSelectedTag() {
        getSelectedOperation();

        if (mNameBuilder.length() > 0) {
            Intent intent = new Intent();
            intent.putExtra(PatientConstants.KEY_OPERATION_NAMES,
                    mNameBuilder.subSequence(0, mNameBuilder.length() - 1));
            setResult(Activity.RESULT_OK, intent);
        } else {
            Intent intent = new Intent();
            intent.putExtra(PatientConstants.KEY_OPERATION_NAMES,
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
