package com.nirshal.util.files;

public enum FileType {
    CSV (".csv"),
    FIT (".fit"),
    TXT (".txt"),
    XLSX(".xlsx"),
    NOT_COMPATIBLE ("not compatible");

    private final String extension;

    FileType(String extension){
        this.extension = extension;
    }
    public String getExtension(){
        return this.extension;
    }

    public static FileType detectType(String filename){
        for (FileType fileType : FileType.values()) {
            if (isOfType(filename, fileType)) return fileType;
        }
        return FileType.NOT_COMPATIBLE;
    }

    public static boolean isOfType(String filename, FileType fileType){
        return filename.endsWith(fileType.getExtension());
    }
}
