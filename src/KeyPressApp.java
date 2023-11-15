import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class KeyPressApp extends Application {
    private static final int ROWS = 3;
    private static final int COLS = 7;
    private static final int RECTANGLE_SIZE = 50;
    private static final String[] LABELS = {"Q", "W", "E", "R", "T", "Y", "U", "A", "S", "D", "F", "G", "H", "J", "Z", "X", "C", "V", "B", "N", "M"};
    private static final String AUDIO_FOLDER = "audio/";
    private static final String BACKGROUND_IMAGE_PATH = "image/bg_round.png"; // Update with your image file

    private final Rectangle[][] rectangles = new Rectangle[ROWS][COLS];
    private final Label[][] labels = new Label[ROWS][COLS];
    private final MediaPlayer[] mediaPlayers = new MediaPlayer[LABELS.length];
    private Set<String> pressedKeys = new HashSet<>();

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-image: url('" + new File(BACKGROUND_IMAGE_PATH).toURI().toString() + "'); " +
                      "-fx-background-size: cover;");

        GridPane gridPane = createGridPane();
        root.getChildren().add(gridPane);

        Scene scene = new Scene(root, root.getWidth(), root.getHeight());

        scene.setOnKeyPressed(this::handleKeyPress);
        scene.setOnKeyReleased(this::handleKeyRelease);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Key Press Demo");
        primaryStage.show();
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.BOTTOM_CENTER);
        gridPane.setVgap(20);
        gridPane.setHgap(50);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Rectangle rectangle = new Rectangle(RECTANGLE_SIZE, RECTANGLE_SIZE);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.TRANSPARENT);

                Label label = new Label(LABELS[row * COLS + col]);
                label.setFont(Font.font(18));
                label.setAlignment(Pos.CENTER);

                gridPane.add(rectangle, col, row);
                gridPane.add(label, col, row);

                rectangles[row][col] = rectangle;
                labels[row][col] = label;

                // Load media player for each label
                String fileName = LABELS[row * COLS + col] + ".wav";
                Media media = new Media(new File(AUDIO_FOLDER + fileName).toURI().toString());
                mediaPlayers[row * COLS + col] = new MediaPlayer(media);
            }
        }

        return gridPane;
    }

    private void handleKeyPress(KeyEvent event) {
        String keyText = event.getText().toUpperCase();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                String label = LABELS[row * COLS + col];
                if (label.equals(keyText)) {
                    rectangles[row][col].setFill(Color.DARKGRAY);
                    mediaPlayers[row * COLS + col].play();
                    break;
                }
            }
        }
    }

    private void handleKeyRelease(KeyEvent event) {
        String keyText = event.getText().toUpperCase();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                String label = LABELS[row * COLS + col];
                if (label.equals(keyText)) {
                    rectangles[row][col].setFill(Color.TRANSPARENT);
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
