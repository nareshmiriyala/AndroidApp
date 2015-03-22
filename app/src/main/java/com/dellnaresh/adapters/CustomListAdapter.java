package com.dellnaresh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dellnaresh.youtubeandroidapp.R;
import com.google.api.services.youtube.model.SearchResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    private List<SearchResult> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListAdapter(Context context, List<SearchResult> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        SearchResult searchItem = listData.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.search_list, null);
            holder = new ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.title);
            holder.reporterNameView = (TextView) convertView.findViewById(R.id.reporter);
            holder.reportedDateView = (TextView) convertView.findViewById(R.id.date);
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (searchItem != null) {
            holder.headlineView.setText(searchItem.getId().getVideoId());
            if (searchItem.getSnippet() != null) {
                if (searchItem.getSnippet().getTitle() != null) {
                    holder.reporterNameView.setText("Title, " + searchItem.getSnippet().getTitle());
                }
                if (searchItem.getSnippet().getPublishedAt() != null)
                    holder.reportedDateView.setText(searchItem.getSnippet().getPublishedAt().toString());

                if (holder.imageView != null) {
                //    new ImageDownloaderTask(holder.imageView).execute(searchItem.getSnippet().getThumbnails().getDefault().getUrl());
                    Picasso.with(context).load(searchItem.getSnippet().getThumbnails().getDefault().getUrl()).into(holder.imageView);
                }
            }
        }
        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView reporterNameView;
        TextView reportedDateView;
        ImageView imageView;
    }
}