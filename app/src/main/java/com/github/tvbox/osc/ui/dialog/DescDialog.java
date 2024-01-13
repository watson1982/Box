package com.github.tvbox.osc.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.bean.Movie;
import com.github.tvbox.osc.event.RefreshEvent;
import com.github.tvbox.osc.ui.adapter.QuickSearchAdapter;
import com.github.tvbox.osc.ui.adapter.SearchWordAdapter;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DescDialog extends BaseDialog {
    private SearchWordAdapter searchWordAdapter;
    private QuickSearchAdapter searchAdapter;
    private TvRecyclerView mGridView;
    private TvRecyclerView mGridViewWord;

    public DescDialog(@NonNull @NotNull Context context) {
        super(context, R.style.CustomDialogStyleDim);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        setContentView(R.layout.dialog_quick_search);
        init(context);
    }



    private void init(Context context) {
        EventBus.getDefault().register(this);
        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                EventBus.getDefault().unregister(this);
            }
        });
        
        
    }
}