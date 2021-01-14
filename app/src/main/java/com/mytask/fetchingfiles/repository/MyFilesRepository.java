package com.mytask.fetchingfiles.repository;

import android.os.Environment;

import com.mytask.fetchingfiles.model.FileModel;
import com.mytask.fetchingfiles.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MyFilesRepository implements FilesRepository {

    private List<FileModel> files = new ArrayList<>();

    /**
     * @return the list of files from the external storage.
     */
    @Override
    public List<FileModel> getAllFiles() {
        files.clear();
        URI entryUri = Environment.getExternalStorageDirectory().toURI();
        getAllFiles(entryUri);
        return files;
    }

    private void getAllFiles(URI uri) {
        File directory = new File(uri);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    this.files.add(new FileModel(
                            file.getName(),
                            file.getPath(),
                            FileUtils.getFileAttributes(file)
                    ));
                } else {
                    getAllFiles(file.toURI());
                }
            }
        }
    }
    @Override
    public String saveFile(String filename, String content) {
        String externalStorageDir = Environment.getExternalStorageDirectory().toString();

        File file = new File(externalStorageDir, filename);
        BufferedWriter bufferedWriter = null;

        try {
            FileWriter fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(content);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file.getAbsolutePath();
    }
}