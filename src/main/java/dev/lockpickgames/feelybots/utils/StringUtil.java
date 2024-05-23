package dev.lockpickgames.feelybots.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class StringUtil {

    public static String translate(String paramString) {
        return ChatColor.translateAlternateColorCodes('&', paramString);
    }

    public static String format(Material material) {
        String[] datas = material.name().toLowerCase().split("_");
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < datas.length; i++) {
            String data = datas[i].substring(0, 1).toUpperCase() + datas[i].substring(1).toLowerCase();
            builder.append(i == 0 ? data : " " + data);
        }

        return builder.toString();
    }
}
