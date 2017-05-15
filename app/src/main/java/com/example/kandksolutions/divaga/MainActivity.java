package com.example.kandksolutions.divaga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mPostRV;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> mPostAdapter;
    private DatabaseReference mPostRef;
    private StorageReference storageReference;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        initialiseScreen();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendPostToFirebase();
                showFileChooser();

            }
        });
    }

    private void sendPostToFirebase() {
        post = new Post();
        String UID = Utils.getUID();

        post.setUID(UID);
        post.setNumLikes(0);
        post.setImageUrl("gs://divaga-dd37b.appspot.com/imagenes/img"+ post.getUID()+".jpg");
       // post.setImageUrl(url);
        mPostRef.child(UID).setValue(post);
    }

    private void initialiseScreen() {
        mPostRV = (RecyclerView) findViewById(R.id.post_rv);
        mPostRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mPostRef = FirebaseDatabase.getInstance().getReference(Constants.POSTS);
        setupAdapter();
        mPostRV.setAdapter(mPostAdapter);
    }

    private void setupAdapter() {
        mPostAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.item_layout_post,
                PostViewHolder.class,
                mPostRef
        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, final Post model, int position) {

                storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.getImageUrl());
                Glide.with(MainActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .into(viewHolder.postIV);

                viewHolder.setNumLikes(model.getNumLikes());
                viewHolder.postLikeIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateNumLikes(model.getUID());
                    }
                });
            }
        };
    }

    private void updateNumLikes(String uid) {
        mPostRef.child(uid).child(Constants.NUM_LIKES)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        long num = (long) mutableData.getValue();
                        num++;
                        mutableData.setValue(num);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }
                });
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        public ImageView postIV;
        public ImageView postLikeIV;
        public TextView numLikesTV;


        public PostViewHolder(View itemView) {
            super(itemView);

            postIV = (ImageView) itemView.findViewById(R.id.post_iv);
            postLikeIV = (ImageView) itemView.findViewById(R.id.like_iv);
            numLikesTV = (TextView) itemView.findViewById(R.id.num_likes_tv);
        }


        public void setNumLikes(long num) {
            numLikesTV.setText(String.valueOf(num));
        }
    }
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Log.i("PRUEBA",String.valueOf(filePath));
                sendPostToFirebase();
                uploadFile();

                //imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //this method will upload the file
    public void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("imagenes/img" + post.getUID() + ".jpg");

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage

                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

}
