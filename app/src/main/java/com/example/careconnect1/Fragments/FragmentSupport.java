package com.example.careconnect1.Fragments;

import static com.example.careconnect1.Utilities.Config.IP;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import androidx.fragment.app.Fragment;
import com.example.careconnect1.R;

public class FragmentSupport extends Fragment {
    private  WebView webView;


    public FragmentSupport() {
        // Required empty public constructor
    }


    public static FragmentSupport newInstance() {

        return new FragmentSupport();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_support, container, false);
        webView=v.findViewById(R.id.webView);
        webView.loadUrl(IP + "support.html");

        return v;
    }
}