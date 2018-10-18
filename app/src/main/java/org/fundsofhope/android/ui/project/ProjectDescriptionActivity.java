package org.fundsofhope.android.ui.project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.fundsofhope.android.R;
import org.fundsofhope.android.util.ServiceGeneratorUtil;
import org.fundsofhope.android.data.api.ApiInterface;
import org.fundsofhope.android.data.model.Project;
import org.fundsofhope.android.data.model.ProjectDescription;
import org.fundsofhope.android.data.model.SignupStatus;
import org.fundsofhope.android.util.NetworkUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static org.fundsofhope.android.R.layout.project_description;

/**
 * Created by anip on 16/12/16.
 */

public class ProjectDescriptionActivity extends AppCompatActivity {
    private ImageAdapter images;
    private Project project;
    private TextView description;
    private org.fundsofhope.android.data.model.ProjectDescription projectDescription;
    ViewPager mViewPager;
    private FloatingActionButton donate;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(project_description);
        Gson gson = new Gson();
        project = gson.fromJson(getIntent().getStringExtra("project"),Project.class);
        getSupportActionBar().setTitle(project.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        donate = (FloatingActionButton) findViewById(R.id.donate);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDonation();

            }
        });

        if (NetworkUtil.isInternet(ProjectDescriptionActivity.this)) {
            ApiInterface apiInterface = ServiceGeneratorUtil.createService(ApiInterface.class);
            apiInterface.project_detail(project.getId(),
                    new retrofit.Callback<ProjectDescription>() {

                        @Override
                        public void success(final ProjectDescription project_description, Response response) {
                            projectDescription = project_description;
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
        }
        description = (TextView) findViewById(R.id.description);

        description.setText(project.getDescription());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(images);
    }

    private void makeDonation(){
        new LongOperation2().execute("");
    }

    private class LongOperation2 extends AsyncTask<String, Void, SignupStatus> {

        private String Error = null;
        private SignupStatus result;
        String studentId;
        private ProgressDialog Dialog = new ProgressDialog(ProjectDescriptionActivity.this);
        String data = "";

        int sizeData = 0;

        protected void onPreExecute() {

            Dialog.setMessage("Please wait..");
            Dialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("project_id", project.getId());
            jsonObject.addProperty("user_id", 2);
            jsonObject.addProperty("amount", 1);

            Gson gson2 = new Gson();

            String jsonString = gson2.toJson(jsonObject);
            Log.i("hell", jsonString);
            data = jsonString;

        }

        protected SignupStatus doInBackground(String... urls) {

            HttpURLConnection httpcon;

            try {

                httpcon = (HttpURLConnection) ((new URL("http://api.fundsofhope.org/project/donate/").openConnection()));
                httpcon.setDoOutput(true);
                httpcon.setRequestMethod("POST");
                httpcon.connect();

                OutputStream os = httpcon.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.close();
                os.close();
                Log.i("hel", String.valueOf(httpcon.getErrorStream())+httpcon.getResponseMessage()+httpcon.getResponseCode());


                BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

                String line;
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                Gson gson2 = new Gson();
                Log.i("he;;", sb.toString());

                result = gson2.fromJson(sb.toString(), SignupStatus.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
        protected void onPostExecute(SignupStatus response) {

            Dialog.dismiss();

            Log.i("response", String.valueOf(response)+Error);
            if(response.getStatus().equals("Donation Successful") || response.getStatus().equals("User updated"))
            {
                Intent intent = new Intent(ProjectDescriptionActivity.this, DonationSuccessActivity.class);
                Gson gson = new Gson();
                intent.putExtra("project",gson.toJson(projectDescription));
                startActivity(intent);
               Toast.makeText(ProjectDescriptionActivity.this, response.getStatus(),Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(ProjectDescriptionActivity.this,response.getStatus(),Toast.LENGTH_LONG).show();
            }


        }

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager
                    .getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
