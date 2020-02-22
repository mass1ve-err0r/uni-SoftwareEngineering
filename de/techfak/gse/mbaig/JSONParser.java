package de.techfak.gse.mbaig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JSONParser {

    private static final String ERROR_SERIALIZE = "Serializing failed!";
    private static final String ERROR_DESERIALIZE = "Deserialization failed!";
    private ObjectMapper objectMapper;

    public JSONParser() {
        objectMapper = new ObjectMapper()
            .findAndRegisterModules();
    }

    /**
     * toJSON (Song) - Serialize a Song-Obj.
     * @param song - The Song Object
     * @return A JSON-compliant String
     * @throws SerializationException - A custom exception to indicate failure
     */
    public String toJSON(Song song) throws SerializationException {
        try {
            return  objectMapper.writeValueAsString(song);
        } catch (JsonProcessingException e) {
            throw new SerializationException(ERROR_SERIALIZE);
        }
    }

    /**
     * parseSongJSON - Create a Song-Obj from the JSON String.
     * @param json - The given JSON String
     * @return A Song Object
     * @throws DeserializationException - A custom exception to indicate failure
     */
    public Song parseSongJSON(String json) throws DeserializationException {
        try {
            return objectMapper.readValue(json, Song.class);
        } catch (JsonProcessingException e) {
            throw new DeserializationException(ERROR_DESERIALIZE);
        }
    }

    /**
     * toJSON (Playlist) - Serialize a Playlist-Obj.
     * @param playlist - The Playlist Object
     * @return A JSON-compliant String
     * @throws SerializationException - A custom exception to indicate failure
     */
    public String toJSON(Playlist playlist) throws SerializationException {
        try {
            return  objectMapper.writeValueAsString(playlist);
        } catch (JsonProcessingException e) {
            throw new SerializationException(ERROR_SERIALIZE);
        }
    }

    /**
     * parsePlaylistJSON - Create a Playlist-Obj from the JSON String.
     * @param json - The given JSON String
     * @return A Playlist Object
     * @throws DeserializationException - A custom exception to indicate failure
     */
    public Playlist parsePlaylistJSON(String json) throws DeserializationException {
        try {
            return objectMapper.readValue(json, Playlist.class);
        } catch (JsonProcessingException e) {
            throw new DeserializationException(ERROR_DESERIALIZE);
        }
    }

}
