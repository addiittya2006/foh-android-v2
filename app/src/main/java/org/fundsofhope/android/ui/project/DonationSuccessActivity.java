package org.fundsofhope.android.ui.project;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.fundsofhope.android.R;
import org.fundsofhope.android.data.model.Ngo;
import org.fundsofhope.android.data.model.ProjectDescription;

/**
 * Created by addiittya on 20/12/16.
 */

public class DonationSuccessActivity extends AppCompatActivity {
    private Ngo ngo;
    ImageView profile;
    private TextView description;
    private ProjectDescription project;
    ViewPager mViewPager;
    private FloatingActionButton donate;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ngo_description);

        getSupportActionBar().setTitle(project.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        description = (TextView) findViewById(R.id.description);
        profile = (ImageView) findViewById(R.id.profile);

        Gson gson = new Gson();
        project = gson.fromJson(getIntent().getStringExtra("project"), ProjectDescription.class);

        description.setText("Congratulations !!! You have successfully donated to this ngo."+ "\n\n\n"+project.getDescription());
        Picasso.with(getApplicationContext()).load("http://api.fundsofhope.org"+project.getImages().get(0)).into(profile);

        donate = (FloatingActionButton) findViewById(R.id.donate);
        donate.setVisibility(View.GONE);
//        donate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                makeDonation();
//
//            }
//        });

//                              title.setText(project_description.getTitle());


//            DebugLog.d(getString(R.string.error_no_internet_connection))
//        title = (TextView) findViewById(R.id.title);


//        Log.i("hell",project.getDescription());

//
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


