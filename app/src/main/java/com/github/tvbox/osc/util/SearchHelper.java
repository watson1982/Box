package com.github.tvbox.osc.util;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.api.ApiConfig;
import com.github.tvbox.osc.bean.SourceBean;
import com.github.tvbox.osc.ui.activity.HomeActivity;
import com.orhanobut.hawk.Hawk;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class SearchHelper {
    private static final Pattern p = Pattern.compile("\\D+|(?:19|20)\\d{2}", Pattern.MULTILINE);

    public static boolean searchContains(String data, List < String > keys) {
        boolean search_ok = true;
        for (int i = 0; i < keys.size(); i++) {
            LOG.e("FenCi", keys.get(i)
                .toLowerCase());
            if (!data.toLowerCase()
                .contains(keys.get(i)
                .toLowerCase())) {
                search_ok = false;
                break;
            }
        }

        return search_ok;
    }

    public static HashMap < String, String > getSourcesForSearch() {
        String api = Hawk.get(HawkConfig.API_URL, HomeActivity.getRes()
            .getString(R.string.app_source));
        //String api = Hawk.get(HawkConfig.API_URL, "");
        if (api.isEmpty()) {
            return null;
        }
        HashMap < String, String > mCheckSources = new HashMap < > ();
        try {
            HashMap < String, HashMap < String, String >> mCheckSourcesForApi = Hawk.get(HawkConfig.SOURCES_FOR_SEARCH, new HashMap < > ());
            mCheckSources = mCheckSourcesForApi.get(api);
        } catch (Exception ignored) {

        }
        if (mCheckSources == null || mCheckSources.size() <= 0) {
            if (mCheckSources == null) {
                mCheckSources = new HashMap < > ();
            }
            for (SourceBean bean: ApiConfig.get()
                .getSourceBeanList()) {
                if (!bean.isSearchable()) {
                    continue;
                }
                mCheckSources.put(bean.getKey(), "1");
            }
        }
        return mCheckSources;
    }

    public static void putCheckedSources(HashMap < String, String > mCheckSources) {
        String api = Hawk.get(HawkConfig.API_URL, HomeActivity.getRes()
            .getString(R.string.app_source));
        //String api = Hawk.get(HawkConfig.API_URL, "");
        if (api.isEmpty()) {
            return;
        }
        HashMap < String, HashMap < String, String >> mCheckSourcesForApi = Hawk.get(HawkConfig.SOURCES_FOR_SEARCH, new HashMap < > ());
        if (mCheckSourcesForApi == null || mCheckSourcesForApi.isEmpty()) {
            mCheckSourcesForApi = new HashMap < > ();
        }
        mCheckSourcesForApi.put(api, mCheckSources);
        Hawk.put(HawkConfig.SOURCES_FOR_SEARCH, mCheckSourcesForApi);
    }

    public static void putCheckedSource(String siteKey, boolean checked) {
        String api = Hawk.get(HawkConfig.API_URL, HomeActivity.getRes()
            .getString(R.string.app_source));
        if (api.isEmpty()) {
            return;
        }
        HashMap < String, HashMap < String, String >> mCheckSourcesForApi = Hawk.get(HawkConfig.SOURCES_FOR_SEARCH, new HashMap < > ());
        if (mCheckSourcesForApi == null || mCheckSourcesForApi.isEmpty()) {
            mCheckSourcesForApi = new HashMap < > ();
        }
        if (mCheckSourcesForApi.get(api) == null) {
            mCheckSourcesForApi.put(api, new HashMap < > ());
        }
        if (checked) {
            mCheckSourcesForApi.get(api)
                .put(siteKey, "1");
        } else {
            if (mCheckSourcesForApi.get(api)
                .containsKey(siteKey)) {
                mCheckSourcesForApi.get(api)
                    .remove(siteKey);
            }
        }
        Hawk.put(HawkConfig.SOURCES_FOR_SEARCH, mCheckSourcesForApi);
    }
}