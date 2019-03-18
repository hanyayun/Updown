package com.cnlive.updownfile.view;


import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.cnlive.updownfile.R;
import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;

public class RefreshHeaderView extends AppCompatTextView implements IHeaderView {


    public RefreshHeaderView(Context context) {
        super(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public View getView() {
        setGravity(Gravity.CENTER);
        setTextSize(15f);
        setTextColor(getResources().getColor(R.color.color_333333));
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) setText(R.string.pull_down_refresh);
        if (fraction > 1f) setText(R.string.release_refresh);
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) setText(R.string.pull_down_refresh);
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        setText(R.string.is_refreshing);
    }

    @Override
    public void onFinish(OnAnimEndListener listener) {
        listener.onAnimEnd();
    }

    @Override
    public void reset() {
        setText(R.string.pull_down_refresh);
    }
}