package com.example.beikeapp.Util;

/**
 * Created by SugarSugar on 2018/3/21.
 */
import java.util.List;

/*
 * 回调接口
 */
public interface AsyncResponse {
    void onDataReceivedSuccess(List<String> listData);
    void onDataReceivedFailed();
}