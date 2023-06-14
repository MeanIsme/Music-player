package com.mobile.MusicApp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHoder> {
    public List<MusicModel> data = new ArrayList<>();
    private Callback callback;
    public MusicAdapter(Callback callback) {
        this.callback = callback;
    }

    public MusicAdapter() {

    }

    @NonNull
    @Override
    public MusicViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new MusicViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHoder holder, @SuppressLint("RecyclerView") int position) {
        MusicModel musicModel = data.get(position);
        View view = holder.view;
        TextView tvName = view.findViewById(R.id.tvName);
        tvName.setText(musicModel.getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<MusicModel> data) {

        this.data = data;
        notifyDataSetChanged();
    }

    static class MusicViewHoder extends RecyclerView.ViewHolder{
        private View view;
        public MusicViewHoder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }
        View getView()
        {
            return view;
        }
    }

    interface Callback{
        void onClickItem(int position);
    }
}
