package backend;

import java.util.*;

/**
 * Simple JSON parser for parsing Groq API responses
 * No external libraries used
 */
public class JsonParser {
    
    public static Map<String, Object> parseJson(String json) {
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new IllegalArgumentException("Invalid JSON format");
        }
        
        Map<String, Object> result = new HashMap<>();
        json = json.substring(1, json.length() - 1); // Remove outer braces
        
        parseObject(json, result);
        return result;
    }
    
    private static void parseObject(String json, Map<String, Object> result) {
        List<String> pairs = splitJsonPairs(json);
        
        for (String pair : pairs) {
            int colonIndex = findColonIndex(pair);
            if (colonIndex == -1) continue;
            
            String key = pair.substring(0, colonIndex).trim();
            String value = pair.substring(colonIndex + 1).trim();
            
            // Remove quotes from key
            if (key.startsWith("\"") && key.endsWith("\"")) {
                key = key.substring(1, key.length() - 1);
            }
            
            result.put(key, parseValue(value));
        }
    }
    
    private static Object parseValue(String value) {
        value = value.trim();
        
        if (value.equals("null")) {
            return null;
        }
        
        if (value.equals("true")) {
            return true;
        }
        
        if (value.equals("false")) {
            return false;
        }
        
        if (value.startsWith("\"") && value.endsWith("\"")) {
            // String value - handle escape sequences
            String stringValue = value.substring(1, value.length() - 1);
            return stringValue.replace("\\\\", "\\").replace("\\\"", "\"").replace("\\n", "\n").replace("\\r", "\r").replace("\\t", "\t");
        }
        
        if (value.startsWith("[") && value.endsWith("]")) {
            // Array value
            return parseArray(value);
        }
        
        if (value.startsWith("{") && value.endsWith("}")) {
            // Object value
            Map<String, Object> nestedObj = new HashMap<>();
            parseObject(value.substring(1, value.length() - 1), nestedObj);
            return nestedObj;
        }
        
        // Try to parse as number
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value);
            } else {
                return Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            // Return as string if not a number
            return value;
        }
    }
    
    private static List<Object> parseArray(String arrayStr) {
        List<Object> result = new ArrayList<>();
        arrayStr = arrayStr.substring(1, arrayStr.length() - 1); // Remove brackets
        
        if (arrayStr.trim().isEmpty()) {
            return result;
        }
        
        List<String> elements = splitJsonElements(arrayStr);
        for (String element : elements) {
            Object value = parseValue(element.trim());
            result.add(value);
        }
        
        return result;
    }
    
    private static List<String> splitJsonPairs(String json) {
        List<String> pairs = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int braceLevel = 0;
        int bracketLevel = 0;
        boolean inString = false;
        boolean escaped = false;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (escaped) {
                escaped = false;
                current.append(c);
                continue;
            }
            
            if (c == '\\') {
                escaped = true;
                current.append(c);
                continue;
            }
            
            if (c == '"') {
                inString = !inString;
                current.append(c);
                continue;
            }
            
            if (!inString) {
                if (c == '{') {
                    braceLevel++;
                } else if (c == '}') {
                    braceLevel--;
                } else if (c == '[') {
                    bracketLevel++;
                } else if (c == ']') {
                    bracketLevel--;
                } else if (c == ',' && braceLevel == 0 && bracketLevel == 0) {
                    pairs.add(current.toString());
                    current = new StringBuilder();
                    continue;
                }
            }
            
            current.append(c);
        }
        
        if (current.length() > 0) {
            pairs.add(current.toString());
        }
        
        return pairs;
    }
    
    private static List<String> splitJsonElements(String json) {
        List<String> elements = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int braceLevel = 0;
        int bracketLevel = 0;
        boolean inString = false;
        boolean escaped = false;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (escaped) {
                escaped = false;
                current.append(c);
                continue;
            }
            
            if (c == '\\') {
                escaped = true;
                current.append(c);
                continue;
            }
            
            if (c == '"') {
                inString = !inString;
                current.append(c);
                continue;
            }
            
            if (!inString) {
                if (c == '{') {
                    braceLevel++;
                } else if (c == '}') {
                    braceLevel--;
                } else if (c == '[') {
                    bracketLevel++;
                } else if (c == ']') {
                    bracketLevel--;
                } else if (c == ',' && braceLevel == 0 && bracketLevel == 0) {
                    elements.add(current.toString());
                    current = new StringBuilder();
                    continue;
                }
            }
            
            current.append(c);
        }
        
        if (current.length() > 0) {
            elements.add(current.toString());
        }
        
        return elements;
    }
    
    private static int findColonIndex(String pair) {
        boolean inString = false;
        boolean escaped = false;
        
        for (int i = 0; i < pair.length(); i++) {
            char c = pair.charAt(i);
            
            if (escaped) {
                escaped = false;
                continue;
            }
            
            if (c == '\\') {
                escaped = true;
                continue;
            }
            
            if (c == '"') {
                inString = !inString;
                continue;
            }
            
            if (!inString && c == ':') {
                return i;
            }
        }
        
        return -1;
    }
}