package net.milkbowl.vault.uuid;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A callback to be ran after mapping a playerName <-> uuid
 */
public abstract class PlayerUUIDMapCallback {
    /**
     * Convenience method for calling {@link #onCallback(Throwable, String, UUID)} with an option to call it on the main thread. Catches any Throwable resulting from the callback and prints the stack trace.
     * The originally supplied {@code playerName} or {@code playerUuid} MUST be included in this callback even if the mapping failed or an error occurred.
     *
     * @param plugin The JavaPlugin running the callback (needed for scheduling on the main thread)
     * @param error A link Throwable if an error occurred, or null
     * @param playerName The player's name with correct capitalization or null if it could not be found or an error occurred
     * @param playerUuid The player's UUID or null if it could not be found or an error occurred
     * @param runOnMainThread If true, runs {@link #onCallback(Throwable, String, UUID)} on the main thread, otherwise it will run on the calling thread
     */
    public final void doCallback(JavaPlugin plugin, final Throwable error, final String playerName, final UUID playerUuid, boolean runOnMainThread) {
        if(runOnMainThread) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    doCallback(error, playerName, playerUuid);
                }
            });
        } else {
            doCallback(error, playerName, playerUuid);
        }
    }

    private void doCallback(Throwable error, String playerName, UUID playerUuid) {
        try {
            onCallback(error, playerName, playerUuid);
        } catch (Throwable outsideError) {
            outsideError.printStackTrace();
        }
    }

    /**
     * A callback to be ran after mapping a {@code playerName} <-> {@code playerUuid}.
     * The originally supplied {@code playerName} or {@code playerUuid} MUST be included in this callback even if the mapping failed or an error occurred.
     *
     * @param error A Throwable if an error occurred, or null
     * @param playerName The player's name with correct capitalization or null if it could not be found or an error occurred
     * @param playerUuid The player's UUID or null if it could not be found or an error occurred
     */
    public abstract void onCallback(Throwable error, String playerName, UUID playerUuid);
}
