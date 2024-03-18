package us.azcode.azlogin.listeners;

import us.azcode.azlogin.HikariCPManager;
import us.azcode.azlogin.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        try (Connection connection = HikariCPManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE name = ?")) {
            statement.setString(1, playerName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    player.sendMessage(ChatColor.YELLOW + "Please use /login <password>.");
                } else {
                    player.sendMessage(ChatColor.GREEN + "Please use /register <password>.");
                }
            }

            PlayerUtils.applyBlindness(player);
            PlayerUtils.restrictMovement(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (PlayerUtils.isRestricted(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
