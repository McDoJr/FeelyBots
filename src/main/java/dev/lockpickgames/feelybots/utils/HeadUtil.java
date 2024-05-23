package dev.lockpickgames.feelybots.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.UUID;

public class HeadUtil {

    public static ItemStack getHead(UUID uuid){
        return getOfflinePlayerHead(uuid, getDefaultHead());
    }

    private static ItemStack getOfflinePlayerHead(UUID uuid, ItemStack itemStack){
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    public static ItemStack getHeadFromUrl(String url) {
        ItemStack item = getDefaultHead();

        return headWithUrl(item, url);
    }

    public static ItemStack headWithUrl(ItemStack item, String url) {
        notNull(item, "item");
        notNull(url, "url");

        return headWithBase64(item, urlToBase64(url));
    }

    public static ItemStack headWithBase64(ItemStack item, String base64) {
        notNull(item, "item");
        notNull(base64, "base64");

        UUID hashAsId = new UUID(base64.hashCode(), base64.hashCode());
        return Bukkit.getUnsafe().modifyItemStack(item,
                "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}"
        );
    }

    public static ItemStack getDefaultHead(){
        if(latestVersion()){
            return new ItemStack(Material.valueOf("PLAYER_HEAD"));
        }else{
            return new ItemStack(Material.valueOf("SKULL"), 1, (byte) 3);
        }
    }

    private static String urlToBase64(String url) {

        URI actualUrl;
        try {
            actualUrl = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String toEncode = "{\"textures\":{\"SKIN\":{\"url\":\"" + actualUrl + "\"}}}";
        return Base64.getEncoder().encodeToString(toEncode.getBytes());
    }

    public static void notNull(Object object, String name){
        if(object == null){
            throw new NullPointerException(name + " cannot be null!");
        }
    }

    public static boolean latestVersion(){
        try{
            Material.valueOf("PLAYER_HEAD");
            return true;
        }catch (IllegalArgumentException e){
            return false;
        }
    }
}
