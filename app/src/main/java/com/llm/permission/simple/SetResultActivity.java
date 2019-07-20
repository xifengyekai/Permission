package com.llm.permission.simple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

public class SetResultActivity extends AppCompatActivity {
    String[] array = {
            "来自SetResultActivity数据",
            "From SetResultActivity数据",
            "来自SetResultActivity DATA",
            "From SetResultActivity DATA"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_resutl);
    }

    public void onClick(View view) {
        Intent intent = new Intent();
        int code = new Random().nextInt(4);
        intent.putExtra("data", array[code]);
        setResult(code, intent);
        finish();
    }
}
