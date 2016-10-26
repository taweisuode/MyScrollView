package com.example.pengge.myscrollview;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MyScrollView scrollView;
    private int lastSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInflateData(0);
        scrollView = (MyScrollView)findViewById(R.id.scroll_view);

        scrollView.setOnRefreshListener(new MyScrollView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                testArray(0);
            }

            @Override
            public void onReload() {
                testArray(1);
            }
        },1);
    }
    private void testArray(final int flag) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putInt("flag",flag);
                msg.setData(data);
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            switch (msg.what) {
                case  1:
                    setInflateData(data.getInt("flag"));
                    break;
                default:
                    break;
            }
        }
    };
    private void setInflateData(int flag) {
        String[] strings = {"A","B","C","D","E","F"};
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        LinearLayout serviceList = (LinearLayout) findViewById(R.id.service_list);
        switch (flag) {
            case  0:
                if(serviceList.getChildAt(0) != null) {
                    serviceList.removeAllViews();
                }
                break;
            case 1:
            default:
                break;
        }
        if(flag == 0) {
            lastSize = strings.length;
        }else if(flag == 1) {
            //这块记录上一次的jsonarray的长度
            lastSize = lastSize + strings.length;
        }
        for (int i = 0;i <strings.length;i++) {
            inflater.inflate(R.layout.service_listview,serviceList,true);
            //在这块要注意 获取子孩子区间在 (lastSize-strings.length,lastSize);
            TextView topTitle =  (TextView)serviceList.getChildAt(i+lastSize-strings.length).findViewById(R.id.service_top_title);
            topTitle.setText(strings[i]);
        }
        if(scrollView != null) {
            scrollView.finishRefreshing();
        }

    }
}
