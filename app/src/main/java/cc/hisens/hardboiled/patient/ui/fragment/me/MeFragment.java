package cc.hisens.hardboiled.patient.ui.fragment.me;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.socks.library.KLog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.MyApplication;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.ActivityCollector;
import cc.hisens.hardboiled.patient.base.BaseFragment;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.db.impl.UserRepositoryImpl;
import cc.hisens.hardboiled.patient.eventbus.OnMessageCome;
import cc.hisens.hardboiled.patient.eventbus.OnWebviewFile;
import cc.hisens.hardboiled.patient.model.AndroidForJs;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.chat.ChatActivity;
import cc.hisens.hardboiled.patient.ui.activity.login.GetVoliatCodeActivity;
import cc.hisens.hardboiled.patient.ui.activity.main.MainActivity;
import cc.hisens.hardboiled.patient.utils.AvatarIntentUtils;
import cc.hisens.hardboiled.patient.utils.PictureSelectUtil;
import cc.hisens.hardboiled.patient.utils.SelectPhoto;
import cc.hisens.hardboiled.patient.utils.ToastUtils;
import io.reactivex.functions.Consumer;
import okhttp3.Cookie;

import static android.app.Activity.RESULT_OK;
import static com.luck.picture.lib.config.PictureConfig.CHOOSE_REQUEST;
import static com.luck.picture.lib.config.PictureConfig.REQUEST_CAMERA;

public class MeFragment extends BaseFragment implements SelectPhoto.PhotoCallback {

    @BindView(R.id.webview_me)
    public WebView webView;
    public String url = "file:///android_asset/dist/index.html";
    // public String url="http://10.0.2.49:8084";
    //5.0以下使用
    private ValueCallback<Uri> uploadMessage;
    // 5.0及以上使用
    private ValueCallback<Uri[]> uploadMessageAboveL;

    public SelectPhoto selectPhoto;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }



    //订阅的evnetbus回调是否有消息到来,需要重新查找数据库，排序
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onMessage(OnWebviewFile webviewFile) {
         if (webviewFile!=null){
             try {
                 postFile(webviewFile.path);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }

    }


    public static String fileToBase64(File file) {
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            int length = in.read(bytes);
            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return base64;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initWebview();


    }











    //初始化webview
    private void initWebview() {
        selectPhoto = new SelectPhoto(getActivity());
        selectPhoto.initCallback(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);// 支持javaScript脚本
        // 将Android里面定义的类对象AndroidJs暴露给javascript
        webView.addJavascriptInterface(new AndroidForJs(getActivity()), "AndroidJs");
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient() {
            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                uploadMessage = valueCallback;
                selectPhoto.inintDialog();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                uploadMessage = valueCallback;
                selectPhoto.inintDialog();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                selectPhoto.inintDialog();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                selectPhoto.inintDialog();
                return true;
            }
        });

        syncCookie(Url.BaseUrl);  //给服务器设置cookie，获取同步状态
        webView.loadUrl(url);


    }



    //同步cookie给webview
    public void syncCookie(String url) {
        String cookies = "";
        HashSet<String> preferences = (HashSet) MyApplication.getInstance().getSharedPreferences("cookie", MyApplication.getInstance().MODE_PRIVATE).getStringSet("cookie", null);
        if (preferences != null) {
            for (String cookie : preferences) {
                int end = cookie.indexOf(";");
                cookies = cookie.substring(0, end) + ";Path=/;";
                Log.e("OkHttp", "Adding Header: " + cookies);
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(getActivity());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();// 移除
        cookieManager.removeAllCookie();
        cookieManager.setCookie(url, cookies);// cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
        String newCookie = cookieManager.getCookie(url);
        Log.e("cookies", newCookie);

    }







    @Override
    protected int getLayoutId() {
        return R.layout.me;
    }

    @Override
    public BasePresenter getPresenter() {

        return null;
    }

    @Override
    public void onCarema(int type) {


        if (type == 1) {  //拍照
            PictureSelectUtil.OpenCamera(getActivity());
            restoreUploadMsg();
        } else if (type == 2) {  //相机
            PictureSelectUtil.OpenPicture(getActivity(), 1, false);
            restoreUploadMsg();
        } else {
            restoreUploadMsg();

        }

    }

//上传头像
    public void postFile(String path){
        initProgressDialog("正在上传..");
        RequestUtils.upImage(getActivity(), Url.UpLoadAvator, path, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                dismissProgressDialog();
                if (result.result==0){
                    webView.loadUrl("javascript:javaToJS()");    //webview调用js方法刷新头像
                }else{
                    ShowToast(result.message);
                }
                restoreUploadMsg();
            }
            @Override
            public void onFailure(Throwable e, String errorMsg) {
                dismissProgressDialog();
                ShowToast(errorMsg);
                restoreUploadMsg();
            }
        });


    }


    //重置  解决无法重复选择
    private void restoreUploadMsg() {

        if (uploadMessage != null) {
            uploadMessage.onReceiveValue(null);
            uploadMessage = null;

        } else if (uploadMessageAboveL != null) {
            uploadMessageAboveL.onReceiveValue(null);
            uploadMessageAboveL = null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
