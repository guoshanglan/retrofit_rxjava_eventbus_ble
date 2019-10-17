package cc.hisens.hardboiled.patient.retrofit;

public class Url {
    //设置默认超时时间
    public static final int DEFAULT_TIME = 10;

    public static String BaseUrl = "https://www.hiensor.net:10443";   //部署到服务器上的正式地址

    //public static String BaseUrl = "http://10.0.0.200:8080";  //测试地址

    //患者用户登录接口
    public static String paientLogin = "/api/v1/user/login";

    //退出登录
    public static final String LoginOut = "/api/v1/user/logout";

    //患者User获取手机验证码
    public static String getVerificationCode = "/api/v1/user/phone_code";

    //上传APP新版本信息
    public static String uploadVersion = "/api/v1/device/version";

    //检查APP版本是否有更新
    public static String getAppInfo = "/api/v1/device/version";

    //获取ed文件
    public static String getEdFile = "/api/v1/inspection/ed_algo_file";

    //上传ed记录
    public static String UpLoadEdRecord = "/api/v1/inspection/ed_record";

    //上传ehs评分记录
    public static String uploadEhsScore = "/api/v1/inspection/ehs";

    //上传IIEF_5评分记录
    public static String uploadIIEFScore = "/api/v1/inspection/iief5";

    //获取ed记录
    public static String getEdRecord = "/api/v1/inspection/ed_record";

    //获取ED颗粒值记录
    public static String GetEdInfoRecord = "/api/v1/inspection/ed_record_list";

    //获取ehs评分记录
    public static String getEhs = "/api/v1/inspection/ehs";

    //获取IIEF_5评分记录
    public static String getIIEF_5 = "/api/v1/inspection/iief5";

    //根据条件查询医生
    public static String SearchDoctor = "/api/v1/doctor/list_query";

    //关注医生
    public static String FollowedDocotor = "/api/v1/doctor/follow";

    //查询医生关注状态
    public static final String FolledState = "/api/v1/doctor/follow_status";

    //获取用户关注列表
    public static final String FolledList = "/api/v1/doctor/follow_list";

    //获取聊天记录
    public static final String chatHistory = "/api/v1/message/record";

    //获取用户历史问诊
    public static final String getConsultionHistory = "/api/v1/doctor/chat_trails";

    //websocket连接地址
    public static final String WEB_SOCKET_URL = "wss://www.hiensor.net:10443/api/v1/ws";

    //获取城市列表
    public static final String City = "/api/v1/doctor/cities";

    //获取评论列表
    public static final String Pinlun = "/api/v1/doctor/score_list";

    //查询医生信息
    public static final String DoctorInfo = "/api/v1/doctor/infos";

    //生成支付订单
    public static final String getOrder = "/api/v1/order/wechat_pre";

    //上传监测模式
    public static final String UpLoadMonitorType = "/api/v1/inspection/ed_monitor_type";

    //获取监测模式
    public static final String GetMonitorType = "/api/v1/inspection/ed_monitor_type";

    //上传ED数据
    public static final String GetEDRecord = "/api/v1/inspection/ed_record";

    //发送首条消息
    public static final String SendFristMessage = "/api/v1/message/first";

    //获取用户账单信息
    public static final String getUserOrderInfo = "/api/v1/order/infos";

    //获取资费包信息
    public static final String getProduct_Info = "/api/v1/doctor/product_info";

    //激活服务包订单
    public static final String ActivationOrder = "/api/v1/order/fire";

    //发送文本消息
    public static final String SendTextMessages = "/api/v1/message/text";

    //发送图片信息
    public static final String SendImageMessages = "/api/v1/message/image";

    //发送音频消息
    public static final String SendAudioMessages = "/api/v1/message/audio";

    //上传用户头像
    public static final String UpLoadAvator = "/api/v1/user/avatar";

    //上传个人档案
    public static final String UpLoadUserInfo="/api/v1/inspection/cases_file";


}
