package androidified.logavimadmin;



import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class SplashActivity extends Activity {
    //ImageView logo;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

      //  logo=(ImageView)findViewById(R.id.imagelogo);
        Animation animation= AnimationUtils.loadAnimation(SplashActivity.this,R.anim.rotate_move_down);
       // logo.startAnimation(animation);
        final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
       // LottieAnimationView animationView1 = (LottieAnimationView) findViewById(R.id.animation_view1);
        animationView.setAnimation("motorcycle.json");
        //animationView1.setAnimation("splashy_loader.json");
        animationView.playAnimation();
        //animationView.loop(true);
        //animationView1.playAnimation();
        final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(getResources().getColor(R.color.animation1), PorterDuff.Mode.OVERLAY);
        final PorterDuffColorFilter colorFilter1 = new PorterDuffColorFilter(getResources().getColor(R.color.animation2), PorterDuff.Mode.SRC_OVER);

        //animationView1.addColorFilter(colorFilter1);
// Adding a color filter to the whole view
        animationView.addColorFilter(colorFilter);
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                finishscreen();
            }
        };
        Timer t = new Timer();
        t.schedule(task, 3200);
//        }
    }
    private void finishscreen() {
        this.finish();
    }
}
