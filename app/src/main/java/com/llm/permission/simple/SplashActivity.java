package com.llm.permission.simple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.llm.activityresult.ActivityResultHelper;
import com.llm.activityresult.ResultCallback;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_permission) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (view.getId() == R.id.btn_start_for_result) {
            ActivityResultHelper.init(this)
                    .startActivityForResult(SetResultActivity.class, new ResultCallback() {
                        @Override
                        public void onActivityResult(int resultCode, Intent data) {
                            ((Button) findViewById(R.id.btn_start_for_result))
                                    .setText(resultCode + ", " + data.getStringExtra("data"));
                        }
                    });
        }
    }
}
