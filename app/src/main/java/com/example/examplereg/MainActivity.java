package com.example.examplereg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.util.VKUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference myRef;
    private HashMap<String,String> DiscrTasks;
    private String[] scope = new String[]{VKScope.EMAIL};

    Button registration;
    Button login;
    ImageButton VKbutton;

    EditText log;
    EditText pass;
    TextView label;



    public void updateUI()
    {
        myRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        String str= user.getUid().toString();

       /* FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("UvbprWzQtohKYAUjCllGsUt9OeB2");

        //myRef.setValue("Hello, World!");*/
        myRef.child(str).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String,String>> t = new GenericTypeIndicator<HashMap<String,String>>() {};
                DiscrTasks = dataSnapshot.getValue(t);
                Intent MainActivity2 = new Intent(MainActivity.this, MainActivity_Navigation.class);
                MainActivity2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                MainActivity2.putExtra("first_name", ""+DiscrTasks.get("first_name").toString());
                MainActivity2.putExtra("second_name", ""+DiscrTasks.get("second_name").toString());
                MainActivity2.putExtra("Flag", "mAuth");

                startActivity(MainActivity2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myRef = FirebaseDatabase.getInstance().getReference();


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
                                FirebaseUser user = mAuth.getCurrentUser();
                                String str= user.getUid().toString();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference(str);

                                myRef.child("first_name").setValue("InsertName");
                                myRef.child("second_name").setValue("InsertName");
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

                            updateUI();
                            Toast.makeText( MainActivity.this, "Aвторизация успешна", Toast.LENGTH_SHORT).show();

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
    private VKAccessToken AccessTOKEN;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
// Пользователь успешно авторизовался

                Toast.makeText(MainActivity.this, "Aвторизация успешна", Toast.LENGTH_SHORT).show();
                AccessTOKEN =res;
                VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,"first_name, last_name"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        JSONObject jObject=new JSONObject();
                        String firstname="InsertName",secondname="InsertName",id="nul";
                        try {

                             jObject = new JSONObject(response.responseString);
                             JSONArray jsonArray = jObject.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                firstname = jsonObject.getString("first_name");
                                secondname = jsonObject.getString("last_name");
                                id = jsonObject.getString("id");

                            }
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            String var = AccessTOKEN.accessToken.toString();
                            DatabaseReference myRef = database.getReference(id);

                            myRef.child("first_name").setValue(firstname);
                            myRef.child("second_name").setValue(secondname);

                            Intent MainActivity2 = new Intent(MainActivity.this, MainActivity_Navigation.class);
                            MainActivity2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            MainActivity2.putExtra("first_name", ""+firstname);
                            MainActivity2.putExtra("second_name", ""+secondname);
                            MainActivity2.putExtra("id", id);
                            MainActivity2.putExtra("Flag", "VK");

                            startActivity(MainActivity2);
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "Ошибка получения имени фамилии", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onError(VKError error) {
//Do error stuff
                    }
                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
//I don't really believe in progress
                    }
                });

String var="22";
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

