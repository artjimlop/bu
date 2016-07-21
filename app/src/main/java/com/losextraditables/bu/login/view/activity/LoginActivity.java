package com.losextraditables.bu.login.view.activity;

import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.OnClick;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.login.LoginModule;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends BuAppCompatActivity {

    private static final int STARTUP_DELAY = 300;
    private static final int ANIM_ITEM_DURATION = 1000;
    private static final int ITEM_DELAY = 300;

    @Bind(R.id.logo) ImageView logo;
    @Bind(R.id.container) LinearLayout container;

    private boolean animationStarted = false;

    @Override protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override protected List<Object> getActivityScopeModules() {
        return Arrays.asList((Object) new LoginModule());
    }

    @Override protected void redirectToLogin() {
        /* no - op */
    }

    @Override protected void onPreparePresenter() {
        super.onPreparePresenter();
    }

    @OnClick(R.id.btnConnect) public void onLoginPressed() {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    @Override public void onWindowFocusChanged(boolean hasFocus) {
        if(!hasFocus || animationStarted) {
            return;
        }
        animate();
        super.onWindowFocusChanged(hasFocus);
    }

    private void animate() {
        ViewCompat.animate(logo)
            .translationY(250)
            .setStartDelay(STARTUP_DELAY)
            .setDuration(ANIM_ITEM_DURATION).setInterpolator(
            new DecelerateInterpolator(1.2f)).start();

        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            ViewPropertyAnimatorCompat viewAnimator;

            if (!(v instanceof Button)) {
                viewAnimator = ViewCompat.animate(v)
                    .translationY(-50).alpha(1)
                    .setStartDelay((ITEM_DELAY * i) + 500)
                    .setDuration(1000);
            } else {
                viewAnimator = ViewCompat.animate(v)
                    .scaleY(1).scaleX(1)
                    .setStartDelay((ITEM_DELAY * i) + 500)
                    .setDuration(500);
            }

            viewAnimator.setInterpolator(new DecelerateInterpolator()).start();
        }
    }
}
