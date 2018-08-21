package com.example.wenbiaozheng.redpoint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RedPointManager mRedPointManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRedPointManager = RedPointManager.Instance;
        TextView tvShow = findViewById(R.id.tv_show);
        tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRedPointManager.add(MainActivity.this, R.id.tv_show, RedPointReason.TEXT_CLICK, true);
                mRedPointManager.show(MainActivity.this, R.id.tv_show, RedPointManager.TYPE_NORMAL);
                Log.i(TAG, "这个节点显示的红点数量:" + mRedPointManager.getViewRedPoint(R.id.tv_show));
            }
        });

        mRedPointManager.add(MainActivity.this, R.id.tv_show, RedPointReason.TEXT_SECOND_CLICK, true);
        TextView tvHide = findViewById(R.id.tv_hide);
        tvHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRedPointManager.hide(MainActivity.this, RedPointReason.TEXT_CLICK, false);
                Log.i(TAG, "这个节点显示的红点数量:" + mRedPointManager.getViewRedPoint(R.id.tv_show));
            }
        });
    }
}
