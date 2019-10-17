package cc.hisens.hardboiled.patient.ui.fragment.doctor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.ConversationAdapter;
import cc.hisens.hardboiled.patient.base.BaseFragment;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.ChatMessage;
import cc.hisens.hardboiled.patient.db.bean.Conversation;
import cc.hisens.hardboiled.patient.db.impl.ConversationRepoImpl;
import cc.hisens.hardboiled.patient.eventbus.OnCurrentConsultion;
import cc.hisens.hardboiled.patient.eventbus.OnMessage;
import cc.hisens.hardboiled.patient.eventbus.OnMessageCome;
import cc.hisens.hardboiled.patient.ui.activity.chat.ChatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


//当前问诊
public class CurrentconsultationFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.listview_current)
    public ListView listView;
    @BindView(R.id.ly_nomessage)
    public LinearLayout lyNodata;
    public ConversationAdapter conversationAdapter;  //当前会话的adapter
    public List<Conversation> list;
    public ConversationRepoImpl conversationRepo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    //订阅的evnetbus回调是否有消息到来,需要重新查找数据库，排序
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onMessage(OnMessage message) {
        if (message != null) {
            getConversions();
           conversationAdapter.notifyDataSetChanged();
        }

    }

    //订阅的evnetbus回调是否有消息到来,需要重新查找数据库，排序
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onMessage(OnMessageCome message) {
        if (message != null) {
            getConversions();
            conversationAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    //当前会话获取焦点的时候，上面的红点需要隐藏
    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new OnCurrentConsultion(true));
    }

    //初始化数据
    private void initData() {
        list = new ArrayList<>();
        conversationAdapter = new ConversationAdapter(getActivity(), list);
        conversationRepo = new ConversationRepoImpl();
        listView.setAdapter(conversationAdapter);

        getConversions();
        listView.setOnItemClickListener(this);

    }



    @SuppressLint("CheckResult")
    public void getConversions(){
        conversationRepo.getConversations().observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Conversation>>() {
            @Override
            public void accept(List<Conversation> conversations) throws Exception {
                if (conversations!=null){
                    if (conversations.size()!=0) {
                        lyNodata.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        list.clear();
                        list.addAll(conversations);

                        conversationAdapter.notifyDataSetChanged();
                    }else{
                        lyNodata.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }

                }

            }
        });
    }






    @Override
    protected int getLayoutId() {
        return R.layout.current_fragment;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    //列表点击事件，
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("id", list.get(position).getFriendId());
        intent.putExtra("doctorname", list.get(position).getFriendName());
        intent.putExtra("headurl", list.get(position).getImageUrl());
        intent.putExtra("level", list.get(position).getLevel());
        startActivity(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
