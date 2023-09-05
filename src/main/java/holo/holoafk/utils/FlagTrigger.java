package holo.holoafk.utils;

import java.util.Objects;

/**
 * Utility class for a flag trigger disconnect detection.
 */
public class FlagTrigger {
    private final String message;
    private final String trigger;
    private String content;

    public FlagTrigger(String message, String key) {
        this.message = message;
        this.trigger = key;
        this.content = "";
    }

    public void setSuccess(boolean success) {
        if (trigger == null) {
            this.content = Events.getKickEventContent(Objects.requireNonNull(Events.getKickEvent(success, Events.RecoveryEvent.DISCONNECT_RECOVERY)));
            return;
        }
        this.content = Events.getKickEventContent(Objects.requireNonNull(Events.getKickEvent(success, Events.FLAGGED_MSGS.get(this.trigger))));
    }

    public String getMessage() {
        return message;
    }

    public String getContent() {
        return content;
    }
}
