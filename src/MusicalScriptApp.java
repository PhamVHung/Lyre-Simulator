
import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MusicalScriptApp extends Application {

    private static final int RECTANGLE_SIZE = 50;
    private static final String[] SCRIPT_LINES = {
        "FSN__D",
        "DVM_S_G_D_SBM__A",
        "AN_M_DCM_M_MV_N",
        "NC_MB_SV_M_AN__SM__DA",
        "",
        "NC_G_FXN__D",
        "D-D.F.DB_S_G_D_SM__A",
        "A.S.AN_M_DC_J_HN__G_M_FV",
        "DC__S_DB_M__N",
        "",
        "MB_A_FHN-M-A-D",
        "AH-A-D-H-HBM",
        "G_G_F_GZ_B_M__N",
        "",
        "M_A_FX-N-M-A-FN",
        "MAFB_G_D__S_DZ_B_MB__NZ",
        "",
        "N_M_A_HAN_M-A-D-H-",
        "M-A-D-HAD_W_J_GM_DZ",
        "B_DM_B_DN",
        "",
        "QA_HF_A_DX_S_FV_N_NC",
        "C_M__NC",
        "C_M_A__NC",
        "",
        "FSX__D_DVM_S_G_D_SZM_A",
        "AN_M_DCM_M_MV_N",
        "NC_MB_SV_M_AN-C_SM-C_AD",
        "",
        "NC_G_FXN__D",
        "D-D.F.DBM_S_G__DB_SZM__AN",
        "A.S.AN_M_DC_J_HN-C_G_M_FV",
        "DC_S_DB_M_N",
        "",
        "MB_A_HXN_N-M-A",
        "HFN-M-A-HD_HXB-M-GA",
        "GXB-M-A-FS_GZ_C_MB_C_NZ",
        "",
        "MB_A_FXV_N-M-A-FV-N-M-A",
        "FXB_GM_DB_C-S-AC_Z",
        "SV_X-D-S-A-S-DC",
        "",
        "NX_G_HFX-M-A-D-HN",
        "M-A-D-HAX_W_JBM__GM_DZC",
        "B_MC_B_NC",
        "",
        "QA_HF_A_DX_S_FV_N_NC.M.N",
        "MB__NC",
        "C_M__N",
        "C.N.A.H"
    };

    private int currentLineIndex = 0;
    private int currentNoteIndex = 0;
    private Timeline timeline;

    private Rectangle[][] rectangles = new Rectangle[SCRIPT_LINES.length][];
    private Label[][] labels = new Label[SCRIPT_LINES.length][];

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: black;");

        GridPane gridPane = createGridPane();
        root.getChildren().add(gridPane);

        Scene scene = new Scene(root, 7 * RECTANGLE_SIZE, SCRIPT_LINES.length * RECTANGLE_SIZE);

        scene.setOnKeyPressed(this::handleKeyPress);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Musical Script Demo");
        primaryStage.show();

        // Initialize the timeline
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), event -> playNextNote());
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        for (int i = 0; i < SCRIPT_LINES.length; i++) {
            String line = SCRIPT_LINES[i];
            rectangles[i] = new Rectangle[line.length()];
            labels[i] = new Label[line.length()];

            for (int j = 0; j < line.length(); j++) {
                Rectangle rectangle = new Rectangle(RECTANGLE_SIZE, RECTANGLE_SIZE);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.WHITE);

                Label label = new Label(String.valueOf(line.charAt(j)));
                label.setFont(Font.font(18));
                label.setAlignment(Pos.CENTER);
                label.setLayoutX((rectangle.getHeight()-label.getPrefHeight())/2);
                label.setLayoutY((rectangle.getWidth()-label.getPrefWidth())/2);

                gridPane.add(rectangle, j, i);
                gridPane.add(label, j, i);

                rectangles[i][j] = rectangle;
                labels[i][j] = label;
            }
        }

        return gridPane;
    }

    private void handleKeyPress(KeyEvent event) {
        // Handle any additional key press events if needed
    }

    private void playNextNote() {
        if (currentLineIndex < SCRIPT_LINES.length) {
            String line = SCRIPT_LINES[currentLineIndex];
            if (currentNoteIndex < line.length()) {
                char noteChar = line.charAt(currentNoteIndex);

                // Skip over consecutive characters and calculate the duration
                double duration = 0.0;
                while (currentNoteIndex + 1 < line.length() && isConsecutiveCharacter(line.charAt(currentNoteIndex), line.charAt(currentNoteIndex + 1))) {
                    duration += getDuration(line.charAt(currentNoteIndex));
                    currentNoteIndex++;
                }

                // Play the note after the calculated duration
                playNoteAsync(noteChar, duration);

                // Update the current note index
                currentNoteIndex++;
            } else {
                // Move to the next line
                currentLineIndex++;
                currentNoteIndex = 0;

                // Reset colors for the next line
                for (Rectangle[] row : rectangles) {
                    for (Rectangle rectangle : row) {
                        rectangle.setFill(Color.WHITE);
                    }
                }
            }
        } else {
            // Stop the timeline when the script is finished
            timeline.stop();
        }
    }

    private boolean isConsecutiveCharacter(char currentChar, char nextChar) {
        return currentChar != '_' && currentChar != '-' && currentChar != '.' &&
               nextChar != '_' && nextChar != '-' && nextChar != '.';
    }

    private double getDuration(char noteChar) {
        switch (noteChar) {
            case '_':
                return 0.15;
            case '-':
                return 0.1;
            case '.':
                return 0.01;
            default:
                return 0.0;
        }
    }

    private void playNoteAsync(char noteChar, double duration) {
        new Thread(() -> {
            try {
                Thread.sleep((long) (duration * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String audioFilePath = getAudioFilePath(noteChar);
            AudioClip audioClip = new AudioClip(new File(audioFilePath).toURI().toString());
            audioClip.play();
        }).start();
    }

    private String getAudioFilePath(char noteChar) {
        switch (noteChar) {
            case 'A':
                return "audio/A.wav";
            case 'B':
                return "audio/B.wav";
            case 'C':
                return "audio/C.wav";
            case 'D':
                return "audio/D.wav";
            case 'E':
                return "audio/E.wav";
            case 'F':
                return "audio/F.wav";
            case 'G':
                return "audio/G.wav";
            case 'H':
                return "audio/H.wav";
            case 'J':
                return "audio/J.wav";
            case 'M':
                return "audio/M.wav";
            case 'N':
                return "audio/N.wav";
            case 'Q':
                return "audio/Q.wav";
            case 'R':
                return "audio/R.wav";
            case 'S':
                return "audio/S.wav";
            case 'T':
                return "audio/T.wav";
            case 'U':
                return "audio/U.wav";
            case 'V':
                return "audio/V.wav";
            case 'W':
                return "audio/W.wav";
            case 'X':
                return "audio/X.wav";
            case 'Y':
                return "audio/Y.wav";
            case 'Z':
                return "audio/Z.wav";
            // Add more cases for other notes
            default:
                return ""; // Handle other cases or return a default audio file
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
