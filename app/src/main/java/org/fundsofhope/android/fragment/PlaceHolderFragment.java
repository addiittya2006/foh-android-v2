package org.fundsofhope.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.fundsofhope.android.R;
import org.fundsofhope.android.model.Project;

import java.util.ArrayList;

/**
 * Created by anip on 02/08/16.
 */
public class PlaceHolderFragment extends Fragment {
        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
        private ArrayList<Project> project;

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceHolderFragment() {
        }

        public static PlaceHolderFragment newInstance(int sectionNumber) {
            PlaceHolderFragment fragment = new PlaceHolderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
//            mLayoutManager = new LinearLayoutManager(rootView.getContext());
//            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            return rootView;
        }

}
