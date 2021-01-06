package com.aliyun.rtc.superclassroom.api.net;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class RequestFilterInterceptor implements Interceptor {
    static WeakHashMap<String, ResonseForClone> responseWeakHashMap = new WeakHashMap<>();
    static WeakHashMap<String, WeakReference<Call>> calls = new WeakHashMap<>();

    static Charset UTF_8 = Charset.forName("UTF-8");
    static IConfig config;
    static boolean enableFilter;
    static boolean debug;
    static Handler handler;

    /**
     * @param enableFilter 全局开关
     * @param config       配置信息
     */
    public static void config(boolean debug, boolean enableFilter, IConfig config) {
        RequestFilterInterceptor.enableFilter = enableFilter;
        RequestFilterInterceptor.config = config;
        RequestFilterInterceptor.debug = debug;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!enableFilter) {
            return chain.proceed(request);
        }

        if (config == null) {
            return chain.proceed(request);
        }
        if (!config.shouldFilter(request.url().toString())) {
            return chain.proceed(request);
        }

        String key = generateKey(request);

        return check(chain, request, key);
    }

    private Response check(Chain chain, Request request, String key) throws IOException {
        try {
            //从缓存的call和response中判断要不要等待
            boolean needwait = needwait(key);

            if (!needwait) {
                if (responseWeakHashMap.containsKey(key)) {
                    return responseWeakHashMap.get(key).getClonedResonse();
                } else {
                    //直接执行请求:
                    //将response缓存起来
                    return realExceute(chain, request, key);
                }
            } else {
                Thread.sleep(2000);
                return check(chain, request, key);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            if (responseWeakHashMap.containsKey(key)) {
                return responseWeakHashMap.get(key).getClonedResonse();
            } else {
                //直接执行请求:
                //将response缓存起来
                return realExceute(chain, request, key);
            }
        }
    }

    @NonNull
    private Response realExceute(Chain chain, Request request, final String key) throws IOException {
//        calls.put(key, new WeakReference<>(chain.call()));
        Response response = chain.proceed(request);


        if (response.isSuccessful() && response.body() != null) {
            ResponseBody responseBody = response.body();
            BufferedSource source = responseBody.source();
            source.request(responseBody.contentLength() > 0 ? responseBody.contentLength() : Integer.MAX_VALUE);
            //吓人?
            Buffer buffer = source.buffer();
            Charset charset = UTF_8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF_8);
            }
            String bodyString = buffer.clone().readString(charset);

            ResponseBody cloneBody = ResponseBody.create(response.body().contentType(), bodyString);

            Response responseClone = new Response.Builder()
                    .code(response.code())
                    .protocol(response.protocol())
                    .message(response.message())
                    .body(cloneBody)
                    .headers(response.headers())
                    .header("cachedResonse", "yes")
                    .request(request)
                    .build();
            responseWeakHashMap.put(key, new ResonseForClone(bodyString, responseClone));
            calls.remove(key);
            getMainHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //1min后移除缓存的response:
                    responseWeakHashMap.remove(key);
                    logw(config.responseCacheTimeInMills() + "时间到了,清除缓存的response");
                }
            }, config.responseCacheTimeInMills());

        }

        return response;
    }

    private static Handler getMainHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    private boolean needwait(String key) {
        if (responseWeakHashMap.containsKey(key)) {
            logw("有缓存的response,直接去读缓存,并组装新的response");
            return false;
        }
        if (calls.containsKey(key)) {
            WeakReference<Call> callWeakReference = calls.get(key);
            if (callWeakReference == null) {
                logw("不需要等待,直接发请求 call WeakReference not exist:");
                return false;
            }
            Call call = callWeakReference.get();
            if (call == null || call.isCanceled()) {
                logw("不需要等待,直接发请求 call not exist or is canceld:" + call);
                return false;
            }
            logw("请求可能正在等待或正在执行-needwait call is running:" + call);
            //请求可能正在等待或正在执行
            return true;
        }
        logw("任何地方都没有,不需要等,直接执行请求");
        //任何地方都没有,不需要等,直接执行请求
        return false;
    }

    private static void logw(String str) {
        if (debug) {
            Log.w("requestFilter", str);
        }
    }

    /**
     * @param request
     * @return
     */
    private String generateKey(Request request) {

        return config.generateCacheKey(request);
    }

    class ResonseForClone {
        String body;
        Response response;

        public ResonseForClone(String body, Response response) {
            this.body = body;
            this.response = response;
        }

        public Response getClonedResonse() {
            ResponseBody cloneBody = ResponseBody.create(response.body().contentType(), body);
            Response responseClone = new Response.Builder()
                    .code(response.code())
                    .protocol(response.protocol())
                    .message(response.message())
                    .body(cloneBody)
                    .headers(response.headers())
                    .header("cachedResonse", "yes")
                    .request(response.request())
                    .build();
            return responseClone;
        }
    }

    public interface IConfig {
        boolean shouldFilter(String url);

        String generateCacheKey(Request request);

        long responseCacheTimeInMills();
    }
}
