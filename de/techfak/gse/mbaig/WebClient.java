package de.techfak.gse.mbaig;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class WebClient {

    private static final String PREFIX = "http://";
    JSONParser parser;

    protected WebClient() {
        parser = new JSONParser();
    }

    /**
     * getResponse - Get the Response from a GET Request.
     * @param url - The URL for the GET request
     * @return the response code
     */
    public int getResponse(String url) {
        HttpResponse<String> response = null;
        try {
            if (!url.contains(PREFIX)) {
                url = PREFIX + url;
            }
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            return -1;
        }
        return response.statusCode();
    }

    /**
     * retrieveSong - Send a GET-request to retrieve the Song as JSON-Obj and then parsed.
     * @param url - The url to ask
     * @return the Song, fully parsed
     */
    public Song retrieveSong(String url) {
        HttpResponse<String> response = null;
        try {
            if (!url.contains(PREFIX)) {
                url = PREFIX + url;
            }
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url + "/current-song"))
                .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Song song = parser.parseSongJSON(response.body());
            return song;
        } catch (IOException | InterruptedException | IllegalArgumentException | DeserializationException e) {
            return new Song();
        }
    }

    /**
     * retrievePlaylist - Send a GET-request to retrieve the Playlist as JSON-Obj and then parsed.
     * @param url - The url to ask
     * @return the Paylist, fully parsed
     */
    public Playlist retrievePlaylist(String url) {
        HttpResponse<String> response = null;
        try {
            if (!url.contains(PREFIX)) {
                url = PREFIX + url;
            }
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url + "/playlist"))
                .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Playlist playlist = parser.parsePlaylistJSON(response.body());
            return playlist;
        } catch (IOException | InterruptedException | IllegalArgumentException | DeserializationException e) {
            return new Playlist();
        }
    }
    //
}
