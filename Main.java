package frontend;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import backend.AuthService;
import backend.DataStorage;

/**
 * Main entry point for the TravelExplorer JavaFX application
 * Equivalent to the React App.tsx and main.tsx
 */
public class Main extends Application {
    
    private static final String APP_TITLE = "TravelExplorer - Discover the World";
    private static final double WINDOW_WIDTH = 1200;
    private static final double WINDOW_HEIGHT = 800;
    
    private AuthService authService;
    private Stage primaryStage;
    private MainController mainController;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        // Initialize services
        DataStorage.initialize();
        this.authService = new AuthService();
        
        // Create main controller
        this.mainController = new MainController(authService, primaryStage);
        
        // Setup primary stage
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setResizable(true);
        
        // Show login/main interface
        showInitialView();
        
        primaryStage.show();
    }
    
    private void showInitialView() {
        if (authService.getCurrentUser() != null) {
            showMainView();
        } else {
            showAuthView();
        }
    }
    
    private void showAuthView() {
        AuthView authView = new AuthView(authService, this::showMainView);
        Scene scene = new Scene(authView.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
    }
    
    private void showMainView() {
        TravelExplorerView mainView = new TravelExplorerView(authService, this::showAuthView);
        Scene scene = new Scene(mainView.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
    }
    
    @Override
    public void stop() throws Exception {
        super.stop();
        // Cleanup resources if needed
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}