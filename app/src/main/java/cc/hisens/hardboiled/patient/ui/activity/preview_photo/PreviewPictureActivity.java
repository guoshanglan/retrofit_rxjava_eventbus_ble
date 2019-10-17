package cc.hisens.hardboiled.patient.ui.activity.preview_photo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.PreviewBannerAdapter;
import cc.hisens.hardboiled.patient.adapter.PreviewPhotoAdapter;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;

public class PreviewPictureActivity extends BaseActivity implements PreviewPhotoAdapter.CallBack, ViewPager.OnPageChangeListener, View.OnClickListener {
    @BindView(R.id.tv_title)
    public TextView tvTitle;   //标题数量
    @BindView(R.id.tv_cancel)
    public TextView tvCancel;  //取消
    @BindView(R.id.preview_viewpager)
    public ViewPager viewPager;   //viewpager
    @BindView(R.id.recyclerview_preview)
    public RecyclerView recyclerView;  //recyclerview
    private List<String> list = new ArrayList<>();
    private List<PreviewModel>modelList=new ArrayList<>();
    private PreviewBannerAdapter previewBannerAdapter; //viewpager的适配器
    private PreviewPhotoAdapter previewPhotoAdapter; //recyclerview的适配器
    private int index; //当前选中第几个


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    //初始化数据
    private void initView() {
        Intent intent=getIntent();
        if (intent.getStringArrayListExtra("path")!=null){
            list.addAll(intent.getStringArrayListExtra("path"));
        }
        if (list.contains("add")){
            list.remove("add");
        }
        index= Integer.parseInt(intent.getStringExtra("index"));
        for (int i=0;i<list.size();i++){
            PreviewModel model=new PreviewModel();
            model.setImagePath(list.get(i));
            if (i==0){
                model.setClick(true);
            }else {
                model.setClick(false);
            }
            modelList.add(model);
        }

        previewBannerAdapter=new PreviewBannerAdapter(this,list);
        previewPhotoAdapter=new PreviewPhotoAdapter(modelList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //新加入的代码
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(previewPhotoAdapter);
        viewPager.setAdapter(previewBannerAdapter);
        previewPhotoAdapter.notifyDataSetChanged();
        previewBannerAdapter.notifyDataSetChanged();
        previewPhotoAdapter.setOnItemClick(this);
       viewPager.addOnPageChangeListener(this);
       viewPager.setCurrentItem(index);
        tvCancel.setOnClickListener(this);
        tvTitle.setText((index+1)+"/"+list.size());

    }

    @Override
    protected int getLayoutId() {
        return R.layout.preview_image2;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onItemClick(int position) {    //recyclerview的点击事件
        viewPager.setCurrentItem(position);
        for (int i=0;i<modelList.size();i++){
            if (i==position){
                modelList.get(i).setClick(true);
            }else{
                modelList.get(i).setClick(false);
            }
        }

        previewPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        recyclerView.scrollToPosition(position);
        for (int i=0;i<modelList.size();i++){
            if (i==position){
                modelList.get(i).setClick(true);
            }else{
                modelList.get(i).setClick(false);
            }
        }
        tvTitle.setText((position+1)+"/"+list.size());
        previewPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:  //返回
                  finish();
                break;
        }
    }
}
