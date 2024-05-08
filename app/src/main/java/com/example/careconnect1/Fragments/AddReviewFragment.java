package com.example.careconnect1.Fragments;

import static com.example.careconnect1.Utilities.Config.IP;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.UserData;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddReviewFragment extends Fragment {
    private UserData userData;
    private EditText text;
    String user_id, provider_id, book_id;

    private MaterialButton btn_add;

    public AddReviewFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userData = new UserData(requireContext());
        text = view.findViewById(R.id.text);
        btn_add = view.findViewById(R.id.btn_add);

        Bundle bundle = requireActivity().getIntent().getExtras();
        if (bundle != null) {
            provider_id = bundle.getString("provider_id", "");
            book_id = bundle.getString("book_id", "");
        }
        user_id = userData.getId();

        btn_add.setOnClickListener(v -> add());
    }

    private void add() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP + "add_review.php", response -> {
            if (!response.toLowerCase(Locale.ROOT).contains("success")) {
                Toast.makeText(requireContext(), response.trim(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "New review added successfully", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        }, error -> Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("text", text.getText().toString());
                map.put("provider_id", provider_id);
                map.put("book_id", book_id);
                map.put("user_id", userData.getId());
                map.put("date", getCurrentDate());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    private String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return df.format(c);
    }
}
