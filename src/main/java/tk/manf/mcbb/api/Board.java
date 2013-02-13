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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import tk.manf.mcbb.api.config.Config;

/**
 *
 * Represents the board
 * @author Björn Heinrichs
 *
 */
public class Board {
    private Logger logger = null;
    private File boardFile;
    private Globals globals;
    private Config config;

    public Board(Logger logger, File boardFile, Config config) {
        this.logger = logger;
        this.boardFile = boardFile;
        this.config = config;
        reloadScripts();
    }

	/**
	 * Indicates whether or not a username has been registered in the Forum.
	 * Used for simple whitelist.
	 *
	 * @param username
	 * @return boolean
	 */
	public boolean containsUsername(String username) {
	    return getID(username) > 0;
	}

	/**
	 * Returns current posts amount.
	 * May return less amount of posts for uncounted posts
	 *
	 * @param username
	 * @return amount of posts
	 */
	public int getPostAmount(String username) {
	    boardFile.exists();
		return 0;
	}

	/**
	 * Returns amount of posts that have been paid already.
	 * Used for prevention of double payment for Spam
	 *
	 * @param username
	 * @return amount of already paid posts
	 */
	public int getPaidPosts(String username){
		return 0;
	}

    /**
     * Returns the default Language
     * @return locale
     */
    public String getDefaultLocale(){
        return config.getDefaultLanguage();
    }

	/**
	 * Returns the personal User-specific language for multi-lingual Support
	 * @param username
	 * @return locale
	 */
	public String getLocale(String username){
        logger.log(Level.INFO, "getLocale(" + username + ")");
        LuaValue containsUsername = globals.get("getLocale");
        LuaValue call = containsUsername.call(LuaValue.valueOf(username));
        String lang = call.checkjstring();
        if(lang.equalsIgnoreCase("")) {
            return config.getDefaultLanguage();
        }
        return lang;
	}

	/**
     * Returns userid for any founded user or -1 if no User was found.
     * -2 indicates MySQL Errors
     *
	 * @param username
	 * @return userid or -1 or -2
	 */
	public int getID(String username){
	    logger.log(Level.INFO, "getID(" + username + ")");
	    LuaValue containsUsername = globals.get("getID");
	    LuaValue call = containsUsername.call(LuaValue.valueOf(username));
	    return call.checkint();
	}

	/**
	 * Returns the main Group for the given User, null if no User was found
	 * @return groupname
	 */
	public String getMainGroup(String username) {
		return "";
	}

	/**
	 * Returns an Array with all Usergroups
	 * @return Grouparray
	 */
	public String[] getGroups() {
		return new String[]{};
	}

	/**
	 * Reloads script
	 */
	public void reloadScripts(){
        globals = JsePlatform.standardGlobals();
        //globals.loadFile(boardFile.getAbsolutePath());
        globals.get("dofile").call(LuaValue.valueOf(boardFile.getAbsolutePath()));
	}
}
