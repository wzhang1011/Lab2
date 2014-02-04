//Wang Zhang
//Lab 2 for CSC 560
package com.example.lab2_weathers;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.os.*;
//import android.os.StrictMode;
import android.app.*;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private Button Button1;
	private EditText editText1;
	private EditText editText2;
	private EditText editText3;
	private EditText editText4;
	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
 
	private long fileSize = 0;
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(policy); 
        Button1 = (Button) findViewById(R.id.button1);
        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);
        editText3 = (EditText)findViewById(R.id.editText3);
        editText4 = (EditText)findViewById(R.id.editText4);
        addListenerOnButton();
    }
        public void addListenerOnButton() {
        	 
    		//Button1 = (Button) findViewById(R.id.button1);
        Button1.setOnClickListener(new View.OnClickListener(){
       	 //private Button.OnClickListener ButtonCapture = new Button.OnClickListener(){
       			
       			public void onClick(View v){
       			// prepare for a progress bar dialog
       				progressBar = new ProgressDialog(v.getContext());
       				progressBar.setCancelable(true);
       				progressBar.setMessage("Loading.....");
       				progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       				progressBar.setProgress(0);
       				progressBar.setMax(100);
       				progressBar.show();
       	 
       				//reset progress bar status
       				progressBarStatus = 0;
       	 
       				//reset filesize
       				fileSize = 0;
      
       	 
       	        	
       				HttpClient httpclient = new DefaultHttpClient();
       				HttpResponse response;
       				String responseString = null;
       				try {
       				 HttpPost httpPost = new HttpPost("http://api.wunderground.com/api/36b799dc821d5836/conditions/q/KS/Kansas_City.json");
       	             response = httpclient.execute(httpPost);
       				    StatusLine statusLine = response.getStatusLine();
       				    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
       				        ByteArrayOutputStream out = new ByteArrayOutputStream();
       				        response.getEntity().writeTo(out);
       				        out.close();
       				        responseString = out.toString();
       				     String combine = JSONAnalysis(responseString);
       				     String tempF = combine.split(";")[0];
       				     String tempC = combine.split(";")[1];
       				     String state = JSONAnalysis2(responseString);
       				     String weather = JSONAnalysis1(responseString);
       				     String city = JSONAnalysis3(responseString);
       				     
       				     
       				     editText3.setText(" "+tempF + "F"+"("+tempC+"C)");
       				     editText4.setText(" "+weather);
       				     editText1.setText(" "+state);
       				     editText2.setText(" "+city);
       				     
       				    } else{
       				        //Closes the connection.
       				        response.getEntity().getContent().close();
       				        throw new IOException(statusLine.getReasonPhrase());
       				    }	
       				} catch (ClientProtocolException e) {
       				    e.printStackTrace();
       				} catch (IOException e) {
       				    e.printStackTrace();
       				}
       			 	
       				new Thread(new Runnable() {
       				  public void run() {
       					while (progressBarStatus < 100) {
       	 
       					  // process some tasks
       					  progressBarStatus = doSomeTasks();
       	 
       					  // sleep 1 second
       					  try {
       						Thread.sleep(1000);
       					  } catch (InterruptedException e) {
       						e.printStackTrace();
       					  }
       	 
       					  // Update the progress bar
       					  progressBarHandler.post(new Runnable() {
       						public void run() {
       						  progressBar.setProgress(progressBarStatus);
       						}
       					  });
       					}
       	 
       					// file is loaded,
       					if (progressBarStatus >= 100) {
       	 
       						// sleep 1 seconds, so that you can see the 100%
       						try {
       							Thread.sleep(1000);
       						} catch (InterruptedException e) {
       							e.printStackTrace();
       						}
       	 
       						// close the progress bar dialog
       						progressBar.dismiss();
       					}
       				  }
       			       }).start();
       	 
       		           }
       	 
       	                });}
                        //});
        
    //}
    public int doSomeTasks() {
    	 
		while (fileSize <= 1000000) {
 
			fileSize++;
 
			if (fileSize == 500000) {
				return 50;
			} else {
				return 100;
			}
 
		}
 
		return 100;
 
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public String JSONAnalysis(String jsonString)
    {
    	String temperatureF="";
    	String temperatureC="";
    	String combine="1";
    	JSONObject jsonObj; 
		try {
			jsonObj = new JSONObject(jsonString);
			JSONObject  obser=jsonObj.getJSONObject("current_observation");    	
	    	temperatureF=obser.getString("temp_f");
	    	temperatureC = obser.getString("temp_c");
	    	combine = temperatureF+";"+temperatureC;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return combine;
    }
    public String JSONAnalysis1(String jsonString){
    	String weather="";
    	JSONObject jsonObj1;
		try{
			jsonObj1 = new JSONObject(jsonString);
			JSONObject  obser1=jsonObj1.getJSONObject("current_observation");  
			weather = obser1.getString("weather");
		} catch(JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  weather;
		
    }
    public String JSONAnalysis3(String jsonString){
    	String State="";
    	JSONObject jsonObj2;
		try{
			jsonObj2 = new JSONObject(jsonString);
			JSONObject  obser1=jsonObj2.getJSONObject("current_observation");
			JSONObject obser2 = obser1.getJSONObject("display_location");
			State = obser2.getString("state");
		} catch(JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  State;
		
    }
    public String JSONAnalysis2(String jsonString){
    	String city="";
    	JSONObject jsonObj3;
		try{
			jsonObj3 = new JSONObject(jsonString);
			JSONObject  obser1=jsonObj3.getJSONObject("current_observation");
			JSONObject obser2 = obser1.getJSONObject("display_location");
			city = obser2.getString("city");
		} catch(JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  city;
		
    }
    
}
