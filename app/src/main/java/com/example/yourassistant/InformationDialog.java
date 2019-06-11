package com.example.yourassistant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.ContextThemeWrapper;

public class InformationDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.MyDialogTheme));
        builder.setTitle("Thông tin thiết bị")
                .setMessage("Chủ sở hữu: nvhoang\nMã nhà: 1\nPhòng: bedroom\nĐịa chỉ MAC: 68:C6:3A:C2:CC:61")
                .setPositiveButton("Ẩn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
