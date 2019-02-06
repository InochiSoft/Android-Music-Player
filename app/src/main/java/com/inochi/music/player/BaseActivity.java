package com.inochi.music.player;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
    }

    public void showProgressLoading(String message){
        try {
            hideProgressLoading();
            if (progressDialog != null){
                progressDialog.setCancelable(false);
                progressDialog.setMessage(message);
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgressLoading(){
        try {
            if (progressDialog != null){
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
