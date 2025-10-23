package frontend;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import backend.*;
import java.util.concurrent.CompletableFuture;

/**
 * Main travel explorer view
 * Equivalent to the main Index.tsx page in React
 */
public class TravelExplorerView {
    
    private BorderPane root;
    private AuthService authService;
    private Runnable onLogout;
    private TextField searchField;
    private Button searchButton;
    private ScrollPane contentArea;
    private VBox loadingPane;
    private boolean isSearching = false;
    
    public TravelExplorerView(AuthService authService, Runnable onLogout) {
        this.authService = authService;
        this.onLogout = onLogout;
        createUI();
    }
    
    private void createUI() {
        root = new BorderPane();
        // Clean, professional gradient background
        root.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #f8fafc 0%, #e2e8f0 100%);");
        
        // Create header
        createHeader();
        
        // Create main content
        createMainContent();
        
        root.setTop(createHeader());
        root.setCenter(createMainContent());
    }
    
    private VBox createHeader() {
        VBox header = new VBox();
        // Clean white header with subtle shadow
        header.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-width: 0 0 1 0; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);");
        header.setPadding(new Insets(20, 30, 20, 30));
        
        HBox headerContent = new HBox();
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setSpacing(25);
        
        // App branding with improved readability
        VBox appInfo = new VBox(5);
        Label appTitle = new Label("✈ TravelExplorer");
        appTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        // Strong, readable color with excellent contrast
        appTitle.setStyle("-fx-text-fill: #1f2937; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 2, 0, 1, 1);");
        
        Label appSubtitle = new Label("Discover Amazing Destinations Worldwide");
        appSubtitle.setFont(Font.font("Segoe UI", 14));
        appSubtitle.setStyle("-fx-text-fill: #6b7280; -fx-font-weight: 500;");
        
        appInfo.getChildren().addAll(appTitle, appSubtitle);
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // User info section with better visibility
        VBox userInfo = new VBox(3);
        userInfo.setAlignment(Pos.CENTER_RIGHT);
        
        User currentUser = authService.getCurrentUser();
        Label userName = new Label("👤 " + currentUser.getName());
        userName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        userName.setStyle("-fx-text-fill: #111827;");
        
        Label userEmail = new Label("📧 " + currentUser.getEmail());
        userEmail.setFont(Font.font("Segoe UI", 13));
        userEmail.setStyle("-fx-text-fill: #6b7280;");
        
        userInfo.getChildren().addAll(userName, userEmail);
        
        // Modern logout button with hover effect
        Button logoutButton = new Button("🚪 Logout");
        logoutButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        logoutButton.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 20; -fx-effect: dropshadow(gaussian, rgba(220,38,38,0.3), 4, 0, 0, 2); -fx-cursor: hand;");
        
        // Add hover effect
        logoutButton.setOnMouseEntered(e -> 
            logoutButton.setStyle("-fx-background-color: #b91c1c; -fx-text-fill: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 20; -fx-effect: dropshadow(gaussian, rgba(220,38,38,0.4), 6, 0, 0, 3); -fx-cursor: hand;"));
        logoutButton.setOnMouseExited(e -> 
            logoutButton.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 20; -fx-effect: dropshadow(gaussian, rgba(220,38,38,0.3), 4, 0, 0, 2); -fx-cursor: hand;"));
        
        logoutButton.setOnAction(e -> {
            authService.logout();
            clearSearchResults();
            onLogout.run();
        });
        
        HBox userSection = new HBox(20);
        userSection.setAlignment(Pos.CENTER_RIGHT);
        userSection.getChildren().addAll(userInfo, logoutButton);
        
        headerContent.getChildren().addAll(appInfo, spacer, userSection);
        header.getChildren().add(headerContent);
        
        return header;
    }
    
    private ScrollPane createMainContent() {
        contentArea = new ScrollPane();
        contentArea.setFitToWidth(true);
        contentArea.setStyle("-fx-background-color: transparent;");
        
        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(40, 20, 40, 20));
        mainContent.setAlignment(Pos.TOP_CENTER);
        
        // Welcome section
        VBox welcomeSection = createWelcomeSection();
        
        // Search section
        VBox searchSection = createSearchSection();
        
        // Content area (will show loading or results)
        VBox contentSection = new VBox();
        contentSection.setAlignment(Pos.CENTER);
        contentSection.setMinHeight(200);
        
        // Default content
        VBox defaultContent = createDefaultContent();
        contentSection.getChildren().add(defaultContent);
        
        mainContent.getChildren().addAll(welcomeSection, searchSection, contentSection);
        contentArea.setContent(mainContent);
        
        return contentArea;
    }
    
    private VBox createWelcomeSection() {
        VBox welcome = new VBox(25);
        welcome.setAlignment(Pos.CENTER);
        welcome.setMaxWidth(1000);
        welcome.setPadding(new Insets(50, 30, 30, 30));
        // Light, clean card background
        welcome.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 20, 0, 0, 8);");
        
        // Main title with excellent readability
        Label title = new Label("✨ Explore Amazing Destinations");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 48));
        title.setStyle("-fx-text-fill: #1f2937; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 1, 1);");
        
        // Subtitle with clear contrast
        Label subtitle = new Label("Search for any city and discover comprehensive travel information, from breathtaking tourist spots to authentic local cuisine and hidden gems waiting to be explored.");
        subtitle.setFont(Font.font("Segoe UI", 20));
        subtitle.setStyle("-fx-text-fill: #4b5563; -fx-font-weight: 400; -fx-line-spacing: 5;");
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(800);
        subtitle.setAlignment(Pos.CENTER);
        
        welcome.getChildren().addAll(title, subtitle);
        return welcome;
    }
    
    private VBox createSearchSection() {
        VBox searchSection = new VBox(20);
        searchSection.setAlignment(Pos.CENTER);
        searchSection.setMaxWidth(800);
        searchSection.setPadding(new Insets(40, 30, 40, 30));
        // Clean white search card
        searchSection.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 5);");
        
        Label searchTitle = new Label("🔍 Start Your Journey");
        searchTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        searchTitle.setStyle("-fx-text-fill: #1f2937;");
        
        HBox searchBox = new HBox(15);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPadding(new Insets(10, 0, 0, 0));
        
        // Modern search input with excellent visibility
        searchField = new TextField();
        searchField.setPromptText("Enter a city name (e.g., Paris, Tokyo, Mumbai)...");
        searchField.setPrefHeight(65);
        searchField.setFont(Font.font("Segoe UI", 16));
        searchField.setStyle("-fx-background-color: #f9fafb; -fx-border-color: #d1d5db; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 10; -fx-padding: 0 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 3, 0, 0, 1); -fx-text-fill: #111827;");
        
        // Focus styling
        searchField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                searchField.setStyle("-fx-background-color: white; -fx-border-color: #3b82f6; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 10; -fx-padding: 0 20; -fx-effect: dropshadow(gaussian, rgba(59,130,246,0.2), 5, 0, 0, 2); -fx-text-fill: #111827;");
            } else {
                searchField.setStyle("-fx-background-color: #f9fafb; -fx-border-color: #d1d5db; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 10; -fx-padding: 0 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 3, 0, 0, 1); -fx-text-fill: #111827;");
            }
        });
        
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        // Modern explore button with gradient and hover effects
        searchButton = new Button("🌍 Explore");
        searchButton.setPrefHeight(65);
        searchButton.setPrefWidth(160);
        searchButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        searchButton.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #3b82f6 0%, #1d4ed8 100%); -fx-text-fill: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(59,130,246,0.3), 8, 0, 0, 4); -fx-cursor: hand;");
        
        // Hover effects for the button
        searchButton.setOnMouseEntered(e -> {
            if (!isSearching) {
                searchButton.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #2563eb 0%, #1e40af 100%); -fx-text-fill: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(59,130,246,0.4), 12, 0, 0, 6); -fx-cursor: hand; -fx-scale-x: 1.02; -fx-scale-y: 1.02;");
            }
        });
        searchButton.setOnMouseExited(e -> {
            if (!isSearching) {
                searchButton.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #3b82f6 0%, #1d4ed8 100%); -fx-text-fill: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(59,130,246,0.3), 8, 0, 0, 4); -fx-cursor: hand; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
            }
        });
        
        searchBox.getChildren().addAll(searchField, searchButton);
        
        // Event handlers
        searchButton.setOnAction(e -> performSearch());
        searchField.setOnAction(e -> performSearch());
        
        searchSection.getChildren().addAll(searchTitle, searchBox);
        return searchSection;
    }
    
    private VBox createDefaultContent() {
        VBox defaultContent = new VBox(20);
        defaultContent.setAlignment(Pos.CENTER);
        defaultContent.setPadding(new Insets(60));
        
        Label iconLabel = new Label("✈");
        iconLabel.setFont(Font.font(64));
        iconLabel.setStyle("-fx-text-fill: #cbd5e1;");
        
        Label message = new Label("Start by searching for a city to explore");
        message.setFont(Font.font("System", 16));
        message.setStyle("-fx-text-fill: #64748b;");
        
        defaultContent.getChildren().addAll(iconLabel, message);
        return defaultContent;
    }
    
    private VBox createLoadingPane() {
        VBox loading = new VBox(20);
        loading.setAlignment(Pos.CENTER);
        loading.setPadding(new Insets(60));
        
        ProgressIndicator progress = new ProgressIndicator();
        progress.setPrefSize(50, 50);
        
        Label loadingText = new Label("Loading city information...");
        loadingText.setFont(Font.font("System", 16));
        loadingText.setStyle("-fx-text-fill: #64748b;");
        
        loading.getChildren().addAll(progress, loadingText);
        return loading;
    }
    
    private void performSearch() {
        String cityName = searchField.getText().trim();
        if (cityName.isEmpty() || isSearching) {
            return;
        }
        
        isSearching = true;
        searchButton.setText("Searching...");
        searchButton.setDisable(true);
        searchField.setDisable(true);
        
        // Show loading
        VBox mainContent = (VBox) contentArea.getContent();
        VBox contentSection = (VBox) mainContent.getChildren().get(2);
        contentSection.getChildren().clear();
        contentSection.getChildren().add(createLoadingPane());
        
        // Perform API call
        CompletableFuture<CityData> future = GroqApiService.getCityInformation(cityName);
        
        future.thenAccept(cityData -> {
            Platform.runLater(() -> {
                // Show results
                contentSection.getChildren().clear();
                CityInfoView cityInfoView = new CityInfoView(cityData);
                contentSection.getChildren().add(cityInfoView.getRoot());
                
                // Reset search controls
                resetSearchControls();
                
                showAlert("Success", "Information about " + cityName + " loaded successfully!", Alert.AlertType.INFORMATION);
            });
        }).exceptionally(throwable -> {
            Platform.runLater(() -> {
                // Show error
                contentSection.getChildren().clear();
                VBox errorContent = createErrorContent(throwable.getMessage());
                contentSection.getChildren().add(errorContent);
                
                // Reset search controls
                resetSearchControls();
                
                showAlert("Error", "Could not fetch city information. Please try again.", Alert.AlertType.ERROR);
            });
            return null;
        });
    }
    
    private VBox createErrorContent(String errorMessage) {
        VBox errorContent = new VBox(20);
        errorContent.setAlignment(Pos.CENTER);
        errorContent.setPadding(new Insets(60));
        
        Label iconLabel = new Label("⚠");
        iconLabel.setFont(Font.font(64));
        iconLabel.setStyle("-fx-text-fill: #ef4444;");
        
        Label errorLabel = new Label("Unable to fetch city information");
        errorLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        errorLabel.setStyle("-fx-text-fill: #dc2626;");
        
        Label messageLabel = new Label("Please check your internet connection and try again");
        messageLabel.setFont(Font.font("System", 14));
        messageLabel.setStyle("-fx-text-fill: #64748b;");
        
        Button retryButton = new Button("Try Again");
        retryButton.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-background-radius: 5;");
        retryButton.setOnAction(e -> {
            VBox mainContent = (VBox) contentArea.getContent();
            VBox contentSection = (VBox) mainContent.getChildren().get(2);
            contentSection.getChildren().clear();
            contentSection.getChildren().add(createDefaultContent());
        });
        
        errorContent.getChildren().addAll(iconLabel, errorLabel, messageLabel, retryButton);
        return errorContent;
    }
    
    private void resetSearchControls() {
        isSearching = false;
        searchButton.setText("Explore");
        searchButton.setDisable(false);
        searchField.setDisable(false);
    }
    
    private void clearSearchResults() {
        if (contentArea != null && contentArea.getContent() != null) {
            VBox mainContent = (VBox) contentArea.getContent();
            if (mainContent.getChildren().size() > 2) {
                VBox contentSection = (VBox) mainContent.getChildren().get(2);
                contentSection.getChildren().clear();
                contentSection.getChildren().add(createDefaultContent());
            }
        }
        if (searchField != null) {
            searchField.clear();
        }
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public BorderPane getRoot() {
        return root;
    }
}