package com.example.pralhad.dailyexpneses.activity;

import android.content.Intent;
//import android.support.design.widget.TextInputEditText;
//import android.support.design.widget.TextInputLayout;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.dataExchange.UsersDataSource;
import com.example.pralhad.dailyexpneses.extra.SharedVariable;
import com.example.pralhad.dailyexpneses.extra.Validation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class UserRegistration extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText userFirstName, userLastName, userContact, userPassword, userConfirmUserPassword;
    private TextInputLayout inputContact, inputPassword, inputConfirmPassword, inputFirstName, inputLastName;
    private String usrContact, usrPassword, usrFirstName, usrLastName, usrConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        //set text field.
        userFirstName = (TextInputEditText) findViewById(R.id.user_first_name);
        userLastName = (TextInputEditText) findViewById(R.id.user_last_name);
        userContact = (TextInputEditText) findViewById(R.id.user_contact);
        userPassword = (TextInputEditText) findViewById(R.id.user_password);
        userConfirmUserPassword = (TextInputEditText) findViewById(R.id.confirm_user_password);

        //set input text field.
        inputFirstName = (TextInputLayout) findViewById(R.id.input_first_name);
        inputLastName = (TextInputLayout) findViewById(R.id.input_last_name);
        inputContact = (TextInputLayout) findViewById(R.id.input_contact);
        inputPassword = (TextInputLayout) findViewById(R.id.input_password);
        inputConfirmPassword = (TextInputLayout) findViewById(R.id.input_confirm_password);

        //set button listener.
        Button registerBtn = (Button) findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(this);
    }

    public boolean checkValidation() {
        // variable initialize
        usrFirstName = userFirstName.getText() + "";
        usrLastName = userLastName.getText() + "";
        usrContact = userContact.getText() + "";
        usrPassword = userPassword.getText() + "";
        usrConfirmPassword = userConfirmUserPassword.getText() + "";


        // check validation.

        if (Validation.nameValidation(usrFirstName)) {
            inputFirstName.setErrorEnabled(false);
        } else {
            inputFirstName.setError(getResources().getString(R.string.error_message_first_name));
            SharedVariable.hideKeyboardFrom(userFirstName, this);
            return false;
        }

        if (Validation.nameValidation(usrLastName)) {
            inputLastName.setErrorEnabled(false);
        } else {
            inputLastName.setError(getResources().getString(R.string.error_message_last_name));
            SharedVariable.hideKeyboardFrom(userLastName, this);
            return false;
        }

        if (Validation.contactValidation(usrContact, inputContact, this))
            inputContact.setErrorEnabled(false);
        else {
            SharedVariable.hideKeyboardFrom(userContact, this);
            return false;
        }

        if (Validation.passwordValidation(usrPassword)) {
            inputPassword.setErrorEnabled(false);
        } else {
            inputPassword.setError(getResources().getString(R.string.error_message_password));
            SharedVariable.hideKeyboardFrom(userContact, this);
            return false;
        }

        if (usrConfirmPassword.equals(usrPassword)) {
            inputConfirmPassword.setErrorEnabled(false);

        } else {
            inputConfirmPassword.setError(getResources().getString(R.string.error_message_confirm_password));
            SharedVariable.hideKeyboardFrom(userConfirmUserPassword, this);
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn:
                if (checkValidation()) {
                    redirectLogin(addUser());
                }
                break;
        }
    }
    private Map<String, String> addUser(){
        Map<String, String> user = new HashMap<>();
        user.put("usrFirstName", usrFirstName);
        user.put("usrLastName", usrLastName);
        user.put("usrContact", usrContact);
        user.put("usrPassword", usrPassword);
        long returnValue = new UsersDataSource(MainActivity.dataSource).createUser(user);
        Log.i("***query result", returnValue + "");
        return user;
    }

    private void redirectLogin( Map<String, String> user){
        Intent intent = new Intent(getBaseContext(), UserSignUp.class);
        intent.putExtra("usrContact", user.get("usrContact"));
        intent.putExtra("usrPassword", user.get("usrPassword"));
        intent.putExtra("userName", user.get("usrFirstName") + " " + user.get("usrLastName"));
        startActivity(intent);
        finish();
    }
}
