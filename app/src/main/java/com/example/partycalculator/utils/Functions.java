package com.example.partycalculator.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.partycalculator.R;
import com.example.partycalculator.entity.Party;
import com.example.partycalculator.entity.PartySingleton;

public abstract class Functions {
    public static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return ((String) value).isEmpty();
        }
        return value.toString().isEmpty();
    }

    public static Party getParty() {
        return PartySingleton.getInstance().getParty();
    }

    public static Long getPartySysId() {
        if (getParty() != null) {
            return PartySingleton.getInstance().getParty().getSysId();
        } else {
            return null;
        }
    }
    public static void showErrorDialog(Context context, String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error")
                .setMessage(errorMessage)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }
    public static void showErrorToast(Context context, String errorMessage) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
