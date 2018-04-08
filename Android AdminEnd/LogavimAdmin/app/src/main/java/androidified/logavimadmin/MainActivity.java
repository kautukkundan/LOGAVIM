package androidified.logavimadmin;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private boolean doubleBackToExitPressedOnce = false;
    Window window;
    LottieAnimationView l;
    boolean i=true;
    Menu menuClone;
    int deep1,light1;
    ImageView qrscan,adminpanel;
    private static final int REQUEST_WRITE_PERMISSION = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        window = getWindow();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        l=(LottieAnimationView)findViewById(R.id.animation_view);
        l.setAnimation("waves.json");
        l.playAnimation();
        colorizeAB();

        qrscan=(ImageView)findViewById(R.id.qrbutton);
        adminpanel=(ImageView)findViewById(R.id.adminbutton);

        qrscan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, QRScanner.class);
                startActivity(i);
            }
        });
        adminpanel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent i = new Intent(MainActivity.this, TrackPackage.class);
                //startActivity(i);
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);


    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.exit_press_back_twice_message, Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuClone=menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    protected void onResume() {
        super.onResume();
        // .... other stuff in my onResume ....
        this.doubleBackToExitPressedOnce = false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {           //3 dot button

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            System.exit(0);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.nav_package_update)
        {
            Intent i=new Intent(this,QRScanner.class);
            startActivity(i);
        }
        else if (id == R.id.nav_admin_panel) {
            Intent i = new Intent(MainActivity.this, AdminPanelActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_website)
        {
            Intent i = new Intent(MainActivity.this, WebsiteActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void colorizeAB(){

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        Runnable r=new Runnable() {
            @Override
            public void run() {
                colorizeAB();

            }
        };
        Handler h =new Handler();
        try
        {
//            int b =bottomBar.getCurrentTabId();
//            if(b==R.id.tab_main) {
//                deep1=R.color.colorPrimaryDark;
//                light1=R.color.voilettab;
//                if(h!=null) {
//                    h.removeCallbacks(r);
//                    h.removeMessages(0);
//                    h.postDelayed(r, 2000);
//                }
//            }
//            else if(b==R.id.tab_help)
//            {
//                deep1=R.color.darkbluetab;
//                light1=R.color.bluetab;
//                h.removeCallbacks(r);
//                h.removeMessages(0);
//                h.postDelayed(r,2000);
//            }
////            else if(b==R.id.tab_locate)
////            {
////                deep1=R.color.darkbluetab;
////                light1=R.color.bluetab;
////                h.removeCallbacks(r);
////                h.removeMessages(0);
////                h.postDelayed(r,2000);
////            }
//
            h.removeCallbacks(r);
                    h.removeMessages(0);
                    h.postDelayed(r, 2000);
                  deep1=R.color.colorPrimaryDark;
                light1=R.color.voilettab;
            ColorDrawable Light,Deep;
            Light = new ColorDrawable(getResources().getColor(light1));
            Deep = new ColorDrawable(getResources().getColor(deep1));
            window.setStatusBarColor(getResources().getColor(deep1));
            //ColorDrawable Black= new ColorDrawable(Color.BLACK);
            if (i== true) {
                ColorDrawable[] color0 = {Deep,Light};
                TransitionDrawable trans0 = new TransitionDrawable(color0);

                //window.setStatusBarColor(getResources().getColor(deep1));
                getSupportActionBar().setBackgroundDrawable(trans0);
                trans0.startTransition(2000);
                i=false;

            }
            else
            {
                ColorDrawable[] color1 = {Light, Deep};
                TransitionDrawable trans1 = new TransitionDrawable(color1);
                //window.setStatusBarColor(getResources().getColor(R.color.lightPurple));
                getSupportActionBar().setBackgroundDrawable(trans1);
                //window.setStatusBarColor(getResources().getColor(light1));

                trans1.startTransition(2000);
                i=true;
            }
        }
        catch (Exception e)
        {
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
