package com.yxnne.mysides.view;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.util.log.LogGenerator;

/**
 * My BaseActivity
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //add this to the Appliction
        YApplication.activities.add(this);
    }
}
