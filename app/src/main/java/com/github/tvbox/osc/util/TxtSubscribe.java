package com.github.tvbox.osc.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TxtSubscribe {

    public static void subscribe(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> allLives, String url, HashMap<String, String> headers) {
        String content = FileUtils.get(url, headers);
        parse(allLives, content);
    }

    public static void parse(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> linkedHashMap, String str) {
        ArrayList<String> arrayList;
        try {
            BufferedReader bufferedReader = new BufferedReader(new StringReader(str));
            String readLine = bufferedReader.readLine();
            LinkedHashMap<String, ArrayList<String>> linkedHashMap2 = new LinkedHashMap<>();
            LinkedHashMap<String, ArrayList<String>> linkedHashMap3 = linkedHashMap2;
            while (readLine != null) {
                if (readLine.trim().isEmpty()) {
                    readLine = bufferedReader.readLine();
                } else {
                    String[] split = readLine.split(",");
                    if (split.length < 2) {
                        readLine = bufferedReader.readLine();
                    } else {
                        if (readLine.contains("#genre#")) {
                            String trim = split[0].trim();
                            if (!linkedHashMap.containsKey(trim)) {
                                linkedHashMap3 = new LinkedHashMap<>();
                                linkedHashMap.put(trim, linkedHashMap3);
                            } else {
                                linkedHashMap3 = linkedHashMap.get(trim);
                            }
                        } else {
                            String trim2 = split[0].trim();
                            for (String str2 : split[1].trim().split("#")) {
                                String trim3 = str2.trim();
                                if (!trim3.isEmpty() && (trim3.startsWith("http") || trim3.startsWith("rtsp") || trim3.startsWith("rtmp"))) {
                                    if (!linkedHashMap3.containsKey(trim2)) {
                                        arrayList = new ArrayList<>();
                                        linkedHashMap3.put(trim2, arrayList);
                                    } else {
                                        arrayList = linkedHashMap3.get(trim2);
                                    }
                                    if (!arrayList.contains(trim3)) {
                                        arrayList.add(trim3);
                                    }
                                }
                            }
                        }
                        readLine = bufferedReader.readLine();
                    }
                }
            }
            bufferedReader.close();
            if (linkedHashMap2.isEmpty()) {
                return;
            }
            linkedHashMap.put("未分组", linkedHashMap2);
        } catch (Throwable unused) {
        }
    }

    public static String live2Json(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> lives) {
        JSONArray groups = new JSONArray();
        for (String group : lives.keySet()) {
            JSONArray channels = new JSONArray();
            LinkedHashMap<String, ArrayList<String>> allChannel = lives.get(group);
            if (allChannel.isEmpty())
                continue;
            for (String channel : allChannel.keySet()) {
                ArrayList<String> allUrls = allChannel.get(channel);
                if (allUrls.isEmpty())
                    continue;
                JSONArray urls = new JSONArray();
                for (int i = 0; i < allUrls.size(); i++) {
                    urls.put(allUrls.get(i));
                }
                JSONObject newChannel = new JSONObject();
                try {
                    newChannel.put("name", channel);
                    newChannel.put("urls", urls);
                } catch (JSONException e) {
                }
                channels.put(newChannel);
            }
            JSONObject newGroup = new JSONObject();
            try {
                newGroup.put("group", group);
                newGroup.put("channels", channels);
            } catch (JSONException e) {
            }
            groups.put(newGroup);
        }
        return groups.toString();
    }

    public static JsonArray live2JsonArray(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> linkedHashMap) {
        JsonArray jsonarr = new JsonArray();
        for (String str : linkedHashMap.keySet()) {
            JsonArray jsonarr2 = new JsonArray();
            LinkedHashMap<String, ArrayList<String>> linkedHashMap2 = linkedHashMap.get(str);
            if (!linkedHashMap2.isEmpty()) {
                for (String str2 : linkedHashMap2.keySet()) {
                    ArrayList<String> arrayList = linkedHashMap2.get(str2);
                    if (!arrayList.isEmpty()) {
                        JsonArray jsonarr3 = new JsonArray();
                        for (int i = 0; i < arrayList.size(); i++) {
                            jsonarr3.add(arrayList.get(i));
                        }
                        JsonObject jsonobj = new JsonObject();
                        try {
                            jsonobj.addProperty("name", str2);
                            jsonobj.add("urls", jsonarr3);
                        } catch (Throwable e) {
                        }
                        jsonarr2.add(jsonobj);
                    }
                }
                JsonObject jsonobj2 = new JsonObject();
                try {
                    jsonobj2.addProperty("group", str);
                    jsonobj2.add("channels", jsonarr2);
                } catch (Throwable e) {
                }
                jsonarr.add(jsonobj2);
            }
        }
        return jsonarr;
    }

    public static Object[] load(String ext) {
        try {
            LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> allLives = new LinkedHashMap<>();
            TxtSubscribe.subscribe(allLives, ext, null);
            String json = TxtSubscribe.live2Json(allLives);
            Object[] result = new Object[3];
            result[0] = 200;
            result[1] = "text/plain; charset=utf-8";
            ByteArrayInputStream baos = new ByteArrayInputStream(json.getBytes("UTF-8"));
            result[2] = baos;
            return result;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return null;
    }
}
