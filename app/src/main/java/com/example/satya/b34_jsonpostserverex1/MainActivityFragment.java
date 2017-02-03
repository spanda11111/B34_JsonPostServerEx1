package com.example.satya.b34_jsonpostserverex1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    //10: declare all variables
    EditText et1,et2,et3;
    Button bt1,bt2;
    TextView tv1;

    //
    public class MyReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String result=bundle.getString("result");
            tv1.setText("SERVER STATUS:"+result);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyReceiver myReceiver = new MyReceiver() ;
        IntentFilter intentFilter = new IntentFilter() ;
        intentFilter.addAction("Task_Done");
        getActivity().registerReceiver(myReceiver,intentFilter);
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_main, container, false);
        et1= (EditText) v.findViewById(R.id.et1);
        et2= (EditText) v.findViewById(R.id.et2);
        et3= (EditText) v.findViewById(R.id.et3);
        bt1= (Button) v.findViewById(R.id.bt1);
        bt2= (Button) v.findViewById(R.id.bt2);
        tv1= (TextView) v.findViewById(R.id.tv1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //will start service
                Intent intent = new Intent(getActivity(),MyService.class);
                intent.putExtra("name",et1.getText().toString());
                intent.putExtra("country",et2.getText().toString());
                intent.putExtra("twitter",et3.getText().toString());
                getActivity().startService(intent);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //post after 1 min in server-using alaram
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getActivity(),MyService.class);
                PendingIntent pendingIntent = PendingIntent.getService(getActivity(),0,intent,0);
                //alarmManager.set(AlarmManager.RTC_WAKEUP,60000,pendingIntent);
                //for repeted alaram process
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+60000, 60000,pendingIntent);
                Toast.makeText(getActivity(), "alarm started", Toast.LENGTH_SHORT).show();

            }
        });
        return  v;
    }
}
