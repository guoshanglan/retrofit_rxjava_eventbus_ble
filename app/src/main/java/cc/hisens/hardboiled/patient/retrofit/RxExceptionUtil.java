package cc.hisens.hardboiled.patient.retrofit;


import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * 异常处理
 */
public class RxExceptionUtil {
    public static String exceptionHandler(Throwable e){
        String errorMsg = "未知错误";
        if (e instanceof UnknownHostException) {
            errorMsg = "网络不可用";
        } else if (e instanceof SocketTimeoutException) {
            errorMsg = "请求网络超时";
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            errorMsg = convertStatusCode(httpException);
        } else if (e instanceof ParseException || e instanceof JSONException
                || e instanceof JSONException) {
            errorMsg = "数据解析错误";
        }
        return errorMsg;
    }

    private static String convertStatusCode(HttpException httpException) {
        String msg;

        msg = httpException.message();
        return msg;
    }
}
