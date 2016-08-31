package com.example.eric.demo2.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eric.demo2.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/8/4.
 */
public class MyTabBar extends LinearLayout implements View.OnClickListener{
    private String mCurrentTag;
    private int mDefaultSelectedTab = 0;//选中第几个
    private int mCurrentSelectedTab;
    private int mNormalTextColor=0xfffff;
    private int mSelectedTextColor=Color.RED;
    private FragmentActivity mFragmentActivity;
    private ArrayList<ViewHolder> mViewHolders;
    private float mTextSize=0;
    private int mFrameLayout;

    public MyTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.TabText);
        mNormalTextColor=typedArray.getColor(R.styleable.TabText_NormalTextColor,0xfffff);
        mSelectedTextColor=typedArray.getColor(R.styleable.TabText_SelectedTextColor,Color.RED);
        mTextSize=typedArray.getDimensionPixelSize(R.styleable.TabText_textSize,0);
        typedArray.recycle();
        mViewHolders=new ArrayList<>();

    }


    public void setFragmentActivity(FragmentActivity fragmentActivity)
    {
        this.mFragmentActivity=fragmentActivity;
    }

    @Override
    public void onClick(View view) {
        Object object=view.getTag();
        if(object!=null&&object instanceof ViewHolder)
        {
            ViewHolder viewHolder=(ViewHolder)object;
            showFragment(viewHolder);
        }

    }

    @Nullable
    public void initialize(int mFrameLayout,FragmentActivity fragmentActivity) {
        this.mFrameLayout = mFrameLayout;
        this.mFragmentActivity=fragmentActivity;
    }

    public void setDefaultSelectedTab(int i)
    {
        mDefaultSelectedTab=i;
    }

    public void setNormalTextColor(int mNormalTextColor) {
        this.mNormalTextColor = mNormalTextColor;
    }

    public void setSelectedTextColor(int mSelectedTextColor) {
        this.mSelectedTextColor = mSelectedTextColor;
    }

    private void showFragment(ViewHolder viewHolder)
    {
        String tag=viewHolder.tag;
        FragmentManager fragmentManager=mFragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        if(isFragmentShown(fragmentTransaction,tag))
        {
            return;
        }
        changeView(viewHolder);
        Fragment fragment=fragmentManager.findFragmentByTag(tag);
        if(fragment==null)
        {
            fragment=getFragmentInstance(tag);
            fragmentTransaction.add(mFrameLayout,fragment,tag);
        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
        mCurrentTag=tag;
        mCurrentSelectedTab=viewHolder.tabIndex;
    }

    private boolean isFragmentShown(FragmentTransaction fragmentTransaction,String tag)
    {
        if(TextUtils.equals(mCurrentTag,tag))
        {
            return true;
        }
        if(TextUtils.isEmpty(mCurrentTag))
        {
            return false;
        }
        Fragment fragment=mFragmentActivity.getSupportFragmentManager().findFragmentByTag(mCurrentTag);
        if(fragment!=null&&!fragment.isHidden())
        {
            fragmentTransaction.hide(fragment);
            resetTab();
        }

        return false;
    }

    private void resetTab()
    {
        ViewHolder viewHolder=mViewHolders.get(mCurrentSelectedTab);
        viewHolder.tabText.setTextColor(mNormalTextColor);
        viewHolder.tabIcon.setImageResource(viewHolder.tabParam.iconResId);
    }

    private void changeView(ViewHolder viewHolder)
    {
        if(viewHolder.tabParam!=null)
        {
            TabParam tabParam=viewHolder.tabParam;
            if(tabParam.iconSelectedResId>0)
            {
                viewHolder.tabIcon.setImageResource(tabParam.iconSelectedResId);
            }

                viewHolder.tabText.setTextColor(mSelectedTextColor);


        }

    }

    private Fragment getFragmentInstance(String tag)
    {
        for(ViewHolder viewHolder:mViewHolders)
        {
            if(TextUtils.equals(viewHolder.tag,tag))
            {
                try {
                    Fragment fragment=(Fragment) Class.forName(viewHolder.tabFrag.getName()).newInstance();
                    return fragment;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
        return null;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        hideAllFragment();
        if(mViewHolders.size()-1>=mDefaultSelectedTab)
        {
            ViewHolder viewHolder=mViewHolders.get(mDefaultSelectedTab);
            mCurrentTag=null;
            showFragment(viewHolder);
        }



    }




    private void hideAllFragment()
    {
        if(mFragmentActivity!=null)
        {
            FragmentManager fragmentManager=mFragmentActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

            for(ViewHolder viewHolder:mViewHolders) {
                String tag = viewHolder.tag;

                Fragment  fragment= fragmentManager.findFragmentByTag(tag);
                if (fragment!=null) {
                    fragmentTransaction.hide(fragment);
                }
            }
        }

    }

    public void setTextSize(float size)
    {
       mTextSize=size;
    }


    public void addTab(Class fragment,TabParam tabParam)
    {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.tab_param,null);
        ViewHolder viewHolder=new ViewHolder();
        viewHolder.tabFrag=fragment;
        viewHolder.tabIcon= (ImageView) view.findViewById(R.id.imv);
        viewHolder.tabText=(TextView)view.findViewById(R.id.tv);
        viewHolder.tabIndex=mViewHolders.size();
        viewHolder.tabParam=tabParam;
        if (tabParam.titleRes>0)
        {
            tabParam.title=getContext().getResources().getString(tabParam.titleRes);
        }
        if(mTextSize!=0) {
            viewHolder.tabText.setTextSize(mTextSize);
        }

        viewHolder.tabText.setText(tabParam.title);
        viewHolder.tag=tabParam.title;
        viewHolder.tabIcon.setImageResource(tabParam.iconResId);
        viewHolder.tabText.setTextColor(mNormalTextColor);
        addView(view,new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
        view.setTag(viewHolder);
        mViewHolders.add(viewHolder);
        view.setOnClickListener(this);
//        int black = android.R.color.black;
//        TextView textView;


    }



    private static class ViewHolder
    {
        public String tag;
        public TabParam tabParam;
        public ImageView tabIcon;
        public TextView tabText;
        public Class tabFrag;
        public int tabIndex;


    }
    public  static class TabParam
    {

        public int backgroundcolor= android.R.color.white;
        public int iconResId;
        public int iconSelectedResId;
        public String title;
        public int titleRes;


        public TabParam(int iconResId,int iconSelectedResId,String title)
        {
            this.iconResId=iconResId;
            this.iconSelectedResId=iconSelectedResId;
            this.title=title;
        }

        public TabParam(int iconResId,int iconSelectedResId,int titleRes)
        {
            this.iconResId=iconResId;
            this.iconSelectedResId=iconSelectedResId;
            this.titleRes=titleRes;
        }

        public TabParam(int iconResId,String title)
        {
            this.iconResId=iconResId;
            this.title=title;
        }

        public TabParam(int iconResId,int  titleRes)
        {
            this.iconResId=iconResId;
            this.titleRes=titleRes;
        }



    }

    //fragment = (Fragment) Class.forName(holder.fragmentClass.getName()).newInstance();
}
