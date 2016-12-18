package org.fundsofhope.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.fundsofhope.android.adapters.ImageAdapter;
import org.fundsofhope.android.adapters.ProjectAdapter;
import org.fundsofhope.android.config.ApiInterface;
import org.fundsofhope.android.config.ServiceGenerator;
import org.fundsofhope.android.model.Project;
import org.fundsofhope.android.model.Project_Description;
import org.fundsofhope.android.util.NetworkUtilities;
import org.fundsofhope.android.util.RecyclerItemClickListener;
import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by anip on 16/12/16.
 */
public class ProjectDescription extends AppCompatActivity {
    private ImageAdapter images;
    Intent intent;
    private Project project;
    private TextView title;
    private TextView description;
    ViewPager mViewPager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_description);
        Gson gson = new Gson();
        project = gson.fromJson(getIntent().getStringExtra("project"),Project.class);


        if (NetworkUtilities.isInternet(ProjectDescription.this)) {
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            apiInterface.project_detail(project.getId(),
                    new retrofit.Callback<Project_Description>() {

                        @Override
                        public void success(final Project_Description project_description, Response response) {
                            // specify an adapter (see also next example)
                            title.setText(project_description.getTitle());
                            description.setText(project_description.getDescription());
                            mViewPager = (ViewPager) findViewById(R.id.pager);
                            images = new ImageAdapter(getApplicationContext(),project_description.getImages());
                            mViewPager.setAdapter(images);


                        }


                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("TAG", "Response : Failure " + error.getMessage());
                        }
                    });
        } else {
//            DebugLog.d(getString(R.string.error_no_internet_connection));
        }
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);

//        Log.i("hell",project.getDescription());

        title.setText(project.getTitle());
        description.setText(project.getDescription());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(images);
    }
}
