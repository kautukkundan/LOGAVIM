package androidified.logavim.fragment;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONException;
import org.json.JSONObject;

import androidified.logavim.CalculatePostage;
import androidified.logavim.FindPincode;
import androidified.logavim.HttpHandler;
import androidified.logavim.MainActivity;
import androidified.logavim.QRScanner;
import androidified.logavim.R;
import androidified.logavim.TrackPackage;
import androidified.logavim.TrackWebPackage;


public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";
    Context mContext;
    FrameLayout frameLayout;
    LottieAnimationView l;
    TextView destination_text;
    public MainFragment() {}
    ClipData myClip;
    ImageView qrcodebutton,trackbutton,packagebutton,costbutton,findpincodebutton;
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.mainfragmentlayout, container, false);
        frameLayout=(FrameLayout)rootView.findViewById(R.id.frame);
        l=(LottieAnimationView)rootView.findViewById(R.id.animation_view);
        l.setAnimation("waves.json");
        l.playAnimation();
        qrcodebutton=(ImageView) rootView.findViewById(R.id.qrscaniconbutton);
        trackbutton=(ImageView) rootView.findViewById(R.id.trackpackageiconbutton);
        packagebutton=(ImageView) rootView.findViewById(R.id.packageiconbutton);
        costbutton=(ImageView) rootView.findViewById(R.id.estimatepostageiconbutton);
        findpincodebutton=(ImageView) rootView.findViewById(R.id.findpincodebutton);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        qrcodebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), QRScanner.class);
                startActivity(i);
            }
        });
        trackbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TrackWebPackage.class);
                startActivity(i);
            }
        });
        packagebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TrackPackage.class);
                startActivity(i);
            }
        });
        costbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CalculatePostage.class);
                startActivity(i);
            }
        });
        findpincodebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FindPincode.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        l.pauseAnimation();
        }
    }

//public class GetDetails extends AsyncTask<Void, Void, Void> {
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//    @Override
//    protected Void doInBackground(Void... arg0) {
//        HttpHandler sh = new HttpHandler();
//        // Making a request to url and getting response
//        String url = "http://192.168.43.82:5000/pkg/EU123499999IN";
//        String jsonStr = sh.makeServiceCall(url);
//        String destination;
//        Log.e("MainActivity", "Response from url: " + jsonStr);
//        if (jsonStr != null) {
//            try {
//                JSONObject jsonObj = new JSONObject(jsonStr);
//                String package1 = jsonObj.getString("package");
//                JSONObject jsonObj1 = new JSONObject(package1);
//                destination = jsonObj1.getString("destination");
//                Log.e("MainActivity", "Response from destination: " + destination);
//
//                //Toast.makeText(getApplicationContext(),destination,Toast.LENGTH_LONG).show();
//                //String destination;
//                // Getting JSON Array node
//                //JSONArray contacts = jsonObj.getJSONArray("package");
//
//                //JSONObject c = contacts.getJSONObject(0);
//                //for (int i = 0; i < contacts.length(); i++)
//                //{
//                //   JSONObject c = contacts.getJSONObject(0);
//                // destination = c.getString("delivery_cost");
//                // Toast.makeText(MainActivity.this,destination,Toast.LENGTH_LONG).show();
//
//                //}
//
//
//                // Phone node is JSON Object
////                        JSONObject phone = c.getJSONObject("phone");
////                        String mobile = phone.getString("mobile");
////                        String home = phone.getString("home");
////                        String office = phone.getString("office");
//
////                        // tmp hash map for single contact
////                        HashMap<String, String> contact = new HashMap<>();
////
////                        // adding each child node to HashMap key => value
////                        contact.put("id", id);
////                        contact.put("name", name);
////                        contact.put("email", email);
////                        contact.put("mobile", mobile);
////
////                        // adding contact to contact list
////                        contactList.add(contact);
////
//            } catch (final JSONException e) {
//                Log.e("MainActivity", "Json parsing error: " + e.getMessage());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Json parsing error: " + e.getMessage(),
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//
//            }
//
//        } else {
//            Log.e("MainActivity", "Couldn't get json from server.");
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(),
//                            "Couldn't get json from server. Check LogCat for possible errors!",
//                            Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//
//        return null;
//    }


