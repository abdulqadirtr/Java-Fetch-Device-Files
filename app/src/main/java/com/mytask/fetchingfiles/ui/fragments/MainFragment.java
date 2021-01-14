package com.mytask.fetchingfiles.ui.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mytask.fetchingfiles.R;
import com.mytask.fetchingfiles.adapter.FileAdapter;
import com.mytask.fetchingfiles.databinding.FragmentMainBinding;
import com.mytask.fetchingfiles.model.FilesDescriptionParcelable;
import com.mytask.fetchingfiles.model.MyFileItems;
import com.mytask.fetchingfiles.model.ProgressState;
import com.mytask.fetchingfiles.utils.Utils;
import com.mytask.fetchingfiles.viewmodel.MainViewModel;


public class MainFragment extends Fragment implements FileAdapter.OnItemClickListener {

    private static final String PM_FETCH_FILES_TAG = "PM_FETCH_FILES_TAG";
    private static final String PM_SAVE_FILES_LIST_TAG = "PM_SAVE_FILES_LIST_TAG";
    private static final int RC_MANAGE_ALL_FILES_ACCESS_PERMISSION = 100;
    private static final int RC_READ_WRITE_EXTERNAL_STORAGE_PERMISSION = 101;
    private static final int RC_OPEN_APP_SETTINGS = 102;
    private FileAdapter filesAdapter = new FileAdapter(this);
    private MainViewModel viewModel;
    private FragmentMainBinding fragmentMainBinding;
    private SortFileFragment sortDialog = new SortFileFragment();
    private String tag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()))
                .get(MainViewModel.class);

        if (savedInstanceState == null) {
            requestStoragePermission(PM_FETCH_FILES_TAG);

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMainBinding  =  DataBindingUtil.inflate(inflater, R.layout.fragment_main, container,false);
        View view = fragmentMainBinding.getRoot();
        fragmentMainBinding.filesRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        fragmentMainBinding. filesRecycler.setAdapter(filesAdapter);
        setHasOptionsMenu(true);
        observe();
        setListeners();


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemSort) {
            sortDialog.show(getActivity().getSupportFragmentManager(), null);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void onStoragePermissionGranted(String tag) {
        switch (tag) {
            case PM_FETCH_FILES_TAG:
                viewModel.fetchFiles();
                break;

            case PM_SAVE_FILES_LIST_TAG:
                saveFilesList();
                break;
        }
    }


    public void onStoragePermissionDenied(String tag) {
        showStoragePermissionNotGranted(v -> grantStoragePermissionManually(tag));
    }

    @Override
    public void onItemClick(MyFileItems fileItem) {

        FilesDescriptionParcelable attrs = fileItem.getFileModel().getAttributes();
        Bundle bundle = new Bundle();
        bundle.putString("image", String.valueOf(fileItem.getFileModel().getPath()));
        bundle.putLong("size", attrs.getSize());
        bundle.putLong("create_date", attrs.getCreationTime());
        bundle.putLong("modified_date", attrs.getLastModifiedTime());


        NavHostFragment.findNavController(this).navigate(R.id.action_myHomeFragment_to_mySecondFragment, bundle);

    }

    private void observe() {
        viewModel.files().observe(this, files -> {
            filesAdapter.setItems(files);
        });

        viewModel.fetchFilesProgressState().observe(this, state -> {
            switch (state) {
                case IDLE:
                case DONE:
                case ERROR:
                    fragmentMainBinding.filesRecycler.setVisibility(View.VISIBLE);
                    fragmentMainBinding.progressBar.setVisibility(View.GONE);
                    break;
                case IN_PROGRESS:
                    fragmentMainBinding.filesRecycler.setVisibility(View.INVISIBLE);
                    fragmentMainBinding.progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        });

        viewModel.saveFilesListProgressState().observe(this, state -> {
            fragmentMainBinding.saveFilesListFab.setEnabled(state != ProgressState.IN_PROGRESS);
        });

        viewModel.numberOfHighlightedFiles().observe(this, number -> {
            String text = getString(R.string.number_of_matches_d, number);

            Toast.makeText(getActivity(), text, Toast.LENGTH_LONG)
                    .show();
        });
    }

    private void saveFilesList() {
        viewModel.saveFilesList().observe(this, filePath -> {
            String text;

            if (filePath != null) {
                text = getString(R.string.file_saved_s, filePath);
            } else {
                text = getString(R.string.error_saving_file);
            }

            Toast.makeText(getActivity(), text, Toast.LENGTH_LONG)
                    .show();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        onActivityResult(requestCode, resultCode, data);
    }

    private void showStoragePermissionNotGranted(View.OnClickListener listener) {
        // rootLayout = findViewById(R.id.rootLayout);

        Snackbar.make(fragmentMainBinding.rootLayout, R.string.storage_permission_not_granted, Snackbar.LENGTH_LONG)
                .setAction(R.string.grant, listener)
                .show();
    }


    private void setListeners() {

        fragmentMainBinding.highlightEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String highlight = v.getText().toString();

                viewModel.highlightFiles(highlight);

                Utils.hideSoftInput(getActivity());
                return true;
            }

            return false;
        });

        fragmentMainBinding.saveFilesListFab.setOnClickListener(v -> {
            requestStoragePermission(PM_SAVE_FILES_LIST_TAG);
        });

        sortDialog.setListener(option -> {
            viewModel.sortFiles(option);
            sortDialog.dismiss();
        });
    }


    public void requestStoragePermission(String tag) {
        this.tag = tag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestManageExternalStoragePermission();
        } else {
            requestReadExternalStoragePermission();
        }
    }

    @TargetApi(30)
    private void requestManageExternalStoragePermission() {
        if (isManageExternalStoragePermissionGranted()) {
            onStoragePermissionGranted(tag);
        } else {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                getActivity().startActivityForResult(intent, RC_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            } catch (ActivityNotFoundException e) {
                onStoragePermissionDenied(tag);
            }
        }
    }

    private void requestReadExternalStoragePermission() {
        if (isExternalStoragePermissionGranted()) {
            onStoragePermissionGranted(tag);
            //     viewModel.fetchFiles();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    RC_READ_WRITE_EXTERNAL_STORAGE_PERMISSION
            );
        }
    }

    @TargetApi(30)
    private boolean isManageExternalStoragePermissionGranted() {
        return Environment.isExternalStorageManager();
    }

    private boolean isExternalStoragePermissionGranted() {
        return ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void grantStoragePermissionManually(String tag) {
        this.tag = tag;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestManageExternalStoragePermission();
        } else {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
            intent.setData(uri);

            getActivity().startActivityForResult(intent, RC_OPEN_APP_SETTINGS);
        }
    }

}