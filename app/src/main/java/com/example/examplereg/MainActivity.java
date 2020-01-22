package com.example.examplereg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String[] scope = new String[]{VKScope.EMAIL};

    Button registration;
    Button login;
    ImageButton VKbutton;

    EditText log;
    EditText pass;
    TextView label;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        //System.out.println(Arrays.asList(fingerprints));

        //VKSdk.login(this,scope);

        login = (Button) findViewById(R.id.login);
        registration = (Button) findViewById(R.id.registration);
        VKbutton = (ImageButton) findViewById(R.id.VKButton);

        log = (EditText)findViewById((R.id.log));
        pass = (EditText)findViewById((R.id.pass));
        label = (TextView)findViewById((R.id.label));
        //если в сессии произошли изменение
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in


                } else {
                    // User is signed out

                }

            }
        };
        //клик регистрации
        View.OnClickListener clickregistration = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            mAuth.createUserWithEmailAndPassword(log.getText().toString(), pass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {

                                Toast.makeText(MainActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();

                            }
                            else
                                Toast.makeText(MainActivity.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    };

      // клик логин
        View.OnClickListener clicklogin = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mAuth.signInWithEmailAndPassword(log.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent MainActivity2 = new Intent(MainActivity.this, MainActivity2.class);
                            MainActivity2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(MainActivity2);
                            Toast.makeText(MainActivity.this, "Aвторизация успешна", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(MainActivity.this, "Aвторизация провалена", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        };


        View.OnClickListener clickVK = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
                {
                VKSdk.login(MainActivity.this,scope);
            }
        };

        VKbutton.setOnClickListener(clickVK);
        registration.setOnClickListener(clickregistration);
        login.setOnClickListener(clicklogin);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
// Пользователь успешно авторизовался
                Intent MainActivity2 = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(MainActivity2);
                Toast.makeText(MainActivity.this, "Aвторизация успешна", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(MainActivity.this, "Aвторизация провалена", Toast.LENGTH_SHORT).show();
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



}

