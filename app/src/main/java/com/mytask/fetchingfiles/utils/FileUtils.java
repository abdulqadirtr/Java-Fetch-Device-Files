package com.mytask.fetchingfiles.utils;

import android.webkit.MimeTypeMap;


import com.mytask.fetchingfiles.model.FilesDescriptionParcelable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class FileUtils {

    public static FilesDescriptionParcelable getFileAttributes(File file) {
        FilesDescriptionParcelable attributes = new FilesDescriptionParcelable();
        /*
         * Seems like Android does not support a time stamp to indicate the time of last modification.
         */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                Path path = Paths.get(file.toURI());
                BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

                attributes.setCreationTime(attrs.creationTime().toMillis());
                attributes.setLastModifiedTime(attrs.lastModifiedTime().toMillis());
                attributes.setSize(attrs.size());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                attributes.setCreationTime(file.lastModified());
                attributes.setLastModifiedTime(file.lastModified());
                attributes.setSize(file.length());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        attributes.setExtension(MimeTypeMap.getFileExtensionFromUrl(file.toString()));

        return attributes;
    }
}
