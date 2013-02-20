package tk.manf.mcbb.api.manager;

import static tk.manf.mcbb.api.manager.PermissionsManager.getUserGroups;
import static tk.manf.mcbb.api.manager.PermissionsManager.setUserGroups;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;

import tk.manf.mcbb.api.Board;
import tk.manf.mcbb.api.MCbb;

public class SyncManager {
    //TODO: initialize
    private static FileConfiguration groups = null;
    //TODO: initialize
    private static MCbb mcbb = null;

    private static Logger logger = Logger.getLogger("Minecraft");

    private SyncManager() {
        throw new AssertionError();
    }

    public static final void syncUser(String username) {
        Board board = mcbb.getBoard();
        switch(mcbb.getSyncHost()) {
            case FORUM:
                setUserGroups(username, convertToSlaveGroup(board.getGroups()));
                return;
            case SERVER:
                board.setGroups(username, convertToSlaveGroup(getUserGroups(username)));
                return;
            default:
                logger.severe("Unknown Sync Host for syncUser()");
                return;
        }
    }

    private static final String[] convertToSlaveGroup(String[] hostGroup) {
        String[] slaveGroup = new String[hostGroup.length];
        for(int i=0;i<hostGroup.length;i++) {
            if(groups.contains("group." + hostGroup[i])){
                slaveGroup[i] = groups.getString("group." + hostGroup[i]);
            }
        }
        return slaveGroup;
    }
}
