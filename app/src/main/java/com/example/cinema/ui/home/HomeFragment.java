package com.example.cinema.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema.R;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView,recyclerView2;
    private MyAdapter mAdapter,mAdapter1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        recyclerView2 = rootView.findViewById(R.id.recyclerView2);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager1);

        String[] data = {"", "", "", "", ""};
        int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3,
                R.drawable.image4, R.drawable.image5};
        mAdapter = new MyAdapter(data, images);
        mAdapter1 = new MyAdapter(data, images);

        mRecyclerView.setAdapter(mAdapter);
        recyclerView2.setAdapter(mAdapter1);

        return rootView;

    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private String[] mData;
        private int[] mImages;

        public MyAdapter(String[] data,int[] images) {

            mData = data;
            mImages = images;
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
            holder.mTextView.setText(mData[position]);
            holder.mImageView.setImageResource(mImages[position]);
        }

        @Override
        public int getItemCount() {
            return mData.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public ImageView mImageView;

            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.textView);
                mImageView = itemView.findViewById(R.id.imageView);
                mImageView.setAdjustViewBounds(true);
            }
        }
    }
}
