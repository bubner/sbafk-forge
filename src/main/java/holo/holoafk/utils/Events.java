package holo.holoafk.utils;

import java.util.HashMap;

/**
 * Definitions for alert statuses and kick events.
 */
public class Events {
    public static HashMap<String, RecoveryEvent> FLAGGED_MSGS = new HashMap<String, RecoveryEvent>() {{
        put("evacuating to hub", RecoveryEvent.HUB_RECOVERY);
        put("you are being transferred", RecoveryEvent.HUB_RECOVERY);
        put("an exception occurred", RecoveryEvent.KICK_RECOVERY);
        put("a disconnect occurred", RecoveryEvent.KICK_RECOVERY);
        put("a kick occurred", RecoveryEvent.KICK_RECOVERY);
        put("you are afk", RecoveryEvent.LIMBO_RECOVERY);
        put("you were spawned in limbo", RecoveryEvent.LIMBO_RECOVERY);
    }};

    public static String getKickEventContent(KickEvent event) {
        switch (event) {
            case UNSUCCESSFUL_HUB_RECOVERY:
                return "User was spawned into the hub, and automatic recovery was unsuccessful. Please check your account.";
            case SUCCESSFUL_HUB_RECOVERY:
                return "User was spawned into the hub, but automatic recovery was successful. No actions required.";
            case UNSUCCESSFUL_LIMBO_RECOVERY:
                return "User was kicked to limbo, and automatic recovery was unsuccessful. Please check your account.";
            case SUCCESSFUL_LIMBO_RECOVERY:
                return "User was kicked to limbo, but automatic recovery was successful. No actions required.";
            case UNSUCCESSFUL_KICK_RECOVERY:
                return "User was kicked from the island, and automatic recovery was unsuccessful. Please check your account.";
            case SUCCESSFUL_KICK_RECOVERY:
                return "User was kicked from the island, but automatic recovery was successful. No actions required.";
            case SUCCESSFUL_DISCONNECT_RECOVERY:
                return "User has lost connection to the server and has been recovered automatically. No actions required.";
            case UNSUCCESSFUL_DISCONNECT_RECOVERY:
                return "User has lost connection to the server and cannot be recovered automatically. Please check your account.";
        }
        return "";
    }

    public static KickEvent getKickEvent(boolean success, RecoveryEvent recovery) {
        switch (recovery) {
            case HUB_RECOVERY:
                return success ? KickEvent.SUCCESSFUL_HUB_RECOVERY : KickEvent.UNSUCCESSFUL_HUB_RECOVERY;
            case LIMBO_RECOVERY:
                return success ? KickEvent.SUCCESSFUL_LIMBO_RECOVERY : KickEvent.UNSUCCESSFUL_LIMBO_RECOVERY;
            case KICK_RECOVERY:
                return success ? KickEvent.SUCCESSFUL_KICK_RECOVERY : KickEvent.UNSUCCESSFUL_KICK_RECOVERY;
            case DISCONNECT_RECOVERY:
                return success ? KickEvent.SUCCESSFUL_DISCONNECT_RECOVERY : KickEvent.UNSUCCESSFUL_DISCONNECT_RECOVERY;
        }
        return null;
    }

    public enum KickEvent {
        UNSUCCESSFUL_HUB_RECOVERY,
        SUCCESSFUL_HUB_RECOVERY,
        UNSUCCESSFUL_LIMBO_RECOVERY,
        SUCCESSFUL_LIMBO_RECOVERY,
        UNSUCCESSFUL_KICK_RECOVERY,
        SUCCESSFUL_KICK_RECOVERY,
        SUCCESSFUL_DISCONNECT_RECOVERY,
        UNSUCCESSFUL_DISCONNECT_RECOVERY,
    }

    public enum RecoveryEvent {
        HUB_RECOVERY,
        LIMBO_RECOVERY,
        KICK_RECOVERY,
        DISCONNECT_RECOVERY,
    }

    /**
     * Priorities of alert messages, HIGH will be pinged and displayed in red.
     */
    public enum AlertPriority {
        LOW,
        HIGH
    }
}
