package com.hans.alpha.network.core;

import com.hans.alpha.utils.io.HttpsUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

/**
 * Created by changelcai on 2016/3/12.
 * HTTP Core
 * <p/>
 * Changed by Hans on 2016/7/29 添加executor方法 允许配置OkHttp的内部线程池
 */
public class OkHttpCore {

    private OkHttpClient mClient = null;
    private final static int DEFAULT_TIMEOUT = 4_000;
    private final static int DEFAULT_MAX_CACHED_SIZE = 5 * 1024 * 1024;

    private GzipRequestInterceptor gzipRequestInterceptor = new GzipRequestInterceptor();

    public OkHttpClient getClient() {
        return mClient;
    }

    private OkHttpCore() {
        mClient = new OkHttpClient();
    }

    private OkHttpCore(int maxCacheSize, String cachePath, long timeOut, long readTimeout
            , long writeTimeout, HostnameVerifier hostNameVerifier, SSLSocketFactory sslSocketFactory
            , CookieJar cookieJar, List<Interceptor> networkInterceptors, List<Interceptor> interceptors
            , boolean isGzip, ExecutorService executor) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (null != sslSocketFactory) {
            clientBuilder.sslSocketFactory(sslSocketFactory);
        }

        if (null != hostNameVerifier) {
            clientBuilder.hostnameVerifier(hostNameVerifier);
        }

        if (null != cookieJar) {
            clientBuilder.cookieJar(cookieJar);
        }

        if (null != cachePath) {
            clientBuilder.cache(new Cache(new File(cachePath), 0 == maxCacheSize ? DEFAULT_MAX_CACHED_SIZE : maxCacheSize));
        }

        if (isGzip) {
            if (!clientBuilder.interceptors().contains(gzipRequestInterceptor)) {
                clientBuilder.addInterceptor(new GzipRequestInterceptor());
            }
        }

        if (networkInterceptors != null && !networkInterceptors.isEmpty()) {
            clientBuilder.networkInterceptors().addAll(networkInterceptors);
        }

        if (interceptors != null && !interceptors.isEmpty()) {
            clientBuilder.interceptors().addAll(interceptors);
        }
        if (executor != null) {
            clientBuilder.dispatcher(new Dispatcher(executor));
        }
        clientBuilder.connectTimeout(timeOut, TimeUnit.MILLISECONDS);
        clientBuilder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        clientBuilder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        mClient = clientBuilder.build();

    }

    private static OkHttpCore INSTANCE = null;

    public static OkHttpCore INSTANCE() {
        if (null == INSTANCE) {
            synchronized (OkHttpCore.class) {
                if (null == INSTANCE) {
                    INSTANCE = initDefault();
                }
            }
        }
        return INSTANCE;
    }

    public static OkHttpCore initDefault() {
        return new OkHttpCore();
    }

    static class GzipRequestInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
                return chain.proceed(originalRequest);
            }

            Request compressedRequest = originalRequest.newBuilder()
                    .header("Accept-Encoding", "gzip")
                    .method(originalRequest.method(), gzip(originalRequest.body()))
                    .build();
            return chain.proceed(compressedRequest);
        }

        private RequestBody gzip(final RequestBody body) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return body.contentType();
                }

                @Override
                public long contentLength() {
                    return -1; // We don't know the compressed length in advance!
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                    body.writeTo(gzipSink);
                    gzipSink.close();
                }
            };
        }
    }

    public static Builder getConfigBuilder() {
        return new Builder();
    }

    public static final class Builder {

        public Builder() {
        }

        public OkHttpCore build() {
            INSTANCE = new OkHttpCore(maxCachedSize, cachePath, timeout, readTimeout, writeTimeout, hostNameVerifier, sslSocketFactory, cookieJar, networkInterceptors, interceptors, isGzip, executor);
            return INSTANCE;
        }

        private int maxCachedSize = DEFAULT_MAX_CACHED_SIZE;

        public Builder maxCachedSize(int maxCachedSize) {
            this.maxCachedSize = maxCachedSize;
            return this;
        }

        private String cachePath;

        public Builder cachedDir(String cachePath) {
            this.cachePath = cachePath;
            return this;
        }

        private long timeout = DEFAULT_TIMEOUT;

        public Builder connectTimeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        private long readTimeout = DEFAULT_TIMEOUT;

        public Builder readTimeout(int timeout) {
            this.readTimeout = timeout;
            return this;
        }

        private long writeTimeout = DEFAULT_TIMEOUT;

        public Builder writeTimeout(int timeout) {
            this.writeTimeout = timeout;
            return this;
        }

        SSLSocketFactory sslSocketFactory = null;

        public Builder certificates(InputStream... certificates) {
            this.sslSocketFactory = HttpsUtil.getSslSocketFactory(certificates, null, null);
            return this;
        }

        public Builder certificates(InputStream[] certificates, InputStream bksFile, String password) {
            this.sslSocketFactory = HttpsUtil.getSslSocketFactory(certificates, bksFile, password);
            return this;
        }

        HostnameVerifier hostNameVerifier;

        public Builder hostNameVerifier(HostnameVerifier hostNameVerifier) {
            this.hostNameVerifier = hostNameVerifier;
            return this;
        }

        CookieJar cookieJar;

        public Builder cookieJar(CookieJar cookieJar) {
            this.cookieJar = cookieJar;
            return this;
        }


        private boolean isGzip = false;

        public Builder gzip(boolean openGzip) {
            this.isGzip = openGzip;
            return this;
        }


        private List<Interceptor> interceptors;

        /**
         * 拦截器使用可参考这篇文章  <a href="http://www.tuicool.com/articles/Uf6bAnz">http://www.tuicool.com/articles/Uf6bAnz</a>
         *
         * @param interceptors
         */
        public Builder interceptors(List<Interceptor> interceptors) {
            this.interceptors = interceptors;
            return this;
        }


        private List<Interceptor> networkInterceptors;

        /**
         * 拦截器使用可参考这篇文章  <a href="http://www.tuicool.com/articles/Uf6bAnz">http://www.tuicool.com/articles/Uf6bAnz</a>
         *
         * @param networkInterceptors
         */
        public Builder networkInterceptors(List<Interceptor> networkInterceptors) {
            this.networkInterceptors = networkInterceptors;
            return this;
        }


        private ExecutorService executor;

        /**
         * 配置OkHttp内部的执行线程池
         *
         * @param executor
         * @return
         */
        public Builder executor(ExecutorService executor) {
            this.executor = executor;
            return this;
        }

    }
}
