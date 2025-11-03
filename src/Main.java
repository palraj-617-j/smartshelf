import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/Login.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/resources/css/style.css").toExternalForm());
            
            primaryStage.setTitle("Smart Shelf - Library Management");
            primaryStage.setScene(scene);
            
            // Better window sizing for login
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double width = Math.min(540, screenBounds.getWidth() * 0.9);
            double height = Math.min(800, screenBounds.getHeight() * 0.92); // Increased height
            
            primaryStage.setWidth(width);
            primaryStage.setHeight(height);
            primaryStage.centerOnScreen();
            
            primaryStage.setMinWidth(380);
            primaryStage.setMinHeight(650); // Increased min height
            primaryStage.setResizable(true);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
