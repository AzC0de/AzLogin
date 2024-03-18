package us.azcode.azlogin.commands;

import us.azcode.azlogin.HikariCPManager;
import us.azcode.azlogin.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        String playerName = player.getName();

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /register <password>");
            return true;
        }

        String password = args[0];

        try (Connection connection = HikariCPManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO players (name, password) VALUES (?, ?)")) {
            statement.setString(1, playerName);
            statement.setString(2, password);
            statement.executeUpdate();

            player.sendMessage(ChatColor.GREEN + "You have successfully registered!");
            PlayerUtils.removeBlindness(player);
            PlayerUtils.allowMovement(player);
        } catch (SQLException e) {
            player.sendMessage(ChatColor.RED + "An error occurred. Please try again.");
            e.printStackTrace();
        }

        return true;
    }
}
