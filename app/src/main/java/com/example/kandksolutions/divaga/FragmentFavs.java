package com.example.kandksolutions.divaga;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by dakedroid on 8/1/16.
 */
public class FragmentFavs extends Fragment {

    int tilePadding = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
        return recyclerView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name;
        public CardView descriptionCardView;
        public TextView description;
        private RatingBar ratingBar;
        private int descriptionViewFullHeight;
        private int descriptionViewMinHeight;
        ImageButton favoriteImageButton;
        boolean mode_favorite = true;
        int mode = 0;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_card_favs, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.card_image);
            name = (TextView) itemView.findViewById(R.id.card_text);
            descriptionCardView = (CardView) itemView.findViewById(R.id.card_view);
            description = (TextView) itemView.findViewById(R.id.card_content);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

            addListenerOnRatingBar();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivityPlaces.class);
                    intent.putExtra(DetailActivityPlaces.EXTRA_POSITION, getAdapterPosition());
                    context.startActivity(intent);
                }
            });

            favoriteImageButton = (ImageButton) itemView.findViewById(R.id.favorite_button);
            favoriteImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.i("mode after",""+mode);
                    Snackbar.make(v, "Added to Favorite", Snackbar.LENGTH_LONG).show();

                    if(mode_favorite){
                        favoriteImageButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                        mode_favorite = false;
                    }else{
                        favoriteImageButton.setImageResource(R.drawable.ic_favorite_outline_white_24dp);
                        mode_favorite = true;
                    }

                }
            });
            ImageButton detailImageButton = (ImageButton) itemView.findViewById(R.id.detail_button);
            detailImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    toggleProductDescriptionHeight();

                }
            });


        }



        private void toggleProductDescriptionHeight() {

            descriptionViewFullHeight = descriptionCardView.getHeight()+ (int) getActivity().getResources().getDimension(R.dimen.card_expand_places);
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

                        mode = 1;
                    }
                });
                anim.start();
                description.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
            } else {
                // collapse
                descriptionViewMinHeight = (int) getActivity().getResources().getDimension(R.dimen.card_size_prueba);
                ValueAnimator anim = ValueAnimator.ofInt(descriptionCardView.getMeasuredHeightAndState(),
                        descriptionViewMinHeight);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = descriptionCardView.getLayoutParams();
                        layoutParams.height = val;
                        descriptionCardView.setLayoutParams(layoutParams);

                        mode = 0;
                    }
                });
                anim.start();
                description.setVisibility(View.INVISIBLE);
                ratingBar.setVisibility(View.INVISIBLE);
            }
        }

        public void addListenerOnRatingBar() {


            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {
                }
            });
        }
    }


    /**
     * Adapter to display recycler view.
     */
    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of Tiles in RecyclerView.
        private static final int LENGTH = 2;

        private final String[] mPlaces;
        private final Drawable[] mPlacePictures;
        private final String[] mDescriptions;

        public ContentAdapter(Context context) {

            Resources resources = context.getResources();
            mPlaces = resources.getStringArray(R.array.lugares_titulos);
            TypedArray a = resources.obtainTypedArray(R.array.lugares_imagenes);
            mPlacePictures = new Drawable[a.length()];
            for (int i = 0; i < mPlacePictures.length; i++) {
                mPlacePictures[i] = a.getDrawable(i);
            }
            mDescriptions = resources.getStringArray(R.array.lugares_detalles);

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
            holder.picture.setAlpha((float) 0.9);
            holder.name.setText(mPlaces[position % mPlaces.length]);
            holder.description.setText(mDescriptions[position % mPlaces.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

}