package net.milkbowl.vault.uuid;

/**
 * An exception for when a mapping does not exist. Must be added as a cause to {@link PlayerUUIDMapCallback#onCallback(Throwable, String, java.util.UUID)} if a mapping does not exist.
 * Note: This exception should only be used if a mapping really does not exist, do not use it when it could be the case that a mapping does exist and another error prevented its retrieval.
 *
 * @see PlayerUUIDMapCallback
 */
public final class MappingDoesNotExistException extends Exception {
    /**
     * An exception for when a mapping does not exist. Must be passed to {@link PlayerUUIDMapCallback#onCallback(Throwable, String, java.util.UUID)} if a mapping does not exist.
     *
     * @param playerName The originally supplied playerName or null
     * @param playerUUID The originally supplied playerUUID as a string or null
     */
    public MappingDoesNotExistException(String playerName, String playerUUID) {
        super(getErrorMessage(playerName, playerUUID, null));
    }

    /**
     * An exception for when a mapping does not exist. Must be passed to {@link PlayerUUIDMapCallback#onCallback(Throwable, String, java.util.UUID)} if a mapping does not exist.
     *
     * @param playerName The originally supplied playerName or null
     * @param playerUUID The originally supplied playerUUID as a string or null
     * @param cause A cause if it is relevant
     */
    public MappingDoesNotExistException(String playerName, String playerUUID, Throwable cause) {
        super(getErrorMessage(playerName, playerUUID, null), cause);
    }

    /**
     * An exception for when a mapping does not exist. Must be passed to {@link PlayerUUIDMapCallback#onCallback(Throwable, String, java.util.UUID)} if a mapping does not exist.
     *
     * @param playerName The originally supplied playerName or null
     * @param playerUUID The originally supplied playerUUID as a string or null
     * @param additionalInformation Any additional information that should be included
     */
    public MappingDoesNotExistException(String playerName, String playerUUID, String additionalInformation) {
        super(getErrorMessage(playerName, playerUUID, additionalInformation));
    }

    /**
     * An exception for when a mapping does not exist. Must be passed to {@link PlayerUUIDMapCallback#onCallback(Throwable, String, java.util.UUID)} if a mapping does not exist.
     *
     * @param playerName The originally supplied playerName or null
     * @param playerUUID The originally supplied playerUUID as a string or null
     * @param additionalInformation Any additional information that should be included
     * @param cause A cause if it is relevant
     */
    public MappingDoesNotExistException(String playerName, String playerUUID, String additionalInformation, Throwable cause) {
        super(getErrorMessage(playerName, playerUUID, additionalInformation), cause);
    }

    private static String getErrorMessage(String playerName, String playerUUID, String additionalInformation) {
        String ret = null;

        if((playerName == null || playerName.equals("")) && (playerUUID == null || playerUUID.equals(""))) {
            ret = String.format("(\"%s\" <-> \"%s\") cannot be mapped!", playerName, playerUUID);
        }

        if(ret == null && (playerName == null || playerName.equals(""))) {
            ret = playerName + " does not map to a player UUID!";
        }

        if(ret == null && (playerUUID == null || playerUUID.equals(""))) {
            ret = playerUUID + " does not map to a player name!";
        }

        if(ret == null) {
            ret = String.format("(%s <-> %s) mapping does not exist!", playerName, playerUUID);
        }

        if(additionalInformation != null && !additionalInformation.equals("")) {
            ret += " " + additionalInformation;
        }

        return ret;
    }
}
