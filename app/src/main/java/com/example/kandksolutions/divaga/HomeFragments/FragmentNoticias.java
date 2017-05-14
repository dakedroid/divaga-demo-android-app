package com.example.kandksolutions.divaga.HomeFragments;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.example.kandksolutions.divaga.Models.Noticia;
import com.example.kandksolutions.divaga.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * Created by dakedroid on 8/1/16.
 */
public class FragmentNoticias extends Fragment {

    public RecyclerView recyclerView;
    private ProgressDialog mProgressDialog;
    DatabaseReference mDatabaseReference;
    //FirebaseStorage storage;
    //StorageReference storageRef;

    @Override
    public void onStart() {
        super.onStart();
        //storage = FirebaseStorage.getInstance();
        //storageRef = FirebaseStorage.getInstance().getReference().child("noticias/noticia_14.jpeg");
        //storageRef =  storage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int playServicesStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());
        if(playServicesStatus != ConnectionResult.SUCCESS){
            //If google play services in not available show an error dialog and return
            final Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), playServicesStatus, 0, null);
            errorDialog.show();

        }


        recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
        recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);

        FirebaseRecyclerAdapter<Noticia,NoticiaViewHolder> adapter = new FirebaseRecyclerAdapter<Noticia, NoticiaViewHolder>(
                Noticia.class,
                R.layout.item_card_news,
                NoticiaViewHolder.class,
                mDatabaseReference.child("MisNoticias").getRef()
        ) {
            @Override
            protected void populateViewHolder(NoticiaViewHolder viewHolder, Noticia model, int position) {


                viewHolder.titulo.setText(model.getTitulo());
                viewHolder.description.setText(model.getDescripcion());

                //Log.i("DEBUG",st.getPath());
             /*   Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(model.getImagen())
                        .into(viewHolder.imagen);*/
                // viewHolder.imagen.setImageBitmap(myBitmap);

                //  StorageReference s
                // Load the image using Glide
                /*  Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(storageRef)
                        .into(viewHolder.imagen);
*/
                //Log.i("DEBUG KEVIN", String.valueOf(storageRef));
                //viewHolder.imagen.setAlpha((float) 0.9);
                //viewHolder.description.setRating(model.getMovieRating());
                Picasso.with(getContext()).load("http://www.themistermen.co.uk/images/mrmen_uk/small.gif").into(viewHolder.imagen);
            }
        };

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        return recyclerView;
    }

    private void showMessageDialog(String title, String message) {
        AlertDialog ad = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .create();
        ad.show();
    }

    private void showProgressDialog(String caption) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.setMessage(caption);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * Adapter to display recycler view.
     */

    public static class NoticiaViewHolder extends RecyclerView.ViewHolder{


        TextView titulo;
        TextView description;
        ImageView imagen;
        CardView descriptionCardView;
        private int descriptionViewFullHeight;
        private int descriptionViewMinHeight;
        private static final int ARROW_ROTATION_DURATION = 150;
        int mode = 0;

        public NoticiaViewHolder(View v) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.card_title);
            description = (TextView) itemView.findViewById(R.id.card_text);
            imagen = (ImageView) itemView.findViewById(R.id.card_image);
            descriptionCardView = (CardView) itemView.findViewById(R.id.card_view);

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

            int size_card_default = 300;
            int size_card_min_height = 1056;
            // (int) getActivity().getResources().getDimension(R.dimen.card_expand)
            descriptionViewFullHeight = descriptionCardView.getHeight()+ size_card_default ;
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
                // collapse  (int) getActivity().getResources().getDimension(R.dimen.card_height_news)
                descriptionViewMinHeight = size_card_min_height;
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

}