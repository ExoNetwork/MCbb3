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

package tk.manf.mcbb;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import tk.manf.mcbb.api.MCbb;
import tk.manf.mcbb.api.manager.LanguageManager;
import tk.manf.mcbb.util.IOTools;
//TODO: Documentation
public class MCbbPlugin extends JavaPlugin {
    private static final String LANG_FOLDER = "language";
    private MCbb mcbb;
    private Logger logger;


    @Override
    public void onEnable() {
        logger = getLogger();
        getDataFolder().mkdirs();
        mcbb = new MCbb(this);
   }

    @Override
    public void onDisable() {

    }

    public final MCbb getMCbb(){
        return mcbb;
    }

    public void loadLanguages(LanguageManager langManager) {
        try {
            String template = IOTools.toString(getResource("available_langs"));
            String[] langs = template.split("\r?\n|\r");
            for(int i=0;i<langs.length;i++){
                registerLanguage(langManager, langs[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerLanguage(LanguageManager langManager, String locale) {
        try {
            langManager.addLanguage(locale, getResource(LANG_FOLDER + "/" + locale), getDataFolder());
            logger.info("Registering Language: " + locale);
        } catch (IOException e) {
            logger.severe("Errr while loading locale: " + locale);
            e.printStackTrace();
        }
    }
}