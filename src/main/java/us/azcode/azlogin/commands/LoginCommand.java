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
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        String playerName = player.getName();

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /login <password>");
            return true;
        }

        String password = args[0];

        if (!PlayerUtils.isRestricted(player)) {
            player.sendMessage(ChatColor.RED + "You are already logged in.");
            return true;
        }

        try (Connection connection = HikariCPManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE name = ? AND password = ?")) {
            statement.setString(1, playerName);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    player.sendMessage(ChatColor.GREEN + "You have successfully logged in!");
                    PlayerUtils.removeBlindness(player);
                    PlayerUtils.allowMovement(player);
                } else {
                    player.sendMessage(ChatColor.RED + "Incorrect password. Please try again.");
                }
            }
        } catch (SQLException e) {
            player.sendMessage(ChatColor.RED + "An error occurred. Please try again.");
            e.printStackTrace();
        }

        return true;
    }
}
