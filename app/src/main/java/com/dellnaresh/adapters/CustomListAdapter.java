package com.dellnaresh.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dellnaresh.com.dellnaresh.asynctasks.DownloadFileFromURL;
import com.dellnaresh.com.dellnaresh.asynctasks.FileDownloadTask;
import com.dellnaresh.com.dellnaresh.entity.DownloadInfo;
import com.dellnaresh.com.dellnaresh.util.ViewHolder;
import com.dellnaresh.youtubeandroidapp.R;
import com.google.api.services.youtube.model.SearchResult;
import com.squareup.picasso.Picasso;
import com.youtube.downloader.DownloadJob;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<DownloadInfo> {
    private Context context;
    private static final String TAG = CustomListAdapter.class.getSimpleName();

    public CustomListAdapter(Context context, int resource, List<DownloadInfo> objects) {
        super(context, resource, objects);
        this.context=context;
    }

//    public CustomListAdapter(Context context, List<SearchResult> listData) {
//        this.listData = listData;
//        layoutInflater = LayoutInflater.from(context);
//        this.context=context;
//    }


//
//    @Override
//    public Object getItem(int position) {
//        return listData.get(position);
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final DownloadInfo info = getItem(position);
        ViewHolder holder;
        SearchResult searchItem = info.getSearchResult();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.search_list, parent,false);
            holder = new ViewHolder();
            holder.videoId = (TextView) row.findViewById(R.id.videoId);
            holder.title = (TextView) row.findViewById(R.id.title);
            holder.date = (TextView) row.findViewById(R.id.date);
            holder.imageView = (ImageView) row.findViewById(R.id.thumbImage);
            holder.progressBar=(ProgressBar)row.findViewById(R.id.progressBar);
            holder.button = (Button)row.findViewById(R.id.downloadButton);
            holder.info=info;
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
            holder.info.setProgressBar(null);
            holder.info = info;
            holder.info.setProgressBar(holder.progressBar);
        }

        if (searchItem != null) {
            holder.videoId.setText(searchItem.getId().getVideoId());
            if (searchItem.getSnippet() != null) {
                if (searchItem.getSnippet().getTitle() != null) {
                    holder.title.setText("Title, " + searchItem.getSnippet().getTitle());
                }
                if (searchItem.getSnippet().getPublishedAt() != null)
                    holder.date.setText(searchItem.getSnippet().getPublishedAt().toString());

                if (holder.imageView != null) {
                //    new ImageDownloaderTask(holder.imageView).execute(searchItem.getSnippet().getThumbnails().getDefault().getUrl());
                    Picasso.with(context).load(searchItem.getSnippet().getThumbnails().getDefault().getUrl()).resize(200,200).into(holder.imageView);
                }
            }
        }
        holder.progressBar.setProgress(info.getProgress());
        info.setProgressBar(holder.progressBar);
        holder.button.setEnabled(info.getDownloadState() == DownloadInfo.DownloadState.NOT_STARTED);
        final Button button = holder.button;
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setDownloadState(DownloadInfo.DownloadState.QUEUED);
                button.setEnabled(false);
                button.invalidate();
                FileDownloadTask task = new FileDownloadTask(info,context);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        return row;
    }


}