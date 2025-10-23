package backend;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Groq API service for fetching city information
 * Equivalent to the API calls in the React Index.tsx
 */
public class GroqApiService {
    
    private static String GROQ_API_KEY;
    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    
    static {
        loadConfiguration();
    }
    
    private static void loadConfiguration() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("config/api.properties")) {
            props.load(input);
            GROQ_API_KEY = props.getProperty("groq.api.key");
            if (GROQ_API_KEY == null || GROQ_API_KEY.trim().isEmpty()) {
                throw new RuntimeException("Groq API key not found in config/api.properties");
            }
        } catch (IOException e) {
            // Fallback to hardcoded key for testing
            GROQ_API_KEY = "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";
            System.err.println("Warning: Could not load config file, using fallback API key");
        }
    }
    
    public static CompletableFuture<CityData> getCityInformation(String cityName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return fetchCityInformation(cityName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch city information: " + e.getMessage(), e);
            }
        });
    }
    
    private static CityData fetchCityInformation(String cityName) throws Exception {
        String prompt = createPrompt(cityName);
        String requestBody = createRequestBody(prompt);
        
        HttpURLConnection connection = createConnection();
        sendRequest(connection, requestBody);
        
        String response = getResponse(connection);
        
        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("API request failed with code: " + connection.getResponseCode());
        }
        
        return parseApiResponse(response);
    }
    
    // private static String createPrompt(String cityName) {
    //     return String.format(
    //         "Provide comprehensive travel information about %s in the following JSON format:\n" +
    //         "{\n" +
    //         "  \"city\": \"%s\",\n" +
    //         "  \"state\": \"state name\",\n" +
    //         "  \"country\": \"country name\",\n" +
    //         "  \"description\": \"A detailed description of the city\",\n" +
    //         "  \"bestTimeToVisit\": \"Best months to visit\",\n" +
    //         "  \"touristSpots\": [\"spot1\", \"spot2\", \"spot3\"],\n" +
    //         "  \"famousFood\": [\"food1\", \"food2\", \"food3\"],\n" +
    //         "  \"restaurants\": [\"restaurant1\", \"restaurant2\", \"restaurant3\"],\n" +
    //         "  \"streetFood\": [\"food1\", \"food2\",],\n" +
    //         "  \"attractions\": [\"attraction1\", \"attraction2\"],\n" +
    //         "  \"themeParks\": [\"park1\", \"park2\"],\n" +
    //         "  \"festivals\": [\"festival1\", \"festival2\", \"festival3\"],\n" +
    //         "  \"transportation\": {\n" +
    //         "    \"local\": [\"transport1\", \"transport2\"],\n" +
    //         "    \"airport\": \"airport name\"\n" +
    //         "  },\n" +
    //         "  \"hotels\": [\"hotel1\", \"hotel2\", \"hotel3\"],\n" +
    //         "  \"shoppingMarkets\": [\"market1\", \"market2\", \"market3\"],\n" +
    //         "  \"nightlife\": [\"place1\", \"place2\"],\n" +
    //         "  \"weather\": \"Weather description\",\n" +
    //         "  \"localTips\": [\"tip1\", \"tip2\"],\n" +
    //         "  \"emergencyContacts\": {\n" +
    //         "    \"police\": \"number\",\n" +
    //         "    \"ambulance\": \"number\",\n" +
    //         "    \"fire\": \"number\"\n" +
    //         "  },\n" +
    //         "  \"languagesSpoken\": [\"language1\", \"language2\"]\n" +
    //         "}\n\n" +
    //         "Return ONLY the JSON object, no additional text.",
    //         cityName, cityName
    //     );
    // }
    private static String createPrompt(String cityName) {
    return String.format(
        "Provide comprehensive travel information about %s in the following JSON format:\n" +
        "{\n" +
        "  \"city\": \"%s\",\n" +
        "  \"state\": \"state name\",\n" +
        "  \"country\": \"country name\",\n" +
        "  \"description\": \"A detailed description of the city\",\n" +
        "  \"population\": \"Approximate population count\",\n" +
        "  \"area\": \"Area in square kilometers\",\n" +
        "  \"bestTimeToVisit\": \"Best months to visit\",\n" +
        "  \"touristSpots\": [\"spot1\", \"spot2\", \"spot3\", \"spot4\", \"spot5\", \"spot6\"],\n" +
        "  \"historicalSites\": [\"site1\", \"site2\", \"site3\", \"site4\", \"site5\", \"site6\"],\n" +
        "  \"famousFood\": [\"food1\", \"food2\", \"food3\", \"food4\", \"food5\", \"food6\"],\n" +
        "  \"restaurants\": [\"restaurant1\", \"restaurant2\", \"restaurant3\", \"restaurant4\", \"restaurant5\", \"restaurant6\"],\n" +
        "  \"streetFood\": [\"food1\", \"food2\", \"food3\", \"food4\", \"food5\", \"food6\"],\n" +
        "  \"attractions\": [\"attraction1\", \"attraction2\", \"attraction3\", \"attraction4\", \"attraction5\", \"attraction6\"],\n" +
        "  \"themeParks\": [\"park1\", \"park2\", \"park3\", \"park4\", \"park5\", \"park6\"],\n" +
        "  \"festivals\": [\"festival1\", \"festival2\", \"festival3\", \"festival4\", \"festival5\", \"festival6\"],\n" +
        "  \"famousPersonalities\": [\"person1\", \"person2\", \"person3\", \"person4\", \"person5\", \"person6\"],\n" +
        "  \"transportation\": {\n" +
        "    \"local\": [\"transport1\", \"transport2\", \"transport3\", \"transport4\", \"transport5\", \"transport6\"],\n" +
        "    \"airport\": \"airport name\"\n" +
        "  },\n" +
        "  \"hotels\": [\"hotel1\", \"hotel2\", \"hotel3\", \"hotel4\", \"hotel5\", \"hotel6\"],\n" +
        "  \"shoppingMarkets\": [\"market1\", \"market2\", \"market3\", \"market4\", \"market5\", \"market6\"],\n" +
        "  \"nightlife\": [\"place1\", \"place2\", \"place3\", \"place4\", \"place5\", \"place6\"],\n" +
        "  \"weather\": \"detailed summary of weather conditions (temperature range, humidity, general climate).\",\n" +
        "  \"currency\": \"Local currency name\",\n" +
        "  \"localTips\": [\"tip1\", \"tip2\", \"tip3\", \"tip4\", \"tip5\", \"tip6\"],\n" +
        "  \"emergencyContacts\": {\n" +
        "    \"police\": \"number\",\n" +
        "    \"ambulance\": \"number\",\n" +
        "    \"fire\": \"number\"\n" +
        "  },\n" +
        "  \"languagesSpoken\": [\"language1\", \"language2\", \"language3\", \"language4\", \"language5\", \"language6\"]\n" +
        "}\n\n" +
        "Return ONLY the JSON object, no additional text.",
        cityName, cityName
    );
}

    private static String createRequestBody(String prompt) {
        return "{\n" +
            "  \"model\": \"llama-3.3-70b-versatile\",\n" +
            "  \"messages\": [\n" +
            "    {\n" +
            "      \"role\": \"system\",\n" +
            "      \"content\": \"You are a travel information assistant. Always respond with valid JSON only.\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"role\": \"user\",\n" +
            "      \"content\": \"" + escapeJsonString(prompt) + "\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"temperature\": 0.7,\n" +
            "  \"max_tokens\": 2000\n" +
            "}";
    }
    
    private static String escapeJsonString(String str) {
        return str.replace("\\", "\\\\")
                 .replace("\"", "\\\"")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }
    
    private static HttpURLConnection createConnection() throws Exception {
        URL url = new URL(GROQ_API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + GROQ_API_KEY);
        connection.setDoOutput(true);
        
        return connection;
    }
    
    private static void sendRequest(HttpURLConnection connection, String requestBody) throws Exception {
        try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
            writer.write(requestBody);
            writer.flush();
        }
    }
    
    private static String getResponse(HttpURLConnection connection) throws Exception {
        StringBuilder response = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    connection.getResponseCode() >= 400 ? 
                        connection.getErrorStream() : connection.getInputStream()
                )
            )) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        
        return response.toString();
    }
    
    private static CityData parseApiResponse(String jsonResponse) throws Exception {
        // Parse the response JSON
        Map<String, Object> response = JsonParser.parseJson(jsonResponse);
        
        // Extract the content from choices[0].message.content
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        
        if (choices == null || choices.isEmpty()) {
            throw new RuntimeException("No choices in API response");
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        
        if (message == null) {
            throw new RuntimeException("No message in API response");
        }
        
        String content = (String) message.get("content");
        
        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("No content in API response");
        }
        
        // Extract JSON from content (in case there's extra text)
        String jsonContent = extractJsonFromContent(content);
        
        // Parse the city data JSON
        Map<String, Object> cityDataMap = JsonParser.parseJson(jsonContent);
        
        return mapToCityData(cityDataMap);
    }
    
    private static String extractJsonFromContent(String content) {
        // Handle escaped newlines in the content
        content = content.replace("\\n", "\n").replace("\\r", "\r").replace("\\t", "\t");
        
        // Find the JSON object in the content
        int start = content.indexOf('{');
        int end = content.lastIndexOf('}');
        
        if (start == -1 || end == -1 || start >= end) {
            throw new RuntimeException("No valid JSON found in API response");
        }
        
        return content.substring(start, end + 1);
    }
    
    @SuppressWarnings("unchecked")
    private static CityData mapToCityData(Map<String, Object> dataMap) {
        CityData cityData = new CityData();
        
        cityData.setCity((String) dataMap.get("city"));
        cityData.setState((String) dataMap.get("state"));
        cityData.setCountry((String) dataMap.get("country"));
        cityData.setDescription((String) dataMap.get("description"));
        cityData.setBestTimeToVisit((String) dataMap.get("bestTimeToVisit"));
        cityData.setWeather((String) dataMap.get("weather"));
        
        // Handle arrays - convert Objects to Strings
        cityData.setTouristSpots(convertToStringList((List<Object>) dataMap.get("touristSpots")));
        cityData.setFamousFood(convertToStringList((List<Object>) dataMap.get("famousFood")));
        cityData.setRestaurants(convertToStringList((List<Object>) dataMap.get("restaurants")));
        cityData.setStreetFood(convertToStringList((List<Object>) dataMap.get("streetFood")));
        cityData.setAttractions(convertToStringList((List<Object>) dataMap.get("attractions")));
        cityData.setThemeParks(convertToStringList((List<Object>) dataMap.get("themeParks")));
        cityData.setFestivals(convertToStringList((List<Object>) dataMap.get("festivals")));
        cityData.setHotels(convertToStringList((List<Object>) dataMap.get("hotels")));
        cityData.setShoppingMarkets(convertToStringList((List<Object>) dataMap.get("shoppingMarkets")));
        cityData.setNightlife(convertToStringList((List<Object>) dataMap.get("nightlife")));
        cityData.setLocalTips(convertToStringList((List<Object>) dataMap.get("localTips")));
        cityData.setLanguagesSpoken(convertToStringList((List<Object>) dataMap.get("languagesSpoken")));
        
        // Handle transportation object
        Map<String, Object> transportationMap = (Map<String, Object>) dataMap.get("transportation");
        if (transportationMap != null) {
            CityData.Transportation transportation = new CityData.Transportation();
            transportation.setLocal(convertToStringList((List<Object>) transportationMap.get("local")));
            transportation.setAirport((String) transportationMap.get("airport"));
            cityData.setTransportation(transportation);
        }
        
        // Handle emergency contacts object
        Map<String, Object> emergencyMap = (Map<String, Object>) dataMap.get("emergencyContacts");
        if (emergencyMap != null) {
            CityData.EmergencyContacts emergency = new CityData.EmergencyContacts();
            emergency.setPolice((String) emergencyMap.get("police"));
            emergency.setAmbulance((String) emergencyMap.get("ambulance"));
            emergency.setFire((String) emergencyMap.get("fire"));
            cityData.setEmergencyContacts(emergency);
        }
        
        return cityData;
    }
    
    private static List<String> convertToStringList(List<Object> objectList) {
        if (objectList == null) {
            return null;
        }
        List<String> stringList = new ArrayList<>();
        for (Object obj : objectList) {
            if (obj != null) {
                stringList.add(obj.toString());
            }
        }
        return stringList;
    }
}