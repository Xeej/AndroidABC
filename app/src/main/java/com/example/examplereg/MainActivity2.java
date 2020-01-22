package com.example.examplereg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vk.sdk.VKSdk;

public class MainActivity2 extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button LogOut;

        LogOut=(Button) findViewById(R.id.LogOut);


        View.OnClickListener clickLogOut = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                VKSdk.logout();
                MainActivity2.this.finish();
            }
        };
        LogOut.setOnClickListener(clickLogOut);

    }
}
