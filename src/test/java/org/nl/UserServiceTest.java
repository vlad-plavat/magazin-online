package org.nl;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.nl.controllers.RegistrationController;
import org.nl.exceptions.UsernameAlreadyExistsException;
import org.nl.exceptions.WrongPasswordException;
import org.nl.exceptions.WrongUsernameException;
import org.nl.model.User;
import org.nl.services.FileSystemService;
import org.nl.services.StageService;
import org.nl.services.UserService;
import org.testfx.assertions.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testfx.assertions.api.Assertions.assertThat;

import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;



public class UserServiceTest {

    public static final String NAME = "Bob";
    public static final String PASSWORD = "bob1234";
    public static final String ROLE = "Client";
    public static final String AUX = "Somewhere";

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.setAppFolder("database-test");
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
    }
    @AfterEach
    void tearDown() {
        UserService.closeDatabase();
    }



    @Test
    @DisplayName("Database is initialized, and there are no users")
    void testDatabaseIsInitializedAndNoUserIsPersisted() {
        assertThat(UserService.getDatabase()).isNotNull();
        assertThat(UserService.getAllUsers().toList()).isNotNull();
        assertThat(UserService.getAllUsers().toList()).isEmpty();
    }

    @Test
    @DisplayName("User is successfully persisted to Database")
    void testUserIsAddedToDatabase() throws UsernameAlreadyExistsException {
        UserService.addUser(NAME, PASSWORD, ROLE, AUX);
        assertThat(UserService.getAllUsers().toList()).isNotEmpty();
        assertThat(UserService.getAllUsers().toList()).size().isEqualTo(1);
        User user = UserService.getAllUsers().toList().get(0);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(NAME);
        assertThat(user.getPassword()).isEqualTo(UserService.encodePassword(NAME, PASSWORD));
        assertThat(user.getRole()).isEqualTo(ROLE);
        assertThat(user.getAux()).isEqualTo(AUX);
    }

    @Test
    @DisplayName("Encripting passwords works")
    void testEncription() {
        String e1 = UserService.encodePassword("something","else");
        String e2 = UserService.encodePassword("something","else");
        String e3 = UserService.encodePassword("something2","else");
        String e4 = UserService.encodePassword("something","else2");
        assertThat(e2).isEqualTo(e1);
        assertThat(e3).isNotEqualTo(e1);
        assertThat(e4).isNotEqualTo(e1);

    }

    @Test
    @DisplayName("Multiple users are successfully persisted to Database")
    void testUsersAreAddedToDatabase() throws UsernameAlreadyExistsException {
        ArrayList<User> ulist = new ArrayList<>();
        int i=0;
        for(int nruser=0; nruser<100; nruser++){
            User u = new User(""+i,"!@#"+(i+1),""+(i+2),""+i*i*i*i*i*i);
            ulist.add(u);
            UserService.addUser(u.getUsername(), u.getPassword(), u.getRole(), u.getAux());
            assertThrows(UsernameAlreadyExistsException.class, () ->
                    UserService.addUser(u.getUsername(), u.getPassword(), u.getRole(), u.getAux()) );
            i+=5;
        }
        assertThat(UserService.getAllUsers().toList()).isNotEmpty();
        assertThat(UserService.getAllUsers().toList()).size().isEqualTo(100);
        i=0;
        for(User user : UserService.getAllUsers()){
            assertThat(user).isNotNull();
            assertThat(user.getUsername()).isEqualTo(ulist.get(i).getUsername());
            assertThat(user.getPassword()).isEqualTo(
                    UserService.encodePassword(ulist.get(i).getUsername(), ulist.get(i).getPassword()));
            assertThat(user.getRole()).isEqualTo(ulist.get(i).getRole());
            assertThat(user.getAux()).isEqualTo(ulist.get(i).getAux());
            i++;
        }
    }

    @Test
    @DisplayName("Can change data of the logged user")
    void testUserDataIsChanged() throws UsernameAlreadyExistsException {
        User u = new User(NAME, PASSWORD, ROLE, AUX);

        UserService.addUser(NAME, PASSWORD, ROLE, AUX);
        UserService.addUser(NAME+"2", PASSWORD, ROLE, AUX);
        assertThat(UserService.getAllUsers().toList()).isNotEmpty();
        assertThat(UserService.getAllUsers().toList()).size().isEqualTo(2);

        int found=0;
        for(User user : UserService.getAllUsers())
            if (user.getUsername().equals(NAME)) {
                found = 1;
                break;
            }
        assertThat(found).isEqualTo(1);
        RegistrationController.loggeduser = u;
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            //if user wants to change name to an already used name
            UserService.changeUserData(NAME+"2","Pass2","newaux");
        });
        UserService.changeUserData("NewName","Pass2","newaux");
        User foundUser=null;
        for(User user : UserService.getAllUsers())
            if(user.getUsername().equals(NAME))
                foundUser=user;
        assertThat(foundUser).isNull();
        for(User user : UserService.getAllUsers())
            if(user.getUsername().equals("NewName"))
                foundUser=user;
        assertThat(foundUser).isNotNull();

        assertThat(foundUser.getUsername()).isEqualTo("NewName");
        assertThat(foundUser.getPassword()).isEqualTo(UserService.encodePassword("NewName","Pass2"));
        assertThat(foundUser.getRole()).isEqualTo(ROLE);
        assertThat(foundUser.getAux()).isEqualTo("newaux");
    }

    @Test
    @DisplayName("User is successfully deleted from Database")
    void testUserIsDeletedFromDatabase() throws UsernameAlreadyExistsException {
        User u = new User(NAME, PASSWORD, ROLE, AUX);

        UserService.addUser(NAME, PASSWORD, ROLE, AUX);
        UserService.addUser(NAME+"2", PASSWORD, ROLE, AUX);
        assertThat(UserService.getAllUsers().toList()).isNotEmpty();
        assertThat(UserService.getAllUsers().toList()).size().isEqualTo(2);

        int found=0;
        for(User user : UserService.getAllUsers())
            if (user.getUsername().equals(NAME)) {
                found = 1;
                break;
            }
        assertThat(found).isEqualTo(1);
        RegistrationController.loggeduser = u;
        UserService.simpleDelete();
        assertThat(UserService.getAllUsers().toList()).size().isEqualTo(1);
        User foundUser=null;
        for(User user : UserService.getAllUsers())
            if(user.getUsername().equals(NAME))
                foundUser=user;
        assertThat(foundUser).isNull();
    }

    @Test
    @DisplayName("Can check user credentials")
    void testChechCredentials() throws UsernameAlreadyExistsException, WrongUsernameException, WrongPasswordException {
        UserService.addUser(NAME, PASSWORD, ROLE, AUX);
        UserService.addUser(NAME+"2", PASSWORD+"1234", ROLE, AUX);
        assertThat(UserService.getAllUsers().toList()).isNotEmpty();
        assertThat(UserService.getAllUsers().toList()).size().isEqualTo(2);


        assertThat(UserService.checkLoginCredentials(NAME,PASSWORD)).isNotNull();
        assertThat(UserService.checkLoginCredentials(NAME+"2",PASSWORD+"1234")).isNotNull();
        assertThrows(WrongUsernameException.class, () -> UserService.checkLoginCredentials(NAME+"nobody",PASSWORD));
        assertThrows(WrongPasswordException.class, () -> UserService.checkLoginCredentials(NAME,PASSWORD+"nopass"));

    }
}
