import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

public class StockMarketBotUI extends Application {
    private StockMarketBot obj;

    public void start(Stage primaryStage) throws Exception {
        obj= new StockMarketBot();

        Label chatLabel = new Label("Stock Market");
        chatLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
         chatLabel.setFont(Font.font("Lucida Sans Unicode", FontPosture.ITALIC, 20));
        TextArea chatTextArea = new TextArea();
        chatTextArea.setWrapText(true);

        Button sendButton = new Button("Get Advice");
        sendButton.setStyle("-fx-background-color: #0077c0; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setVisible(false);

        sendButton.setOnAction(event -> {
            try {
                String message = "Become a Stock Market Agent and Provide Insight on the following Stock:"+chatTextArea.getText();

                progressBar.setVisible(true);
                progressBar.setProgress(0);
                new Thread(() -> {
                    try {
                        String response = obj.chatGPT(message, progress -> {
                            // Update the progress bar on the UI thread
                            javafx.application.Platform.runLater(() -> progressBar.setProgress(progress));
                        });
                        javafx.application.Platform.runLater(() -> chatTextArea.appendText("\n\n" + response));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        progressBar.setVisible(false);
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        HBox sendBox = new HBox(chatTextArea, sendButton);
        sendBox.setAlignment(Pos.CENTER);
        sendBox.setSpacing(20);

        VBox chatBox = new VBox(chatLabel, sendBox, progressBar);
        chatBox.setSpacing(10);
        chatBox.setPadding(new Insets(10));
        chatBox.setStyle("-fx-background-color: #f2f2f2;");

        BorderPane root = new BorderPane();
        root.setCenter(chatBox);

        Scene scene = new Scene(root, 750, 400);

        primaryStage.setTitle("MantraGPT (Created By: Arpit Singh)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
