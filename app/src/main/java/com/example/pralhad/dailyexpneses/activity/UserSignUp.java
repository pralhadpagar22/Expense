package com.example.pralhad.dailyexpneses.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.data_source.UsersDataSource;
import com.example.pralhad.dailyexpneses.fragment.SimpleAlertDialog;
import com.example.pralhad.dailyexpneses.general.Constants;
import com.example.pralhad.dailyexpneses.model_class.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

//import android.support.design.widget.TextInputEditText;
//import android.support.design.widget.TextInputLayout;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.app.AppCompatActivity;

public class UserSignUp extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText userContact, userPassword; //userContact,
    public TextInputLayout inputContact, inputPassword;
    private static User user = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        userContact = (TextInputEditText) findViewById(R.id.user_contact);
        userPassword = (TextInputEditText) findViewById(R.id.user_password);

        inputContact = (TextInputLayout) findViewById(R.id.input_contact);
        inputPassword = (TextInputLayout) findViewById(R.id.input_password);


        //User create userRegisterBtn
        Button userRegisterBtn = (Button) findViewById(R.id.user_register_btn);
        userRegisterBtn.setOnClickListener(this);

        //User flat_icon_login button
        Button loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);

        if (getIntent().getExtras() != null) {
            user = (User)getIntent().getSerializableExtra("user");
            userContact.setText(user.getUserContact());
            userPassword.setText(user.getUserPassword());
            //userName = user.getUserName();
        } else user = new User();
    }

    public void setData() {
        user.setUserContact(userContact.getText() != null ? userContact.getText().toString() : "");
        user.setUserPassword(userPassword.getText() != null ? userPassword.getText().toString() : "");
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
                finish();
                break;
            case R.id.login_btn:
                setData();
                if (user.validate(null, this)) {
                    UsersDataSource usersDataSource = new UsersDataSource(MainActivity.dataSource);
                    if (usersDataSource.userAuthentication(user) != null) {
                        MainActivity.dataSource.setApplicationData(user);
                        lunchActivity();
                    } else
                        showAlertDialog();
                }
                break;
        }
    }

    private void lunchActivity() {
        startActivity(new Intent(UserSignUp.this, MainActivity.class));
        finish();
    }

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SimpleAlertDialog alertDialog = SimpleAlertDialog.newInstance(R.string.alert_message, getResources().getString(R.string.error_valid_email_password));
        alertDialog.show(fm, Constants.SIMPLE_DIALOG);
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
