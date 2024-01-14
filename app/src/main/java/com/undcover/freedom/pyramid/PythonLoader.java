package com.undcover.freedom.pyramid;

import android.app.Application;
import android.util.Base64;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.tvbox.osc.util.TxtSubscribe;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PythonLoader {
    private static volatile PythonLoader sInstance;
    private static ConcurrentHashMap<String, Spider> spiders = new ConcurrentHashMap<>();
    private Python pyInstance;
    private PyObject pyApp;
    private String cache;
    private int port = -1;
    private Application app;

    public PythonLoader() {
    }

    public static PythonLoader getInstance() {
        if (sInstance == null) {
            synchronized (PythonLoader.class) {
                if (sInstance == null) {
                    sInstance = new PythonLoader();
                }
            }
        }
        return sInstance;
    }

    public void load() {
        spiders.clear();
    }

    public PythonLoader setApplication(Application app) {
        this.app = app;
        cache = app.getExternalCacheDir().getAbsolutePath() + "/pycache/";
        PyLog.getInstance().setLogLevel(5).setFilter(PyLog.FILTER_NW | PyLog.FILTER_LC);
        PyLog.TagConstant.TAG_APP = "PythonLoader";
        if (pyInstance == null) {
            if (!Python.isStarted()) {
                Python.start(new AndroidPlatform(app));
            }
            pyInstance = Python.getInstance();
            pyApp = pyInstance.getModule("app");
        }
        return this;
    }

    public Spider getSpider(String key, String url, String ext) throws Exception {
        if (app == null) throw new Exception("set application first");
        if (spiders.containsKey(key)) {
            PyLog.d(key + " :缓存加载成功！");
            return spiders.get(key);
        }
        try {
            Spider sp = new PythonSpider(pyApp, key, cache, ext);
            sp.init(app, url);
            spiders.put(key, sp);
            return sp;
        } catch (Throwable th) {
            PyLog.e(th.toString());
        }
        return new Spider() {
        };
    }

    public Object[] proxyLocal(String key, String url, Map<String, String> map) {
        String what = map.get("do");
        try {
            if (what.equals("ck")) {
                return new Object[]{200, "text/plain; charset=utf-8", new ByteArrayInputStream("ok".getBytes("UTF-8"))};
            }
            if (what.equals("live")) {
                String type = map.get("type");
                if (type.equals("txt")) {
                    String ext = map.get("ext");
                    ext = new String(Base64.decode(ext, Base64.DEFAULT | Base64.URL_SAFE | Base64.NO_WRAP), "UTF-8");
                    return TxtSubscribe.load(ext);
                }
                return null;
            }

            PythonSpider spider = (PythonSpider) getSpider(key, url, "");
            return spider.proxyLocal(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object[]{};
    }

    public void getPort() {
        if (port <= 0) {
            for (int i = 2691; i < 10000; i++) {
                if (OkHttp.string("http://127.0.0.1:" + i + "/proxy?do=ck&api=python").equals("ok")) {
                    port = i;
                    return;
                }
            }
        }
    }

    public String localProxyUrl() {
        getPort();
        return "http://127.0.0.1:" + port + "/proxy";
    }

}
