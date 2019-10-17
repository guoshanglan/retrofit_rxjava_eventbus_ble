package cc.hisens.hardboiled.patient.db.bean;

import org.json.JSONException;
import org.json.JSONObject;

import cc.hisens.hardboiled.patient.Appconfig;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


//聊天消息的bean类,双方进行会话时需要用到的，不需要主键，因为所有的会话都需要存储数据库
public class ChatMessage extends RealmObject {
    //from 消息发送方,也就是用户id
    private String senderId;

    //to    消息接收方
    private String receiverId;
    @PrimaryKey
    private int msgIndex;

    //消息类型 0 文本，1图片，2语言,3表示首次发送消息
    private int messageType;

    //文本消息内容
    private String textMessage;

    //图片消息，图片url,或者语音url
    private RealmList<String> imagePath;

    //缩略图url
    private RealmList<String> thumbImagePath;

    //图片宽度
    private int imageWidth ;

    //图片高度
    private int imageHeight ;

    //语音消息，语音消息url
    private String voicePath;

    //语音时长
    private double voiceTime;

    //发送消息时间
    private long timestamp;

    //文件保存地址
    private String filePath;

   //用户姓名
    private String userName;

    //用户年龄

    private int age;

    //消息是否阅读
    private boolean isRead = false;

    //消息是否被点击
    private boolean isClickVoice=true;
    private boolean isSend=true;


    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public ChatMessage() {

    }


    public int getMsgIndex() {
        return msgIndex;
    }

    public void setMsgIndex(int msgIndex) {
        this.msgIndex = msgIndex;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public int getMessageType() {
        return messageType;
    }

    public RealmList<String> getImagePath() {
        return imagePath;
    }

    public boolean isClickVoice() {
        return isClickVoice;
    }

    public void setClickVoice(boolean clickVoice) {
        isClickVoice = clickVoice;
    }

    public void setImagePath(RealmList<String> imagePath) {
        this.imagePath = imagePath;
    }

    public RealmList<String> getThumbImagePath() {
        return thumbImagePath;
    }

    public void setThumbImagePath(RealmList<String> thumbImagePath) {
        this.thumbImagePath = thumbImagePath;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }



    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }



    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public double getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(double voiceTime) {
        this.voiceTime = voiceTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public enum MessageType {
        TEXT, PICTURE, VOICE
    }

}
