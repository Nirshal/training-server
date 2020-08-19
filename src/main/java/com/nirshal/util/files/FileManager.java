package com.nirshal.util.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileManager {

    public static boolean existsFileOrDirectory(String filename, boolean checkDirectory){
        File outputFile = new File(filename);
        return outputFile.exists() && (checkDirectory? outputFile.isDirectory(): outputFile.isFile());
    }
    public static boolean exists(String filename){
        File outputFile = new File(filename);
        return outputFile.exists() && outputFile.isFile();
    }
    public static boolean isFile(String filename){
        File outputFile = new File(filename);
        return outputFile.exists() && outputFile.isFile();
    }
    public static boolean isDirectory(String filename){
        File outputFile = new File(filename);
        return outputFile.exists() && outputFile.isDirectory();
    }
    public static List<String> listFilesWithExtension(String inputDirName, String extension){
        if (existsFileOrDirectory(inputDirName,true)) {
            File inputDir = new File(inputDirName);
            String[] files = inputDir.list(
                    (File dir, String name) ->
                            extension == null || name.toLowerCase().endsWith(extension.toLowerCase())
                    );
            return files != null ? Arrays.asList(files) : new ArrayList<>();
        } else {
            System.err.println("Input directory does not exists: please create ./input");
            return new ArrayList<>();
        }
    }
}
