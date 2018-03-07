package com.yueyue.testapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = MainActivity.class.getSimpleName();
    private Button btn_click1;
    private Button btn_click2;
    private Button btn_click3;
    private EditText et_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private void initView() {
        btn_click1 = (Button) findViewById(R.id.btn_click1);
        btn_click2 = (Button) findViewById(R.id.btn_click2);
        btn_click3 = (Button) findViewById(R.id.btn_click3);
        et_show = (EditText) findViewById(R.id.et_show);

        btn_click1.setOnClickListener(this);
        btn_click2.setOnClickListener(this);
        btn_click3.setOnClickListener(this);

//        et_show.setEnabled(false);
//        et_show.setFocusable(false);
        et_show.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "beforeTextChanged: s:" + s);
                Log.i(TAG, "beforeTextChanged: start:" + start);
                Log.i(TAG, "beforeTextChanged: count:" + count);
                Log.i(TAG, "beforeTextChanged: after:" + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "onTextChanged: s:" + s);
                Log.i(TAG, "onTextChanged: start:" + start);
                Log.i(TAG, "onTextChanged: count:" + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "afterTextChanged: s.toString():" + s.toString());
                Log.i(TAG, "afterTextChanged: s:" + s);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_click1:

                break;
            case R.id.btn_click2:

                break;
            case R.id.btn_click3:

                break;
        }
    }

    private void submit() {
        // validate
        String show = et_show.getText().toString().trim();
        if (TextUtils.isEmpty(show)) {
            Toast.makeText(this, "show不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
