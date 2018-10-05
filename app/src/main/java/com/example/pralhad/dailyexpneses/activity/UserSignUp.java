package com.example.pralhad.dailyexpneses.activity;

import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.TextInputEditText;
//import android.support.design.widget.TextInputLayout;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.dataExchange.UsersDataSource;
import com.example.pralhad.dailyexpneses.extra.Constant;
import com.example.pralhad.dailyexpneses.extra.SharedVariable;
import com.example.pralhad.dailyexpneses.extra.Validation;
import com.example.pralhad.dailyexpneses.fragment.SimpleDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class UserSignUp extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText userContact, userPassword; //userContact,
    private TextInputLayout inputContact, inputPassword;
    private String usrContact, usrPassword, userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        userContact = (TextInputEditText) findViewById(R.id.user_contact);
        userPassword = (TextInputEditText) findViewById(R.id.user_password);

        inputContact = (TextInputLayout) findViewById(R.id.input_contact);
        inputPassword = (TextInputLayout) findViewById(R.id.input_password);


        //user create userRegisterBtn
        Button userRegisterBtn = (Button) findViewById(R.id.user_register_btn);
        userRegisterBtn.setOnClickListener(this);

        //user flat_icon_login button
        Button loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);

        if (getIntent().getExtras() != null) {
            userContact.setText(getIntent().getStringExtra("usrContact"));
            userPassword.setText(getIntent().getStringExtra("usrPassword"));
            userName = getIntent().getStringExtra("userName");
        }
    }

    public boolean checkValidation() {
        usrContact = userContact.getText() + "";
        usrPassword = userPassword.getText() + "";

        if (Validation.contactValidation(usrContact)) {
            inputContact.setErrorEnabled(false);
        } else {
            inputContact.setError(getResources().getString(R.string.error_message_contact));
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
        return true;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.user_register_btn:
                startActivity(new Intent(UserSignUp.this, UserRegistration.class));

                break;
            case R.id.login_btn:
                if (checkValidation()) {
                    if (new UsersDataSource(MainActivity.dataSource).userAuthentication(usrContact, usrPassword)) {
                        setPrefix();
                        lunchActivity();
                    } else
                        showAlertDialog();
                }
                break;
        }
    }

    private void setPrefix() {
        MainActivity.dataSource.sPref.setUserContact(usrContact);
        MainActivity.dataSource.sPref.setUserPassword(usrPassword);
        MainActivity.dataSource.sPref.setUserName(userName);
    }

    private void lunchActivity() {
        startActivity(new Intent(UserSignUp.this, MainActivity.class));
        finish();
    }

    private void showAlertDialog() {
        String title, message;
        title = getResources().getString(R.string.alert_message);
        message = getResources().getString(R.string.error_valid_email_password);
        FragmentManager fm = getSupportFragmentManager();
        SimpleDialog alertDialog = SimpleDialog.newInstance(title, message, true);
        alertDialog.show(fm, Constant.SIMPLE_DIALOG);
    }
}


//thi is used for animation

//                Intent intent = new Intent(UserSignUp.this, UserRegistration.class);
//                Pair[] pairs = new Pair[3];
//                pairs[0] = new Pair<View, String>(cmpLogo, "cmpLogo");
//                pairs[1] = new Pair<View, String>(userContact, "userContact");
//                pairs[2] = new Pair<View, String>(userPassword, "userPassword");
//
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserSignUp.this, pairs);
//                startActivity(intent,options.toBundle());


//                Intent intent = new Intent(UserSignUp.this, UserRegistration.class);
//                // Pass data object in the bundle and populate details activity.
//                ActivityOptionsCompat options = ActivityOptionsCompat.
//                        makeSceneTransitionAnimation(UserSignUp.this, (View)cmpLogo, "cmpLogo");
//                startActivity(intent, options.toBundle());
