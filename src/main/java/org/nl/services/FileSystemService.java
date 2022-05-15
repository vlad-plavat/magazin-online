package org.nl.services;

import org.nl.model.Product;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemService {
    private static final String APPLICATION_FOLDER = "database";
    private static final String USER_FOLDER = "";
    public static final Path APPLICATION_HOME_PATH = Paths.get(USER_FOLDER, APPLICATION_FOLDER);

    public static Path getPathToFile(String... path) {
        return APPLICATION_HOME_PATH.resolve(Paths.get(".", path));
    }
    public static Path getFullPath(String... path){
        return APPLICATION_HOME_PATH.resolve(Paths.get("", path)).toAbsolutePath();
    }
    public static String imgFromPrd(Product p){
        return "file:/"+APPLICATION_HOME_PATH.resolve(Paths.get( "productImages")).toAbsolutePath()+"/"+p.getImageAddr();
    }
}
