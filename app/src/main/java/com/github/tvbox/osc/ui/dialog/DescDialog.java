package com.github.tvbox.osc.ui.dialog;

import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.github.tvbox.osc.R;
import org.jetbrains.annotations.NotNull;

public class DescDialog extends BaseDialog {
    public DescDialog(@NonNull @NotNull Context context) {
        super(context);
        setContentView(R.layout.dialog_desc);
    }
    
    public void setDescribe(String describe) {
    	TextView tvDescribe = findViewById(R.id.describe);
        tvDescribe.setText(describe);
        tvDescribe.requestFocus();
        tvDescribe.requestFocusFromTouch();
    }
}