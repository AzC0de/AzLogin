package us.azcode.azlogin.utils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerUtils {
    private static final Set<UUID> restrictedPlayers = new HashSet<>();

    public static void applyBlindness(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1));
    }

    public static void removeBlindness(Player player) {
        player.removePotionEffect(PotionEffectType.BLINDNESS);
    }

    public static void restrictMovement(Player player) {
        restrictedPlayers.add(player.getUniqueId());
    }

    public static void allowMovement(Player player) {
        restrictedPlayers.remove(player.getUniqueId());
    }

    public static boolean isRestricted(Player player) {
        return restrictedPlayers.contains(player.getUniqueId());
    }
}
