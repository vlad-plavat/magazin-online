package org.nl.services;

import org.nl.model.Product;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemService {
    private static String APPLICATION_FOLDER = "database";
    private static final String USER_FOLDER = "";
    public static Path APPLICATION_HOME_PATH = Paths.get(USER_FOLDER, APPLICATION_FOLDER);

    public static void setAppFolder(String s){
        APPLICATION_FOLDER = s;
        APPLICATION_HOME_PATH = Paths.get(USER_FOLDER, APPLICATION_FOLDER);
    }

    public static Path getPathToFile(String... path) {
        return APPLICATION_HOME_PATH.resolve(Paths.get(".", path));
    }
    public static Path getFullPath(String... path){
        return APPLICATION_HOME_PATH.resolve(Paths.get("", path)).toAbsolutePath();
    }
    public static String imgFromPrd(Product p){
        return "file:/"+APPLICATION_HOME_PATH.resolve(Paths.get( "productImages")).toAbsolutePath()+"/"+p.getImageAddr();
    }

    public static Path getApplicationHomeFolder() {
        return Paths.get(USER_FOLDER, APPLICATION_FOLDER);
    }

    public static void initDirectory() {
        Path applicationHomePath = getApplicationHomeFolder();
        Path imgPath = Paths.get(applicationHomePath.toString(), "productImages");
        if (!Files.exists(applicationHomePath)) {
            if (!applicationHomePath.toFile().mkdirs())
                throw new RuntimeException("Could not initialize directories");
        }
        if (!Files.exists(imgPath)) {
            if (!imgPath.toFile().mkdirs())
                throw new RuntimeException("Could not initialize directories");
        }
    }
}
