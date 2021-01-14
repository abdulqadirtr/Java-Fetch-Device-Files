package com.mytask.fetchingfiles.model;

import android.text.Spannable;

public class MyFileItems {
    private FileModel fileModel;
    private Spannable filename;

    public MyFileItems(FileModel fileModel, Spannable filename) {
        this.fileModel = fileModel;
        this.filename = filename;
    }

    public FileModel getFileModel() {
        return fileModel;
    }

    public Spannable getFilename() {
        return filename;
    }
}