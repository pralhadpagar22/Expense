package com.example.pralhad.dailyexpneses.model_class;

import android.app.Activity;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.UserRegistration;
import com.example.pralhad.dailyexpneses.activity.UserSignUp;
import com.example.pralhad.dailyexpneses.general.SharedVariable;
import com.example.pralhad.dailyexpneses.general.Validation;

import java.io.Serializable;
import java.math.BigInteger;

public class User extends Table implements Serializable {
    private BigInteger userId;
    private String userName;
    private String userContact;
    private String userEmail;
    private String userPassword;

    public User(User user) {
        this.userId = user.userId;
        this.userName = user.userName;
        this.userContact = user.userContact;
        this.userEmail = user.userEmail;
        this.userPassword = user.userPassword;
    }

    public User() {
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean validate(User user, Activity activity) {
        if (activity instanceof UserRegistration) {
            if (!user.getUserName().equals("") && user.getUserName().length() > 2) {
                ((UserRegistration) activity).inputName.setErrorEnabled(false);
            } else {
                ((UserRegistration) activity).inputName.setError((activity.getResources().getString(R.string.error_message_first_name)));
                SharedVariable.hideKeyboardFrom(((UserRegistration) activity).userName, activity);
                //SharedVariable.setDrawableColor(activity.getResources().getDrawable(R.drawable.outline_person_pin_black), Color.RED, ((UserRegistration)activity).userName);
                return false;
            }

            if (Validation.contactValidation(user.getUserContact(), ((UserRegistration) activity).inputContact, activity))
                ((UserRegistration) activity).inputContact.setErrorEnabled(false);
            else {
                SharedVariable.hideKeyboardFrom(((UserRegistration) activity).userContact, activity);
                //SharedVariable.setDrawableColor(activity.getResources().getDrawable(R.drawable.outline_call_black), Color.RED, ((UserRegistration)activity).userContact);
                return false;
            }

            if (Validation.emailValidation(user.getUserEmail())) {
                ((UserRegistration) activity).inputEmail.setErrorEnabled(false);
            } else {
                ((UserRegistration) activity).inputEmail.setError(activity.getResources().getString(R.string.error_message_last_name));
                SharedVariable.hideKeyboardFrom(((UserRegistration) activity).userEmail, activity);
                //SharedVariable.setDrawableColor(activity.getResources().getDrawable(R.drawable.outline_email_black), Color.RED, ((UserRegistration)activity).userEmail);
                return false;
            }

            if (!user.getUserPassword().equals("") && user.getUserPassword().length() > 6) {
                ((UserRegistration) activity).inputPassword.setErrorEnabled(false);
            } else {
                ((UserRegistration) activity).inputPassword.setError(activity.getResources().getString(R.string.error_message_password));
                SharedVariable.hideKeyboardFrom(((UserRegistration) activity).userContact, activity);
                //SharedVariable.setDrawableColor(activity.getResources().getDrawable(R.drawable.outline_lock_black), Color.RED, ((UserRegistration)activity).userContact);
                return false;
            }

            if (((UserRegistration) activity).userConfirmUserPassword.getText().toString().equals(user.getUserPassword())) {
                ((UserRegistration) activity).inputConfirmPassword.setErrorEnabled(false);

            } else {
                ((UserRegistration) activity).inputConfirmPassword.setError(activity.getResources().getString(R.string.error_message_confirm_password));
                SharedVariable.hideKeyboardFrom(((UserRegistration) activity).userConfirmUserPassword, activity);
                //SharedVariable.setDrawableColor(activity.getResources().getDrawable(R.drawable.outline_screen_lock_rotation_black), Color.RED, ((UserRegistration)activity).userConfirmUserPassword);
                return false;
            }
        } else {
            if (!userContact.equals("") && (userContact.length() > 2 && userContact.length() < 25)) {
                ((UserSignUp) activity).inputContact.setErrorEnabled(false);
            } else {
                ((UserSignUp) activity).inputContact.setError(activity.getResources().getString(R.string.error_message_contact));
                //SharedVariable.hideKeyboardFrom(userContact, activity);
                return false;
            }

            if (!userPassword.equals("") && userPassword.length() > 6) {
                ((UserSignUp) activity).inputPassword.setErrorEnabled(false);
            } else {
                ((UserSignUp) activity).inputPassword.setError(activity.getResources().getString(R.string.error_message_password));
                //SharedVariable.hideKeyboardFrom(userContact, this);
                return false;
            }
        }

        return true;
    }
}
