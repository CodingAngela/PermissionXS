package com.permissionxs.permissionxslib;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class PermissionXS {
    public static PermissionMediator init(FragmentActivity activity) {
        return new PermissionMediator(activity);
    }

    public static PermissionMediator init(Fragment fragment) {
        return new PermissionMediator(fragment);
    }
}
