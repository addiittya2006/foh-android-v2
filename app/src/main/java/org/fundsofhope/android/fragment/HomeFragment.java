package org.fundsofhope.android.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fundsofhope.android.SplashActivity;
import org.fundsofhope.android.adapters.MyAdapter;
import org.fundsofhope.android.R;
import org.fundsofhope.android.config.ApiInterface;
import org.fundsofhope.android.config.ServiceGenerator;
import org.fundsofhope.android.model.Project;
import org.fundsofhope.android.util.NetworkUtilities;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by anip on 02/08/16.
 */
public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Project> project=new ArrayList<Project>();

    private static final String ARG_SECTION_NUMBER = "section_number";

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        mRecyclerView.setHasFixedSize(true);
        getProjects();

        // use a linear layout manager

        return rootView;
    }
    private void getProjects() {

        if (NetworkUtilities.isInternet(getActivity())) {
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            apiInterface.projects(
                    new retrofit.Callback<ArrayList<Project>>() {

                        @Override
                        public void success(ArrayList<Project> projects, Response response) {
                            mLayoutManager = new LinearLayoutManager(getActivity());
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            // specify an adapter (see also next example)
                            mAdapter = new MyAdapter(getActivity(),projects);
                            mRecyclerView.setAdapter(mAdapter);

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("TAG", "Response : Failure " + error.getMessage());
                        }
                    });
        } else {
//            DebugLog.d(getString(R.string.error_no_internet_connection));
        }
    }

}
