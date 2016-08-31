package com.example.eric.demo2;

import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.example.eric.demo2.fragment.frame1;
import com.example.eric.demo2.fragment.frame2;
import com.example.eric.demo2.fragment.frame3;
import com.example.eric.demo2.widget.MyTabBar;

public class MainActivity extends AppCompatActivity {
    MyTabBar mMyTabBar;
    private int mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();

    }

    private void initView()
    {
        setContentView(R.layout.activity_main);

//      getResources().getColor(R.color.colorAccent);
        mFrameLayout=R.id.frame;
        mMyTabBar=(MyTabBar)findViewById(R.id.mytab);
        mMyTabBar.initialize(mFrameLayout,this);
    }

    private void initData()
    {

        MyTabBar.TabParam param1=new MyTabBar.TabParam(R.drawable.govaffairs2,R.drawable.govaffairs_press2,"政府");

        mMyTabBar.addTab(frame1.class,param1);

        MyTabBar.TabParam param2=new MyTabBar.TabParam(R.drawable.newscenter2,R.drawable.newscenter_press2,"新闻");
        mMyTabBar.addTab(frame2.class,param2);

        MyTabBar.TabParam param3=new MyTabBar.TabParam(R.drawable.smartservice2,R.drawable.smartservice_press2,"智慧");
        mMyTabBar.addTab(frame3.class,param3);


    }


}
