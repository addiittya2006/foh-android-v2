package org.fundsofhope.android.fragment;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.fundsofhope.android.ProjectDescription;
import org.fundsofhope.android.R;
import org.fundsofhope.android.adapters.NgoAdapter;
import org.fundsofhope.android.config.ApiInterface;
import org.fundsofhope.android.config.ServiceGenerator;
import org.fundsofhope.android.model.Ngo;
import org.fundsofhope.android.util.NetworkUtilities;
import org.fundsofhope.android.util.RecyclerItemClickListener;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by anip on 18/12/16.
 */
public class NgoFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Ngo> ngo = new ArrayList<Ngo>();
    public static  NgoFragment newInstance(){
        NgoFragment fragment = new NgoFragment();
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_project, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        mRecyclerView.setHasFixedSize(true);
        getNgo();
        return rootView;
    }
    private void getNgo() {

        if (NetworkUtilities.isInternet(getActivity())) {
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            apiInterface.ngo(
                    new retrofit.Callback<ArrayList<Ngo>>() {

                        @Override
                        public void success(final ArrayList<Ngo> ngo, Response response) {
                            mLayoutManager = new LinearLayoutManager(getActivity());
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            // specify an adapter (see also next example)
                            mAdapter = new NgoAdapter(getActivity(), ngo);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(getActivity(), ProjectDescription.class);
                                    Gson gson = new Gson();
                                    intent.putExtra("project",gson.toJson(ngo.get(position)));
                                    Log.i("hell_selected", String.valueOf(ngo.get(position).getName()));
                                    startActivityForResult(intent, 80);
                                }
                            }));
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
