package com.yueyue.todolist.modules.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yueyue.todolist.R;
import com.yueyue.todolist.modules.avatar.ui.AvatarActivity;

public class MainActivity extends AppCompatActivity {

    public static final long DRAWER_CLOSE_DELAY = 230L;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AvatarActivity.launch(this);
    }


}
