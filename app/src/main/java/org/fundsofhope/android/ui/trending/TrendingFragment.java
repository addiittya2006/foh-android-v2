package org.fundsofhope.android.ui.trending;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.fundsofhope.android.R;
import org.fundsofhope.android.data.api.ApiInterface;
import org.fundsofhope.android.data.model.Project;
import org.fundsofhope.android.ui.project.ProjectDescriptionActivity;
import org.fundsofhope.android.util.NetworkUtil;
import org.fundsofhope.android.util.RecyclerItemClickListener;
import org.fundsofhope.android.util.ServiceGeneratorUtil;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by anip on 19/12/16.
 */
public class TrendingFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static TrendingFragment newInstance() {
        TrendingFragment fragment = new TrendingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trending, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        getTrending();

        return rootView;
    }

    private void getTrending() {
        if (NetworkUtil.isInternet(getActivity())) {
            ApiInterface apiInterface = ServiceGeneratorUtil.createService(ApiInterface.class);
            apiInterface.trending(
                    new retrofit.Callback<ArrayList<Project>>() {

                        @Override
                        public void success(final ArrayList<Project> projects, Response response) {

                            mLayoutManager = new LinearLayoutManager(getActivity());
                            mRecyclerView.setLayoutManager(mLayoutManager);

                            mAdapter = new TrendingAdapter(getActivity(), projects);

                            mRecyclerView.setAdapter(mAdapter);

                            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(getActivity(), ProjectDescriptionActivity.class);
                                    Gson gson = new Gson();
                                    intent.putExtra("project",gson.toJson(projects.get(position)));
                                    Log.i("hell_selected", String.valueOf(projects.get(position).getTitle()));
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
        }
    }
}
