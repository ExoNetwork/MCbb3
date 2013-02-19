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

package tk.manf.mcbb.api.config;

import static tk.manf.mcbb.api.config.AuthentificationMode.USERNAME;
import static tk.manf.mcbb.api.config.SecurityMode.WHITELIST;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
    /** Configuration Sections*/
    /** */
    private static final String DEFAULT_LANGUAGE            = "default.language";
    private static final String MODE_SECURITY               = "mode.security";
    private static final String MODE_AUTHENTIFICATION       = "mode.authentification";
    private static final String SCRIPT                      = "script";
    private static final String FIRST_RUN                   = "first-run";
    private static final String GREYLIST_PROTECTIONS        = "greylist.protections";
    /** Greylist Portections*/
    /** */
    public static final String GREYLIST_ENTITY_INTERACT     = "ENTITY_INTERACT";
    public static final String GREYLIST_ITEM_DROP           = "ITEM_DROP";
    public static final String GREYLIST_ITEM_PICKUP         = "ITEM_PICKUP";
    public static final String GREYLIST_PLAYER_BED          = "PLAYER_BED";
    public static final String GREYLIST_PLAYER_BUKKET       = "PLAYER_BUKKET";
    public static final String GREYLIST_PLAYER_CHAT         = "PLAYER_CHAT";
    public static final String GREYLIST_PLAYER_COMMAND      = "PLAYER_COMMAND";
    public static final String GREYLIST_PLAYER_FISHING      = "PLAYER_FISHING";
    public static final String GREYLIST_PLAYER_INTERACT     = "PLAYER_INTERACT";
    public static final String GREYLIST_PLAYER_PORTAL       = "PLAYER_PORTAL";
    public static final String GREYLIST_PLAYER_SHEAR        = "PLAYER_SHEAR";
    /** Values*/
    /** */
    private SecurityMode secMode = null;
    private AuthentificationMode authMode = null;
    private String script = null;
    private String defaultLanguage;
    private Logger logger;
    private File configFile;
    private FileConfiguration config;
    private List<String> greylistProtections;



    /**
     * Initialize a new Config
     * @param dataFolder
     * @param logger
     */
    public Config(File dataFolder, Logger logger){
        configFile = new File(dataFolder, "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        this.logger = logger;
        try {
            load();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public AuthentificationMode getAuthentificationMode() {
        return authMode;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public String getScript() {
        return script;
    }

    public SecurityMode getSecurityMode() {
        return secMode;
    }

    public boolean isGreylistProtected(String protection) {
        return greylistProtections.contains(protection);
    }

    public void setAuthentificationMode(AuthentificationMode authMode) {
        this.authMode = authMode;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public void setSecurityMode(SecurityMode secMode) {
        this.secMode = secMode;
    }

    public void addGreylistProtection(String protection) {
        greylistProtections.add(protection);
    }

    public void removeGreylistProtection(String protection) {
        greylistProtections.remove(protection);
    }

    public void load() throws FileNotFoundException, IOException, InvalidConfigurationException {
        config.load(configFile);
        assign();
    }

    public void save() throws IOException{
        config.set(DEFAULT_LANGUAGE, defaultLanguage);
        config.set(SCRIPT, script);
        config.set(MODE_SECURITY, secMode.name());
        config.set(MODE_AUTHENTIFICATION, authMode.name());
        config.set(FIRST_RUN, null);
        config.set(GREYLIST_PROTECTIONS, greylistProtections);
        config.save(configFile);
    }

    private void assign() {
        defaultLanguage = getString(DEFAULT_LANGUAGE, "deDE");
        script = getString(SCRIPT, "wbb");
        secMode = stringToSecurityMode(getString(MODE_SECURITY, WHITELIST.name()));
        authMode = stringToAuthentificationMode(getString(MODE_AUTHENTIFICATION, USERNAME.name()));
        greylistProtections = getList(GREYLIST_PROTECTIONS);
        if(config.getBoolean("first-run", false)){
            firstRun();
        }
    }

    private void firstRun(){
        logger.info("First-Run: Thanks for using MCbb, i will generate your Configuration now!");
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //UGLY Workaround
    private AuthentificationMode stringToAuthentificationMode(String name){
        try {
            return AuthentificationMode.valueOf(name);
        }catch(IllegalArgumentException e){
            logger.severe("AuthentificationMode invalid!");
            e.printStackTrace();
            return USERNAME;
        }
    }

    //UGLY Workaround
    private SecurityMode stringToSecurityMode(String name){
        try {
            return SecurityMode.valueOf(name);
        }catch(IllegalArgumentException e){
            logger.severe("SecurityMode invalid");
            e.printStackTrace();
            return WHITELIST;
        }
    }

    private String getString(String node, String def){
        return config.getString(node, def);
    }

    private List<String> getList(String node) {
        return config.getStringList(node);
    }

}
