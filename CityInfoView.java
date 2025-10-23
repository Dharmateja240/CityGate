package frontend;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;
import backend.CityData;
public class CityInfoView extends JPanel {
    
    private CityData cityData;
    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY = new Color(75, 85, 99);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    
    public CityInfoView(CityData cityData) {
        this.cityData = cityData;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        createUI();
    }
    
    private void createUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Hero section
        mainPanel.add(createHeroSection());
        mainPanel.add(Box.createVerticalStrut(25));
        
        // Grid sections
        mainPanel.add(createGridSection());
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeroSection() {
        JPanel hero = new JPanel();
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));
        hero.setBackground(new Color(31, 41, 55));
        hero.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(20, new Color(31, 41, 55)),
            BorderFactory.createEmptyBorder(50, 50, 50, 50)
        ));
        hero.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));
        
        // City name
        JLabel cityName = new JLabel("🌆 " + cityData.getCity());
        cityName.setFont(new Font("Segoe UI", Font.BOLD, 48));
        cityName.setForeground(Color.WHITE);
        cityName.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Location
        JLabel location = new JLabel("📍 " + cityData.getState() + ", " + cityData.getCountry());
        location.setFont(new Font("Segoe UI", Font.BOLD, 20));
        location.setForeground(new Color(229, 231, 235));
        location.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Best time badge
        JLabel bestTime = new JLabel("✨ Best Time to Visit: " + cityData.getBestTimeToVisit());
        bestTime.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bestTime.setForeground(new Color(31, 41, 55));
        bestTime.setBackground(new Color(251, 191, 36));
        bestTime.setOpaque(true);
        bestTime.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(25, new Color(251, 191, 36)),
            BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));
        bestTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Description
        JTextArea description = new JTextArea(cityData.getDescription());
        description.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        description.setForeground(new Color(243, 244, 246));
        description.setBackground(new Color(31, 41, 55));
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setMaximumSize(new Dimension(1000, Integer.MAX_VALUE));
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        hero.add(cityName);
        hero.add(Box.createVerticalStrut(15));
        hero.add(location);
        hero.add(Box.createVerticalStrut(20));
        hero.add(bestTime);
        hero.add(Box.createVerticalStrut(25));
        hero.add(description);
        
        return hero;
    }
    
    private JPanel createGridSection() {
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        
        // Row 0: Weather and Languages
        gbc.gridx = 0; gbc.gridy = 0;
        gridPanel.add(createStandardCard("🌦️ Weather", cityData.getWeather(), new Color(59, 130, 246)), gbc);
        gbc.gridx = 1;
        gridPanel.add(createStandardCard("🗣️ Languages", String.join(", ", cityData.getLanguagesSpoken()), new Color(16, 185, 129)), gbc);
        
        // Row 1: Tourist Spots and Attractions
        gbc.gridx = 0; gbc.gridy = 1;
        gridPanel.add(createListCard("🏛️ Tourist Spots", cityData.getTouristSpots(), new Color(245, 158, 11)), gbc);
        gbc.gridx = 1;
        gridPanel.add(createCombinedListCard("🎢 Attractions & Parks", cityData.getAttractions(), cityData.getThemeParks(), new Color(139, 92, 246)), gbc);
        
        // Row 2: Food Section (full width)
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gridPanel.add(createFoodSection(), gbc);
        gbc.gridwidth = 1;
        
        // Row 3: Festivals and Nightlife
        gbc.gridx = 0; gbc.gridy = 3;
        gridPanel.add(createListCard("🎉 Festivals", cityData.getFestivals(), new Color(239, 68, 68)), gbc);
        gbc.gridx = 1;
        gridPanel.add(createListCard("🌙 Nightlife", cityData.getNightlife(), new Color(99, 102, 241)), gbc);
        
        // Row 4: Transportation and Hotels
        gbc.gridx = 0; gbc.gridy = 4;
        gridPanel.add(createTransportationCard(), gbc);
        gbc.gridx = 1;
        gridPanel.add(createListCard("🏨 Hotels", cityData.getHotels(), new Color(5, 150, 105)), gbc);
        
        // Row 5: Shopping and Tips
        gbc.gridx = 0; gbc.gridy = 5;
        gridPanel.add(createListCard("🛒 Shopping", cityData.getShoppingMarkets(), new Color(220, 38, 38)), gbc);
        gbc.gridx = 1;
        gridPanel.add(createTipsEmergencyCard(), gbc);
        
        return gridPanel;
    }
    
    private JPanel createStandardCard(String title, String content, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(16, accentColor, 4),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setPreferredSize(new Dimension(400, 200));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea contentArea = new JTextArea(content);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        contentArea.setForeground(TEXT_SECONDARY);
        contentArea.setBackground(CARD_BG);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(contentArea);
        
        return card;
    }
    
    private JPanel createListCard(String title, List<String> items, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(16, accentColor, 4),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setPreferredSize(new Dimension(400, 200));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(CARD_BG);
        
        if (items != null && !items.isEmpty()) {
            for (int i = 0; i < Math.min(items.size(), 6); i++) {
                JLabel item = new JLabel("• " + items.get(i));
                item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                item.setForeground(TEXT_SECONDARY);
                itemsPanel.add(item);
                itemsPanel.add(Box.createVerticalStrut(5));
            }
            if (items.size() > 6) {
                JLabel more = new JLabel("... and " + (items.size() - 6) + " more");
                more.setFont(new Font("Segoe UI", Font.ITALIC, 13));
                more.setForeground(TEXT_MUTED);
                itemsPanel.add(more);
            }
        } else {
            JLabel noItems = new JLabel("Information not available");
            noItems.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            noItems.setForeground(TEXT_MUTED);
            itemsPanel.add(noItems);
        }
        
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CARD_BG);
        scrollPane.getViewport().setBackground(CARD_BG);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(Box.createVerticalStrut(15), BorderLayout.CENTER);
        card.add(scrollPane, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createCombinedListCard(String title, List<String> list1, List<String> list2, Color accentColor) {
        java.util.List<String> combined = new java.util.ArrayList<>();
        if (list1 != null) combined.addAll(list1);
        if (list2 != null) combined.addAll(list2);
        return createListCard(title, combined, accentColor);
    }
    
    private JPanel createFoodSection() {
        JPanel foodPanel = new JPanel();
        foodPanel.setLayout(new BoxLayout(foodPanel, BoxLayout.Y_AXIS));
        foodPanel.setBackground(CARD_BG);
        foodPanel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(16, new Color(245, 158, 11), 3),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        foodPanel.setPreferredSize(new Dimension(820, 230));
        
        JLabel title = new JLabel("🍽️ Food & Dining Experience");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(245, 158, 11));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel foodContent = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        foodContent.setBackground(CARD_BG);
        foodContent.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        foodContent.add(createFoodSubSection("🍝 Famous Dishes", cityData.getFamousFood()));
        foodContent.add(createFoodSubSection("🍴 Restaurants", cityData.getRestaurants()));
        foodContent.add(createFoodSubSection("🌭 Street Food", cityData.getStreetFood()));
        
        foodPanel.add(title);
        foodPanel.add(Box.createVerticalStrut(15));
        foodPanel.add(foodContent);
        
        return foodPanel;
    }
    
    private JPanel createFoodSubSection(String title, List<String> items) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(CARD_BG);
        section.setPreferredSize(new Dimension(220, 150));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(220, 38, 38));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(CARD_BG);
        itemsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        if (items != null && !items.isEmpty()) {
            for (int i = 0; i < Math.min(items.size(), 4); i++) {
                JLabel item = new JLabel("• " + items.get(i));
                item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                item.setForeground(TEXT_SECONDARY);
                itemsPanel.add(item);
            }
            if (items.size() > 4) {
                JLabel more = new JLabel("... +" + (items.size() - 4) + " more");
                more.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                more.setForeground(TEXT_MUTED);
                itemsPanel.add(more);
            }
        } else {
            JLabel noItems = new JLabel("No info available");
            noItems.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            noItems.setForeground(TEXT_MUTED);
            itemsPanel.add(noItems);
        }
        
        section.add(titleLabel);
        section.add(Box.createVerticalStrut(8));
        section.add(itemsPanel);
        
        return section;
    }
    
    private JPanel createTransportationCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(16, new Color(14, 165, 233), 4),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setPreferredSize(new Dimension(400, 200));
        
        JLabel titleLabel = new JLabel("🚆 Transportation");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(14, 165, 233));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel localLabel = new JLabel("🚌 Local Transport:");
        localLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        localLabel.setForeground(TEXT_SECONDARY);
        localLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel localList = new JPanel();
        localList.setLayout(new BoxLayout(localList, BoxLayout.Y_AXIS));
        localList.setBackground(CARD_BG);
        localList.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        if (cityData.getTransportation() != null && cityData.getTransportation().getLocal() != null) {
            for (String transport : cityData.getTransportation().getLocal()) {
                JLabel item = new JLabel("• " + transport);
                item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                item.setForeground(TEXT_SECONDARY);
                localList.add(item);
            }
        }
        
        JLabel airportLabel = new JLabel("✈️ Airport:");
        airportLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        airportLabel.setForeground(TEXT_SECONDARY);
        airportLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel airportText = new JLabel(cityData.getTransportation() != null ? 
            cityData.getTransportation().getAirport() : "N/A");
        airportText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        airportText.setForeground(TEXT_SECONDARY);
        airportText.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(localLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(localList);
        card.add(Box.createVerticalStrut(10));
        card.add(airportLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(airportText);
        
        return card;
    }
    
    private JPanel createTipsEmergencyCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(16, new Color(236, 72, 153), 4),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setPreferredSize(new Dimension(400, 200));
        
        JLabel titleLabel = new JLabel("💡 Tips & Emergency");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(236, 72, 153));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel tipsLabel = new JLabel("✨ Local Tips:");
        tipsLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tipsLabel.setForeground(TEXT_SECONDARY);
        tipsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel tipsList = new JPanel();
        tipsList.setLayout(new BoxLayout(tipsList, BoxLayout.Y_AXIS));
        tipsList.setBackground(CARD_BG);
        tipsList.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        if (cityData.getLocalTips() != null && !cityData.getLocalTips().isEmpty()) {
            for (int i = 0; i < Math.min(cityData.getLocalTips().size(), 2); i++) {
                JLabel tip = new JLabel("• " + cityData.getLocalTips().get(i));
                tip.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tip.setForeground(TEXT_SECONDARY);
                tipsList.add(tip);
            }
        }
        
        JLabel emergencyLabel = new JLabel("🆘 Emergency Contacts:");
        emergencyLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        emergencyLabel.setForeground(new Color(220, 38, 38));
        emergencyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel emergencyList = new JPanel();
        emergencyList.setLayout(new BoxLayout(emergencyList, BoxLayout.Y_AXIS));
        emergencyList.setBackground(CARD_BG);
        emergencyList.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        if (cityData.getEmergencyContacts() != null) {
            JLabel police = new JLabel("👮 Police: " + cityData.getEmergencyContacts().getPolice());
            JLabel ambulance = new JLabel("🚑 Ambulance: " + cityData.getEmergencyContacts().getAmbulance());
            JLabel fire = new JLabel("🚒 Fire: " + cityData.getEmergencyContacts().getFire());
            
            police.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            ambulance.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            fire.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            
            police.setForeground(TEXT_SECONDARY);
            ambulance.setForeground(TEXT_SECONDARY);
            fire.setForeground(TEXT_SECONDARY);
            
            emergencyList.add(police);
            emergencyList.add(ambulance);
            emergencyList.add(fire);
        }
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(tipsLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(tipsList);
        card.add(Box.createVerticalStrut(10));
        card.add(emergencyLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(emergencyList);
        
        return card;
    }
    
    // Custom rounded border class
    private static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;
        private int thickness;
        
        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
            this.thickness = 0;
        }
        
        RoundedBorder(int radius, Color color, int thickness) {
            this.radius = radius;
            this.color = color;
            this.thickness = thickness;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (thickness > 0) {
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(thickness));
                g2d.drawLine(x, y, x, y + height);
            } else {
                g2d.setColor(color);
                g2d.fillRoundRect(x, y, width - 1, height - 1, radius, radius);
            }
            g2d.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }
    }
}