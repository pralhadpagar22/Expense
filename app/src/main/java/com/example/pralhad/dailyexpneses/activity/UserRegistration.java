package com.example.pralhad.dailyexpneses.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.data_source.UsersDataSource;
import com.example.pralhad.dailyexpneses.general.Constants;
import com.example.pralhad.dailyexpneses.general.SharedVariable;
import com.example.pralhad.dailyexpneses.model_class.User;
import com.example.pralhad.dailyexpneses.project_db.DBExpenses;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Timestamp;

import static com.example.pralhad.dailyexpneses.activity.MainActivity.dataSource;

//import android.support.design.widget.TextInputEditText;
//import android.support.design.widget.TextInputLayout;
//import android.support.v7.app.AppCompatActivity;

public class UserRegistration extends AppCompatActivity implements View.OnClickListener {

    public TextInputEditText userName, userEmail, userContact, userPassword, userConfirmUserPassword;
    public TextInputLayout inputName, inputEmail, inputContact, inputPassword, inputConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);


        //set EditText field.
        userName = findViewById(R.id.user_name);
        userContact = findViewById(R.id.user_contact);
        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_password);
        userConfirmUserPassword = findViewById(R.id.confirm_user_password);
        //set drawable Dynamic color on focusable, Error or unelected For EditText
        SharedVariable.setOnFocusChangeToET(userName, getResources().getDrawable(R.drawable.outline_person_pin_black), this);
        SharedVariable.setOnFocusChangeToET(userContact, getResources().getDrawable(R.drawable.outline_call_black), this);
        SharedVariable.setOnFocusChangeToET(userEmail, getResources().getDrawable(R.drawable.outline_email_black), this);
//        SharedVariable.setOnFocusChangeToET(userPassword, getResources().getDrawable(R.drawable.outline_lock_black), this);
//        SharedVariable.setOnFocusChangeToET(userConfirmUserPassword, getResources().getDrawable(R.drawable.outline_screen_lock_rotation_black), this);

        //set input text field.
        inputName = findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);
        inputContact = findViewById(R.id.input_contact);
        inputPassword = findViewById(R.id.input_password);
        inputConfirmPassword = findViewById(R.id.input_confirm_password);

        //set button listener.
        Button registerBtn = (Button) findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register_btn) {
            User user = new User();
            setUserData(user);
            if (user.validate(user, UserRegistration.this)) {
                redirectLogin(user);
            }
        }
    }

    private void redirectLogin(User user) {
            UsersDataSource usersDataSource = new UsersDataSource(dataSource);
            if (usersDataSource.createUser(user) != null) {
                usersDataSource.showData();
                dataSource.setApplicationData(user);
                lunchActivity();
            }
    }

    private void lunchActivity() {
        startActivity(new Intent(UserRegistration.this, MainActivity.class));
        finish();
    }

    public void setUserData(User user) {
        String currentTime = dataSource.getCurrentTimeUTC();
        user.setUserName(userName.getText() != null ? userName.getText().toString() : null);
        user.setUserContact(userContact.getText() != null ? userContact.getText().toString() : null);
        user.setUserEmail(userEmail.getText() != null ? userEmail.getText().toString() : null);
        user.setUserPassword(userPassword.getText() != null ? userPassword.getText().toString() : null);
        user.setUpdated_on(Timestamp.valueOf(currentTime));
        user.setCreated_on(Timestamp.valueOf(currentTime));
        user.setIsActive(DBExpenses.ACTIVE);
        Intent intent = getIntent();
        intent.putExtra("user", user);
        intent.putExtra(Constants.PhoneNumber, user.getUserContact());
        setIntent(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        invalidateOptionsMenu();
//        menu.findItem(R.id.action_settings).setVisible(true);
//        return super.onPrepareOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}