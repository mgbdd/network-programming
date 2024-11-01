import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import API.Description.DescriptionResponse;
import API.Location.GeoResponse;
import API.Places.Feature;
import API.Places.PlaceResponse;
import API.Weather.WeatherResponse;
import API.Location.Location;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import static java.lang.Math.round;

public class LocationExplorer {
    int locationsNum; //количество найденных локаций
    private static String location; // введенная пользователем локация
    private Scanner scanner;
    public LocationExplorer(String loc, Scanner scanner)
    {
        location = loc;
        this.scanner = scanner;
    }



    public void startExploring() {
        CompletableFuture<List<Location>> futureLocations = searchForLocations();
        CountDownLatch latch = new CountDownLatch(1);
        futureLocations.thenAccept(locations -> {
                    locationsNum = locations.size();
                    if(locationsNum == 0) {
                        System.out.println("No locations were found");
                        latch.countDown();
                        return;
                    }
                    else if (locationsNum > 1) System.out.println(Integer.toString(locationsNum)+ " locations were found successfully");
                    else System.out.println(Integer.toString(locationsNum)+ " location was found successfully");

                    System.out.println("Choose one location you would like to know more about (type number from 1 to " + Integer.toString(locationsNum) +"):");
                    int chosenLocation = scanner.nextInt();
                    System.out.println("You chose location № " + Integer.toString(chosenLocation) + "\n");



                    if(chosenLocation > 0 && chosenLocation <= locationsNum)
                    {
                        Location location = locations.get(chosenLocation - 1);

                        CompletableFuture<Void> weatherFuture = searchForWeather(location);
                        CompletableFuture<Void> attractionsFuture = searchForPlaces(location);


                        CompletableFuture<Void> allOf = CompletableFuture.allOf(weatherFuture, attractionsFuture);
                        allOf.thenRun(() -> {
                                    System.out.println("Finished fetching weather and attractions info");
                                    latch.countDown();
                                })
                                .exceptionally(ex -> {
                                    System.err.println("Error in fetching data: " + ex.getMessage());
                                    return null;
                                });
                    }
                    else {
                        System.out.println("Invalid location number chosen.");
                        latch.countDown();
                    }
                })
                .exceptionally(ex -> {
                    System.err.println("Error occurred: " + ex.getMessage());
                    latch.countDown();
                    return null;
                });
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public CompletableFuture<List<Location>> searchForLocations() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Searching for location " + location + "... ");
            String locationUrl =  "https://graphhopper.com/api/1/geocode?q=" + location + "&locale=en&key=8bc7bbfe-84da-4400-8185-3d40e1be6421";

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(locationUrl)
                    .get()
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {

                    throw new IOException("Unexpected code " + response);
                }

                String jsonResponse = response.body().string();
                File responseFile = new File("responses/locations/" + location + "Response.json");
                try (FileWriter fileWriter = new FileWriter(responseFile)) {
                    fileWriter.write(jsonResponse);
                }

                ObjectMapper objectMapper = new ObjectMapper();
                GeoResponse geoResponse = objectMapper.readValue(responseFile, GeoResponse.class);
                List<Location> locations = geoResponse.getHits();
                for(Location loc : locations)
                {
                    System.out.println("location name: " + loc.getLocationName());
                    if(loc.getCity() != null) System.out.println("country: " + loc.getCountry() + ", city: " + loc.getCity());
                    else System.out.println("country: " + loc.getCountry());
                    System.out.println("lat: " + loc.getPoint().getLatitude() + ", lng: " + loc.getPoint().getLongitude());
                    System.out.println("");
                }
                return locations; // замените на реальный список локаций
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Void> searchForWeather(Location location) {
        return CompletableFuture.runAsync(() -> {                                        //lat                                         //lon
            String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + location.getPoint().getLatitude() + "&lon=" + location.getPoint().getLongitude() + "&appid=e1f94bb924dbcc405fdacb5275f32e46";

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(weatherUrl)
                    .get()
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {

                    throw new IOException("Unexpected code " + response);
                }

                String jsonResponse = response.body().string();
                File responseFile = new File("responses/weather/" + location.getLocationName() + "Response.json");
                try (FileWriter fileWriter = new FileWriter(responseFile)) {
                    fileWriter.write(jsonResponse);
                }

                ObjectMapper objectMapper = new ObjectMapper();
                WeatherResponse weatherResponse = objectMapper.readValue(jsonResponse, WeatherResponse.class);

                String description = weatherResponse.getWeather()[0].getDescription();
                int temperature = (int)round(weatherResponse.getMain().getTemperature() - 273.15);
                int feelsLike = (int)round(weatherResponse.getMain().getFeelsLike() - 273.15);
                int humidity = weatherResponse.getMain().getHumidity();
                double windSpeed = weatherResponse.getWind().getSpeed();

                System.out.println("Weather description: " + description);
                System.out.println("Temperature: " + temperature + "°C");
                System.out.println("Feels Like: " + feelsLike + "°C");
                System.out.println("Humidity: " + humidity + "%");
                System.out.println("Wind Speed: " + windSpeed + "m/s\n");



            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }

    public CompletableFuture<Void> searchForPlaces(Location location) {
        return CompletableFuture.runAsync(() -> {
            String placesUrl = "https://api.opentripmap.com/0.1/ru/places/radius?lat=" + location.getPoint().getLatitude() +"&lon=" + location.getPoint().getLongitude() + "&radius=10000&lang=ru&apikey=5ae2e3f221c38a28845f05b66a63b3acfb556adbb46e3c1df58ff9d5&limit=10";
            OkHttpClient placeClient = new OkHttpClient();
            Request request1 = new Request.Builder()
                    .url(placesUrl)
                    .get()
                    .build();
            try(Response response1 = placeClient.newCall(request1).execute())
            {
                if (!response1.isSuccessful()) {

                    throw new IOException("Unexpected code " + response1);
                }

                String jsonResponse1 = response1.body().string();
                File responseFile1 = new File("responses/places/" + location.getLocationName() + "Response.json");
                try (FileWriter fileWriter = new FileWriter(responseFile1)) {
                    fileWriter.write(jsonResponse1);
                }

                ObjectMapper objectMapper1 = new ObjectMapper();
                PlaceResponse placeResponse = objectMapper1.readValue(jsonResponse1, PlaceResponse.class);
                Feature[] features = placeResponse.getFeatures();
                for(int i = 0; i < features.length; i++)
                {
                    System.out.println(features[i].getProperties().getName());

                    String xid = features[i].getProperties().getXid();
                    String descriptionUrl = "https://api.opentripmap.com/0.1/ru/places/xid/" + xid + "?apikey=5ae2e3f221c38a28845f05b66a63b3acfb556adbb46e3c1df58ff9d5";
                    OkHttpClient descriptionClient = new OkHttpClient();
                    Request request2 = new Request.Builder()
                            .url(descriptionUrl)
                            .get()
                            .build();
                    try(Response response2 = descriptionClient.newCall(request2).execute())
                    {
                        if (!response2.isSuccessful()) {

                            throw new IOException("Unexpected code " + response1);
                        }
                        String jsonResponse2 = response2.body().string();
                        File responseFile2 = new File("responses/descriptions/" + location.getLocationName() + "Response.json");
                        try (FileWriter fileWriter2 = new FileWriter(responseFile2)) {
                            fileWriter2.write(jsonResponse2);
                        }
                        ObjectMapper objectMapper2 = new ObjectMapper();
                        DescriptionResponse descriptionResponse = objectMapper2.readValue(jsonResponse2, DescriptionResponse.class);
                        if (descriptionResponse.getWikiExtracts() != null) {
                            String wikiText = descriptionResponse.getWikiExtracts().getText();
                            System.out.println(wikiText + "\n");
                        } else {
                            System.out.println("Wikimedia extracts are not available.");
                        }

                    }

                }

            }catch(IOException e){
                throw new RuntimeException(e);
            }



        });
    }
}
