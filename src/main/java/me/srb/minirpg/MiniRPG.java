package me.srb.minirpg;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class MiniRPG extends JavaPlugin {

    @Getter
    public static MiniRPG plugin;
    @Override
    public void onEnable() {
        plugin = this;
    }
}
