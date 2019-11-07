package com.weexplus.jim.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.weexplus.jim.R;
import com.weexplus.jim.activity.ChatMsgActivity;

public class Sample extends Activity {

    TextView textView;
    Button button1, button2;

    public static void start(Context context, String contactId, String userName, String title) {
        Intent intent = new Intent();
        intent.setClass(context, Sample.class);
//        intent.putExtra("USERNAME", userName);
//        intent.putExtra("NAKENAME", title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        textView = findViewById(R.id.textView);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("left");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("right");
            }
        });

    }
}
