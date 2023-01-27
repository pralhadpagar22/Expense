package com.example.pralhad.dailyexpneses.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.general.GraphView;

import java.util.ArrayList;
import java.util.Date;

//import android.support.v4.app.Fragment;

public class Dashboard extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Spinner barChartSpinner, pieChartSpinner;
    private View root;

    public static Dashboard newInstance() {
        Dashboard fragment = new Dashboard();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        TextView remainBalance = root.findViewById(R.id.remain_balance);
        remainBalance.setText(String.valueOf(MainActivity.dataSource.sPref.getRemainingAmount()));
        setupGraph();
        return root;
    }

    public void setupGraph() {
        //bar chart header and year spinner
        TextView barChartHeader = (TextView) root.findViewById(R.id.bar_chart_header);
        barChartSpinner = (Spinner) root.findViewById(R.id.bar_chart_spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, getDateList());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        barChartSpinner.setAdapter(spinnerArrayAdapter);
        barChartSpinner.setOnItemSelectedListener(this);


        //pie chart header and year spinner
//        TextView pieChartHeader = (TextView) root.findViewById(R.id.pieChartHeader);
//        pieChartSpinner = (Spinner) root.findViewById(R.id.pieChartSpinner);
//        pieChartSpinner.setAdapter(spinnerArrayAdapter);
//        pieChartSpinner.setOnItemSelectedListener(this);
//        pieChartHeader.setText("Total " + inTypeInvoiceString + " Amount");
    }

    private ArrayList<String> getDateList() {
        ArrayList arrayYearList = new ArrayList();
        String charSequence = (String) DateFormat.format("yyyy", new Date().getTime());
        for (int i = Integer.parseInt(charSequence); i >= 2015; i--) {
            arrayYearList.add(String.valueOf(i));
        }
        return arrayYearList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        if (spinner.getId() == R.id.bar_chart_spinner)
            new GraphView(adapterView.getItemAtPosition(i).toString(), root, getActivity(), GraphView.GRAPH_TYPE_BAR);
//            else if (spinner.getId() == R.id.pieChartSpinner)
//                new GraphView(adapterView.getItemAtPosition(i).toString(), root, getActivity(), GraphView.GRAPH_TYPE_PIE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
