package holo.holoafk.actions;

import holo.holoafk.actions.threads.HubAction;
import holo.holoafk.actions.threads.KickAction;
import holo.holoafk.actions.threads.LimboAction;
import holo.holoafk.utils.Events;
import holo.holoafk.utils.Utils;

public class CommandAction {

    /**
     * Run a recovery action based on the given recovery event.
     *
     * @param maxTries The maximum number of times to try to recover.
     * @param recovery The recovery event to run.
     */
    public static void runRecovery(int maxTries, Events.RecoveryEvent recovery) {
        if (Utils.isOnPrivateIsland() && Utils.isInSkyblock()) {
            return;
        }
        switch (recovery) {
            case HUB_RECOVERY:
                HubAction hubAction = new HubAction(maxTries);
                hubAction.start();
                break;
            case LIMBO_RECOVERY:
                LimboAction limboAction = new LimboAction(maxTries);
                limboAction.start();
                break;
            case KICK_RECOVERY:
                KickAction kickAction = new KickAction(maxTries);
                kickAction.start();
                break;
            case DISCONNECT_RECOVERY:
                // noop
                break;
        }
    }
}
