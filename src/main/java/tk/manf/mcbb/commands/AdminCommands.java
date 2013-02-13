package tk.manf.mcbb.commands;

import static tk.manf.mcbb.api.MCbb._;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import se.ranzdo.bukkit.methodcommand.Command;
import tk.manf.mcbb.api.config.Config;

public class AdminCommands {
    /** Admin command label*/
    public final String ADMIN_LABEL = "mcbb";
    public final String ADMIN_CONFIG = ADMIN_LABEL + " config";
    /** Command to reload Config*/
    public final String ADMIN_CONFIG_RELOAD = ADMIN_CONFIG + " reload";

    private Config config;

    public AdminCommands(Config config) {
        this.config = config;
    }

    //TODO: ADD LanguageManager reload
    //TODO: ADD Script reload
    @Command(identifier = ADMIN_CONFIG_RELOAD, description = "Reloads configuration" , permissions = {ADMIN_CONFIG_RELOAD})
    public void reloadConfiguration(CommandSender sender) {
        try{
            config.load();
            sender.sendMessage(_(sender, "config.reloaded"));
        }catch(FileNotFoundException e){
            sender.sendMessage(_(sender,"error.config.filenotfound"));
            e.printStackTrace();
        }catch(InvalidConfigurationException e){
            sender.sendMessage(_(sender,"error.config.illegalconfiguration"));
            e.printStackTrace();
        }catch(IOException e){
            sender.sendMessage(_(sender, "error.config.io"));
            e.printStackTrace();
        }
    }
}