package androidified.logavim;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidified.logavim.fragment.HelpFragment;
import androidified.logavim.fragment.MainFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private boolean doubleBackToExitPressedOnce = false;
    TextView navEmail;
    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    Window window;
    boolean i=true;
    Menu menuClone;
    protected PowerManager.WakeLock mWakeLock;
    int deep1,light1;
    BottomBar bottomBar;
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
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:1924"));
//                startActivity(callIntent);
//            }
//        });

        colorizeAB();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        bottomBar= (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId==R.id.tab_main)
                {
                    if(getSupportFragmentManager().getFragments()==null)
                    {
                        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            ColorDrawable Light = new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark));
                            getSupportActionBar().setBackgroundDrawable(Light);
                        }

                        MainFragment mainFragment= new MainFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.contentContainer,mainFragment).commit();
                    }
                    else {
                        // add fragment to the fragment container layout
                        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            ColorDrawable Light = new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark));
                            getSupportActionBar().setBackgroundDrawable(Light);
                        }

                        MainFragment mainFragment= new MainFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,mainFragment).commit();

                    }
                }

//                else if (tabId == R.id.tab_locate) {
//                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                        ColorDrawable Light = new ColorDrawable(getResources().getColor(R.color.colorPrimary));
//                        getSupportActionBar().setBackgroundDrawable(Light);
//                    }
//                    MainFragment mainFragment= new MainFragment();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,mainFragment).commit();
//                }
                else if(tabId==R.id.tab_help)
                {
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        ColorDrawable Light = new ColorDrawable(getResources().getColor(R.color.pinkytab));
                        getSupportActionBar().setBackgroundDrawable(Light);
                    }
                    HelpFragment helpFragment= new HelpFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,helpFragment).commit();

                }
            }
        });

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



    public class GetDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://192.168.43.82:5000/pkg/EU123499999IN";
            String jsonStr = sh.makeServiceCall(url);
            String destination;
            Log.e("MainActivity", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String package1 = jsonObj.getString("package");
                    JSONObject jsonObj1 = new JSONObject(package1);
                    destination = jsonObj1.getString("destination");
                    Log.e("MainActivity", "Response from destination: " + destination);

                    //Toast.makeText(getApplicationContext(),destination,Toast.LENGTH_LONG).show();
                    //String destination;
                    // Getting JSON Array node
                    //JSONArray contacts = jsonObj.getJSONArray("package");

                    //JSONObject c = contacts.getJSONObject(0);
                    //for (int i = 0; i < contacts.length(); i++)
                    //{
                     //   JSONObject c = contacts.getJSONObject(0);
                       // destination = c.getString("delivery_cost");
                       // Toast.makeText(MainActivity.this,destination,Toast.LENGTH_LONG).show();

                    //}



                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("phone");
//                        String mobile = phone.getString("mobile");
//                        String home = phone.getString("home");
//                        String office = phone.getString("office");

//                        // tmp hash map for single contact
//                        HashMap<String, String> contact = new HashMap<>();
//
//                        // adding each child node to HashMap key => value
//                        contact.put("id", id);
//                        contact.put("name", name);
//                        contact.put("email", email);
//                        contact.put("mobile", mobile);
//
//                        // adding contact to contact list
//                        contactList.add(contact);
//
                } catch (final JSONException e) {
                    Log.e("MainActivity", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e("MainActivity", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList,
//                    R.layout.list_item, new String[]{ "email","mobile"},
//                    new int[]{R.id.email, R.id.mobile});
//            lv.setAdapter(adapter);
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

        if(id==R.id.nav_package_details)
        {
            Intent i=new Intent(this,TrackPackage.class);
            startActivity(i);
        }
        else if (id == R.id.nav_scan_code) {
            Intent i = new Intent(MainActivity.this, QRScanner.class);
            startActivity(i);
        } else if (id == R.id.nav_track_package) {
              Intent i = new Intent(MainActivity.this, TrackWebPackage.class);
              startActivity(i);
        } else if (id == R.id.nav_calculate_postage) {
            Intent i = new Intent(MainActivity.this, CalculatePostage.class);
            startActivity(i);
        }
        else if (id == R.id.nav_find_pincode) {

            Intent i = new Intent(MainActivity.this, FindPincode.class);
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
        bottomBar=(BottomBar)findViewById(R.id.bottomBar);
        Runnable r=new Runnable() {
            @Override
            public void run() {
                colorizeAB();

            }
        };
        Handler h =new Handler();
        try
        {
            int b =bottomBar.getCurrentTabId();
            if(b==R.id.tab_main) {
                deep1=R.color.colorPrimaryDark;
                light1=R.color.voilettab;
                if(h!=null) {
                    h.removeCallbacks(r);
                    h.removeMessages(0);
                    h.postDelayed(r, 2000);
                }
            }
            else if(b==R.id.tab_help)
            {
                deep1=R.color.darkbluetab;
                light1=R.color.bluetab;
                h.removeCallbacks(r);
                h.removeMessages(0);
                h.postDelayed(r,2000);
            }
//            else if(b==R.id.tab_locate)
//            {
//                deep1=R.color.darkbluetab;
//                light1=R.color.bluetab;
//                h.removeCallbacks(r);
//                h.removeMessages(0);
//                h.postDelayed(r,2000);
//            }

            ColorDrawable Light,Deep;
            Light = new ColorDrawable(getResources().getColor(light1));
            Deep = new ColorDrawable(getResources().getColor(deep1));
            window.setStatusBarColor(getResources().getColor(light1));
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