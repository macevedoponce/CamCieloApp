package com.acevedo.caminoalcielo.Clases;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogHelper {

    private ProgressDialog progressDialog;

    public ProgressDialogHelper(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
    }

    public void showProgressDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
