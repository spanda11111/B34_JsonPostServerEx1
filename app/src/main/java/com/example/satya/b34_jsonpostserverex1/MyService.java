package com.example.satya.b34_jsonpostserverex1;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MyService extends Service {
    private String name,country,twitter;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //12:read data coming from fragment
        Bundle bundle = intent.getExtras();
        name= bundle.getString("name");
        country= bundle.getString("country");
        twitter= bundle.getString("twitter");
        //async task will start
        MyTask myTask = new MyTask();
        myTask.execute("http://hmkcode.appspot.com/jsonservlet");
        return super.onStartCommand(intent, flags, startId);
    }

    public class MyTask extends AsyncTask<String,Void,String>
    {
        //declare required variable
        URL myurl;
        HttpURLConnection connection;
        OutputStream outputStream ;
        OutputStreamWriter outputStreamWriter ;

        @Override
        protected String doInBackground(String... strings) {
            try {
                myurl=new URL(strings[0]);

                connection= (HttpURLConnection) myurl.openConnection();
                //priliminary code for checking for connection
                connection.setDoOutput(true);
                connection.setConnectTimeout(60000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json");
                //prepare json for posting
                JSONObject jsonObject = new JSONObject() ;
                jsonObject.accumulate("name",name);
                jsonObject.accumulate("country",country);
                jsonObject.accumulate("twitter",twitter);
                //prepare outputstream
                outputStream = connection.getOutputStream();
                outputStreamWriter=new OutputStreamWriter(outputStream);
                Log.d("B34","5");
                //write json data into output stream writer
                outputStreamWriter.write(jsonObject.toString());//convert into stream
                Log.d("B34","6");
                //forcefully throw everything to server
                outputStreamWriter.flush();
                //here at this point if time -server will start reading
                //letus ask server -FOR RESPONSE
                int responsecode= connection.getResponseCode();
                Log.d("B34","Respond:"+responsecode);
                //here return response code
                if (responsecode==HttpURLConnection.HTTP_OK)
                {
                    return "success";
                }
                else
                {
                    return  "faliure";
                }

            } catch (MalformedURLException e) {
                Log.d("B34","1");
                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.d("B34","2");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("B34","ERROR IS.."+e.getMessage()+".."+e.getCause());
                e.printStackTrace();
            } catch (JSONException e) {
                Log.d("B34","4");
                e.printStackTrace();
            }

            finally {
                //close all connection
                if (connection!= null)
                {
                    connection.disconnect();//close connection
                    if (outputStream!=null)
                    {
                        try {
                            outputStream.close();
                            if (outputStreamWriter!=null)
                            {
                                outputStreamWriter.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(MyService.this, "Status:"+s, Toast.LENGTH_SHORT).show();
            //send broadcast to dynamic receiver
            Intent intent = new Intent() ;
            intent.setAction("Task_Done");
            intent.putExtra("result",s);
            sendBroadcast(intent);
            super.onPostExecute(s);
        }
    }
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
