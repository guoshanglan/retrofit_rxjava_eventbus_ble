package cc.hisens.hardboiled.patient.websocket;

import java.io.Serializable;
import java.util.List;

public class MessageModel implements Serializable {


    /**
     * msg_index : 37
     * from : 50003
     * to : 512
     * type : 0
     * create_time : 1565668104
     * content : [{"id":74,"msg_index":37,"text":"hello","url":"","thumb_url":"","height":0,"width":0,"duration":0}]
     */

    private int msg_index;
    private int from;   //发送人的userid
    private int to;   //消息接收者id
    private int type;   //消息类型；0.文本消息1.图片消息2.音频消息3.首条消息图片+内容4.用户注册消息[提交用户的user id]
    private int create_time;   //消息创建时间
    private List<ContentBean> content;  //消息内容

    public int getMsg_index() {
        return msg_index;
    }

    public void setMsg_index(int msg_index) {
        this.msg_index = msg_index;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * id : 74
         * msg_index : 37
         * text : hello
         * url :
         * thumb_url :
         * height : 0
         * width : 0
         * duration : 0
         */

        private int id;
        private int msg_index;
        private String text;  //文本信息
        private String url;   //图片路径或者语音文件路径
        private String thumb_url; //缩略图
        private int height;   //图片高度
        private int width;   //图片宽度
        private int duration;  //语音文件时长

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMsg_index() {
            return msg_index;
        }

        public void setMsg_index(int msg_index) {
            this.msg_index = msg_index;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getThumb_url() {
            return thumb_url;
        }

        public void setThumb_url(String thumb_url) {
            this.thumb_url = thumb_url;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        @Override
        public String toString() {
            return "{" +
                    "id=" + id +
                    ", msg_index=" + msg_index +
                    ", text='" + text + '\'' +
                    ", url='" + url + '\'' +
                    ", thumb_url='" + thumb_url + '\'' +
                    ", height=" + height +
                    ", width=" + width +
                    ", duration=" + duration +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "msg_index=" + msg_index +
                ", from=" + from +
                ", to=" + to +
                ", type=" + type +
                ", create_time=" + create_time +
                ", content=" + content +
                '}';
    }
}
