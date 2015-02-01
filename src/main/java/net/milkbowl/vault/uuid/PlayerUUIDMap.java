package net.milkbowl.vault.uuid;

import java.util.UUID;

/**
 * An API for mapping (player names -> player UUIDs) and vice versa. Can also be used to get the correct capitalization of a player's name.
 */
public abstract class PlayerUUIDMap {
    /**
     * Returns the UUID represented by the {@code uuidString} or null if it is not a valid UUID.
     *
     * @param uuidString A String representing a UUID, does not have to include the dashes.
     * @return A UUID object or null.
     */
    public static UUID getUuidFromString(String uuidString) {
        if(!uuidString.contains("-")) {
            //TODO: Add the dashes, UUID.fromString doesn't work without them. I know I've done this before, but I can't seem to find the code :\
        }
        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Returns a player's name with correct capitalization if it is cached locally.
     * Must not be a long-running operation.
     *
     * @param playerName The player's name, does not have to be capitalized correctly
     * @return The inputted player's name with correct capitalization or null if it could not be found
     */
    public abstract String getCachedCorrectedPlayerName(String playerName);

    /**
     * Checks if the correctly capitalized version of a player's name is cached locally.
     *
     * @param playerName The String representation of a player's name, does not have to be capitalized correctly
     * @return True if the player's name is cached
     */
    public abstract boolean isCorrectedPlayerNameCached(String playerName);

    /**
     * Checks if a player's name is cached locally.
     *
     * @param playerUuid The String representation of a player's {@link UUID}, does not have to contain dashes
     * @return True if the player's name is cached
     */
    public boolean isPlayerNameCached(String playerUuid) {
        UUID playerUuidObj = getUuidFromString(playerUuid);
        if(playerUuidObj == null)
            return false;
        return isPlayerNameCached(playerUuidObj);
    }

    /**
     * Checks if a player's name is cached locally.
     *
     * @param playerUuid The player's UUID
     * @return True if the player's name is cached
     */
    public abstract boolean isPlayerNameCached(UUID playerUuid);

    /**
     * Get the player's name if it is cached locally.
     * Must not be a long-running operation.
     *
     * @param playerUuid The player's UUID
     * @return The player's name with correct capitalization or null
     */
    public abstract String getCachedPlayerName(UUID playerUuid);

    /**
     * Get the player's name if it is cached locally.
     * Must not be a long-running operation.
     *
     * @param playerUuid The String representation of a player's {@link UUID}, does not have to include the dashes
     * @return The player's name with correct capitalization or null
     */
    public String getCachedPlayerName(String playerUuid) {
        return getCachedPlayerName(getUuidFromString(playerUuid));
    }

    /**
     * Checks if a player's {@link UUID} is cached locally.
     *
     * @param playerName The player's name, does NOT have to capitalized correctly
     * @return True if the player's UUID is cached
     */
    public abstract boolean isPlayerUuidCached(String playerName);

    /**
     * Get a player's {@link UUID} if it is cached cached locally.
     * Must not be a long-running operation.
     *
     * @param playerName The player's name, does NOT have to be capitalized correctly
     * @return The player's UUID or null
     */
    public abstract UUID getCachedPlayerUuid(String playerName);

    /**
     * Get a player's {@link UUID} as a String if it is cached locally.
     * Must not be a long-running operation.
     *
     * @param playerName A player's name, does NOT have to be capitalized correctly
     * @return The String equivalent of the player's UUID (with the dashes) or null
     */
    public String getCachedPlayerUuidString(String playerName) {
        UUID playerUuid = getCachedPlayerUuid(playerName);
        if(playerUuid != null)
            return playerUuid.toString();
        return null;
    }

    /**
     * Gets a player's {@link UUID} and returns it using the supplied PlayerUUIDMapCallback
     * Potentially a long-running operation. If so, it should be carried out asynchronously.
     *
     * @param playerName A player's name, does NOT have to be capitalized correctly
     * @param callback A callback which the results of this method will be passed to. {@code playerName}s passed to the callback must be properly capitalized or null
     */
    public abstract void getPlayerUuid(String playerName, PlayerUUIDMapCallback callback);

    /**
     * Gets a player's name with corrected capitalization and returns it using the supplied PlayerUUIDMapCallback
     * This method is actually just a wrapper around {@link #getPlayerUuid} by default, but depending on the implementation the callback may or may not include the player's {@link UUID}
     *
     * @param playerName A player's name, does NOT have to be capitalized correctly
     * @param callback A callback which the results of this method will be passed to. {@code playerName}s passed to the callback must be properly capitalized or null
     * @see PlayerUUIDMapCallback
     */
    public void getCorrectedPlayerName(String playerName, PlayerUUIDMapCallback callback) {
        getPlayerUuid(playerName, callback);
    }

    /**
     * Gets a player's name and returns it using the supplied PlayerUUIDMapCallback.
     * Potentially a long-running operation. If so, it should be carried out asynchronously.
     *
     * @param uuidString The String representation of the player's {@link UUID}
     * @param callback A callback which the results of this method will be passed to. {@code playerName}s passed to the callback must be properly capitalized or null
     * @see PlayerUUIDMapCallback
     */
    public void getPlayerName(String uuidString, PlayerUUIDMapCallback callback) { getPlayerName(getUuidFromString(uuidString), callback); }

    /**
     * Gets a player's name and returns it using the supplied PlayerUUIDMapCallback.
     * Potentially a long-running operation. If so, it should be carried out asynchronously.
     *
     * @param playerUuid The player's UUID
     * @param callback A callback which the results of this method will be passed to
     * @see PlayerUUIDMapCallback
     */
    public abstract void getPlayerName(UUID playerUuid, PlayerUUIDMapCallback callback);

    /**
     * This method should fire a {@link PlayerNameChangeDetectedEvent} when a player name change is detected.
     * This serves as more of a reminder for developers extending this class that their plugin should call {@link #firePlayerNameChangeDetectedEvent} when the opportunity presents itself.
     * This method should only be called by the plugin that extends this class, however you may need to write a wrapper method with less restrictive visibility depending on your implementation.
     *
     * @param uuid The UUID of the player who changed their name
     * @param originalName The player's original name with correct capitalization
     * @param newName The player's new name with correct capitalization
     * @see #firePlayerNameChangeDetectedEvent
     */
    protected abstract void onPlayerNameChangeDetected(UUID uuid, String originalName, String newName);

    /**
     * Fires a {@link PlayerNameChangeDetectedEvent} which other plugins may listen for.
     * This event should be fired whenever the PlayerUUIDMap has to change a {@code playerName} <-> {@code playerUUID} mapping, NOT when a new mapping is created.
     * In other words, only fire this event whenever you notice a player that has played on this server before has changed their name.
     * This method should not be called outside of the extending class.
     *
     * @param uuid The UUID of the player who changed their name
     * @param originalName The player's original name with correct capitalization
     * @param newName The player's new name with correct capitalization
     * @see #onPlayerNameChangeDetected
     */
    protected void firePlayerNameChangeDetectedEvent(UUID uuid, String originalName, String newName) {
        //TODO: Write the event and fire it
    }
}
