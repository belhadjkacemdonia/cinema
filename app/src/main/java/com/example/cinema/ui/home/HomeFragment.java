package com.example.cinema.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinema.R;
import com.example.cinema.film;
import com.example.cinema.loginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private List<String> mImageUrls;
    private List<String> mTitles;
    private List<String> mDescriptions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mFirestore = FirebaseFirestore.getInstance();

        mImageUrls = new ArrayList<>();
        mTitles = new ArrayList<>();
        mDescriptions = new ArrayList<>();
        mAdapter = new MyAdapter(mImageUrls, mTitles, mDescriptions);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore.collection("Images").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String documentId = document.getId();


                        mFirestore.collection("Images").document(documentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String imageUrl = document.getString("imageUrls");
                                        String title = document.getString("titles");
                                        String description = document.getString("descriptions");

                                        mImageUrls.add(imageUrl);
                                        mTitles.add(title);
                                        mDescriptions.add(description);

                                        mAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    // Handle error
                                }
                            }
                        });
                    }
                } else {
                    // Handle error
                }
            }
        });

        return rootView;

    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<String> mImageUrls;
        private List<String> mTitles;
        private List<String> mDescriptions;

        public MyAdapter(List<String> imageUrls, List<String> titles, List<String> descriptions) {
            mImageUrls = imageUrls;
            mTitles = titles;
            mDescriptions = descriptions;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String imageUrl = mImageUrls.get(position);
            String title = mTitles.get(position);
            String description = mDescriptions.get(position);

            // Load the image from the download URL using Glide
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.mImageView);

            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FirebaseAuth.getInstance().getCurrentUser() !=null) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, film.class);
                        intent.putExtra("title", title);
                        intent.putExtra("description", description);
                        intent.putExtra("imageUrl", imageUrl);
                        context.startActivity(intent);
                    }else{ // User is not logged in, navigate to LoginActivity
                        Context context = v.getContext();
                        Intent intent = new Intent(context, loginActivity.class);
                        context.startActivity(intent);

                    }

                }

            });
        }

        @Override
        public int getItemCount() {
            return mImageUrls.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView mImageView;

            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.imageView);
                mImageView.setAdjustViewBounds(true);
            }
        }
    }
}

