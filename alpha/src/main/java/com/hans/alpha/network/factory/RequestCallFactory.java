package com.hans.alpha.network.factory;

import com.hans.alpha.network.builder.GetBuilder;
import com.hans.alpha.network.builder.OkHttpRequestBuilder;
import com.hans.alpha.network.builder.OtherRequestBuilder;
import com.hans.alpha.network.builder.PostFileBuilder;
import com.hans.alpha.network.builder.PostFormBuilder;
import com.hans.alpha.network.builder.PostStringBuilder;
import com.hans.alpha.network.request.OtherRequest;

/**
 * Created by changelcai on 2016/3/17.
 * RequestCall Single Factory
 */
public class RequestCallFactory {

    public static final String TAG = "MotherShip.RequestCallFactory";

    public enum TypeBuilder {
        TYPE_POST_STRING,
        TYPE_POST_FILE,
        TYPE_POST_FORM,
        TYPE_GET,

        TYPE_OTHER_DELETE,
        TYPE_OTHER_HEADER,
        TYPE_OTHER_PATCH,
        TYPE_OTHER_PUT
    }

    public static OkHttpRequestBuilder Builder(TypeBuilder type) {
        OkHttpRequestBuilder builder = null;
        switch (type) {
            case TYPE_POST_STRING:
                builder = new PostStringBuilder();
                break;
            case TYPE_GET:
                builder = new GetBuilder();
                break;
            case TYPE_POST_FILE:
                builder = new PostFileBuilder();
                break;
            case TYPE_POST_FORM:
                builder = new PostFormBuilder();
                break;
            case TYPE_OTHER_DELETE:
                builder = new OtherRequestBuilder(OtherRequest.METHOD.DELETE);
                break;
            case TYPE_OTHER_HEADER:
                builder = new OtherRequestBuilder(OtherRequest.METHOD.HEAD);
                break;
            case TYPE_OTHER_PATCH:
                builder = new OtherRequestBuilder(OtherRequest.METHOD.PATCH);
                break;
            case TYPE_OTHER_PUT:
                builder = new OtherRequestBuilder(OtherRequest.METHOD.PUT);
                break;
        }
        return builder;
    }

}
