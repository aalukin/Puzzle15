package com.hse.aalukin.puzzle15;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import java.io.IOException;
import java.util.*;

/**
 * Controller of game's mane form
 */
public class Controller {

    /**
     * Game's main pain
     */
    public BorderPane mainPain;

    /**
     * Open file button
     */
    public Button openButton;

    /**
     * Grid pane which contains image panes
     */
    public GridPane grid;

    /**
     * Image pane with full picture
     */
    public AnchorPane fullImagePane;

    /**
     * Label with top players
     */
    public Label topLabel;

    // Image panes which contain fragments of full picture
    public AnchorPane imagePane00;
    public AnchorPane imagePane01;
    public AnchorPane imagePane02;
    public AnchorPane imagePane03;
    public AnchorPane imagePane10;
    public AnchorPane imagePane11;
    public AnchorPane imagePane12;
    public AnchorPane imagePane13;
    public AnchorPane imagePane20;
    public AnchorPane imagePane21;
    public AnchorPane imagePane22;
    public AnchorPane imagePane23;
    public AnchorPane imagePane30;
    public AnchorPane imagePane31;
    public AnchorPane imagePane32;
    public AnchorPane hiddenImagePane;

    /**
     * Rows number
     */
    private final int ROWS = 4;

    /**
     * Columns number
     */
    private final int COLUMNS = 4;

    /**
     * Checking for correct order list
     */
    private List<AnchorPane> imageList = null;

    /**
     * List of current images's order
     */
    private List<Integer> orderList = null;

    /**
     * Top of players
     */
    private LeadersTable players = null;

    /**
     * Moves count
     */
    private int moves = 0;

    /**
     * Form initializing
     */
    @FXML
    public void initialize(){
        players = new LeadersTable();
        fillTable();
    }

    /**
     * Fill top players label
     */
    private void fillTable(){
        String text = "TOP10 Players:\n";
        for (int i = 0; i < players.getPlayersList().size() && i < 10; ++i){
            text += players.getPlayersList().get(i).toString() + "\n";
        }
        topLabel.setText(text);
    }

    /**
     * Open new image action
     */
    public void openImageAction(ActionEvent actionEvent) {
        grid.setVisible(false);
        // Open image file
        openImage();
        moves = 0;
        grid.setVisible(true);
    }

    /**
     * Open image file for game
     */
    private void openImage(){
        // Select file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Picture");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image File " +
                PictureLoader.IMAGE_FILES_EXTENSIONS.toString(),
                PictureLoader.IMAGE_FILES_EXTENSIONS));
        // Reading of image
        try{
            PictureLoader pictureLoader = new PictureLoader(fileChooser.showOpenDialog(mainPain.getScene().getWindow()), ROWS, COLUMNS);
            // Set up full image
            BackgroundImage backgroundImage = new BackgroundImage(pictureLoader.getImage(),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    new BackgroundSize(fullImagePane.getWidth(), fullImagePane.getHeight(),
                            true, true, true,true));
            fullImagePane.setBackground(new Background(backgroundImage));
            fullImagePane.setVisible(true);
            openButton.setVisible(false);
            // Set up images
            initImageList();
            setUpImages(pictureLoader.getCroppedImages());
            initGridPane();
        } catch (IOException ioe){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ioe.getMessage());
            alert.show();
        }
    }

    /**
     * Create matrix of imagePanes
     */
    private void initImageList(){
        this.imageList = Collections.unmodifiableList(Arrays.asList(
            imagePane00, imagePane10, imagePane20, imagePane30,
                imagePane01, imagePane11, imagePane21, imagePane31,
                imagePane02, imagePane12, imagePane22, imagePane32,
                imagePane03, imagePane13, imagePane23, hiddenImagePane
        ));
    }

    /**
     * Set up imagePanes backgrounds
     * @param images source of images
     */
    private void setUpImages(List<Image> images){
        for (int i = 0; i < images.size(); ++i){
            BackgroundImage backgroundImage = new BackgroundImage(images.get(i),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    new BackgroundSize(imageList.get(i).getWidth(), imageList.get(i).getHeight(),
                            true, true, true,true));
            imageList.get(i).setBackground(new Background(backgroundImage));
        }
        imageList.get(ROWS * COLUMNS - 1).setVisible(false);
    }

    /**
     * Set up image panes in grid
     */
    private void initGridPane(){
        grid.getChildren().clear();
        orderList = shuffle();
        for (int column = 0, i = 0; column < COLUMNS; ++column){
            for (int row = 0; row < ROWS; ++row, ++i){
                AnchorPane imagePane = imageList.get(i);
                imagePane.setDisable(false);
                grid.add(imageList.get(orderList.get(i)), column, row);
            }
        }
    }

    /**
     * Shuffle images
     * @return list with positions of image panes
     */
    private ArrayList<Integer> shuffle(){
        ArrayList<Integer> numbers;
        do {
            numbers = new ArrayList<>();
            for (int i = 0; i < ROWS * COLUMNS; ++i) {
                numbers.add(i);
            }
            Collections.shuffle(numbers);
        } while (evenChecker(numbers) && isGameOver());

        return numbers;
    }

    /**
     * Checking for puzzle even order
     * @param list - list to checking
     * @return true - order is even, otherwise - false
     */
    private boolean evenChecker(List<Integer> list){
        List<Integer> temp = new ArrayList<>();
        for (int i = 0; i < COLUMNS; ++i){
            for (int j = 0; j < ROWS; ++j){
                temp.add(list.get(i * ROWS + j));
            }
        }
        int sum = 0;
        for (int i = 0; i < temp.size(); ++i){
            for (int j = i + 1; j < temp.size(); ++j){
                ++sum;
            }
            if (temp.get(i) == 15){
                sum += 1 + i / COLUMNS;
            }
        }
        return sum % 2 == 1;
    }

    /**
     * Moving the image panes
     * @param mouseEvent mouse click event
     */
    public void cellClickAction(MouseEvent mouseEvent) {
        ++moves;
        AnchorPane clickedPane = (AnchorPane) mouseEvent.getSource();
        // Get clicked image row and column
        int row = GridPane.getRowIndex(clickedPane);
        int column = GridPane.getColumnIndex(clickedPane);
        // Get hidden image row and column
        int hiddenRow = GridPane.getRowIndex(hiddenImagePane);
        int hiddenColumn = GridPane.getColumnIndex(hiddenImagePane);
        // Checking for positions of clicked and hidden images
        if (row == hiddenRow && (column - 1 == hiddenColumn || column + 1 == hiddenColumn) ||
                (column == hiddenColumn && (row - 1 == hiddenRow || row + 1 == hiddenRow))) {
            // Moving of images
            grid.getChildren().remove(clickedPane);
            grid.getChildren().remove(hiddenImagePane);
            grid.add(clickedPane, hiddenColumn, hiddenRow);
            grid.add(hiddenImagePane, column, row);
            // Moving the positions
            Collections.swap(orderList, hiddenColumn * ROWS + hiddenRow, column * ROWS + row);
            // Checking for game over
            if (isGameOver()){
                GameOver();
            }
        }
    }

    /**
     * Checking for game over
     * @return true - game over, otherwise - false
     */
    private boolean isGameOver(){
        // checking for sorting
        for (int i = 0; i < orderList.size() - 1; ++i){
            if (orderList.get(i + 1) < orderList.get(i)){
                return false;
            }
        }

        return true;
    }

    /**
     * End of game
     */
    private void GameOver(){
        hiddenImagePane.setVisible(true);
        fullImagePane.setVisible(false);
        openButton.setVisible(true);
        for (Node node : grid.getChildren()){
            node.setDisable(true);
        }
        // Get user name
        String name = InputNameDialog.showDialog(moves);
        players.addPlayer(new Player(name, moves));
        fillTable();
    }

    /**
     * Getting info about program
     */
    public void infoAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Puzzle 15");
        alert.setContentText("Student: Lukin Artur\nGroup: BPI153(2)");
        alert.show();
    }
}