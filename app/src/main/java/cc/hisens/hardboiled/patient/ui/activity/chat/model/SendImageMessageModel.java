package cc.hisens.hardboiled.patient.ui.activity.chat.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.chat.ChatPresenter.ChatPresenter;

public class SendImageMessageModel {


    /**
     * result : 0
     * message : 请求成功
     * datas : [{"from_user_id":50006,"height":1920,"origins":"http://10.0.0.200:8083/group/hisens_main/messages/images/20190802/14/01/3/83aed7154f77a3448e626ee61a134061.png?download=0","thumbs":"http://10.0.0.200:8083/group/hisens_main/messages/images/20190802/14/01/3/b2aa8f6342d3cb23b07c25e36980ed70.png?download=0","times":1564725706,"to_user_id":511,"width":1080}]
     */

    private int result;
    private String message;
    private List<DatasBean> datas;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * from_user_id : 50006
         * height : 1920
         * origins : http://10.0.0.200:8083/group/hisens_main/messages/images/20190802/14/01/3/83aed7154f77a3448e626ee61a134061.png?download=0
         * thumbs : http://10.0.0.200:8083/group/hisens_main/messages/images/20190802/14/01/3/b2aa8f6342d3cb23b07c25e36980ed70.png?download=0
         * times : 1564725706
         * to_user_id : 511
         * width : 1080
         */

        private int from_user_id;
        private int height;
        private String origin;  //原始图路径
        private String thumb; //缩略图路径
        private long create_time;
        private int to_user_id;
        private int width;  //图片宽度
        private int current_index;  //当前使用次数
        private int expire_date;  //当前有效期时间
        private int msg_index;

        public int getMsg_index() {
            return msg_index;
        }

        public void setMsg_index(int msg_index) {
            this.msg_index = msg_index;
        }


        public int getFrom_user_id() {
            return from_user_id;
        }

        public void setFrom_user_id(int from_user_id) {
            this.from_user_id = from_user_id;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getCurrent_index() {
            return current_index;
        }

        public void setCurrent_index(int current_index) {
            this.current_index = current_index;
        }

        public int getExpire_date() {
            return expire_date;
        }

        public void setExpire_date(int expire_date) {
            this.expire_date = expire_date;
        }

        public String getOrigins() {
            return origin;
        }

        public void setOrigins(String origins) {
            this.origin = origin;
        }

        public String getThumbs() {
            return thumb;
        }

        public void setThumbs(String thumbs) {
            this.thumb = thumbs;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public int getTo_user_id() {
            return to_user_id;
        }

        public void setTo_user_id(int to_user_id) {
            this.to_user_id = to_user_id;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }


    //发送图片消息
    public void SendImagemessage2(Context context, List<LocalMedia>imageList, String userId, String key, ChatPresenter presenter){

        List<File>fileList=new ArrayList<>();
        for (int i=0;i<imageList.size();i++){
            File file=new File(imageList.get(i).getPath());
            fileList.add(file);
        }


        RequestUtils.postImageMessage2(context, Url.SendImageMessages, fileList,key,userId, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                Log.e("tupian",result.toString());
                if (result.result==0){
                    Gson gson=new Gson();
                    SendImageMessageModel messageModel=gson.fromJson(gson.toJson(result),SendImageMessageModel.class);
                    if (messageModel!=null){
                        presenter.SendImageSuccess(messageModel);
                    }else{
                        presenter.SendFaire(result.message);
                    }
                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
               presenter.SendFaire(errorMsg);
            }
        });



    }
}
