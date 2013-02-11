package tk.manf.mcbb.api.manager;

import org.bukkit.command.CommandSender;
//TODO: Documentation
public final class PermissionsManager {
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
}

enum PermissionNode {
	ADMIN(""), ASDF("");

	PermissionNode(String node){
		node = PARENT + node;
	}

	public String getNode() {
		return node;
	}

	private String node;

	private static final String PARENT = "MCbb3";
}