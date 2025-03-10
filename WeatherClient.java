package com.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WeatherClient {
    private static final String API_KEY = "c6434416b72fdc55829bfebc754413b0"; // Replace with your OpenWeatherMap API key
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    public static void main(String[] args) {
        // Create a Scanner object to read input from the console
        Scanner scanner = new Scanner(System.in);

        // Prompt the user for the city name
        System.out.print("Enter the city name for weather check: ");
        String city = scanner.nextLine();

        try {
            String response = getWeatherData(city);
            parseAndDisplayWeather(response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the scanner
            scanner.close();
        }
    }

    private static String getWeatherData(String city) throws IOException {
        // Encode the city name to handle spaces and special characters
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String url = String.format(API_URL, encodedCity, API_KEY);
        
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    private static void parseAndDisplayWeather(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        String cityName = jsonObject.get("name").getAsString();
        double temperature = jsonObject.get("main").getAsJsonObject().get("temp").getAsDouble();
        String weatherDescription = jsonObject.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString();

        System.out.printf("Weather in %s:\n", cityName);
        System.out.printf("Temperature: %.2f °C\n", temperature);
        System.out.printf("Description: %s\n", weatherDescription);
    }
}