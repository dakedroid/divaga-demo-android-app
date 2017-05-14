package com.example.kandksolutions.divaga;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kandksolutions.divaga.Helpers.BaseActivity;
import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class RestaurarClaveActivity extends BaseActivity {


    private static final String TAG = "RestaurarClaveActivity";


    @InjectView(R.id.field_email) EditText mEmailField;
    @InjectView(R.id._loginButton) Button _loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_recuperar_clave);
        ButterKnife.inject(this);


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Restore(mEmailField.getText().toString());
            }
        });

    }

    void Restore (String emailAddress){

        showProgressDialog();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            hideProgressDialog();
                            Intent intent = new Intent(RestaurarClaveActivity.this, IniciarSesionActivity.class);
                            startActivity(intent);
                        }

                        if(!task.isSuccessful()){

                            Toast.makeText(RestaurarClaveActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }



}
