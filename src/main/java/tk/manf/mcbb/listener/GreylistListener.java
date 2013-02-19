package tk.manf.mcbb.listener;

import static tk.manf.mcbb.api.MCbb._;
import static tk.manf.mcbb.api.config.Config.GREYLIST_ENTITY_INTERACT;
import static tk.manf.mcbb.api.config.Config.GREYLIST_ITEM_DROP;
import static tk.manf.mcbb.api.config.Config.GREYLIST_ITEM_PICKUP;
import static tk.manf.mcbb.api.config.Config.GREYLIST_PLAYER_BED;
import static tk.manf.mcbb.api.config.Config.GREYLIST_PLAYER_BUKKET;
import static tk.manf.mcbb.api.config.Config.GREYLIST_PLAYER_CHAT;
import static tk.manf.mcbb.api.config.Config.GREYLIST_PLAYER_COMMAND;
import static tk.manf.mcbb.api.config.Config.GREYLIST_PLAYER_FISHING;
import static tk.manf.mcbb.api.config.Config.GREYLIST_PLAYER_INTERACT;
import static tk.manf.mcbb.api.config.Config.GREYLIST_PLAYER_PORTAL;
import static tk.manf.mcbb.api.config.Config.GREYLIST_PLAYER_SHEAR;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

import tk.manf.mcbb.api.MCbb;
public class GreylistListener implements Listener{
    /** Cache containing each greylisted User*/
    private ArrayList<String> greylist = new ArrayList<String>();
    /** MCbb Instance*/
    private MCbb mcbb;

    /**
     * Initializes a new Greylist-Listener
     * @param mcbb
     */
    public GreylistListener(final MCbb mcbb) {
        this.mcbb = mcbb;
    }


    /**
     * Called everytime a User joins.
     * Add users to cache.
     * @param ev
     */
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent ev) {
        final Player player = ev.getPlayer();
        final String username = player.getName();
        if(mcbb.isRegistered(username)) {
            player.sendMessage(_(player, "registered.welcome", username));
        } else {
            greylist.add(username);
        }
    }

    /**
     * Called everytime a User leaves.
     * Clears cache
     * @param ev
     */
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        final String username = player.getName();
        if(greylist.contains(username)) {
            greylist.remove(username);
        }
    }

    @EventHandler
    public void onBucketFill(final PlayerBucketFillEvent ev) {
        handleEvent(GREYLIST_PLAYER_BUKKET, ev, ev.getPlayer());
    }

    @EventHandler
    public void onBucketEmpty(final PlayerBucketEmptyEvent ev) {
        handleEvent(GREYLIST_PLAYER_BUKKET, ev, ev.getPlayer());
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent ev) {
        handleEvent(GREYLIST_PLAYER_COMMAND, ev, ev.getPlayer());
    }

    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent ev) {
        handleEvent(GREYLIST_PLAYER_CHAT, ev, ev.getPlayer());

    }

    @EventHandler
    public void onPlayerBedEnter(final PlayerBedEnterEvent ev) {
        handleEvent(GREYLIST_PLAYER_BED, ev, ev.getPlayer());
    }

    @EventHandler
    public void onItemDrop(final PlayerDropItemEvent ev) {
        handleEvent(GREYLIST_ITEM_DROP, ev, ev.getPlayer());
    }

    @EventHandler
    public void onItemPickup(final PlayerPickupItemEvent ev) {
        handleEvent(GREYLIST_ITEM_PICKUP, ev, ev.getPlayer());
    }

    @EventHandler
    public void onPortalPort(final PlayerPortalEvent ev) {
        handleEvent(GREYLIST_PLAYER_PORTAL, ev, ev.getPlayer());
    }

    @EventHandler
    public void onAnimation(final PlayerAnimationEvent ev) {
        handleEvent(GREYLIST_PLAYER_INTERACT, ev, ev.getPlayer());
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent ev) {
        handleEvent(GREYLIST_PLAYER_INTERACT, ev, ev.getPlayer());
    }

    @EventHandler
    public void onEntity(final PlayerInteractEntityEvent ev) {
        handleEvent(GREYLIST_ENTITY_INTERACT, ev, ev.getPlayer());
    }

    @EventHandler
    public void onPortalPort(final PlayerShearEntityEvent ev) {
        handleEvent(GREYLIST_PLAYER_SHEAR, ev, ev.getPlayer());
    }
    @EventHandler
    public void onPortalPort(final PlayerFishEvent ev) {
        handleEvent(GREYLIST_PLAYER_FISHING, ev, ev.getPlayer());
    }

    private void handleEvent(final String protection, final Cancellable ev, final Player player){
        handleEvent(protection, ev, player, true);
    }

    private void handleEvent(final String protection, final Cancellable ev, final Player player, boolean announce){
        if(ev.isCancelled()) return;
        if(mcbb.isGreylistProtected(protection)) {
            ev.setCancelled(mcbb.isRegistered(player.getName()));
            if(announce) {
                player.sendMessage(_(player, "greylist.protected"));
            }
        }
    }
}
