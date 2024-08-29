package com.permissionxs.permissionxslib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import java.util.List;

public abstract class RationaleDialog extends Dialog {
    public RationaleDialog(Context context) {
        super(context);
    }

    public RationaleDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected RationaleDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    abstract public View getPositiveButton();

    abstract public View getNegativeButton();

    abstract public List<String> getPermissionToRequest();

}
