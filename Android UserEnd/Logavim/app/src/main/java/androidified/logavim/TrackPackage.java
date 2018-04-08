package androidified.logavim;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import static android.view.View.GONE;

public class TrackPackage extends AppCompatActivity {
TextView ConsignmentId, DeliveryCost, Destination, ReceiverName, SenderAddress, SenderName, Source, Weight,
        ReceiverAddress,BookingDate;
EditText enteredConId;
String EnteredConsignmentId;
Button submit;
CardView cardsender, cardreceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_track_package);
        enteredConId=(EditText)findViewById(R.id.enterconsignment);
        submit=(Button)findViewById(R.id.button_submit);
        ConsignmentId=(TextView)findViewById(R.id.consignment_id);
        DeliveryCost=(TextView)findViewById(R.id.cost);
        Destination=(TextView)findViewById(R.id.destination);
        ReceiverName=(TextView)findViewById(R.id.receiver_name);
        ReceiverAddress=(TextView)findViewById(R.id.receiver_address);
        SenderAddress=(TextView)findViewById(R.id.sender_address);
        SenderName=(TextView)findViewById(R.id.sender_name);
        Source=(TextView)findViewById(R.id.source);
        Weight=(TextView)findViewById(R.id.weight);
        BookingDate=(TextView)findViewById(R.id.booking_date);
        cardsender=(CardView)findViewById(R.id.card);
        cardreceiver=(CardView)findViewById(R.id.card1);
        //cardreceiver.setVisibility(GONE);
        //cardsender.setVisibility(GONE);
        Intent intent = getIntent();
        if(intent.getStringExtra("Consid")!=null)
        {
            enteredConId.setVisibility(GONE);
            submit.setVisibility(GONE);
            GetDetails myTask = new GetDetails(ConsignmentId,DeliveryCost,Destination,ReceiverName,SenderAddress,SenderName,
                    Source,Weight,ReceiverAddress,BookingDate,cardsender,cardreceiver,intent.getStringExtra("Consid"));
            myTask.execute();
        }
        else {
            submit.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    EnteredConsignmentId = enteredConId.getText().toString();
                    GetDetails myTask = new GetDetails(ConsignmentId, DeliveryCost, Destination, ReceiverName, SenderAddress, SenderName,
                            Source, Weight,ReceiverAddress,BookingDate, cardsender, cardreceiver, EnteredConsignmentId);
                    myTask.execute();
                }
            });
        }
    }
}


class GetDetails extends AsyncTask<Void, Void, Void> {
    TextView consignmentid, deliverycost, destination, receivername,
            receiveraddress,senderaddress, sendername, source, weight,bookingdate;
    CardView Cardsender, Cardreceiver;
    String enteredconid;
    GetDetails(TextView consignmentid, TextView deliverycost, TextView destination,TextView receivername, TextView senderaddress,
               TextView sendername, TextView source, TextView weight, TextView receiveraddress, TextView bookingdate,
               CardView Cardsender, CardView Cardreceiver,String enteredconid)
    {
        this.consignmentid=consignmentid;
        this.deliverycost=deliverycost;
        this.destination=destination;
        this.receivername=receivername;
        this.senderaddress=senderaddress;
        this.sendername=sendername;
        this.source=source;
        this.weight=weight;
        this.enteredconid=enteredconid;
        this.receiveraddress=receiveraddress;
        this.bookingdate=bookingdate;
        this.Cardsender=Cardsender;
        this.Cardreceiver=Cardreceiver;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response
        String url = "http://192.168.43.82:5000/pkg/"+enteredconid;
        String jsonStr = sh.makeServiceCall(url);
        String receivernamestr,sendernamestr,sourcestr, destinationstr,
                considstr, deliverycoststr,senderaddressstr, destinationaddressstr,weightstr,bookingdatestr,packagetypestr ;
        Log.e("MainActivity", "Response from url: " + jsonStr);
        if ((jsonStr != null ))   //&&(jsonStr.contains("No package found"))
             {
            try {
                //Cardreceiver.setVisibility(View.VISIBLE);
                //Cardsender.setVisibility(View.VISIBLE);
                JSONObject jsonObj = new JSONObject(jsonStr);
                String package1 = jsonObj.getString("package");
                JSONObject jsonObj1 = new JSONObject(package1);
                destinationstr = jsonObj1.getString("destination");
                sourcestr = jsonObj1.getString("source");
                receivernamestr = jsonObj1.getString("receiver_name");
                sendernamestr = jsonObj1.getString("sender_name");
                weightstr = jsonObj1.getString("weight");
                considstr = jsonObj1.getString("consignment_id");
                deliverycoststr = jsonObj1.getString("delivery_cost");
                senderaddressstr = jsonObj1.getString("sender_address");
                destinationaddressstr=jsonObj1.getString("receiver_address");
                bookingdatestr=jsonObj1.getString("booking_date");
                packagetypestr=jsonObj1.getString("package_type");

//                deliverycost.setVisibility(View.VISIBLE);
//                weight.setVisibility(View.VISIBLE);
//                bookingdate.setVisibility(View.VISIBLE);
                consignmentid.setText("Consignment No. : " + considstr);
                destination.setText(destinationstr);
                source.setText(sourcestr);
                receivername.setText(receivernamestr);
                sendername.setText(sendernamestr);
                weight.setText("Weight : "+weightstr+" grams");
                deliverycost.setText("Cost : Rs. "+deliverycoststr);
                senderaddress.setText(senderaddressstr);
                receiveraddress.setText(destinationaddressstr);
                bookingdate.setText(bookingdatestr);

            } catch (final JSONException e) {
                Log.e("Trackpackage", "Json parsing error: " + e.getMessage());
                consignmentid.setText("Consignment Not Available");
                destination.setText("");
                source.setText("");
                receivername.setText("");
                sendername.setText("");
                weight.setText("");
                deliverycost.setText("");
                senderaddress.setText("");
                receiveraddress.setText("");
                bookingdate.setText("");
            }

        } else {
            Log.e("Trackpackage", "Couldn't get json from server.");
            consignmentid.setText("Consignment Not Available");
            destination.setText("");
            source.setText("");
            receivername.setText("");
            sendername.setText("");
            weight.setText("");
            deliverycost.setText("");
            senderaddress.setText("");
            receiveraddress.setText("");
            bookingdate.setText("");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}

