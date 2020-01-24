package com.example.examplereg.ui.slideshow;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.example.examplereg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    Button SaveButton;
    TextView text_slideshow;
    EditText first_name, second_name;

    String Flag="nul",id="nul";
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);


        first_name = (EditText) root.findViewById(R.id.editText);
        second_name = (EditText)  root.findViewById(R.id.editText2);
        SaveButton = (Button) root.findViewById(R.id.button);

        Intent intent = getActivity().getIntent();
        first_name.setText(intent.getStringExtra("first_name"));
        second_name.setText(intent.getStringExtra("second_name"));
        Flag= intent.getStringExtra("Flag");
        id= intent.getStringExtra("id");
        mAuth = FirebaseAuth.getInstance();



        SaveButton.setOnClickListener(clickSaveButton);

        return root;

    }

    //клик сохранения
    View.OnClickListener clickSaveButton = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            String str;
            if (Flag.equals("mAuth")) {
                FirebaseUser user = mAuth.getCurrentUser();
                str = user.getUid();
            }
            else
            {
                str=id;
            }
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(str);

            myRef.child("Information").child("first_name").setValue(first_name.getText().toString());
            myRef.child("Information").child("second_name").setValue(second_name.getText().toString());
        }
    };
}