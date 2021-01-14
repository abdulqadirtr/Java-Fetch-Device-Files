package com.mytask.fetchingfiles.ui.fragments;

import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mytask.fetchingfiles.R;
import com.mytask.fetchingfiles.databinding.FragmentDetailsBinding;
import com.mytask.fetchingfiles.utils.Utils;
import com.squareup.picasso.Picasso;
import java.io.File;

public class DetailsFragment extends Fragment {

    private static final String KEY_FILE_MODEL = "KEY_FILE_MODEL";
    private FragmentDetailsBinding fragmentDetailsBinding;

    Long create_data;
    Long size;
    Long modified_date;
    String imgUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);
        View view = fragmentDetailsBinding.getRoot();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            create_data = bundle.getLong("create_date", 0);
            size = bundle.getLong("size", 0);
            modified_date = bundle.getLong("modified_date", 0);
            imgUrl = bundle.getString("image", null);

        }

        initView();


        return view;
    }

    void initView() {
        fragmentDetailsBinding.creationTimeTextView.setText(Utils.formatDateTime(create_data));
        fragmentDetailsBinding.sizeTextView.setText(Utils.getFileSize(size));
        fragmentDetailsBinding.lastModifiedTimeTextView.setText(Utils.formatDateTime(modified_date));
        fragmentDetailsBinding.pathTextView.setText(imgUrl);

        Picasso.with(getActivity()).load(new File(imgUrl)).into(fragmentDetailsBinding.previewImageView);
    }


    void OnBackPressedCallback() {
        requireActivity().getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        NavHostFragment.findNavController(DetailsFragment.this).navigate(R.id.action_mySecondFragment_to_myHomeFragment3);
                    }
                }
        );
    }



}
