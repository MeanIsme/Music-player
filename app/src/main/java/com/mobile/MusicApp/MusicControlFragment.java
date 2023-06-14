package com.mobile.MusicApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicControlFragment extends Fragment {
    private static boolean isSmallController;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CONTROLLER_TYPE_KEY = "CONTROLLER_TYPE";



    public MusicControlFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MusicControlFragment newInstance(boolean isSmallController) {
        MusicControlFragment fragment = new MusicControlFragment();
        Bundle args = new Bundle();
        args.putBoolean(CONTROLLER_TYPE_KEY, isSmallController);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isSmallController = getArguments().getBoolean(CONTROLLER_TYPE_KEY, false);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int layout = isSmallController ? R.layout.fragment_music_control_small : R.layout.fragment_music_control;
        return inflater.inflate(layout, container, false);
    }
}