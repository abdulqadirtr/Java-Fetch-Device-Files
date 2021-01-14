package com.mytask.fetchingfiles.repository;

import com.mytask.fetchingfiles.model.FileModel;

import java.util.List;

public interface FilesRepository {

    List<FileModel> getAllFiles();
    String saveFile(String filename, String content);
}