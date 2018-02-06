package com.hse.aalukin.puzzle15;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

/**
 * Input user name form
 */
class InputNameDialog {

    static String showDialog(int result){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("You Are Win!!!");
        dialog.setHeaderText("Your result: " + result + " move(s)");
        Optional<String> name;
        String nameString = "";
        while (nameString.length() == 0){
            try{
                name = dialog.showAndWait();
                nameString = name.get();
            } catch (Exception ex){
                System.out.println("1");
            }
        }

        return nameString;
    }
}