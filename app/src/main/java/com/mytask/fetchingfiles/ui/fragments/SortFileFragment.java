package com.mytask.fetchingfiles.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mytask.fetchingfiles.R;
import com.mytask.fetchingfiles.adapter.FileSortAdapter;
import com.mytask.fetchingfiles.model.FilesSort;

import java.util.Arrays;
import java.util.List;

public class SortFileFragment extends BottomSheetDialogFragment {

    private FileSortAdapter adapter = new FileSortAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sort_file, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<FilesSort> options = Arrays.asList(FilesSort.values());
        adapter.setItems(options);

        RecyclerView recyclerView = view.findViewById(R.id.sortOptionsRecycler);
        recyclerView.setAdapter(adapter);
    }

    public void setListener(FileSortAdapter.OnItemClickListener listener) {
        adapter.setListener(listener);
    }
}