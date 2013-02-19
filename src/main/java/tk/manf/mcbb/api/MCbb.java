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

package tk.manf.mcbb.api;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import se.ranzdo.bukkit.methodcommand.CommandHandler;
import tk.manf.mcbb.MCbbPlugin;
import tk.manf.mcbb.api.config.Config;
import tk.manf.mcbb.api.manager.LanguageManager;
import tk.manf.mcbb.commands.AdminCommands;
import tk.manf.mcbb.listener.WhitelistListener;

public class MCbb {
    private static Board board;
    private static LanguageManager langManager;

    private Config config;
    private Logger logger;

    public MCbb(MCbbPlugin plugin){
        logger = plugin.getLogger();
        File dataFolder = plugin.getDataFolder();
        config = new Config(dataFolder, logger);
        File scriptFile = new File(dataFolder , config.getScript() + ".lua");
        board = new Board(logger, scriptFile, config);
        langManager = new LanguageManager(logger);
        plugin.loadLanguages(langManager);
        //luaTest(plugin.getDataFolder());
        registerListener(plugin);
        CommandHandler handler = new CommandHandler(plugin);
        handler.registerCommands(new AdminCommands(config));
    }

    /**
     * Test if the given User is considered to be a full Member
     * or if he needs to register on the forum first.
     * @param username
     * @return boolean
     */
    public boolean isRegistered(String username) {
        switch(config.getAuthentificationMode()) {
            case CUSTOM_PROFILE_FIELDS:
                throw new UnsupportedOperationException("Custome Profile Fields not implemented yet!");
            case USERNAME:
                return board.containsUsername(username);
            default:
                logger.severe("Unknown Authentification Mode for isRegistered()");
                return false;
        }
    }

    public boolean isGreylistProtected(String protection) {
        return config.isGreylistProtected(protection);
    }

    public final Board getBoard(){
        return board;
    }

    /**
     *
     * @param username
     * @return locale
     */
    public String getLocale(String username) {
        return board.getLocale(username);
    }

    private void registerListener(MCbbPlugin plugin) {
        switch(config.getSecurityMode()){
            case GREYLIST:
                break;
            case WHITELIST:
                plugin.getServer().getPluginManager().registerEvents(new WhitelistListener(this), plugin);
                break;
            default:
                break;
        }
    }

    public static String _(CommandSender sender, String node, Object... args) {
        String locale;
        if(sender instanceof ConsoleCommandSender){
            locale = board.getDefaultLocale();
        }else{
            locale = board.getLocale(sender.getName());
        }
        return ChatColor.translateAlternateColorCodes('&', _(locale, node, args));
    }

    public static String _(String locale, String node, Object... args){
        if (langManager.contains(locale)) {
            return langManager.translate(locale, node, args);
        } else {
            return ChatColor.RED + "Missing Language " + locale + "(" + node + ")";
        }
    }

    @SuppressWarnings("unused")
    private void luaTest(File dataFolder) {
        logger.info("test.lua!");
        String script = new File(dataFolder, "test.lua").getAbsolutePath();
        Globals glo = JsePlatform.standardGlobals();
        LuaValue chunk = glo.loadFile(script);
        chunk.call( LuaValue.valueOf(script) );
        logger.info("function.lua!");
        String function = new File(dataFolder, "function.lua").getAbsolutePath();
        LuaValue func = glo.loadFile(function);
        func.call( LuaValue.valueOf(function) );
    }
}
