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

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
    private SecurityMode secMode = null;
    private AuthentificationMode authMode = null;
    private String script = null;

    public Config(File dataFolder){
        configFile = new File(configFile, "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        secMode = WHITELIST;
        authMode = USERNAME;
        setScript("wbb");
    }

    public AuthentificationMode getAuthentificationMode() {
        return authMode;
    }

    public String getScript() {
        return script;
    }

    public SecurityMode getSecurityMode() {
        return secMode;
    }

    public void load() throws FileNotFoundException, IOException, InvalidConfigurationException{
        config.load(configFile);
    }

    public void save() throws IOException{
        config.save(configFile);
    }


    public void setAuthentificationMode(AuthentificationMode authMode) {
        this.authMode = authMode;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public void setSecurityMode(SecurityMode secMode) {
        this.secMode = secMode;
    }

    private File configFile;
    private FileConfiguration config;
}
