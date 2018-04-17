package pragma.android.kneemonitor_367;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_child_activity_367.R;


public class MainActivity extends Activity implements BluetoothSPPConnectionListener {
	 public static final int MESSAGE_STATE_CHANGE = 1;
	 public static final int MESSAGE_READ = 2;
	 public static final int MESSAGE_WRITE = 3;
	 public static final int MESSAGE_DEVICE_NAME = 4;
	 public static final int MESSAGE_TOAST = 5;
     private boolean connected=false;
     private static final int REQUEST_CONNECT_DEVICE = 1;
     private static final int REQUEST_ENABLE_BT = 2;
     private BluetoothSPPConnection mBluetoothSPPConnection;	
     private BluetoothAdapter mBluetoothAdapter = null;
     String filename ="/sdcard/bluetooth/Knee.txt";
  
	WifiManager wifi;
	
	

	String src_latitude;
	String src_longitude;
	
	boolean gps_enabled=false;
    boolean network_enabled=false;
    String reqURL; 
  
     
     TextView tv_child_activity;
     EditText et_number;
     
     
     TextView txtstatus,et_msg;
     ImageView img_child_activity;
     Button btn_graph,btn_clear;
     Button btnconnect;
     protected static final int RESULT_SPEECH = 11;
     String str_child_activity;
     String readMessage="";
     String temp="Hi";
     DatabaseHelper helper;
     SQLiteDatabase database;
     TextToSpeech speak;
     
     int i;
     
     Mail m;
 	String[] toArr = {"vigneshp8080@gmail.com", "vishalh08@gmail.com"}; 
 	
 	
 	LocationManager mlocManager;
	LocationListener mlocListener;

	double c_lat;
	double c_long;

	String address="";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	try
		{
		get_lat_long_details();

		try{
		//	addressText="";
			Child_Movement_Graph_activity.ar_namelist.clear();
			Child_Movement_Graph_activity.ar_values.clear();
		//	mlocManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		//	get_location_details();
		}
		catch(Exception e){
			Toast.makeText(getApplicationContext(), "error is" + e.getMessage()	, Toast.LENGTH_SHORT).show();
		}

		
			Child_Movement_Graph_activity.ar_namelist.clear();
			Child_Movement_Graph_activity.ar_values.clear();
			helper  = new DatabaseHelper(this);
			database = helper.getWritableDatabase();
	//		DeleteData();
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			txtstatus=(TextView)findViewById(R.id.txtstatus);
			et_number=(EditText)findViewById(R.id.txtphoneno);
			btnconnect=(Button) findViewById(R.id.btnconnect);
			et_msg=(TextView)findViewById(R.id.txtinput);
			tv_child_activity=(TextView)findViewById(R.id.tv_child_activity);
		//	img_child_activity=(ImageView)findViewById(R.id.img_child_activity);
			btn_graph=(Button)findViewById(R.id.btn_graph);
			btn_clear=(Button)findViewById(R.id.btn_clear);
			
			btn_clear.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
			//	Toast.makeText(getApplicationContext(), "Clearing data", Toast.LENGTH_SHORT).show();	
					Child_Movement_Graph_activity.ar_values.clear();
					Child_Movement_Graph_activity.ar_namelist.clear();
					
					helper = new DatabaseHelper(getApplicationContext());
					
					
					SQLiteDatabase db = helper.getWritableDatabase();
					
					
					
					//SELECT _id FROM login WHERE username=uname AND password=pass
					//	Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
						db.delete(DatabaseHelper.Activity_Info_TABLE_NAME, null, null);
				//	cursor = db.query(DatabaseHelper.Activity_Info_TABLE_NAME, columns, selection, selectionArgs, str_groupby, null,null);
						Toast.makeText(getApplicationContext(), "data cleared", Toast.LENGTH_SHORT).show();
				}
			});
		
		
			
			
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		    if (!mBluetoothAdapter.isEnabled()) {
	            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
	        }
		  
	        mBluetoothSPPConnection = new BluetoothSPPConnection(this,mHandler); // Registers the
	        btnconnect.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					
						if (!connected) {
						Intent serverIntent = new Intent(v.getContext(), DeviceListActivity.class);
						startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
						}
						else 
						{
							mBluetoothSPPConnection.close();
						}
					}
				
			});
	    
	
	
	}
	catch(Exception e){
		
		Toast.makeText(getApplicationContext(),"error" + e.getMessage() , Toast.LENGTH_SHORT).show();
		
	}
		
	btn_graph.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(connected){
				 mBluetoothSPPConnection.close();
				}
				 Intent intent = new Intent(MainActivity.this, Child_Movement_Graph_activity.class);
	        	 startActivity(intent);
				
				
			}
		});
	}
	public static JSONObject getLocationInfo(double lat, double lng) {
		 
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
 
            StrictMode.setThreadPolicy(policy);
 
            HttpGet httpGet = new HttpGet(
                    "http://maps.googleapis.com/maps/api/geocode/json?latlng="
                            + lat + "," + lng + "&sensor=true");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            StringBuilder stringBuilder = new StringBuilder();
 
            try {
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }
            } catch (ClientProtocolException e) {
 
            } catch (IOException e) {
 
            }
 
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(stringBuilder.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return jsonObject;
        }
        return null;
    }
	
	
	private void get_lat_long_details() {

	
		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		mlocListener = new MyLocationListener();

		mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
				0, mlocListener);
	}

	/* Class My Location Listener */

	public class MyLocationListener implements LocationListener

	{

		public void onLocationChanged(Location loc)

		{

			loc.getLatitude();

			loc.getLongitude();

			c_lat = loc.getLatitude();

			c_long = loc.getLongitude();

			 Toast.makeText( getApplicationContext(),
			 
			  "latitude->" + c_lat + "\n" + "longitude->" + c_long + "\n" ,
			  
			  Toast.LENGTH_SHORT).show();
			 

			getAddress();

			/*
			 * Toast.makeText( getApplicationContext(),
			 * 
			 * "Address->" + address ,
			 * 
			 * Toast.LENGTH_SHORT).show();
			 */

			// send_SMS(Ph_Number_doctor);

			mlocManager.removeUpdates(mlocListener);

			

		}

		public void onProviderDisabled(String provider)

		{

			Toast.makeText(getApplicationContext(),

			"Gps Disabled",

			Toast.LENGTH_SHORT).show();

		}

		public void onProviderEnabled(String provider)

		{

			Toast.makeText(getApplicationContext(),

			"Gps Enabled",

			Toast.LENGTH_SHORT).show();

		}

		public void onStatusChanged(String provider, int status, Bundle arg2) {
			// TODO Auto-generated method stub

			switch (status) {
			case LocationProvider.OUT_OF_SERVICE:

				// Toast.makeText(this, "Status Changed: Out of Service",
				// Toast.LENGTH_SHORT).show();
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:

				// Toast.makeText(this,
				// "Status Changed: Temporarily Unavailable",
				// Toast.LENGTH_SHORT).show();
				break;
			case LocationProvider.AVAILABLE:

				// Toast.makeText(this, "Status Changed: Available",
				// Toast.LENGTH_SHORT).show();
				break;
			}

		}
	}/* End of Class MyLocationListener */

	void getAddress() {
		try {
			Geocoder gcd = new Geocoder(this, Locale.getDefault());
			List<Address> addresses = gcd.getFromLocation(c_lat, c_long, 1);
			if (addresses.size() > 0) {
				StringBuilder result = new StringBuilder();
				for (int i = 0; i < addresses.size(); i++) {
					Address address = addresses.get(i);
					int maxIndex = address.getMaxAddressLineIndex();
					for (int x = 0; x <= maxIndex; x++) {
						result.append(address.getAddressLine(x));
						result.append(",");
					}

				}
				address = result.toString();
				// tv_get_address.setText(result.toString());
			}
		} catch (IOException ex) {
			// tv_get_address.setText(ex.getMessage().toString());
		}
	}

	@SuppressWarnings("deprecation")
	private void  Send_SMS(){
		try{
			
			
			Toast.makeText(getApplicationContext(),
					"SMS Send," + et_number.getText().toString().trim()+"-NH",
					Toast.LENGTH_LONG).show();
			
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(et_number.getText().toString().trim(), null,"NH" , null, null);
		
		 myAsyncTask myRequest = new myAsyncTask();
         myRequest.execute();
		
	} catch (Exception e) {
		Toast.makeText(getApplicationContext(),
			"SMS faild, please try again later!" + e.getMessage(),
			Toast.LENGTH_LONG).show();
		
		
	  }
	}

	class myAsyncTask extends AsyncTask<Void, Boolean, Boolean>{

		   @Override
	       protected void onPreExecute() {
	           super.onPreExecute();
	    //       Toast.makeText(getApplicationContext(),"calling method", Toast.LENGTH_SHORT).show();
	           m = new Mail("reloadp52@gmail.com", "Hsengiv007!@#"); 
	           
	       }
		   
		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			boolean result = false;
			
			
		      m.setTo(toArr); 
		      m.setFrom("reloadp52@gmail.com"); 
		      m.setSubject("This is an email sent from ANDROID BASED KNEE MONIROING APP"); 
		      m.setBody("Hi User, " + System.getProperty("line.separator") + "Please find below details" +
		    		  System.getProperty("line.separator") + "EMERGNNECY DETECTED" + 
		    		  System.getProperty("line.separator") + "Location: " + address); 
		    		  
		    		
		 
		      try { 
		   
		        if(m.send()) {
		        	result = true;
		        	
		          
		        } else { 
		        	result = false;
		          
		        } 
		      } catch(Exception e) { 
		        //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show(); 
		        Log.e("MailApp", "Could not send email", e); 
		      } 
			return result;
		}
		
		protected void onPostExecute(boolean result){
			if(result){
				 Toast.makeText(MainActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
				 address = "";
			}
			else{
				Toast.makeText(MainActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show(); 
			}
			
			
		}
		   
		   
	   }

    @Override
    protected void onResume(){
    	
    	super.onResume();
    	SharedPreferences prefs = getPreferences(0);
    	String restoredText = prefs.getString("text", null);
    	
    	if(restoredText != null){
    		
    		et_number.setText(restoredText, TextView.BufferType.EDITABLE);
    		int selectionStart = prefs.getInt("selection-start", -1);
    		int selectionEnd = prefs.getInt("selection-end", -1);
    		
    		if(selectionStart != -1 && selectionEnd != -1){
    			
    			et_number.setSelection(selectionStart, selectionEnd);
    			
    			
    		}
    	}
    }
    
    @Override
    protected void onPause(){
    	
    	super.onPause();
    	SharedPreferences.Editor editor = getPreferences(0).edit();
    	editor.putString("text", et_number.getText().toString().trim());
    	editor.putInt("selection-start", et_number.getSelectionStart());
    	editor.putInt("selection-end", et_number.getSelectionEnd());
    	
    	editor.commit();
    	
    }

public final boolean isInternetOn() {
    	
    	try{
    ConnectivityManager connec =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    
    if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
    connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
    connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
    connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {

    	
    return true;
    } else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
    		connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED  ) {
   
    return false;
    }
    	}catch(Exception e){
    //		Toast.makeText(getApplicationContext(), "error in internet" + e.getMessage(), Toast.LENGTH_SHORT).show();
    		return false;
    	}
    return false;
    }
	public void ListentoSpeach()
	{
		Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

        try {
            startActivityForResult(intent, RESULT_SPEECH);
            et_msg.setText("");
        } 
        catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Opps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }
		
	}
    public String actualdata="";	
	 private final Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case MESSAGE_STATE_CHANGE:
	                   break;
	            case MESSAGE_WRITE:
	                byte[] writeBuf = (byte[]) msg.obj;
	                String writeMessage = new String(writeBuf);
	                break;
	            case MESSAGE_READ:
	            	byte[] readBuf = (byte[]) msg.obj;
	                readMessage = new String(readBuf, 0, msg.arg1);
	                
	                

					/*11- Walking
	                 * 22- Running
	                 * 33-Sitting;
	                 * 44 Climbing
	                 * 55-standing
	                 * EE-emergency
	                */
	                if (readMessage.contains("@"))
	                {
	                  actualdata=actualdata+readMessage;
	                  actualdata=actualdata.replace("@", "");
	                //  mConversationArrayAdapter.add(mConnectedDeviceName+":  " + actualdata);
	                  String status="";
		                if (actualdata.equalsIgnoreCase("NH"))
		                { 
		                	status="Need Help";
		                	//addEntry("Walking", "WK");	
		                }
		                else if (actualdata.equalsIgnoreCase("222"))
		                {
		                	status="Running";
		                	addEntry("Running", "RN");
		                }
		                else if (actualdata.equalsIgnoreCase("333"))
		                {
		                	status="Sitting";
		                	addEntry("Sitting", "SI");
		                }
		                else if (actualdata.equalsIgnoreCase("444"))
		                {
		                	Voice_output("Please control Your Movements");
		                	status="Please control Your Movements";
		                	status="Wrong Movement";
		                	addEntry("Wrong Movement", "WM");
		                }
		                else if (actualdata.equalsIgnoreCase("555"))
		                {
		                	status="Standing";
		                	addEntry("Standing", "ST");
		                }
		                else if (actualdata.contains("E"))
		                {
		                
		                	Voice_output("In Emergency");
		                	status="Emergency";
		                //	addEntry("Emergency", "1");
		                	Voice_output("In Emergency");
		                	
		                	 Send_SMS();
		                }
		               
		              
		                else if (actualdata.contains("M"))
		                {
		                
		                	Voice_output("Please control Your Movements");
		                	status="Please control Your Movements";
		                //	addEntry("Emergency", "1");
		                //	Voice_output("Please control Your Movements test");
		                }
		                tv_child_activity.setText(actualdata + "-"+ status);

	                  actualdata="";
	                }
	                else
	                {
	                	actualdata=actualdata+readMessage;
	                }
	                Toast.makeText(getApplicationContext(), "msg is: " + readMessage, Toast.LENGTH_SHORT).show();
	               	               break;
	            case MESSAGE_DEVICE_NAME:
	                break;
	            case MESSAGE_TOAST:

	                break;
	            }
	        }
	    };


	    public void onActivityResult(int requestCode, int resultCode, Intent data) {

	    	switch (requestCode) {
	    	case RESULT_SPEECH: {
	            if (resultCode == RESULT_OK && null != data) {
	 
	                ArrayList<String> text = data
	                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	 
	                et_msg.setText(text.get(0));
	            try{    
	                String spkdata =text.get(0);
	                
	                
	    			 
	           /*    
	                if (spkdata.equalsIgnoreCase("help")||spkdata.equalsIgnoreCase("1"))
	                {
	                	
	                	mlocManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	                	Toast.makeText(getApplicationContext(), " speak if ", Toast.LENGTH_SHORT).show();
		    			get_location_details();
		    		
	                }*/
	            }
	            catch(Exception ex)
	            {
	            	Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
	            }
	               
	            }

	    	}
	    	break;
	        case REQUEST_CONNECT_DEVICE:
	        	if (resultCode == Activity.RESULT_OK)
	        	{
	            	String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
	                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
	                mBluetoothSPPConnection.open(device);                
	            }
	            break;
	        case REQUEST_ENABLE_BT:
	        	if (resultCode == Activity.RESULT_OK) {
	        	}
	        	break;
	        }
	    }


	  
	 public void addEntry(String acctivity_name, String activity_code){
		 
		 
		 
			Toast.makeText(getApplicationContext(), "inserting data", Toast.LENGTH_SHORT).show();

			SQLiteDatabase db = helper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put("activityname", acctivity_name);
			values.put("activitycode", activity_code);
			
			try{
				db.insert(DatabaseHelper.Activity_Info_TABLE_NAME, null, values);
	//			Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
		//		Get_info(this);

			}catch(Exception e){
				e.printStackTrace();
			}
		}
	 @Override
	 public void onBackPressed() {
	     // This will be called either automatically for you on 2.0
	     // or later, or by the code above on earlier versions of the
	     // platform.
		 super.onBackPressed();
		 finish();
	     return;
	 }
	 
	  
	
	 
	
	 public void Voice_output(final String text_speak){

		 
			Toast.makeText(getApplicationContext(),"In Method voice" , Toast.LENGTH_SHORT).show();
		 speak = new TextToSpeech(this,
	                new TextToSpeech.OnInitListener() {

	                    @Override
						public void onInit(int status) {

	                        if (status != TextToSpeech.ERROR) {

	                            speak.setLanguage(Locale.US);
	                          
	                        }
	                        speak.speak(text_speak, TextToSpeech.QUEUE_ADD, null);
	                        
	                       
	                    }
	                });

	 }




		public void bluetoothWrite(int bytes, byte[] buffer) {
			// TODO Auto-generated method stub
    		  byte[] command = new byte[4];
		      command[0]='c';
		      command[1]='a';
		      command[2]='b';
		      command[3]='c';
		      mBluetoothSPPConnection.write(command);
		}
		public void bluetoothWriteCommand(int bytes, byte[] buffer) {
			// TODO Auto-generated method stub
    		 
		      mBluetoothSPPConnection.write(buffer);
		}

		public void onConnecting() {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "connecting here", Toast.LENGTH_LONG).show();
			TextView connectionView = (TextView) findViewById(R.id.txtstatus);
			connectionView.setText("Connecting...");
		}

		public void onConnected() {
			// TODO Auto-generated method stub
			connected = true;

			// Change the text in the connectionInfo TextView
			TextView connectionView = (TextView) findViewById(R.id.txtstatus);
			connectionView.setText("Connected to "+mBluetoothSPPConnection.getDeviceName());

			// Change the text in the connect button.
			Button bt = (Button) findViewById(R.id.btnconnect);
			bt.setText("Disconnect From Kneecap");
			
			// Send the 's' character so that the communication can start.
			byte[] command = new byte[1];
			command[0]='s';
			mBluetoothSPPConnection.write(command);
		}

		public void onConnectionFailed() {
			// TODO Auto-generated method stub
			connected = false;
			
			// Change the text in the connectionInfo TextView
			TextView connectionView = (TextView) findViewById(R.id.txtstatus);
			connectionView.setText("Connection failed!");

			// Change the text in the connect button.
			Button bt = (Button) findViewById(R.id.btnconnect);
			bt.setText("Connect to Kneecap");
		}

		public void onConnectionLost() {
			// TODO Auto-generated method stub
			connected = false;
			
			// Change the text in the connectionInfo TextView
			TextView connectionView = (TextView) findViewById(R.id.txtstatus);
			connectionView.setText("Not Connected!");

			// Change the text in the connect button.
			Button bt = (Button) findViewById(R.id.btnconnect);
			bt.setText("Connect to kneecap");
		}

		public void bluetoothread(int bytes, byte[] buffer) {
			// TODO Auto-generated method stub
			/* byte[] readBuf = (byte[]) msg.obj;
			 String readMessage = new String(readBuf, 0, msg.arg1);
			 mBluetoothSPPConnection.read(command);*/
			
		}
	 

}
