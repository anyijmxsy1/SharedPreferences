package com.geili.sharedpreferences;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.geili.sharedpreferences.Util.BosUtils;

import okhttp3.internal.Util;

public class MainActivity extends AppCompatActivity {
    Button saveData,restoreData;

    private String endpoint = "http://bj.bcebos.com";
    private String ak = "EEdfea963ed6544446235cc168976715";
    private String sk = "5d1246f907a0a81cba98c06d72d9ac78";
    private String bucketName = "geiliweike";
    private String path = "/data/data/com.geili.sharedpreferences/shared_prefs/data.xml";
    private String fileName = "data.xml";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveData = (Button) findViewById(R.id.save_data);
        restoreData = (Button) findViewById(R.id.restore_data);

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("name", "Samon");
                editor.putInt("age",28);
                editor.putBoolean("married",false);
                editor.apply();
            }
        });

        restoreData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
                String name = preferences.getString("name", "");
                int age = preferences.getInt("age", 0);
                boolean married = preferences.getBoolean("married", false);
                Log.d("xsy2","message:"+name+","+age+","+married);
                BosUtils.BosClienthandle(ak,sk,endpoint,bucketName,path,fileName);

            }
        });
    }
}
