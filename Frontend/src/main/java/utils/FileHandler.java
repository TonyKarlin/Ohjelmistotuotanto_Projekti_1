package utils;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class FileHandler {

    public File selectProfilePicture(Window ownerWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "image Files",
                "*.png",
                "*.jpg",
                ".jpeg"));
        return fileChooser.showOpenDialog(ownerWindow);
    }

    public void selectFile() {
        //for future implementation
    }

}
