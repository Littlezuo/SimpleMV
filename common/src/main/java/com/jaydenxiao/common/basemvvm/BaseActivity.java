package com.jaydenxiao.common.basemvvm;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jaydenxiao.common.baseapp.AppManager;
import com.jaydenxiao.common.baseevent.BindBus;
import com.jaydenxiao.common.baseevent.rxbus.RxBus;
import com.jaydenxiao.common.baserx.RxManager;
import com.jaydenxiao.common.commonutils.TUtil;

/**
 * Created by joyin on 17-4-12.
 */

public abstract class BaseActivity<M extends ViewModel> extends AppCompatActivity implements  VMListener {

    public BaseActivity mContext;
    public M model;
    public RxManager mRxManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetContentView();
//        setContentView(getLayoutId());
        mContext = this;
        mRxManager = new RxManager();
//        StatusBarUtil.setStatusBarColor(this,R.color.colorPrimaryDark);
        if(this.getClass().isAnnotationPresent(BindBus.class)) {
//            EventBusUtil.register(this);
            RxBus.getDefault().register(this);
        }
        if(this.getClass().isAnnotationPresent(BindMV.class)) {
            setModel();
        }
//        initData();
    }

//    protected  void initData(){}
//    public abstract int getLayoutId();

    private void doBeforeSetContentView() {
        AppManager.getAppManager().addActivity(this);
        // 无标题
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    protected void setModel() {
        model = TUtil.getT(this, 0);
        if (model != null) {
            model.mContext = this;
            model.mRxManager = mRxManager;
            model.setVMListener(this);
            model.onStart();
        } else {
            throw new IllegalStateException("ViewModel 绑定activity不成功");
        }

    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        mRxManager.clear();
        if (model != null) {
            model.onDestory();
        }
        if(this.getClass().isAnnotationPresent(BindBus.class)) {
//            EventBusUtil.unregister(this);
            RxBus.getDefault().unRegister(this);
        }
        super.onDestroy();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onUpdate(int type) {

    }
}