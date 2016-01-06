package com.example.dkotsopoulos.testdiploma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ChooseTransportMode extends Activity {


    RadioGroup radioGroup;
    RadioGroup radioGroupLocation;
    RadioButton Driving;
    RadioButton HomeToWork;
    RadioButton WorkToHome;
    RadioButton OTher;
    RadioButton Walking;
    RadioButton PublicTransport;
    String selectedType="";
    String selectedTypeLocation;
    Button Submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_choose_transport_mode);

        radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonDrive) {
                    Toast.makeText(getApplicationContext(), "Choice: Driving", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.radioButtonWalk) {
                    Toast.makeText(getApplicationContext(), "Choice: Walking", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Choice: Transit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        radioGroupLocation = (RadioGroup) findViewById(R.id.myRadioGroupLocation);
        radioGroupLocation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioButtonHomeToWork) {
                    Toast.makeText(getApplicationContext(), "Choice: Home to Work",Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radioButtonWorkToHome) {
                    Toast.makeText(getApplicationContext(), "Choice: Work to Home",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Choice: Other",Toast.LENGTH_SHORT).show();
                }
            }
        });


        Driving = (RadioButton) findViewById(R.id.radioButtonDrive);
        Walking = (RadioButton) findViewById(R.id.radioButtonWalk);
        PublicTransport = (RadioButton) findViewById(R.id.radioButtonPT);
        HomeToWork = (RadioButton) findViewById(R.id.radioButtonHomeToWork);
        WorkToHome = (RadioButton) findViewById(R.id.radioButtonWorkToHome);
        OTher = (RadioButton) findViewById(R.id.radioButtonOther);
        Submit=(Button) findViewById(R.id.Radiosubmitbutton);
        Submit.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                // find which location radioButton is checked by id
                int selectedIdlocation = radioGroupLocation.getCheckedRadioButtonId();
                if (selectedIdlocation == HomeToWork.getId()) {
                    selectedTypeLocation= "0";
                } else if (selectedIdlocation == WorkToHome.getId()) {
                    selectedTypeLocation="1";
                }else if (selectedIdlocation == OTher.getId()) {
                    selectedTypeLocation="2";
                }
                // find which transport mode radioButton is checked by id
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == Driving.getId()) {
                    selectedType="driving";
                } else if (selectedId == Walking.getId()) {
                    selectedType="walking";
                }else if (selectedId == PublicTransport.getId()) {
                    selectedType="transit";
                }
            startActivity(new Intent(ChooseTransportMode.this, PublicTransportActivity.class).putExtra("TransportMode", selectedType+","+selectedTypeLocation));
            }
        });
        super.onCreate(savedInstanceState);
        }
}
