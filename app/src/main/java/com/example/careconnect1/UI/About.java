package com.example.careconnect1.UI;
import android.os.Bundle;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;

public class About extends AppCompatClass {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setMethods(getString(R.string.about_us),"");

    }
}
