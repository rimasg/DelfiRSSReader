package com.fortislabs.delfireader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fortislabs.delfireader.items.Content;

/**
 * Created by Okis on 2016.11.05.
 */

public class RssAdapter extends CursorAdapter {
    public RssAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.content_list_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final Content content = new Content(cursor);
        final ViewHolder holder = new ViewHolder(view);
        holder.title.setText(content.title);
        holder.description.setText(content.description);
        holder.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.description.isShown()) {
                    slideUp(context, holder.description);
                    holder.toggleButton.setImageResource(R.drawable.arrow_down);
                    holder.description.setVisibility(View.GONE);
                } else {
                    slideDown(context, holder.description);
                    holder.toggleButton.setImageResource(R.drawable.arrow_up);
                    holder.description.setVisibility(View.VISIBLE);
                }
            }
        });
        Glide
                .with(context)
                .load(content.thumbnailUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(android.R.drawable.ic_menu_view)
                .into(holder.thumbnail);
    }

    private void slideUp(Context context, View v) {
        final Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        if (anim != null) {
            anim.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(anim);
            }
        }
    }

    private void slideDown(Context context, View v) {
        final Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        if (anim != null) {
            anim.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(anim);
            }
        }
    }

    static class ViewHolder {
        TextView title;
        TextView description;
        ImageView thumbnail;
        ImageButton toggleButton;

        ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.rss_title);
            description = (TextView) view.findViewById(R.id.rss_content);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            toggleButton = (ImageButton) view.findViewById(R.id.toggleButton);
        }
    }
}
