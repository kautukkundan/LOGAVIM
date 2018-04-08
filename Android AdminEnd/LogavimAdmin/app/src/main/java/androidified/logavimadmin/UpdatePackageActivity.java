package androidified.logavimadmin;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static android.view.View.GONE;

public class UpdatePackageActivity extends AppCompatActivity {
    TextView CongsId;
    Spinner locationdropdown;
    Spinner statusdropdown;
    RequestQueue MyRequestQueue;
    update updatemodel;
    StringRequest MyStringRequest;
    String consignmentidstr, locationselected, statusselected;
    Button updatebutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_package);
        CongsId=(TextView)findViewById(R.id.upcongid);
        locationdropdown = findViewById(R.id.spinner1);
        statusdropdown= findViewById(R.id.spinner2);
        MyRequestQueue = Volley.newRequestQueue(this);

        updatebutton=(Button)findViewById(R.id.update_button);
        String[] items = new String[]{"110001 - Connaught Place S.O","110009 - Model Town III S.O", "124507 - Bahadurgarh Sector 6 S.O","140603 - Zirakpur S.O","246482 - Tungeshwar B.O","504202 - Ambaripet B.O"};
        String[] items1 = new String[]{"Arrived","Dispatched","Delivered"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        locationdropdown.setAdapter(adapter);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        statusdropdown.setAdapter(adapter1);
        Intent intent = getIntent();
        consignmentidstr=intent.getStringExtra("Consid");
        if(consignmentidstr!=null)
        {
            CongsId.setText("Consignment No. : "+consignmentidstr);
        }
        locationdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                locationselected=(String) parent.getItemAtPosition(position);
                Toast.makeText(UpdatePackageActivity.this,locationselected,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        statusdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                statusselected=(String) parent.getItemAtPosition(position);
                Toast.makeText(UpdatePackageActivity.this,statusselected,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        updatebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (statusselected != null && locationselected != null) {
                    String url = "http://192.168.43.82:5000/scan/" + consignmentidstr;
                    senddatatoserver(locationselected,statusselected,consignmentidstr);
//                    MyStringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Toast.makeText(UpdatePackageActivity.this,"Successfully Updated",Toast.LENGTH_LONG).show();
//                        }
//                    }, new com.android.volley.Response.ErrorListener() { //Create an error listener to handle errors appropriately.
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(UpdatePackageActivity.this,"Update Failed",Toast.LENGTH_LONG).show();
//                        }
//                    }) {
//                        protected Map<String, String> getParams() {
//                            Map<String, String> MyData = new HashMap<String, String>();
//                            MyData.put("stamp", statusselected); //Add the data you'd like to send to the server.
//                            MyData.put("location",locationselected);
//                            return MyData;
//                        }
//                    };
//                    MyRequestQueue.add(MyStringRequest);

                }
                else
                {
                    Toast.makeText(UpdatePackageActivity.this,"Please fill all the fields",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void senddatatoserver(String locationselected,String statusselected, String consignmentidstr) {

        JSONObject post_dict = new JSONObject();

        try {
            post_dict.put("location", locationselected);
            post_dict.put("stamp", statusselected);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (post_dict.length() > 0) {
            new SendJsonDataToServer().execute(String.valueOf(post_dict));
        }
    }

    //add background inline class here
        class SendJsonDataToServer extends AsyncTask<String,String,String>{
            @Override
            protected String doInBackground(String... params) {
                String JsonResponse = null;
                String JsonDATA = params[0];
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("http://192.168.43.82:5000/scan/"+consignmentidstr);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");
//set headers and method
                    Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                    writer.write(JsonDATA);
// json data
                    writer.close();
                    InputStream inputStream = urlConnection.getInputStream();
//input stream
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String inputLine;
                    while ((inputLine = reader.readLine()) != null)
                        buffer.append(inputLine + "\n");
                    if (buffer.length() == 0) {
                        // Stream was empty. No point in parsing.
                        return null;
                    }
                    JsonResponse = buffer.toString();
                    try {
                        return JsonResponse;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;



                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {

                        }
                    }
                }
                return null;
            }
        }
















//    public static String POST(String url, Person person){
//        InputStream inputStream = null;
//        String result = "";
//        try {
//
//            // 1. create HttpClient
//            HttpClient httpclient = new DefaultHttpClient();
//
//            // 2. make POST request to the given URL
//            HttpPost httpPost = new HttpPost(url);
//
//            String json = "";
//
//            // 3. build jsonObject
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.accumulate("name", person.getName());
//            jsonObject.accumulate("country", person.getCountry());
//            jsonObject.accumulate("twitter", person.getTwitter());
//
//            // 4. convert JSONObject to JSON to String
//            json = jsonObject.toString();
//
//            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
//            // ObjectMapper mapper = new ObjectMapper();
//            // json = mapper.writeValueAsString(person);
//
//            // 5. set json to StringEntity
//            StringEntity se = new StringEntity(json);
//
//            // 6. set httpPost Entity
//            httpPost.setEntity(se);
//
//            // 7. Set some headers to inform server about the type of the content
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json");
//
//            // 8. Execute POST request to the given URL
//            HttpResponse httpResponse = httpclient.execute(httpPost);
//
//            // 9. receive response as inputStream
//            inputStream = httpResponse.getEntity().getContent();
//
//            // 10. convert inputstream to string
//            if(inputStream != null)
//                result = convertInputStreamToString(inputStream);
//            else
//                result = "Did not work!";
//
//        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
//        }
//
//        // 11. return result
//        return result;
//    }
//
//    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//
//            updatemodel = new update();
//            updatemodel.setName(etName.getText().toString());
//            person.setCountry(etCountry.getText().toString());
//            person.setTwitter(etTwitter.getText().toString());
//
//            return POST(urls[0],person);
//        }
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(String result) {
//            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
//        }
//    }
//    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
//        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
//        String line = "";
//        String result = "";
//        while((line = bufferedReader.readLine()) != null)
//            result += line;
//
//        inputStream.close();
//        return result;
//
//    }

}
