package com.example.pralhad.dailyexpneses.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.data_source.UsersDataSource;
import com.example.pralhad.dailyexpneses.general.SharedVariable;
import com.example.pralhad.dailyexpneses.general.Validation;
import com.example.pralhad.dailyexpneses.model_class.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

//import android.support.design.widget.TextInputEditText;
//import android.support.design.widget.TextInputLayout;
//import android.support.v7.app.AppCompatActivity;

public class UserRegistration extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText userFirstName, userLastName, userContact, userPassword, userConfirmUserPassword;
    private TextInputLayout inputContact, inputPassword, inputConfirmPassword, inputFirstName, inputLastName;
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
        // check validation.

        if (Validation.nameValidation(userFirstName.getText().toString().trim())) {
            inputFirstName.setErrorEnabled(false);
        } else {
            inputFirstName.setError(getResources().getString(R.string.error_message_first_name));
            SharedVariable.hideKeyboardFrom(userFirstName, this);
            return false;
        }

        if (Validation.nameValidation(userLastName.getText().toString().trim())) {
            inputLastName.setErrorEnabled(false);
        } else {
            inputLastName.setError(getResources().getString(R.string.error_message_last_name));
            SharedVariable.hideKeyboardFrom(userLastName, this);
            return false;
        }

        if (Validation.contactValidation(userContact.getText().toString().trim(), inputContact, this))
            inputContact.setErrorEnabled(false);
        else {
            SharedVariable.hideKeyboardFrom(userContact, this);
            return false;
        }

        if (Validation.passwordValidation(userPassword.getText().toString())) {
            inputPassword.setErrorEnabled(false);
        } else {
            inputPassword.setError(getResources().getString(R.string.error_message_password));
            SharedVariable.hideKeyboardFrom(userContact, this);
            return false;
        }

        if (userConfirmUserPassword.getText().toString().equals(userPassword.getText().toString())) {
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
    private User addUser(){
        User user = new User();
        user.setUserFName(userFirstName.getText().toString().trim());
        user.setUserLName(userLastName.getText().toString().trim());
        user.setUserContact(userContact.getText().toString().trim());
        user.setUserPassword(userPassword.getText() + "");
        long returnValue = new UsersDataSource(MainActivity.dataSource).createUser(user);
        return user;
    }

    private void redirectLogin(User user){
        Intent intent = new Intent(getBaseContext(), UserSignUp.class);
        intent.putExtra("usrContact", user.getUserContact());
        intent.putExtra("usrPassword", user.getUserPassword());
        intent.putExtra("userName", user.getUserFName() + " " + user.getUserLName());
        startActivity(intent);
        finish();
    }
}
