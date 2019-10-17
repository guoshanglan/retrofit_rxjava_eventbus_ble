package cc.hisens.hardboiled.patient.eventbus;


//照片选择成功，我的页面回调上传头像
public class OnWebviewFile {
    public String path;

    public OnWebviewFile(String path){
        this.path=path;
    }
}
