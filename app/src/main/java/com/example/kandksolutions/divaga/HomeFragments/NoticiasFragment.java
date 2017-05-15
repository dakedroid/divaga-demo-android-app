package com.example.kandksolutions.divaga.HomeFragments;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kandksolutions.divaga.R;

/**
 * Created by dakedroid on 8/1/16.
 */
public class NoticiasFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        //recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
        recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name;
        public TextView description;
        public CardView descriptionCardView;
        private int descriptionViewFullHeight;
        private int descriptionViewMinHeight;
        private static final int ARROW_ROTATION_DURATION = 150;
        int mode = 0;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.modelo_noticias, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.card_image);
            name = (TextView) itemView.findViewById(R.id.card_title);
            description = (TextView) itemView.findViewById(R.id.card_text);
            descriptionCardView = (CardView) itemView.findViewById(R.id.card_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            // Adding Snackbar to Action Button inside card
            ImageButton detailImageButton =
                    (ImageButton) itemView.findViewById(R.id.detail_button);
            detailImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    toggleProductDescriptionHeight(v);

                }
            });

            Button verNoticiaCompletaButton =
                    (Button) itemView.findViewById(R.id.action_button);
            verNoticiaCompletaButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Added to Favorite",
                            Snackbar.LENGTH_LONG).show();
                }
            });

            ImageButton shareImageButton = (ImageButton) itemView.findViewById(R.id.share_button);
            shareImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Share article",
                            Snackbar.LENGTH_LONG).show();
                }
            });
        }

        private void toggleProductDescriptionHeight(final View view) {

            descriptionViewFullHeight = descriptionCardView.getHeight()+ (int) getActivity().getResources().getDimension(R.dimen.card_expand);
            descriptionViewMinHeight = descriptionCardView.getHeight();

            if (descriptionCardView.getHeight() == descriptionViewMinHeight && mode == 0) {
                // expand
                ValueAnimator anim = ValueAnimator.ofInt(descriptionCardView.getMeasuredHeightAndState(), descriptionViewFullHeight);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = descriptionCardView.getLayoutParams();
                        layoutParams.height = val;
                        descriptionCardView.setLayoutParams(layoutParams);
                        view.animate().setDuration(ARROW_ROTATION_DURATION).rotation(180);
                        mode = 1;
                    }
                });
                anim.start();
            } else {
                // collapse
                descriptionViewMinHeight = (int) getActivity().getResources().getDimension(R.dimen.card_height_news);
                ValueAnimator anim = ValueAnimator.ofInt(descriptionCardView.getMeasuredHeightAndState(),
                        descriptionViewMinHeight);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = descriptionCardView.getLayoutParams();
                        layoutParams.height = val;
                        descriptionCardView.setLayoutParams(layoutParams);
                        view.animate().setDuration(ARROW_ROTATION_DURATION).rotation(0);
                        mode = 0;
                    }
                });
                anim.start();
            }
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of Card in RecyclerView.
        private static final int LENGTH = 6;

        private final String[] mPlaces;
        private final String[] mPlaceDesc;
        private final Drawable[] mPlacePictures;

        public ContentAdapter(Context context) {
            Resources resources = context.getResources();
            mPlaces = resources.getStringArray(R.array.noticias_fecha);
            mPlaceDesc = resources.getStringArray(R.array.noticias_contenido);
            TypedArray a = resources.obtainTypedArray(R.array.noticias_imagenes);
            mPlacePictures = new Drawable[a.length()];
            for (int i = 0; i < mPlacePictures.length; i++) {
                mPlacePictures[i] = a.getDrawable(i);
            }
            a.recycle();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.picture.setImageDrawable(mPlacePictures[position % mPlacePictures.length]);
            holder.picture.setAlpha((float) 0.8);
            holder.picture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.name.setText(mPlaces[position % mPlaces.length]);
            holder.description.setText(mPlaceDesc[position % mPlaceDesc.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}