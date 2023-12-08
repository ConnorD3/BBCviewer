package com.example.bbcviewer;

import static android.icu.text.MessagePattern.ArgType.SELECT;

import static com.example.bbcviewer.DBHelper.TABLE;
import static com.example.bbcviewer.DBHelper.URL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        SharedPreferences sp = this.getActivity().getSharedPreferences("prefs", 0);
        String title1 = sp.getString("title", "");
        String desc1 = sp.getString("desc", "");
        String link1 = sp.getString("uri", "");
        Uri link = Uri.parse(link1);
        TextView t1 = getView().findViewById(R.id.tv1);
        TextView t2 = getView().findViewById(R.id.tv2);
        Button bt1 = getView().findViewById(R.id.b1);
        Button bt2 = getView().findViewById(R.id.b2);
        t1.setText(title1);
        t2.setText(desc1);
        bt1.setOnClickListener((click) ->{
            Intent intent = new Intent(Intent.ACTION_VIEW, link);
            startActivity(intent);
        });
        bt2.setOnClickListener((click) ->{

           DBHelper dbHelper = new DBHelper(this.getActivity());

           dbHelper.addFave(title1, desc1, link1);
        });


        return inflater.inflate(R.layout.fragment_layout, container, false);
    }
}