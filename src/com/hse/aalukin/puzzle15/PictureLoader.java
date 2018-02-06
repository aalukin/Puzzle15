package com.hse.aalukin.puzzle15;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class for load and division picture into 16 fragments
 */
class PictureLoader {

    /**
     * All possible image file's extensions
     */
    static final List<String> IMAGE_FILES_EXTENSIONS = Collections.unmodifiableList(Arrays.asList(
            "*.bmp", "*.jpg", "*.png", ".jpeg"
    ));

    /**
     * Directory which contains all program resources
     */
    private static final String RES_DIRECTORY_PATH = "img";

    /**
     * Cropped to square image file name
     */
    private static final String FULL_IMAGE_NAME = "full.jpg";

    /**
     * Cropped to square image
     */
    private File imageFile = new File(RES_DIRECTORY_PATH + File.separator + FULL_IMAGE_NAME);

    /**
     * Number of rows for cropping
     */
    private int rows;

    /**
     * Number of columns for cropping
     */
    private int columns;

    /**
     * Cropped to square image
     */
    private Image image = null;

    /**
     * List of cropped images
     */
    private List<Image> images = null;

    /**
     * Constructor of picture loader
     * @param file image file
     * @throws IOException if program has problems with reading of image file
     */
    PictureLoader(File file, int rows, int columns)throws IOException{
        deleteOldResources();
        Files.copy(file.toPath(), imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        this.rows = rows;
        this.columns = columns;
        image = trimToSquare();
        images = cropImage();
    }

    /**
     * Getter of full image
     * @return full image
     */
    Image getImage(){
        return image;
    }

    /**
     * Getter of image's cropped fragments
     * @return list of images
     */
    List<Image> getCroppedImages(){
        return images;
    }

    /**
     * Clear resources directory
     */
    private void deleteOldResources(){
        File resDirectory = new File(RES_DIRECTORY_PATH);
        if (resDirectory.exists()){
            resDirectory.delete();
        }

        resDirectory.mkdir();
    }

    /**
     * Trim input image for square
     * @return cropped to square image
     * @throws IOException if program has problems with reading image file
     */
    private Image trimToSquare() throws IOException{
        FileInputStream fis = new FileInputStream(imageFile);
        Image image = new Image(fis);
        fis.close();
        double size = Math.min(image.getWidth(), image.getHeight());
        double x = 0, y = 0;
        if (image.getWidth() > size){
            x = image.getWidth() > size ? (image.getWidth() - size) / 2 : 0;
        } else {
            y = image.getHeight() > size ? (image.getHeight() - size) / 2 : 0;
        }

        return new WritableImage(image.getPixelReader(), (int)x, (int)y, (int)size, (int)size);
    }

    /**
     * Crop image to 16 pieces
     * @return list of images
     */
    private List<Image> cropImage(){
        int size = (int)(image.getWidth() / rows);
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < columns; ++i){
            for (int j = 0; j < rows; ++j){
                images.add(new WritableImage(image.getPixelReader(), i * size, j * size, size, size));
            }
        }
        return images;
    }
}
