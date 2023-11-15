
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
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;

public class NewFXMain extends Application {

    private static final int ROWS = 3;
    private static final int COLS = 7;
    private static final int RECTANGLE_SIZE = 50;
    private static final String[] LABELS = {"Q", "W", "E", "R", "T", "Y", "U", "A", "S", "D", "F", "G", "H", "J", "Z", "X", "C", "V", "B", "N", "M"};
    private static final String AUDIO_FOLDER = "audio/";
    private static final String BACKGROUND_IMAGE_PATH = "image/bg_qingce_village.jpg"; // Update with your image file

    private Pane[][] rectangles = new Pane[ROWS][COLS];
    private Label[][] labels = new Label[ROWS][COLS];
    private Map<String, String> audioFiles = new HashMap<>();
    private Set<String> pressedKeys = new HashSet<>();
    private List<MediaPlayer> activeMediaPlayers = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-image: url('" + new File(BACKGROUND_IMAGE_PATH).toURI().toString() + "'); "
                + "-fx-background-size: cover;");

        GridPane gridPane = createGridPane();
        root.getChildren().add(gridPane);

        Scene scene = new Scene(root, 800, 600);

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
                Pane rectangle = new Pane();
                rectangle.setBorder(Border.stroke(Color.BLACK));
                rectangle.setBackground(Background.fill(Color.BLACK));
                rectangle.setPrefSize(100, 100);
//                rectangle.setStroke(Color.BLACK);
//                rectangle.setFill(Color.TRANSPARENT);

                Label label = new Label(LABELS[row * COLS + col]);
                label.setFont(Font.font(18));
                label.setAlignment(Pos.CENTER);

                gridPane.add(rectangle, col, row);
                gridPane.add(label, col, row);

                label.setLayoutY((rectangle.getLayoutY() - rectangle.getHeight()) / 2);
                label.setLayoutX((rectangle.getLayoutX() - rectangle.getWidth()) / 2);
                rectangles[row][col] = rectangle;
                labels[row][col] = label;

                // Store audio file paths
                String fileName = LABELS[row * COLS + col] + ".wav";
                audioFiles.put(LABELS[row * COLS + col], AUDIO_FOLDER + fileName);
            }
        }

        return gridPane;
    }

    private void handleKeyPress(KeyEvent event) {
        String keyText = event.getText().toUpperCase();

        if (!pressedKeys.contains(keyText)) {
            pressedKeys.add(keyText);

            Pane rectangle = findRectangleByKey(keyText);

            // Handle key press
//            rectangle.setFill(Color.DARKGRAY);
            playNoteAsync(audioFiles.get(keyText));

            // Handle key release
            rectangle.setOnKeyReleased(keyEvent -> {
                pressedKeys.remove(keyText);
//                rectangle.setFill(Color.TRANSPARENT);
            });
        }
    }

    private void handleKeyRelease(KeyEvent event) {
        String keyText = event.getText().toUpperCase();
        pressedKeys.remove(keyText);
    }

    private void playNoteAsync(String audioFilePath) {
        new Thread(() -> {
            Media media = new Media(new File(audioFilePath).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnEndOfMedia(() -> {
                activeMediaPlayers.remove(mediaPlayer);
            });
            activeMediaPlayers.add(mediaPlayer);
            mediaPlayer.play();
        }).start();
    }

    private Pane findRectangleByKey(String keyText) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                String label = LABELS[row * COLS + col];
                if (label.equals(keyText)) {
                    return rectangles[row][col];
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
