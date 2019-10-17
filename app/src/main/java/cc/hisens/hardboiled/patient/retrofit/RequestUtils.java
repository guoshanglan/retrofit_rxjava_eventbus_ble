package cc.hisens.hardboiled.patient.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.ActivityCollector;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.db.impl.UserRepositoryImpl;
import cc.hisens.hardboiled.patient.ui.activity.login.GetVoliatCodeActivity;
import cc.hisens.hardboiled.patient.utils.ScreenUtils;
import cc.hisens.hardboiled.patient.utils.SharedUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 提交参数方式
 */
public class RequestUtils {
    protected static Toast mToast;

    /**
     * Get 请求
     *
     * @param context
     */
    public static void get(Context context, String url, Map<String, Object> params, MyObserver<BaseResponse> observer) {

        RetrofitUtils.getApiUrl()
                .getUser(url, params).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {


                        observer.onSuccess(baseResponse);



                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e, RxExceptionUtil.exceptionHandler(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }




    /**
     * Get 请求传递数组参数请求资费包
     *
     * @param context
     */
    public static void get2(Context context, String url, List<String>params, MyObserver<BaseResponse> observer) {


        RetrofitUtils.getApiUrl()
                .getPack(url, params).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,  RxExceptionUtil.exceptionHandler(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * Get 请求传递数组参数请求医生个人信息
     *
     * @param context
     */
    public static void get3(Context context, String url, List<Integer>list, MyObserver<BaseResponse> observer) {


        RetrofitUtils.getApiUrl()
                .getDoctortFollowed(url, list).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,  RxExceptionUtil.exceptionHandler(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 普通Post 请求
     *
     * @param context
     * @param
     */
    public static void post(Context context, String url, Map<String, Object> params, Map<String, String> headsMap, MyObserver<BaseResponse> observer) {

        RetrofitUtils.getApiUrl()
                .postUser(url, convertMapToBody(params), headsMap).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,  RxExceptionUtil.exceptionHandler(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 普通Post 请求传递数组
     *
     * @param context
     * @param
     */

    public static void postList(Context context, String url, List<Object>list, Map<String, String> headsMap, MyObserver<BaseResponse> observer) {

        RetrofitUtils.getApiUrl()
                .postUser(url, convertListToBody(list), headsMap).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,  RxExceptionUtil.exceptionHandler(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * Post 请求  body格式为form-data,这个是用来即时通讯传递图片和语音文件的
     *
     * @param context
     * @param
     */
    public static void postImageMessage2(Context context, String url, List<File>files,String filekey, String userid,MyObserver<BaseResponse> observer) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            builder.addFormDataPart(filekey, file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }

        builder.addFormDataPart("to_user_id", userid);
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitUtils.getApiUrl().uploadImages(url, parts).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,  RxExceptionUtil.exceptionHandler(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * Post 请求  body格式为form-data,这个是用来即时通讯传递图片和语音文件的
     *
     * @param context
     * @param
     */
    public static void postVoiceMessage(Context context, String url, List<File>files,String filekey,int time, String userid,MyObserver<BaseResponse> observer) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            builder.addFormDataPart(filekey, file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }

        builder.addFormDataPart("to_user_id", userid);
        builder.addFormDataPart("dur_times", String.valueOf(time));
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitUtils.getApiUrl().uploadImages(url, parts).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,  RxExceptionUtil.exceptionHandler(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * Post 请求  body格式为form-data,这个是用来生成预支付订单的，发送首条消息
     *
     * @param context
     * @param
     */
    public static void SendFrist(Context context, String url, List<File>files, int doctid,String content,MyObserver<BaseResponse> observer) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            builder.addFormDataPart("images", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }

        builder.addFormDataPart("to_user_id", String.valueOf(doctid));

        builder.addFormDataPart("text", content);


        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitUtils.getApiUrl().uploadImages(url, parts).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {

                            observer.onSuccess(baseResponse);

                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,  RxExceptionUtil.exceptionHandler(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * Put 请求 ,这个可能是属于表单类型的提交
     *
     * @param context
     * @param
     */
    @SuppressLint("CheckResult")
    public static void put(Context context, String url, Map<String, Object> params, Map<String, String> headsMap, MyObserver<BaseResponse> observer) {
        Map<String, String> headers = new HashMap<String, String>();
        RetrofitUtils.getApiUrl()
                .put(url, convertMapToBody(params), headsMap).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,  RxExceptionUtil.exceptionHandler(e));
                    }

                    @Override
                    public void onComplete() {


                    }
                });

    }

    /**
     * Delete 请求
     *
     * @param context  上下文对象
     * @param observer 观察者
     * @param params   查询参数map集合
     * @param headsMap 请求头参数
     * @param observer 观察者
     */
    @SuppressLint("CheckResult")
    public static void delete(Context context, String url, Map<String, String> params, Map<String, String> headsMap, MyObserver<BaseResponse> observer) {
        RetrofitUtils.getApiUrl()
                .delete(url, convertMapToBody(params), headsMap).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 上传图片，文件
     *
     * @param context
     * @param observer
     */
    public static void upImage(Context context, String url, String pathName, MyObserver<BaseResponse> observer) {
        File file = new File(pathName);
        Map<String, String> header = new HashMap<String, String>();

        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        RetrofitUtils.getApiUrl().uploadImage(url, header, body).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,  RxExceptionUtil.exceptionHandler(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 上传多张图片，多份文件
     *
     * @param files
     */
    public static void upLoadImgs(Context context, String url,String Filekey,  List<File> files, MyObserver<BaseResponse> observer) {
        Map<String, String> header = new HashMap<String, String>();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart(Filekey, file.getName(), photoRequestBody);
        }


        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitUtils.getApiUrl().uploadImages(url, parts).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }




    //下载文件,url  接口地址   filename:文件名称   observer：回调
    public static void DownLoad(String url, final String filename,MyObserver<File>observer) {
        RetrofitUtils.getApiUrl().download(url).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody baseResponse) {
                        Log.e("下载文件",baseResponse.contentLength()+"");
                     observer.onSuccess(saveFile(filename,baseResponse));
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 将map数据转换为 普通的 json RequestBody上传参数给服务器
     *
     * @param map 以前的请求参数
     * @return
     */
    public static RequestBody convertMapToBody(Map<?, ?> map) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new JSONObject(map).toString());
    }

    public static RequestBody convertListToBody(List<Object> list) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(list));
    }



    /**
     * 将map数据转换为图片，文件类型的  RequestBody上传参数给服务器
     *
     * @param map 以前的请求参数
     * @return 待测试
     */
    public static RequestBody convertMapToMediaBody(Map<?, ?> map) {
        return RequestBody.create(MediaType.parse("multipart/form-data; charset=utf-8"), new JSONObject(map).toString());
    }





    //保存文件
    public static File saveFile(String fileName, ResponseBody body) {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file = null;
        try {
            if (fileName == null) {
                return null;
            }
            file = new File(Environment.getExternalStorageDirectory().getPath(),  fileName);
            if (file == null || !file.exists()) {
                file.createNewFile();
            }
            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;
            byte[] fileReader = new byte[4096];
            inputStream = body.byteStream();
            outputStream = new FileOutputStream(file);
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


    protected  static void  ShowToast(Context context,String msg){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        View toastView = LayoutInflater.from(context).inflate(R.layout.error_view, null);
        TextView tv=toastView.findViewById(R.id.txtToastMessage);
        if (msg.contains("1002")){
            tv.setText("你的账号已在其他地方登录,请重新进行登录");

        }else{
            tv.setText(msg);
        }
        if (mToast == null) {
            mToast = new Toast(context);
        }
        LinearLayout relativeLayout = (LinearLayout) toastView.findViewById(R.id.test);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(wm
                .getDefaultDisplay().getWidth(), (int) ScreenUtils.dp2px(context, 40));
        relativeLayout.setLayoutParams(layoutParams);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        mToast.setView(toastView);
        mToast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);//设置Toast可以布局到系统状态栏的下面
        mToast.show();
        if (msg.contains("1002")){
            ActivityCollector.finishAll();
            new UserRepositoryImpl().DeleteAll();
            new SharedUtils(context).writeBoolean(UserConfig.UserInfo.EXTRA_IS_LOGIN,false);
            context.startActivity(new Intent(context, GetVoliatCodeActivity.class));
        }
    }

}

