package com.jssg.servicemanagersystem.base.loadmodel;

public class LoadingStatus {



    //0 loading 1 success 2 error
    public DataStatus loadingStatus;
    public int code;
    public String msg;

    public LoadingStatus() {
        loadingStatus = DataStatus.LOADING;
    }

    public LoadingStatus(DataStatus loadingStatus) {
        this.loadingStatus = loadingStatus;
    }

    public LoadingStatus(int code, String msg) {
        this.loadingStatus = DataStatus.ERROR;
        this.code = code;
        this.msg = msg;
    }

    public DataStatus getLoadingStatus() {
        return loadingStatus;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isLoading(){
        return loadingStatus == DataStatus.LOADING;
    }

    public boolean isSuccess(){
        return loadingStatus == DataStatus.SUCCESS;
    }

    public boolean isError(){
        return loadingStatus == DataStatus.ERROR;
    }
}
