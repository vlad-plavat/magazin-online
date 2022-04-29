package org.nl.services;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.dizitart.no2.Cursor;
import org.dizitart.no2.Document;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.objects.ObjectRepository;
import org.nl.Main;
import org.nl.controllers.AccountSettingsController;
import org.nl.controllers.RegistrationController;
import org.nl.exceptions.UsernameAlreadyExistsException;
import org.nl.exceptions.WrongPasswordException;
import org.nl.exceptions.WrongUsernameException;
import org.nl.model.User;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Objects;

import static org.nl.services.FileSystemService.getPathToFile;

public class UserService {

    private static ObjectRepository<User> userRepository;

    public static Nitrite getDatabase() {
        return database;
    }

    private static Nitrite database;

    public static void initDatabase() {
        database = Nitrite.builder()
                .filePath(getPathToFile("natureleaf.db").toFile())
                .openOrCreate("admin", "admin");

        userRepository = database.getRepository(User.class);

    }

    public static User addUser(String username, String password, String role, String aux) throws UsernameAlreadyExistsException {
        checkUserDoesNotAlreadyExist(username);
        userRepository.insert(new User(username, encodePassword(username, password), role, aux));
        return new User(username, encodePassword(username, password), role, aux);

    }

    public static void changeUserData(String username, String password, String aux) throws UsernameAlreadyExistsException {
        if(!username.equals(RegistrationController.loggeduser.getUsername()))
            checkUserDoesNotAlreadyExist(username);
        String role = RegistrationController.loggeduser.getRole();
        userRepository.remove(RegistrationController.loggeduser);
        userRepository.insert(new User(username, encodePassword(username, password), role, aux));
        //return new User(username, encodePassword(username, password), aux);

    }

    public static void deleteUser(ActionEvent evt){
        userRepository.remove(RegistrationController.loggeduser);
        try {
            URL toFxml = Main.class.getClassLoader().getResource("register.fxml");
            if(toFxml == null)
                throw new RuntimeException("Could not load FXML file register.fxml");
            Parent root = FXMLLoader.load(toFxml);
            Stage stage = AccountSettingsController.getCrStage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            RegistrationController.loggeduser = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void readusers(){
        NitriteCollection nc = userRepository.getDocumentCollection();
        /*Cursor lind = nc.find();
        Iterator it = lind.iterator();
        while (it.hasNext()){
            System.out.println(  ((Document)(it.next())).get("username")  );
        }*/
    }

    public static User checkLoginCredentials(String username, String password) throws WrongPasswordException, WrongUsernameException {
        User foundUser = null;
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername())) {
                foundUser = user;
            }
        }
        if(foundUser == null)
            throw new WrongUsernameException(username);

        if(!foundUser.getPassword().equals(encodePassword(username, password)))
            throw new WrongPasswordException(username);

        return foundUser;
    }

    private static void checkUserDoesNotAlreadyExist(String username) throws UsernameAlreadyExistsException {
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername()))
                throw new UsernameAlreadyExistsException(username);
        }
    }

    public static String encodePassword(String salt, String password) {
        MessageDigest md = getMessageDigest();
        md.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        // This is the way a password should be encoded when checking the credentials
        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\"", ""); //to be able to save in JSON format
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }


}
