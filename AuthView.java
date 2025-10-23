package frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import backend.AuthService;
import backend.AuthException;

/**
 * Authentication view containing login and signup forms
 * Equivalent to LoginForm.tsx and SignupForm.tsx
 */
public class AuthView {
    
    private BorderPane root;
    private AuthService authService;
    private Runnable onLoginSuccess;
    private boolean showLogin = true;
    
    public AuthView(AuthService authService, Runnable onLoginSuccess) {
        this.authService = authService;
        this.onLoginSuccess = onLoginSuccess;
        createUI();
    }
    
    private void createUI() {
        root = new BorderPane();
        // Clean, light background for professional appearance
        root.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #f8fafc 0%, #e2e8f0 100%);");
        
        // Create container for the form
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));
        
        // Add decorative header
        VBox decorativeHeader = createDecorativeHeader();
        
        showLoginForm();
        
        container.getChildren().addAll(decorativeHeader, getCurrentForm());
        root.setCenter(container);
    }
    
    private VBox createDecorativeHeader() {
        VBox header = new VBox(20);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 40, 0));
        
        // Modern, readable title
        Label decorativeTitle = new Label("✈ Welcome to TravelExplorer");
        decorativeTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 42));
        decorativeTitle.setStyle("-fx-text-fill: #1f2937; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 2, 2);");
        
        // Clear, professional subtitle
        Label decorativeSubtitle = new Label("Your gateway to discovering amazing destinations around the world");
        decorativeSubtitle.setFont(Font.font("Segoe UI", 18));
        decorativeSubtitle.setStyle("-fx-text-fill: #4b5563; -fx-font-weight: 400;");
        decorativeSubtitle.setWrapText(true);
        decorativeSubtitle.setMaxWidth(600);
        decorativeSubtitle.setAlignment(Pos.CENTER);
        
        header.getChildren().addAll(decorativeTitle, decorativeSubtitle);
        return header;
    }
    
    private VBox getCurrentForm() {
        return showLogin ? createLoginForm() : createSignupForm();
    }
    
    private void showLoginForm() {
        if (root.getCenter() != null) {
            VBox container = (VBox) root.getCenter();
            container.getChildren().clear();
            container.getChildren().addAll(createDecorativeHeader(), createLoginForm());
        }
    }
    
    private void showSignupForm() {
        if (root.getCenter() != null) {
            VBox container = (VBox) root.getCenter();
            container.getChildren().clear();
            container.getChildren().addAll(createDecorativeHeader(), createSignupForm());
        }
    }
    
    private VBox createLoginForm() {
        VBox form = new VBox(20);
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(450);
        form.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5);");
        form.setPadding(new Insets(40));
        
        // Header with enhanced styling
        Label titleLabel = new Label("🚀 Welcome Back!");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #667eea;");
        
        Label subtitleLabel = new Label("Sign in to continue your travel journey");
        subtitleLabel.setFont(Font.font("System", 16));
        subtitleLabel.setStyle("-fx-text-fill: #64748b; -fx-font-style: italic;");
        
        // Enhanced form fields
        Label emailLabel = new Label("📮 Email Address");
        emailLabel.setStyle("-fx-text-fill: #374151; -fx-font-weight: bold;");
        
        TextField emailField = new TextField();
        emailField.setPromptText("your.email@example.com");
        emailField.setPrefHeight(50);
        emailField.setStyle("-fx-font-size: 16px; -fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 0 15; -fx-background-color: #f9fafb; -fx-border-color: #d1d5db;");
        
        Label passwordLabel = new Label("🔒 Password");
        passwordLabel.setStyle("-fx-text-fill: #374151; -fx-font-weight: bold;");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("••••••••");
        passwordField.setPrefHeight(50);
        passwordField.setStyle("-fx-font-size: 16px; -fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 0 15; -fx-background-color: #f9fafb; -fx-border-color: #d1d5db;");
        
        Button signInButton = new Button("✨ Sign In");
        signInButton.setPrefHeight(55);
        signInButton.setPrefWidth(Double.MAX_VALUE);
        signInButton.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);");
        // Enhanced toggle link
        Label toggleText = new Label("Don't have an account? ");
        toggleText.setFont(Font.font("System", 14));
        toggleText.setStyle("-fx-text-fill: #6b7280;");
        
        Hyperlink signUpLink = new Hyperlink("Sign up here");
        signUpLink.setFont(Font.font("System", FontWeight.BOLD, 14));
        signUpLink.setStyle("-fx-text-fill: #667eea; -fx-underline: true;");
        
        HBox toggleBox = new HBox(5);
        toggleBox.setAlignment(Pos.CENTER);
        toggleBox.getChildren().addAll(toggleText, signUpLink);
        
        // Event handlers
        signInButton.setOnAction(e -> handleLogin(emailField.getText(), passwordField.getText()));
        signUpLink.setOnAction(e -> {
            showLogin = false;
            showSignupForm();
        });
        
        // Add Enter key support
        passwordField.setOnAction(e -> handleLogin(emailField.getText(), passwordField.getText()));
        
        form.getChildren().addAll(
            titleLabel, subtitleLabel,
            emailLabel, emailField,
            passwordLabel, passwordField,
            signInButton, toggleBox
        );
        
        return form;
    }
    
    private VBox createSignupForm() {
        VBox form = new VBox(15);
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(400);
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        form.setPadding(new Insets(30));
        
        // Header
        Label titleLabel = new Label("Create Account");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #1e293b;");
        
        Label subtitleLabel = new Label("Join us to discover amazing destinations");
        subtitleLabel.setStyle("-fx-text-fill: #64748b;");
        
        // Form fields
        TextField nameField = new TextField();
        nameField.setPromptText("John Doe");
        nameField.setPrefHeight(40);
        nameField.setStyle("-fx-font-size: 14px;");
        
        TextField emailField = new TextField();
        emailField.setPromptText("you@example.com");
        emailField.setPrefHeight(40);
        emailField.setStyle("-fx-font-size: 14px;");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("••••••••");
        passwordField.setPrefHeight(40);
        passwordField.setStyle("-fx-font-size: 14px;");
        
        Button signUpButton = new Button("Sign Up");
        signUpButton.setPrefHeight(40);
        signUpButton.setPrefWidth(Double.MAX_VALUE);
        signUpButton.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5;");
        
        // Toggle link
        Label toggleText = new Label("Already have an account? ");
        toggleText.setStyle("-fx-text-fill: #64748b;");
        
        Hyperlink signInLink = new Hyperlink("Sign in");
        signInLink.setStyle("-fx-text-fill: #3b82f6; -fx-underline: false;");
        
        HBox toggleBox = new HBox(toggleText, signInLink);
        toggleBox.setAlignment(Pos.CENTER);
        
        // Event handlers
        signUpButton.setOnAction(e -> handleSignup(nameField.getText(), emailField.getText(), passwordField.getText()));
        signInLink.setOnAction(e -> {
            showLogin = true;
            showLoginForm();
        });
        
        // Add Enter key support
        passwordField.setOnAction(e -> handleSignup(nameField.getText(), emailField.getText(), passwordField.getText()));
        
        form.getChildren().addAll(
            titleLabel, subtitleLabel,
            new Label("Full Name"), nameField,
            new Label("Email"), emailField,
            new Label("Password"), passwordField,
            signUpButton, toggleBox
        );
        
        return form;
    }
    
    private void handleLogin(String email, String password) {
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            showAlert("Error", "Please fill in all fields", Alert.AlertType.ERROR);
            return;
        }
        
        try {
            authService.login(email.trim(), password);
            showAlert("Success", "Welcome back! You have successfully logged in.", Alert.AlertType.INFORMATION);
            onLoginSuccess.run();
        } catch (AuthException e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void handleSignup(String name, String email, String password) {
        if (name.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            showAlert("Error", "Please fill in all fields", Alert.AlertType.ERROR);
            return;
        }
        
        try {
            authService.signup(email.trim(), password, name.trim());
            showAlert("Success", "Welcome! Your account has been created successfully.", Alert.AlertType.INFORMATION);
            onLoginSuccess.run();
        } catch (AuthException e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
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