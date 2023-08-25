package com.jssg.servicemanagersystem.base.loadmodel;

public class LoadDataModel<T> extends LoadingStatus {


    private T data;


    public LoadDataModel(){
        super();
    }


    public LoadDataModel(T data) {
        super(DataStatus.SUCCESS);
        this.data = data;
    }


    public LoadDataModel(DataStatus loadingStatus, T data) {
        super(loadingStatus);
        this.data = data;
    }

    public LoadDataModel(int code, String msg) {
        super(code, msg);
    }

    public T getData() {
        return data;
    }

}
