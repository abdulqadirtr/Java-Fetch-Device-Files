package com.mytask.fetchingfiles.model;

import androidx.annotation.StringRes;

import com.mytask.fetchingfiles.R;

public enum FilesSort {
    FILENAME_ASC(R.string.filename),
    CREATION_TIME_ASC(R.string.creation_time),
    EXTENSION_ASC(R.string.extension);

    public final int caption;

    FilesSort(@StringRes int caption) {
        this.caption = caption;
    }
}