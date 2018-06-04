package com.example.hp.a3pointestimation;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

public class ThreePointEstimation extends Activity
        implements TextView.OnEditorActionListener,View.OnClickListener {

    //define variables from widgets
    private EditText optiEditText;
    private EditText nomiEditText;
    private EditText pessiEditText;
    private TextView meanTextView;
    private TextView sdTextView;
    private Button calculateButton;
    //shared preference object
    private SharedPreferences savedValues;

    //define instance variable that should be saved(where values would be stored)
    private String optiString = "";
    private String nomiString = "";
    private String pessiString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_point_estimation);

        //get id references
        optiEditText = (EditText) findViewById(R.id.optiEditText);
        nomiEditText = (EditText) findViewById(R.id.nomiEditText);
        pessiEditText = (EditText) findViewById(R.id.pessiEditText);
        meanTextView = (TextView) findViewById(R.id.meanTextView);
        sdTextView = (TextView) findViewById(R.id.sdTextView);
        calculateButton = (Button) findViewById(R.id.calculateButton);

        //set editor listener for action listener and on click
        pessiEditText.setOnEditorActionListener(this);
        nomiEditText.setOnEditorActionListener(this);
        pessiEditText.setOnEditorActionListener(this);
        calculateButton.setOnClickListener(this);
        //shared preference
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("optiString", optiString);
        editor.putString("nomiString", nomiString);
        editor.putString("pessiString", pessiString);
        editor.commit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        optiString = savedValues.getString("optiString", "");
        nomiString = savedValues.getString("nomiString", "");
        nomiString = savedValues.getString("pessiString", "");


        // set the bill amount on its widget
        optiEditText.setText(optiString);
        nomiEditText.setText(nomiString);
        pessiEditText.setText(pessiString);
        // calculate and display
        calculateAndDisplay();
        super.onResume();
    }

    public void calculateAndDisplay() {

        //get opti nomi and pessi values
        optiString = optiEditText.getText().toString();
        nomiString = nomiEditText.getText().toString();
        pessiString = pessiEditText.getText().toString();
        float optiValue;
        float nomiValue;
        float pessiValue;

        if (optiString.equals("")) {
            optiValue = 0;
        } else {
            optiValue = Float.parseFloat(optiString);
        }
        if (nomiString.equals("")) {
            nomiValue = 0;
        } else {
            nomiValue = Float.parseFloat(nomiString);
        }
        if (pessiString.equals("")) {
            pessiValue = 0;
        } else {
            pessiValue = Float.parseFloat(pessiString);
        }
        float mean = (optiValue + 4 * nomiValue + pessiValue) / 6;
        float standardDeviation = (pessiValue - optiValue) / 6;


        NumberFormat percent = NumberFormat.getInstance();
        meanTextView.setText(percent.format(mean));
        sdTextView.setText(percent.format(standardDeviation));

    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_NEXT
                || actionId == EditorInfo.IME_ACTION_DONE) {

            calculateAndDisplay();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calculateButton:
                calculateAndDisplay();
                break;
        }
    }
}