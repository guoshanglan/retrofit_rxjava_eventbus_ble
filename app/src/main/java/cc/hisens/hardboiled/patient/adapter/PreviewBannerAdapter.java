package cc.hisens.hardboiled.patient.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.bumptech.glide.Glide;
import com.luck.picture.lib.photoview.PhotoView;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import cc.hisens.hardboiled.patient.R;

/**
 * Created by Administrator on 2018/8/31.
 */

public class PreviewBannerAdapter extends PagerAdapter {

    private List<String> bannerList;
    private Context mContext;


    public PreviewBannerAdapter(Context context, List<String> bannerList) {
        this.bannerList = bannerList;
        this.mContext = context;

    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return bannerList == null ? 0 : bannerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View layoutview = LayoutInflater.from(mContext).inflate(R.layout.preivew_head, null);
        PhotoView image = (PhotoView) layoutview.findViewById(R.id.iv_preview_head);

        Glide.with(mContext).load(bannerList.get(position)).into(image);
        ((ViewPager) view).addView(layoutview, 0);


        return layoutview;
    }
}
