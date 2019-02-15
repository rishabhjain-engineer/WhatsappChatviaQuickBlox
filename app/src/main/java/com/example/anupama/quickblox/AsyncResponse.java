package com.example.anupama.quickblox;

import com.google.gson.JsonElement;

import org.json.JSONObject;

public class AsyncResponse {

    public ResponseStatus responseStatus ;
    public Throwable error ;
    public JsonElement data ;


    AsyncResponse(ResponseStatus responseStatus, JsonElement data ,Throwable error){
        this.responseStatus = responseStatus ;
        this.data = data ;
        this.error = error ;
    }

    public static AsyncResponse loading(){
        return new AsyncResponse(ResponseStatus.LOADING,null,null);
    }

    public static AsyncResponse success(JsonElement data){
        return new AsyncResponse(ResponseStatus.SUCCESS,data,null);
    }

    public static AsyncResponse error(Throwable error){
        return new AsyncResponse(ResponseStatus.ERROR,null,error);
    }

}
