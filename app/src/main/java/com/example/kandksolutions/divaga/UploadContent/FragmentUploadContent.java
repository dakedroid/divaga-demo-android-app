package com.example.kandksolutions.divaga.UploadContent;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kandksolutions.divaga.R;
import com.example.kandksolutions.divaga.UploadContent.AlertaUp;
import com.example.kandksolutions.divaga.UploadContent.EventoUp;
import com.example.kandksolutions.divaga.UploadContent.NoticiaUp;
import com.example.kandksolutions.divaga.UploadContent.PromoUp;

/**
 * Created by dakedroid on 8/1/16.
 */
public class FragmentUploadContent extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avator;
        public TextView name;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list_upload, parent, false));
            avator = (ImageView) itemView.findViewById(R.id.list_avatar);
            name = (TextView) itemView.findViewById(R.id.list_title);
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private static final int LENGTH = 4;
        Intent intent;
        private final String[] mPlaces;
        private final Drawable[] mPlaceAvators;

        public ContentAdapter(Context context) {
            Resources resources = context.getResources();
            mPlaces = resources.getStringArray(R.array.uploadtitles);
            TypedArray a = resources.obtainTypedArray(R.array.upload_imagenes);
            mPlaceAvators = new Drawable[a.length()];
            for (int i = 0; i < mPlaceAvators.length; i++) {
                mPlaceAvators[i] = a.getDrawable(i);
            }
            a.recycle();
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.avator.setImageDrawable(mPlaceAvators[position % mPlaceAvators.length]);
            holder.name.setText(mPlaces[position % mPlaces.length]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0){intent = new Intent(getContext(), PromoUp.class);   startActivity(intent);}
                    if (position == 1){intent = new Intent(getContext(), NoticiaUp.class); startActivity(intent);}
                    if (position == 2){intent = new Intent(getContext(), EventoUp.class);  startActivity(intent);}
                    if (position == 3){intent = new Intent(getContext(), AlertaUp.class);  startActivity(intent);}
                }
            });

        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }

    }
}