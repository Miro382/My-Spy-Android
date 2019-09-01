package com.myspy.myspyandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.myspy.myspyandroid.functions.ClassSaver;
import com.myspy.myspyandroid.functions.Encryption;

public class ChangePasswordActivity extends AppCompatActivity {



    Encryption encryption = Encryption.getDefault("0GzyZdjKFMINl5vtC6rNjz5n9s", "vAlYXMwnrAJYlit5", new byte[16]);
    String decrypted = encryption.decryptOrNull(MySpyService.serviceSettings.EncodedPassword);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e)
        {
            Log.w("Error",""+e);
        }
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }



    public void ChangePassword(View view)
    {
        EditText oldpassword = (EditText) findViewById(R.id.editTextoldpassword);
        EditText password = (EditText) findViewById(R.id.editTextnewpassword);
        EditText passwordrepeat = (EditText) findViewById(R.id.editTextnewpasswordrepeat);

        if(oldpassword.getText().toString() != null && !oldpassword.getText().toString().isEmpty()) {

            if(oldpassword.getText().toString().equals(decrypted)) {
                Log.d("LoginActivity", "OK");

                //GOOD OLD PASSWORD


                if(password.getText().toString() != null && !password.getText().toString().isEmpty()) {

                    if (password.getText().toString().equals(passwordrepeat.getText().toString())) {

                        //No problem with new passwords
                        String encrypted = encryption.encryptOrNull(password.getText().toString());

                        MySpyService.serviceSettings.EncodedPassword = encrypted;
                        ClassSaver classSaver =  new ClassSaver();
                        classSaver.SaveClassToFile(MySpyService.serviceSettings,"MySpy","Settings.dat",this);
                        Log.d("ClassSaver","Saved");
                        Toast.makeText(this, "OK", Toast.LENGTH_LONG).show();
                        finish();

                    } else {
                        Toast.makeText(this, getResources().getString(R.string.NotEquals), Toast.LENGTH_LONG).show();
                        Log.d("StartActivity", "Passwords not equals");
                    }
                }else{
                    Toast.makeText(this, getResources().getString(R.string.EmptyPassword), Toast.LENGTH_LONG).show();
                    Log.d("StartActivity", "Passwords is empty");
                }



            }else{
                Toast.makeText(this, getResources().getString(R.string.BadPassword), Toast.LENGTH_LONG).show();
                Log.d("ChangePassword", "Passwords is bad");
            }

        }else{
            Toast.makeText(this, getResources().getString(R.string.EmptyPassword), Toast.LENGTH_LONG).show();
            Log.d("ChangePassword", "Passwords is empty");
        }


        //Toast.makeText(this,getResources().getString(R.string.PasswordChanged),Toast.LENGTH_LONG).show();
    }
}
