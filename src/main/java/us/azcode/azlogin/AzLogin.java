package us.azcode.azlogin;

import us.azcode.azlogin.commands.LoginCommand;
import us.azcode.azlogin.commands.RegisterCommand;
import us.azcode.azlogin.listeners.PlayerListener;

import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class AzLogin extends JavaPlugin {

    @Override
    public void onEnable() {
        HikariCPManager.setupDataSource();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        this.getCommand("register").setExecutor(new RegisterCommand());
        this.getCommand("login").setExecutor(new LoginCommand());
    }
}
