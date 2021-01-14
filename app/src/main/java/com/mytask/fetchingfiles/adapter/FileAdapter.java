package com.mytask.fetchingfiles.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mytask.fetchingfiles.R;
import com.mytask.fetchingfiles.model.FileModel;
import com.mytask.fetchingfiles.model.MyFileItems;

import java.util.Collections;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(MyFileItems fileItem);
    }

    private List<MyFileItems> items = Collections.emptyList();

    private OnItemClickListener listener;

    public FileAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<MyFileItems> items) {
        this.items = items;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView filenameTextView;
        private TextView pathTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            filenameTextView = itemView.findViewById(R.id.filenameTextView);
            pathTextView = itemView.findViewById(R.id.pathTextView);
        }

        void bind(MyFileItems fileItem, OnItemClickListener listener) {
            pathTextView.setText(fileItem.getFileModel().getPath());
            filenameTextView.setText(fileItem.getFilename(), TextView.BufferType.SPANNABLE);
            // fileItem.getFileModel().getAttributes().

            if (listener != null) {
                itemView.setOnClickListener(v -> listener.onItemClick(fileItem));
            }
        }
    }
}