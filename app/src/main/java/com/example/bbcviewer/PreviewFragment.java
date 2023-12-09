package com.example.bbcviewer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviewFragment extends Fragment {//Fragment used to display info and actions that pertain to article selected

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView t1 = getView().findViewById(R.id.tv1);
    TextView t2 = getView().findViewById(R.id.tv2);
    Button bt1 = getView().findViewById(R.id.b1);
    Button bt2 = getView().findViewById(R.id.b2);

    public PreviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreviewFragment newInstance(String param1, String param2) {
        PreviewFragment fragment = new PreviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        return inflater.inflate(R.layout.fragment_layout, container, false);
    }
    @Override
    public void onAttach(Context context) {//loads shared preferences before layout inflated
        super.onAttach(context);
        SharedPreferences prefs = context.getSharedPreferences("prefs", 0);
        String title1 = prefs.getString("title", " ");
        String desc1 = prefs.getString("desc", " ");
        String link1 = prefs.getString("uri", " ");
        Uri link = Uri.parse(link1);
        t1.setText(title1);
        t2.setText(desc1);
        bt1.setOnClickListener((click) ->{//Clicking button takes you to article on the website
            Intent intent = new Intent(Intent.ACTION_VIEW, link);
            startActivity(intent);
        });
        bt2.setOnClickListener((click) ->{//Button 2 adds data items to faves database on-click

            DBHelper dbHelper = new DBHelper(this.getActivity());

            dbHelper.addFave(title1, desc1, link1);
        });
    }
}