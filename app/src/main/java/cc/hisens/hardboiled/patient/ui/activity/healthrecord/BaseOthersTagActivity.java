package cc.hisens.hardboiled.patient.ui.activity.healthrecord;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;

import static cc.hisens.hardboiled.patient.Appconfig.COMMA;


public abstract class BaseOthersTagActivity extends BaseTagActivity {

    @BindView(R.id.tf_other_diseases_tag)
    protected TagFlowLayout mOthersTag;

    protected List<String> mOthersList;
    protected TagAdapter<String> mOthersAdapter;

    @OnClick(R.id.bt_complete)
    public void onClickCompleteButton() {
        String diseaseName = mEtInputDisease.getText().toString();
        if (mOthersList == null) {
            mOthersList = new ArrayList();
        }
        if (isNameLegal(diseaseName)) {
            mOthersList.add(diseaseName);
            setOtherDiseasesTag(mOthersList);
            mRlInputDisease.setVisibility(View.GONE);
            mAddOtherDisease.setVisibility(View.VISIBLE);
        } else {

        }

    }



    protected void setOtherDiseasesTag(List<String> nameList) {
        final LayoutInflater inflater = LayoutInflater.from(this);
        mOthersTag.setVisibility(View.VISIBLE);
        mOthersAdapter = new TagAdapter<String>(nameList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv;
                if (s.length() > 7) {
                    tv = (TextView) inflater.inflate(R.layout.medium_tv,
                            mOthersTag, false);
                } else {
                    tv = (TextView) inflater.inflate(R.layout.small_tv,
                            mOthersTag, false);
                }
                tv.setText(s);
                return tv;
            }
        };
        Set<Integer> indexSet = new HashSet<>();
        for (int i = 0; i < nameList.size(); i++) {
            indexSet.add(i);
        }
        mOthersAdapter.setSelectedList(indexSet);
        mOthersTag.setAdapter(mOthersAdapter);

    }

    protected void getSelectedOthers() {
        if (mOthersList != null) {
            for (String other : mOthersList) {
                mNameBuilder.append(other).append(COMMA);
            }
        }
    }
}
