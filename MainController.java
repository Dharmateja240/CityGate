package frontend;

import javafx.stage.Stage;
import backend.AuthService;

/**
 * Main controller for the application
 * Handles view transitions and navigation
 */
public class MainController {
    
    private AuthService authService;
    private Stage primaryStage;
    
    public MainController(AuthService authService, Stage primaryStage) {
        this.authService = authService;
        this.primaryStage = primaryStage;
    }
    
    public AuthService getAuthService() {
        return authService;
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public void showAuthView() {
        // Implementation will be added when creating AuthView
    }
    
    public void showMainView() {
        // Implementation will be added when creating TravelExplorerView
    }
}