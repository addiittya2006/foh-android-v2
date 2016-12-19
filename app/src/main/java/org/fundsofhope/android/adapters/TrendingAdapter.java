package org.fundsofhope.android.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.fundsofhope.android.R;
import org.fundsofhope.android.fragment.TrendingFragment;
import org.fundsofhope.android.model.Project;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by anip on 20/12/16.
 */
public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.ViewHolder> {
    private String[] mDataset;
    private ArrayList<Project> projects;
    private Activity context;
    public static final String BASE_URL="http://api.fundsofhope.org/";


    public TrendingAdapter(Activity context, ArrayList<Project> project) {
        Collections.reverse(project);
        projects = project;
        this.context = context;

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView header_image;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.info_text);
            header_image = (ImageView) v.findViewById(R.id.header);

//            mTextView = v;

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TrendingAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TrendingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_adapter_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(projects.get(position).getTitle()+"\n"+projects.get(position).getCost());
        Picasso.with(context).load(BASE_URL+projects.get(position).getImage()).into(holder.header_image);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return projects.size();
    }
}
