package com.github.tvbox.osc.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.event.RefreshEvent;
import com.github.tvbox.osc.server.RemoteServer;
import com.github.tvbox.osc.util.HawkConfig;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DescDialog extends BaseDialog {    

    public DescDialog(@NonNull @NotNull Context context) {
        super(context);
        setContentView(R.layout.dialog_desc);
    }

}