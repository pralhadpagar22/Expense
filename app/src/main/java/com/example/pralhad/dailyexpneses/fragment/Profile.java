package com.example.pralhad.dailyexpneses.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.MainActivity;

public class Profile extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ((TextView)view.findViewById(R.id.user_name)).setText(MainActivity.dataSource.sPref.getUserName());
        ((TextView)view.findViewById(R.id.user_contact)).setText(MainActivity.dataSource.sPref.getUserContact());
        ((TextView)view.findViewById(R.id.user_email)).setText(MainActivity.dataSource.sPref.getUserEmail());
        return view;
    }
}
