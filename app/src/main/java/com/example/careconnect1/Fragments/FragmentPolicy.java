package com.example.careconnect1.Fragments;

import static com.example.careconnect1.Utilities.Config.IP;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import com.example.careconnect1.R;

public class FragmentPolicy extends Fragment {
    private WebView webViewPolicy;

    public FragmentPolicy() {
        // Required empty public constructor
    }

    public static FragmentPolicy newInstance() {

        return new FragmentPolicy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_policy, container, false);
        webViewPolicy = v.findViewById(R.id.webViewPolicy);
        webViewPolicy.loadUrl(IP + "privacy.html");

        return v;
    }
}