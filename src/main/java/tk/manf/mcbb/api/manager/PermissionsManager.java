/**************************************************************************************************
 * Copyright (c) 2013, Björn Heinrichs <manf@derpymail.org>                                       *
 * Permission to use, copy, modify, and/or distribute this software                               *
 * for any purpose with or without fee is hereby granted, provided                                *
 * that the above copyright notice and this permission notice appear in all copies.               *
 *                                                                                                *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD           *
 * TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS.              *
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL     *
 * DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,                 *
 * WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING                 *
 * OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.                          *
 **************************************************************************************************/

package tk.manf.mcbb.api.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;

//TODO: Documentation
public final class PermissionsManager {
    private static Permission permission = null;
    private static Logger logger = Logger.getLogger("Minecraft");

	private PermissionsManager() {
		throw new AssertionError();
	}

	public static final boolean checkPermissions(CommandSender sender, PermissionNode perm){
		if(sender == null || perm == null){
			throw new IllegalArgumentException("Arguments cannot be null");
		}

		if(sender.hasPermission(perm.getNode()) || sender.isOp()){
		    return true;
		}

		//sender.sendMessage(_("", "error.permissions", perm));
		return false;
	}

    public static final String[] getUserGroups(String username) {
        if(detectVault()){
            return permission.getPlayerGroups((World) null, username);
        } else {
            logger.severe("Getting User Group failed: No Vault found");
            return new String[]{};
        }

    }

    private static final boolean detectVault() {
        return (permission != null || setupPermissions());
    }

    private static final boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    public static void setUserGroups(String username, String[] groups) {
        if(detectVault()) {
            final ArrayList<String> tmp = new ArrayList<String>();
            for(int i=0;i<groups.length;i++){
                tmp.add(groups[i]);
            }
            setAllUserGroups(username, tmp);
        } else {
            logger.severe("Setting User Group failed: No Valut found");
        }
    }

    private static void setAllUserGroups(String username, List<String> newGroups) {
        String[] glist = permission.getGroups();
        for(int i=0;i<glist.length;i++) {
            if(newGroups.contains(glist[i])){
                if(!permission.playerInGroup((World) null, username, glist[i])) {
                    permission.playerAddGroup((World) null, username, glist[i]);
                }
            } else {
                permission.playerRemoveGroup((World) null, username, glist[i]);
            }

        }
    }

}

enum PermissionNode {
	MAINTAINER(""),
	VIP("vip"),
	VIP_OVERRIDE(VIP, "override");

	PermissionNode(PermissionNode parent, String node) {
	    this.node = parent.getNode() + "." + node;
	}
	PermissionNode(String node){
		this.node = PARENT + "." + node;
	}

	public String getNode() {
		return node;
	}

	private String node;

	private static final String PARENT = "MCbb3";
}