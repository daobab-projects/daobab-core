package io.daobab.generator;

public enum FileType {

    JAVA(".java"),
    TYPESCRIPT(".ts");

    private String extension;

    FileType(String extension) {
        this.setExtension(extension);
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
