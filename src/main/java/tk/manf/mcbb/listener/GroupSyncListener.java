package tk.manf.mcbb.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GroupSyncListener implements Listener{
    public void onPlayerJoin(final PlayerJoinEvent ev) {
        ev.getPlayer();
    }
}
