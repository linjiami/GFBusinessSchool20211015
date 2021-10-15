package com.gfbusinessschool.InterfaceUtils;

import androidx.appcompat.app.AlertDialog;

import com.gfbusinessschool.dialog.MyAlertDialog;

public abstract class MyDialogCallback {

    public void onPositiveClick(MyAlertDialog dialog){
        if (dialog!=null && dialog.isShowing()) dialog.dismiss();
    }

    public void onNegativeClick(MyAlertDialog dialog){
        if (dialog!=null && dialog.isShowing()) dialog.dismiss();
    }
}