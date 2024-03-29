package cc.hisens.hardboiled.patient.adapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SyncFragmentAdapter extends FragmentPagerAdapter {

    public List<Fragment>list;   //碎片集合


    public SyncFragmentAdapter(FragmentManager fm, List<Fragment>fragmentList) {
        super(fm);
        this.list=fragmentList;

    }

    @Override
    public Fragment getItem(int position) {

        return list.get(position);
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }


}
