package com.example.eric.demo2.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eric.demo2.R;

/**
 * Created by lenovo on 2016/7/25.
 */
public class frame2 extends Fragment {
    private TextView textView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.demo_fragment2,container,false );
        textView=(TextView)view.findViewById(R.id.tv);
        textView.setText("政府");
        return view;
    }

}
