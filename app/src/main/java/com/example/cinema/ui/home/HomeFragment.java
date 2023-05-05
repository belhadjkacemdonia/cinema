package com.example.cinema.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    private StorageReference mStorageRef;
    private List<String> mImageUrls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mStorageRef = FirebaseStorage.getInstance().getReference().child("Images");

        mImageUrls = new ArrayList<>();
        mAdapter = new MyAdapter(mImageUrls);
        mRecyclerView.setAdapter(mAdapter);

        mStorageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mImageUrls.add(uri.toString());
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        return rootView;

    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<String> mImageUrls;

        public MyAdapter(List<String> imageUrls) {
            mImageUrls = imageUrls;
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

            // Charger l'image à partir de l'URL de téléchargement avec Glide
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.mImageView);

            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, film.class);
                    context.startActivity(intent);
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
