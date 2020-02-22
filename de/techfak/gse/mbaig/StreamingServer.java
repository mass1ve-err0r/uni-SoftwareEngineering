package de.techfak.gse.mbaig;

import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;


public class StreamingServer extends NanoHTTPD {

    private static final String TYPE_JSON = "application/json";
    private static final String ERROR_MSG = "An error occured";
    JSONParser parser;
    MPlayer player;

    /**
     * StreamingServer - The Server.
     * @param port - The port to use
     * @param portREST - The REST Server's port
     * @param path - Path to music files because we construct the player here
     * @throws IOException - Demanded by the standard struct
     */
    public StreamingServer(int port, int portREST, String path) throws IOException {
        super(portREST);
        start(SOCKET_READ_TIMEOUT, false);
        this.player = new MPlayer(path, 1, port);
        this.player.playMusic(0, 1);
        parser = new JSONParser();
    }

    @Override
    public Response serve(IHTTPSession session) {
        switch (session.getUri()) {
            case "/current-song":
                try {
                    return newFixedLengthResponse(Response.Status.OK, TYPE_JSON, parser
                        .toJSON(player.playlist.getSong(0)));
                } catch (SerializationException e) {
                    return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, ERROR_MSG);
                }
            case "/playlist":
                try {
                    return newFixedLengthResponse(Response.Status.OK, TYPE_JSON, parser
                        .toJSON(player.playlist));
                } catch (SerializationException e) {
                    return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, ERROR_MSG);
                }
            default:
                break;
        }
        return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, "GSE Radio");
    }

}

