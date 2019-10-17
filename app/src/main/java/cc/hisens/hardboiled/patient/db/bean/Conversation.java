package cc.hisens.hardboiled.patient.db.bean;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


//会话双方的bean类,展示在聊天列表的

public class Conversation extends RealmObject {

    //当前会话者的id
    @PrimaryKey
    private String friendId;

    //当前会话者名字
    private String friendName;

    //当前会话者头像url
    private String imageUrl;

    private int level; //当前会话者的医生等级


    //当前会话者头像缩略图
    private String thumbUrl;

    //与会话者最后一条消息
    private ChatMessage lastMessage;

    //最后一条消息时间
    private double lastMessageTime;

    //是否阅读
    private boolean isRead=false;

    //当前会话剩余次数
    private int countMessage=5;
    //当前会话剩余时间
    private long time;




    public Conversation(String friendId, String friendName) {
        this.friendId = friendId;
        this.friendName = friendName;
    }

    //当前会话的所有消息
    public RealmList<ChatMessage> messages = new RealmList<>();

    public Conversation() {

    }



    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCountMessage() {
        return countMessage;
    }

    public void setCountMessage(int countMessage) {
        this.countMessage = countMessage;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFriendId() {

        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public double getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(double lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public RealmList<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(RealmList<ChatMessage> messages) {
        this.messages = messages;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "friendId='" + friendId + '\'' +
                ", friendName='" + friendName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", level=" + level +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", lastMessage=" + lastMessage +
                ", lastMessageTime=" + lastMessageTime +
                ", isRead=" + isRead +
                ", countMessage=" + countMessage +
                ", time=" + time +
                ", messages=" + messages +
                '}';
    }
}
