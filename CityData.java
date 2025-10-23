package backend;

import java.util.List;
import java.util.Map;

/**
 * City data model representing the travel information
 * Equivalent to the CityData interface in React
 */
public class CityData {
    private String city;
    private String state;
    private String country;
    private String description;
    private String bestTimeToVisit;
    private List<String> touristSpots;
    private List<String> famousFood;
    private List<String> restaurants;
    private List<String> streetFood;
    private List<String> attractions;
    private List<String> themeParks;
    private List<String> festivals;
    private Transportation transportation;
    private List<String> hotels;
    private List<String> shoppingMarkets;
    private List<String> nightlife;
    private String weather;
    private List<String> localTips;
    private EmergencyContacts emergencyContacts;
    private List<String> languagesSpoken;
    
    // Constructors
    public CityData() {}
    
    // Getters and Setters
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getBestTimeToVisit() { return bestTimeToVisit; }
    public void setBestTimeToVisit(String bestTimeToVisit) { this.bestTimeToVisit = bestTimeToVisit; }
    
    public List<String> getTouristSpots() { return touristSpots; }
    public void setTouristSpots(List<String> touristSpots) { this.touristSpots = touristSpots; }
    
    public List<String> getFamousFood() { return famousFood; }
    public void setFamousFood(List<String> famousFood) { this.famousFood = famousFood; }
    
    public List<String> getRestaurants() { return restaurants; }
    public void setRestaurants(List<String> restaurants) { this.restaurants = restaurants; }
    
    public List<String> getStreetFood() { return streetFood; }
    public void setStreetFood(List<String> streetFood) { this.streetFood = streetFood; }
    
    public List<String> getAttractions() { return attractions; }
    public void setAttractions(List<String> attractions) { this.attractions = attractions; }
    
    public List<String> getThemeParks() { return themeParks; }
    public void setThemeParks(List<String> themeParks) { this.themeParks = themeParks; }
    
    public List<String> getFestivals() { return festivals; }
    public void setFestivals(List<String> festivals) { this.festivals = festivals; }
    
    public Transportation getTransportation() { return transportation; }
    public void setTransportation(Transportation transportation) { this.transportation = transportation; }
    
    public List<String> getHotels() { return hotels; }
    public void setHotels(List<String> hotels) { this.hotels = hotels; }
    
    public List<String> getShoppingMarkets() { return shoppingMarkets; }
    public void setShoppingMarkets(List<String> shoppingMarkets) { this.shoppingMarkets = shoppingMarkets; }
    
    public List<String> getNightlife() { return nightlife; }
    public void setNightlife(List<String> nightlife) { this.nightlife = nightlife; }
    
    public String getWeather() { return weather; }
    public void setWeather(String weather) { this.weather = weather; }
    
    public List<String> getLocalTips() { return localTips; }
    public void setLocalTips(List<String> localTips) { this.localTips = localTips; }
    
    public EmergencyContacts getEmergencyContacts() { return emergencyContacts; }
    public void setEmergencyContacts(EmergencyContacts emergencyContacts) { this.emergencyContacts = emergencyContacts; }
    
    public List<String> getLanguagesSpoken() { return languagesSpoken; }
    public void setLanguagesSpoken(List<String> languagesSpoken) { this.languagesSpoken = languagesSpoken; }
    
    public static class Transportation {
        private List<String> local;
        private String airport;
        
        public Transportation() {}
        
        public List<String> getLocal() { return local; }
        public void setLocal(List<String> local) { this.local = local; }
        
        public String getAirport() { return airport; }
        public void setAirport(String airport) { this.airport = airport; }
    }
    
    public static class EmergencyContacts {
        private String police;
        private String ambulance;
        private String fire;
        
        public EmergencyContacts() {}
        
        public String getPolice() { return police; }
        public void setPolice(String police) { this.police = police; }
        
        public String getAmbulance() { return ambulance; }
        public void setAmbulance(String ambulance) { this.ambulance = ambulance; }
        
        public String getFire() { return fire; }
        public void setFire(String fire) { this.fire = fire; }
    }
}