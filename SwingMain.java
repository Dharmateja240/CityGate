package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import backend.*;
import java.util.concurrent.CompletableFuture;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Swing-based main application - works with standard Java
 * Alternative to JavaFX version for immediate preview
 */
public class SwingMain extends JFrame {
    
    private AuthService authService;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private boolean isLoggedIn = false;
    
    // Auth form switching variables
    private JPanel authFormContainer;
    private CardLayout authCardLayout;
    
    public SwingMain() {
        // Initialize services
        DataStorage.initialize();
        this.authService = new AuthService();
        
        // Setup frame
        setTitle("TravelExplorer - Discover the World");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Setup layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create panels
        createAuthPanel();
        createMainAppPanel();
        
        add(mainPanel);
        
        // Show appropriate panel
        showInitialView();
    }
    
    private void showInitialView() {
        if (authService.getCurrentUser() != null) {
            showMainApp();
        } else {
            showAuth();
        }
    }
    
    private void showAuth() {
        cardLayout.show(mainPanel, "AUTH");
        isLoggedIn = false;
    }
    
    private void showMainApp() {
        cardLayout.show(mainPanel, "MAIN");
        isLoggedIn = true;
        refreshMainApp();
    }
    
    private void createAuthPanel() {
        // Create gradient background panel
        JPanel authPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(0, 0, new Color(99, 102, 241), 
                                                         getWidth(), getHeight(), new Color(139, 92, 246));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Create container for the form with shadow effect
        JPanel formContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(8, 8, getWidth() - 8, getHeight() - 8, 20, 20);
                
                // Draw white background
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, 20, 20);
            }
        };
        formContainer.setLayout(new CardLayout());
        formContainer.setOpaque(false);
        formContainer.setPreferredSize(new Dimension(450, 500));
        
        // Create login form
        JPanel loginForm = createLoginForm();
        
        // Create register form 
        JPanel registerForm = createRegisterForm();
        
        // Add both forms to container
        formContainer.add(loginForm, "LOGIN");
        formContainer.add(registerForm, "REGISTER");
        
        authPanel.add(formContainer, gbc);
        mainPanel.add(authPanel, "AUTH");
        
        // Store reference for switching
        this.authFormContainer = formContainer;
        this.authCardLayout = new CardLayout();
        formContainer.setLayout(authCardLayout);
        formContainer.add(loginForm, "LOGIN");
        formContainer.add(registerForm, "REGISTER");
    }
    
    private JPanel createLoginForm() {
        JPanel loginForm = new JPanel();
        loginForm.setLayout(new BoxLayout(loginForm, BoxLayout.Y_AXIS));
        loginForm.setOpaque(false);
        loginForm.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Enhanced title with gradient text effect
        JLabel titleLabel = new JLabel("✈ Welcome Back", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Sign in to explore amazing destinations worldwide", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Enhanced form fields with modern styling
        JTextField emailField = createStyledTextField("Enter your email");
        JPasswordField passwordField = createStyledPasswordField("Enter your password");
        
        // Enhanced login button with hover effects
        JButton loginButton = createStyledButton("Sign In ✈", new Color(59, 130, 246), Color.WHITE);
        loginButton.setPreferredSize(new Dimension(220, 50));
        
        // Add components with better spacing
        loginForm.add(Box.createVerticalStrut(20));
        loginForm.add(titleLabel);
        loginForm.add(Box.createVerticalStrut(8));
        loginForm.add(subtitleLabel);
        loginForm.add(Box.createVerticalStrut(30));
        
        loginForm.add(createFieldLabel("Email Address"));
        loginForm.add(Box.createVerticalStrut(8));
        loginForm.add(emailField);
        loginForm.add(Box.createVerticalStrut(15));
        
        loginForm.add(createFieldLabel("Password"));
        loginForm.add(Box.createVerticalStrut(8));
        loginForm.add(passwordField);
        loginForm.add(Box.createVerticalStrut(25));
        
        loginForm.add(loginButton);
        loginForm.add(Box.createVerticalStrut(20));
        
        // Create account link
        JButton createAccountLink = new JButton("Create a new account");
        createAccountLink.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        createAccountLink.setForeground(new Color(59, 130, 246));
        createAccountLink.setBackground(Color.WHITE);
        createAccountLink.setBorderPainted(false);
        createAccountLink.setContentAreaFilled(false);
        createAccountLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createAccountLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add hover effect for the link
        createAccountLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                createAccountLink.setText("<html><u>Create a new account</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                createAccountLink.setText("Create a new account");
            }
        });
        
        loginForm.add(createAccountLink);
        loginForm.add(Box.createVerticalStrut(10));
        
        // Add "Don't have an account?" text with better contrast
        JLabel signupPrompt = new JLabel("Don't have an account? Click above to register!", SwingConstants.CENTER);
        signupPrompt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        signupPrompt.setForeground(new Color(100, 116, 139));
        signupPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginForm.add(signupPrompt);
        
        // Event handlers with loading animation
        ActionListener loginAction = e -> performLoginWithAnimation(emailField, passwordField, loginButton);
        
        loginButton.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
        createAccountLink.addActionListener(e -> showRegisterForm());
        
        return loginForm;
    }
    
    private JPanel createRegisterForm() {
        JPanel registerForm = new JPanel();
        registerForm.setLayout(new BoxLayout(registerForm, BoxLayout.Y_AXIS));
        registerForm.setOpaque(false);
        registerForm.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Title section
        JLabel titleLabel = new JLabel("🎆 Join TravelExplorer", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Create your account to start exploring amazing destinations", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Database info
        JLabel dbInfoLabel = new JLabel("💾 Your data will be securely stored in MongoDB database", SwingConstants.CENTER);
        dbInfoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        dbInfoLabel.setForeground(new Color(59, 130, 246));
        dbInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        registerForm.add(Box.createVerticalStrut(10));
        registerForm.add(titleLabel);
        registerForm.add(Box.createVerticalStrut(8));
        registerForm.add(subtitleLabel);
        registerForm.add(Box.createVerticalStrut(5));
        registerForm.add(dbInfoLabel);
        registerForm.add(Box.createVerticalStrut(25));
        
        // Form fields with enhanced styling
        JTextField nameField = createStyledTextField("Enter your full name");
        JTextField emailField = createStyledTextField("Enter your email address");
        JPasswordField passwordField = createStyledPasswordField("Create a password (min 6 characters)");
        
        registerForm.add(createFieldLabel("👤 Full Name"));
        registerForm.add(Box.createVerticalStrut(8));
        registerForm.add(nameField);
        registerForm.add(Box.createVerticalStrut(15));
        
        registerForm.add(createFieldLabel("📧 Email Address"));
        registerForm.add(Box.createVerticalStrut(8));
        registerForm.add(emailField);
        registerForm.add(Box.createVerticalStrut(15));
        
        registerForm.add(createFieldLabel("🔒 Password"));
        registerForm.add(Box.createVerticalStrut(8));
        registerForm.add(passwordField);
        registerForm.add(Box.createVerticalStrut(25));
        
        // Buttons
        JButton signupButton = createStyledButton("🎉 Create Account", new Color(59, 130, 246), Color.WHITE);
        signupButton.setPreferredSize(new Dimension(220, 50));
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        registerForm.add(signupButton);
        registerForm.add(Box.createVerticalStrut(15));
        
        // Back to login link
        JButton backToLoginLink = new JButton("Already have an account? Sign in");
        backToLoginLink.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backToLoginLink.setForeground(new Color(59, 130, 246));
        backToLoginLink.setBackground(Color.WHITE);
        backToLoginLink.setBorderPainted(false);
        backToLoginLink.setContentAreaFilled(false);
        backToLoginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backToLoginLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add hover effect for the link
        backToLoginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backToLoginLink.setText("<html><u>Already have an account? Sign in</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                backToLoginLink.setText("Already have an account? Sign in");
            }
        });
        
        registerForm.add(backToLoginLink);
        
        // Event handlers
        signupButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showStyledMessage("Please fill in all fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (password.length() < 6) {
                showStyledMessage("Password must be at least 6 characters long", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Show loading state
            signupButton.setText("🔄 Creating Account...");
            signupButton.setEnabled(false);
            
            // Perform registration in background
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(1000); // Simulate processing time
                    authService.signup(email, password, name);
                    
                    SwingUtilities.invokeLater(() -> {
                        showStyledMessage("Welcome to TravelExplorer! Your account has been created successfully and stored in MongoDB.", "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
                        showMainApp();
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        signupButton.setText("🎉 Create Account");
                        signupButton.setEnabled(true);
                        showStyledMessage(ex.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
                    });
                }
            });
        });
        
        backToLoginLink.addActionListener(e -> showLoginForm());
        
        return registerForm;
    }
    
    private void showLoginForm() {
        authCardLayout.show(authFormContainer, "LOGIN");
    }
    
    private void showRegisterForm() {
        authCardLayout.show(authFormContainer, "REGISTER");
    }
    

    
    private void createMainAppPanel() {
        mainAppPanel = new JPanel(new BorderLayout());
        mainAppPanel.setBackground(new Color(248, 250, 252));
        
        // Header
        headerPanel = createHeaderPanel();
        
        // Main content
        contentPanel = createContentPanel();
        
        mainAppPanel.add(headerPanel, BorderLayout.NORTH);
        mainAppPanel.add(contentPanel, BorderLayout.CENTER);
        
        mainPanel.add(mainAppPanel, "MAIN");
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Left side - App info
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel appTitle = new JLabel("TravelExplorer");
        appTitle.setFont(new Font("Arial", Font.BOLD, 20));
        appTitle.setForeground(new Color(30, 41, 59));
        
        leftPanel.add(appTitle);
        
        // Right side - User info and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.WHITE);
        
        User currentUser = authService.getCurrentUser();
        if (currentUser != null) {
            JLabel userLabel = new JLabel(currentUser.getName() + " (" + currentUser.getEmail() + ")");
            userLabel.setForeground(new Color(100, 116, 139));
            rightPanel.add(userLabel);
        }
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(241, 245, 249));
        logoutButton.setForeground(new Color(71, 85, 105));
        logoutButton.addActionListener(e -> {
            authService.logout();
            showAuth();
        });
        
        rightPanel.add(logoutButton);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel contentPanel;
    private JPanel headerPanel;
    private JPanel mainAppPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JScrollPane resultScrollPane;
    
    private JPanel createContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        
        // Welcome section
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(new Color(248, 250, 252));
        
        JLabel titleLabel = new JLabel("Explore Amazing Destinations", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(59, 130, 246));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Search for any city and discover comprehensive travel information", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        welcomePanel.add(titleLabel);
        welcomePanel.add(Box.createVerticalStrut(10));
        welcomePanel.add(subtitleLabel);
        welcomePanel.add(Box.createVerticalStrut(30));
        
        // Search section
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBackground(new Color(248, 250, 252));
        
        searchField = new JTextField(25);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        searchButton = new JButton("Explore");
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setBackground(new Color(59, 130, 246));
        searchButton.setForeground(Color.WHITE);
        searchButton.setPreferredSize(new Dimension(100, 45));
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        welcomePanel.add(searchPanel);
        
        // Results area
        resultScrollPane = new JScrollPane();
        resultScrollPane.setBorder(BorderFactory.createEmptyBorder());
        resultScrollPane.setBackground(new Color(248, 250, 252));
        
        showDefaultContent();
        
        // Event handlers
        ActionListener searchAction = e -> performSearch();
        searchButton.addActionListener(searchAction);
        searchField.addActionListener(searchAction);
        
        contentPanel.add(welcomePanel, BorderLayout.NORTH);
        contentPanel.add(resultScrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private void showDefaultContent() {
        JPanel defaultPanel = new JPanel();
        defaultPanel.setLayout(new BoxLayout(defaultPanel, BoxLayout.Y_AXIS));
        defaultPanel.setBackground(new Color(248, 250, 252));
        defaultPanel.setBorder(BorderFactory.createEmptyBorder(60, 20, 60, 20));
        
        JLabel iconLabel = new JLabel("✈", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 64));
        iconLabel.setForeground(new Color(203, 213, 225));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel messageLabel = new JLabel("Start by searching for a city to explore", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messageLabel.setForeground(new Color(100, 116, 139));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        defaultPanel.add(iconLabel);
        defaultPanel.add(Box.createVerticalStrut(20));
        defaultPanel.add(messageLabel);
        
        resultScrollPane.setViewportView(defaultPanel);
    }
    
    private void performSearch() {
        String cityName = searchField.getText().trim();
        if (cityName.isEmpty()) {
            return;
        }
        
        // Enhanced loading animation
        searchButton.setText("🔍 Searching...");
        searchButton.setEnabled(false);
        searchField.setEnabled(false);
        
        // Create animated loading panel
        JPanel loadingPanel = new JPanel() {
            private int animationFrame = 0;
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw animated gradient background
                GradientPaint gradient = new GradientPaint(0, 0, new Color(139, 92, 246, 50), 
                                                         getWidth(), getHeight(), new Color(59, 130, 246, 50));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw animated circles
                for (int i = 0; i < 3; i++) {
                    int alpha = Math.max(50, Math.min(255, (int) (100 + 155 * Math.sin((animationFrame + i * 20) * 0.1))));
                    g2d.setColor(new Color(59, 130, 246, alpha));
                    int x = getWidth() / 2 - 60 + i * 30;
                    int y = getHeight() / 2 + (int) (10 * Math.sin((animationFrame + i * 15) * 0.2));
                    g2d.fillOval(x, y, 20, 20);
                }
                
                animationFrame++;
            }
        };
        
        loadingPanel.setLayout(new BoxLayout(loadingPanel, BoxLayout.Y_AXIS));
        loadingPanel.setBackground(new Color(248, 250, 252));
        loadingPanel.setBorder(BorderFactory.createEmptyBorder(80, 20, 80, 20));
        
        JLabel loadingIcon = new JLabel("🌍", SwingConstants.CENTER);
        loadingIcon.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        loadingIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel loadingLabel = new JLabel("Discovering amazing places in " + cityName + "...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loadingLabel.setForeground(new Color(59, 130, 246));
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subLabel = new JLabel("Gathering travel insights from around the world", SwingConstants.CENTER);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subLabel.setForeground(new Color(100, 116, 139));
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        loadingPanel.add(loadingIcon);
        loadingPanel.add(Box.createVerticalStrut(20));
        loadingPanel.add(loadingLabel);
        loadingPanel.add(Box.createVerticalStrut(10));
        loadingPanel.add(subLabel);
        
        resultScrollPane.setViewportView(loadingPanel);
        
        // Start animation timer
        Timer animationTimer = new Timer();
        TimerTask animationTask = new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> loadingPanel.repaint());
            }
        };
        animationTimer.scheduleAtFixedRate(animationTask, 0, 50);
        
        // Perform API call in background
        CompletableFuture<CityData> future = GroqApiService.getCityInformation(cityName);
        
        future.thenAccept(cityData -> {
            SwingUtilities.invokeLater(() -> {
                animationTimer.cancel();
                showCityInfo(cityData);
                resetSearchControls();
                showStyledMessage("Information about " + cityName + " loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            });
        }).exceptionally(throwable -> {
            SwingUtilities.invokeLater(() -> {
                animationTimer.cancel();
                showErrorContent(throwable.getMessage());
                resetSearchControls();
                showStyledMessage("Could not fetch city information. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            });
            return null;
        });
    }
    
    private void showCityInfo(CityData cityData) {
        JPanel cityPanel = new JPanel(new BorderLayout());
        cityPanel.setBackground(new Color(248, 250, 252));
        cityPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Hero section at the top
        JPanel heroPanel = new JPanel();
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setBackground(new Color(59, 130, 246));
        heroPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel cityLabel = new JLabel(cityData.getCity(), SwingConstants.CENTER);
        cityLabel.setFont(new Font("Arial", Font.BOLD, 32));
        cityLabel.setForeground(Color.WHITE);
        cityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel locationLabel = new JLabel(cityData.getState() + ", " + cityData.getCountry(), SwingConstants.CENTER);
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        locationLabel.setForeground(new Color(240, 240, 240));
        locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel bestTimeLabel = new JLabel("Best Time: " + cityData.getBestTimeToVisit(), SwingConstants.CENTER);
        bestTimeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bestTimeLabel.setForeground(new Color(230, 230, 230));
        bestTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextArea descriptionArea = new JTextArea(cityData.getDescription());
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionArea.setForeground(new Color(240, 240, 240));
        descriptionArea.setBackground(new Color(59, 130, 246));
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        heroPanel.add(cityLabel);
        heroPanel.add(Box.createVerticalStrut(10));
        heroPanel.add(locationLabel);
        heroPanel.add(Box.createVerticalStrut(10));
        heroPanel.add(bestTimeLabel);
        heroPanel.add(Box.createVerticalStrut(15));
        heroPanel.add(descriptionArea);
        
        // Create main content panel with grid layout for cards
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(new Color(248, 250, 252));
        
        // Create cards container with grid layout
        JPanel cardsContainer = new JPanel(new GridLayout(0, 2, 20, 20)); // 2 columns, flexible rows
        cardsContainer.setBackground(new Color(248, 250, 252));
        cardsContainer.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        // Add information cards in a nice grid
        addInfoCard(cardsContainer, "Tourist Spots", cityData.getTouristSpots());
        addInfoCard(cardsContainer, "Famous Food", cityData.getFamousFood());
        addInfoCard(cardsContainer, "Restaurants", cityData.getRestaurants());
        addInfoCard(cardsContainer, "Attractions", cityData.getAttractions());
        addInfoCard(cardsContainer, "Hotels", cityData.getHotels());
        addInfoCard(cardsContainer, "Local Tips", cityData.getLocalTips());
        
        // Add weather and languages as full-width cards
        JPanel fullWidthContainer = new JPanel();
        fullWidthContainer.setLayout(new BoxLayout(fullWidthContainer, BoxLayout.Y_AXIS));
        fullWidthContainer.setBackground(new Color(248, 250, 252));
        
        addTextCard(fullWidthContainer, "Weather", cityData.getWeather());
        addInfoCard(fullWidthContainer, "Languages Spoken", cityData.getLanguagesSpoken());
        
        // Assemble the layout
        mainContentPanel.add(cardsContainer, BorderLayout.CENTER);
        mainContentPanel.add(fullWidthContainer, BorderLayout.SOUTH);
        
        cityPanel.add(heroPanel, BorderLayout.NORTH);
        cityPanel.add(mainContentPanel, BorderLayout.CENTER);
        
        resultScrollPane.setViewportView(cityPanel);
    }
    
    private void addInfoSection(JPanel parent, String title, java.util.List<String> items) {
        if (items == null || items.isEmpty()) return;
        
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        section.add(titleLabel);
        section.add(Box.createVerticalStrut(10));
        
        for (String item : items) {
            JLabel itemLabel = new JLabel("• " + item);
            itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            itemLabel.setForeground(new Color(100, 116, 139));
            itemLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            section.add(itemLabel);
        }
        
        parent.add(section);
        parent.add(Box.createVerticalStrut(15));
    }
    
    private void addInfoCard(JPanel parent, String title, java.util.List<String> items) {
        if (items == null || items.isEmpty()) return;
        
        // Create animated card with gradient background
        JPanel card = new JPanel() {
            private boolean isHovered = false;
            
            public void setHovered(boolean hovered) {
                this.isHovered = hovered;
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient based on card title
                Color startColor, endColor;
                switch (title) {
                    case "Tourist Spots":
                        startColor = new Color(254, 240, 138); // Yellow
                        endColor = new Color(251, 191, 36);
                        break;
                    case "Famous Food":
                        startColor = new Color(254, 205, 211); // Pink
                        endColor = new Color(251, 113, 133);
                        break;
                    case "Restaurants":
                        startColor = new Color(196, 254, 211); // Green
                        endColor = new Color(74, 222, 128);
                        break;
                    case "Attractions":
                        startColor = new Color(191, 219, 254); // Blue
                        endColor = new Color(59, 130, 246);
                        break;
                    case "Hotels":
                        startColor = new Color(221, 214, 254); // Purple
                        endColor = new Color(139, 92, 246);
                        break;
                    case "Local Tips":
                        startColor = new Color(254, 215, 170); // Orange
                        endColor = new Color(251, 146, 60);
                        break;
                    default:
                        startColor = new Color(243, 244, 246);
                        endColor = new Color(229, 231, 235);
                }
                
                // Draw shadow
                if (isHovered) {
                    g2d.setColor(new Color(0, 0, 0, 40));
                    g2d.fillRoundRect(6, 6, getWidth() - 6, getHeight() - 6, 15, 15);
                } else {
                    g2d.setColor(new Color(0, 0, 0, 20));
                    g2d.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 15, 15);
                }
                
                // Draw gradient background
                GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 15, 15);
                
                // Draw border
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(endColor.darker());
                g2d.drawRoundRect(1, 1, getWidth() - 8, getHeight() - 8, 15, 15);
            }
        };
        
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(350, 200));
        card.setMinimumSize(new Dimension(350, 200));
        
        // Add hover animation
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel) e.getSource()).putClientProperty("hovered", true);
                animateCardScale(card, 1.05f);
                card.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                ((JPanel) e.getSource()).putClientProperty("hovered", false);
                animateCardScale(card, 1.0f);
                card.repaint();
            }
        });
        
        // Enhanced title with icon
        String icon = getIconForTitle(title);
        JLabel titleLabel = new JLabel(icon + " " + title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(12));
        
        // Create scrollable content for items with enhanced styling
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setOpaque(false);
        
        int itemCount = 0;
        for (String item : items) {
            if (itemCount >= 5) break; // Limit items for better UX
            
            JLabel itemLabel = new JLabel("• " + item);
            itemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            itemLabel.setForeground(new Color(55, 65, 81));
            itemLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            itemLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
            itemsPanel.add(itemLabel);
            itemCount++;
        }
        
        if (items.size() > 5) {
            JLabel moreLabel = new JLabel("... and " + (items.size() - 5) + " more");
            moreLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            moreLabel.setForeground(new Color(107, 114, 128));
            moreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            itemsPanel.add(moreLabel);
        }
        
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        card.add(scrollPane);
        parent.add(card);
    }
    
    private void addTextCard(JPanel parent, String title, String text) {
        if (text == null || text.trim().isEmpty()) return;
        
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setPreferredSize(new Dimension(350, 200)); // Standard card size from memory
        card.setMinimumSize(new Dimension(350, 200));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(new Color(100, 116, 139));
        textArea.setBackground(Color.WHITE);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(scrollPane);
        
        parent.add(card);
        parent.add(Box.createVerticalStrut(15));
    }
    
    private void addTextSection(JPanel parent, String title, String text) {
        if (text == null || text.trim().isEmpty()) return;
        
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(new Color(100, 116, 139));
        textArea.setBackground(Color.WHITE);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        section.add(titleLabel);
        section.add(Box.createVerticalStrut(10));
        section.add(textArea);
        
        parent.add(section);
        parent.add(Box.createVerticalStrut(15));
    }
    
    private void showErrorContent(String error) {
        JPanel errorPanel = new JPanel();
        errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
        errorPanel.setBackground(new Color(248, 250, 252));
        errorPanel.setBorder(BorderFactory.createEmptyBorder(60, 20, 60, 20));
        
        JLabel iconLabel = new JLabel("⚠", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 64));
        iconLabel.setForeground(new Color(239, 68, 68));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel errorLabel = new JLabel("Unable to fetch city information", SwingConstants.CENTER);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 18));
        errorLabel.setForeground(new Color(220, 38, 38));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel messageLabel = new JLabel("Please check your internet connection and try again", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(100, 116, 139));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton retryButton = new JButton("Try Again");
        retryButton.setBackground(new Color(59, 130, 246));
        retryButton.setForeground(Color.WHITE);
        retryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        retryButton.addActionListener(e -> showDefaultContent());
        
        errorPanel.add(iconLabel);
        errorPanel.add(Box.createVerticalStrut(20));
        errorPanel.add(errorLabel);
        errorPanel.add(Box.createVerticalStrut(10));
        errorPanel.add(messageLabel);
        errorPanel.add(Box.createVerticalStrut(20));
        errorPanel.add(retryButton);
        
        resultScrollPane.setViewportView(errorPanel);
    }
    
    private void resetSearchControls() {
        searchButton.setText("Explore");
        searchButton.setEnabled(true);
        searchField.setEnabled(true);
    }
    
    private void refreshMainApp() {
        if (isLoggedIn && mainAppPanel != null && headerPanel != null) {
            // Remove old header and create new one with updated user info
            mainAppPanel.remove(headerPanel);
            headerPanel = createHeaderPanel();
            mainAppPanel.add(headerPanel, BorderLayout.NORTH);
            mainAppPanel.revalidate();
            mainAppPanel.repaint();
        }
    }
    
    // Enhanced UI Helper Methods
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        field.setBackground(new Color(249, 250, 251));
        
        // Add hover effect
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(59, 130, 246), 2),
                    BorderFactory.createEmptyBorder(11, 14, 11, 14)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!field.hasFocus()) {
                    field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                        BorderFactory.createEmptyBorder(12, 15, 12, 15)
                    ));
                }
            }
        });
        
        return field;
    }
    
    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        field.setBackground(new Color(249, 250, 251));
        
        // Add hover effect
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(59, 130, 246), 2),
                    BorderFactory.createEmptyBorder(11, 14, 11, 14)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!field.hasFocus()) {
                    field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                        BorderFactory.createEmptyBorder(12, 15, 12, 15)
                    ));
                }
            }
        });
        
        return field;
    }
    
    private JButton createStyledButton(String text, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Force the blue color for buttons
                Color bgColor = backgroundColor; // Use the passed color directly
                if (getModel().isPressed()) {
                    bgColor = new Color(37, 99, 235); // Darker blue when pressed
                } else if (getModel().isRollover()) {
                    bgColor = new Color(96, 165, 250); // Lighter blue on hover
                }
                
                // Draw solid blue background
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Draw border for definition
                g2d.setColor(new Color(37, 99, 235));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE); // Always white text
        button.setBackground(backgroundColor); // Set background color
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(55, 65, 81));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private void performLoginWithAnimation(JTextField emailField, JPasswordField passwordField, JButton loginButton) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            showStyledMessage("Please fill in all fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Start loading animation
        String originalText = loginButton.getText();
        loginButton.setEnabled(false);
        
        Timer animationTimer = new Timer();
        TimerTask animationTask = new TimerTask() {
            int dots = 0;
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    String loadingText = "Signing In" + ".".repeat((dots % 3) + 1);
                    loginButton.setText(loadingText);
                    dots++;
                });
            }
        };
        animationTimer.scheduleAtFixedRate(animationTask, 0, 500);
        
        // Perform login in background
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1500); // Simulate network delay
                authService.login(email, password);
                
                SwingUtilities.invokeLater(() -> {
                    animationTimer.cancel();
                    loginButton.setText(originalText);
                    loginButton.setEnabled(true);
                    showStyledMessage("Welcome back! Login successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    showMainApp();
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    animationTimer.cancel();
                    loginButton.setText(originalText);
                    loginButton.setEnabled(true);
                    showStyledMessage(ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
                });
            }
        });
    }
    
    private void showStyledMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    private String getIconForTitle(String title) {
        switch (title) {
            case "Tourist Spots":
                return "🏝";
            case "Famous Food":
                return "🍴";
            case "Restaurants":
                return "🏭";
            case "Attractions":
                return "🎡";
            case "Hotels":
                return "🏨";
            case "Local Tips":
                return "💡";
            case "Languages Spoken":
                return "🌍";
            case "Weather":
                return "☀";
            default:
                return "📍";
        }
    }
    
    private void animateCardScale(JPanel card, float targetScale) {
        // Simple scale effect using preferred size
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            float currentScale = 1.0f;
            int steps = 0;
            final int maxSteps = 10;
            
            @Override
            public void run() {
                if (steps >= maxSteps) {
                    timer.cancel();
                    return;
                }
                
                currentScale += (targetScale - currentScale) * 0.3f;
                
                SwingUtilities.invokeLater(() -> {
                    Dimension originalSize = new Dimension(350, 200);
                    int newWidth = (int) (originalSize.width * currentScale);
                    int newHeight = (int) (originalSize.height * currentScale);
                    card.setPreferredSize(new Dimension(newWidth, newHeight));
                    card.revalidate();
                    card.repaint();
                });
                
                steps++;
            }
        };
        timer.scheduleAtFixedRate(task, 0, 20);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            } catch (Exception e) {
                // Use default look and feel
            }
            
            new SwingMain().setVisible(true);
        });
    }
}