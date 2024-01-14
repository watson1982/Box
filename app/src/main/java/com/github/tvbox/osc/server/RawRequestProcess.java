package com.github.tvbox.osc.server;

import android.content.Context;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


/**
 * @author pj567
 * @date :2021/1/5
 * @description: 资源文件加载
 */
public class RawRequestProcess implements RequestProcess {
    private Context mContext;
    private String fileName;
    private int resourceId;
    private String mimeType;

    public RawRequestProcess(Context context, String fileName, int resourceId, String mimeType) {
        this.mContext = context;
        this.fileName = fileName;
        this.resourceId = resourceId;
        this.mimeType = mimeType;
    }

    @Override
    public boolean isRequest(IHTTPSession session, String fileName) {
        return session.getMethod() == Method.GET && this.fileName.equalsIgnoreCase(fileName);
    }

    @Override
    public Response doResponse(IHTTPSession session, String fileName, Map<String, String> params, Map<String, String> files) {
        InputStream inputStream = mContext.getResources().openRawResource(this.resourceId);
        try {
            return Response.newFixedLengthResponse(Status.OK, mimeType + "; charset=utf-8", inputStream, (long) inputStream.available());
        } catch (IOException IOExc) {
            return RemoteServer.createPlainTextResponse(Status.INTERNAL_ERROR, "SERVER INTERNAL ERROR: IOException: " + IOExc.getMessage());
        }
    }
}