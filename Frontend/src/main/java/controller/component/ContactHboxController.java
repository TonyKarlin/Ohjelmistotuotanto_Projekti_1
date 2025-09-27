package controller.component;

import java.io.IOException;

import controller.ChatDashboardController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import lombok.Data;
import model.Contact;
import utils.ImageRounder;

@Data
public class ContactHboxController {

    Contact contact;
    ChatDashboardController parentController;
    ImageRounder imageRounder;

    public void setController(Contact contact, ChatDashboardController parentController) {
        this.contact = contact;
        this.parentController = parentController;
        imageRounder = new ImageRounder(contactUserPicture);
    }

    @FXML
    private ImageView contactUserPicture;

    @FXML
    private Circle contactUserStatus;

    @FXML
    private Label contactUsername;

    public void setUsername(String username) {
        contactUsername.setText(username);
    }

    public void setUserImage(Image image) {
        contactUserPicture.setImage(image);
    }

    @FXML
    public void openContactProfile() throws IOException, InterruptedException {
        // TODO: to be implemented, open the users profile
        System.out.println("To be implemented");
    }

}
