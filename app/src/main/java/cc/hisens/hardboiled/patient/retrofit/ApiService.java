package cc.hisens.hardboiled.patient.retrofit;



import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiService {

    /**
     * TODO Get请求
     */
    @GET
    Observable<BaseResponse> getUser(@Url String url,@QueryMap Map<String, Object> info); //简洁方式   直接获取所需数据

    /**
     * TODO Get请求传递数组参数请求资费包数据单独处理的接口
     */
    @GET
    Observable<BaseResponse> getPack(@Url String url, @Query("product_nos[]")  List<String>list); //简洁方式   直接获取所需数据

    /**
     * TODO Get请求传递数组参数请求医生详情数据单独处理的接口,传入多个id
     */
    @GET
    Observable<BaseResponse> getDoctortFollowed(@Url String url, @Query("user_ids[]") List<Integer>list); //简洁方式   直接获取所需数据


    /**
     * TODO POST请求
     * @parmas url  请求地址
     * @params Body  利用body进行参数封装
     * @params headsMap  请求所需要的特殊请求头map<key,value>集合，如果有特殊的header，没有的话请传控map就可以
     */
    @POST
    //多个参数
    Observable<BaseResponse> postUser(@Url String url, @Body RequestBody body, @HeaderMap Map<String,String>headsMap);



    /**
     * TODO DELETE
     */
    @DELETE
    Observable<BaseResponse> delete(@Url String url, @Body RequestBody body,@HeaderMap Map<String,String>headsMap);

    /**
     * TODO PUT
     */
    @PUT()
    Observable<BaseResponse> put(@Url String url, @Body RequestBody body,@HeaderMap Map<String,String>headsMap);

    /**
     * TODO 文件上传
     */
    //亲测可用
    @Multipart
    @POST
    Observable<BaseResponse> uploadImage(@Url String url,@HeaderMap Map<String, String> headers, @Part MultipartBody.Part file);


    /**
     * 多文件上传
     */

    @Multipart
    @POST
    Observable<BaseResponse> uploadImages(@Url String url, @Part List<MultipartBody.Part> files);

    /**
     *
     * 文件下载
     * @Streaming 这个注解必须添加，否则文件全部写入内存，文件过大会造成内存溢出
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);


}
