package com.jssg.servicemanagersystem.base.http.observer.error;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

public class ExceptionEngin {


    //对应HTTP的状态码
    public static final int UNAUTHORIZED = 401; // 登录信息过期
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVER_ERROR = 512;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;

    public static final int ERROR_CODE_ORDER_REJECTED=10090;

    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;
    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;
    /**
     * 网络错误
     */
    public static final int SSL_ERROR = 1004;
    /**
     * 协议出错
     */
    public static final int HTTP_ERROR = 1003;

    /**
     * 超时错误
     */
    public static final int HTTP_ERROR_TIME_OUT = 1008;

    /**
     * 没有相关权限
     */
    public static final int ERROR_NO_PREMESSION = 1005;


    /**
     * @param e
     * @return
     * @link {https://www.iteye.com/blog/zheyiw-1560711}
     */
    public static ApiException handleException(Throwable e) {
        if (e != null) {
            e.printStackTrace();
        }
        ApiException ex;

        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, httpException.code());
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    ex.setDisplayMessage("当前登录信息过期,请重新登录");
                    break;
                case NOT_FOUND:
                    ex.setDisplayMessage("请求出错");
                    break;
                case FORBIDDEN:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case SERVER_ERROR:
                case SERVICE_UNAVAILABLE:
                case REQUEST_TIMEOUT:
                default:
                    //均视为网络错误
                    ex.setDisplayMessage("网络错误");
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            /***服务器返回的业务类型的错误**/
            ServerException resultException = (ServerException) e;
            ex = new ApiException(resultException, resultException.getCode());
            ex.setDisplayMessage(resultException.getMsg());
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(e, PARSE_ERROR);
            ex.setDisplayMessage("网络错误");            //均视为解析错误
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, HTTP_ERROR_TIME_OUT);
            ex.setDisplayMessage("网络错误");  //均视为网络错误
            return ex;
        } else if (e instanceof IllegalArgumentException) {
            ex = new ApiException(e, HTTP_ERROR_TIME_OUT);
            ex.setDisplayMessage("网络错误");  //均视为网络错误
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, HTTP_ERROR_TIME_OUT);
            ex.setDisplayMessage("网络错误");  //均视为网络错误
            return ex;
        } else if (e instanceof UnknownHostException) {
            ex = new ApiException(e, HTTP_ERROR);
            ex.setDisplayMessage("网络错误");  //均视为网络错误
            return ex;
        } else {
            ex = new ApiException(e, UNKNOWN);
            ex.setDisplayMessage(e.getMessage());          //未知错误
            return ex;
        }
    }

}
