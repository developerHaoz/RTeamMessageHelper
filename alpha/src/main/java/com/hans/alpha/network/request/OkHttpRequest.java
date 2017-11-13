package com.hans.alpha.network.request;


import com.hans.alpha.network.RequestCall;
import com.hans.alpha.network.callback.SceneCallback;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhy on 15/11/6.
 * Modify by changelcai on 2016/03/17
 * <p/>
 * Changed by Hans on 16/8/5
 * 1.添加{@link OkHttpRequest#buildRequest()}无参方法
 * 2.添加{@link OkHttpRequest#buildRequest(SceneCallback)}添加null判断
 */
public abstract class OkHttpRequest {
    protected String url;
    protected Object tag;
    protected Map<String, String> params;
    protected Map<String, String> headers;

    protected Request.Builder builder = new Request.Builder();

    protected OkHttpRequest(String url, Object tag,
                            Map<String, String> params, Map<String, String> headers) {
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;

        if (url == null) {
            throw new NullPointerException("url can not be null.");
        }

        initBuilder();
    }

    /**
     * 初始化一些基本参数 url , tag , headers
     */
    private void initBuilder() {
        builder.url(url).tag(tag);
        appendHeaders();
    }

    protected abstract RequestBody buildRequestBody();

    protected RequestBody wrapRequestBody(RequestBody requestBody, SceneCallback callback) {
        return requestBody;
    }

    protected abstract Request generateRequest(RequestBody requestBody);

    public RequestCall build() {
        return new RequestCall(this);
    }


    public Request buildRequest(SceneCallback callback) {
        RequestBody requestBody = buildRequestBody();
        RequestBody wrappedRequestBody = requestBody;
        if (callback != null)
            wrappedRequestBody = wrapRequestBody(requestBody, callback);
        Request request = generateRequest(wrappedRequestBody);
        return request;
    }

    public Request buildRequest() {
        return buildRequest(null);
    }


    protected void appendHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;

        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

}
