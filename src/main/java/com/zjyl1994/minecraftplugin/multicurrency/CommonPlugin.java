package com.zjyl1994.minecraftplugin.multicurrency;

import org.bukkit.plugin.java.JavaPlugin;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;
import java.io.File;

public abstract class CommonPlugin extends JavaPlugin {
    private static HikariDataSource hikariDataSource;

    protected HikariDataSource getDataSource() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to find JDBC driver", e);
        }
        String name = "MultiCurrency";
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(this.getConfig().getString(name + ".jdbcUrl"));
        config.setUsername(this.getConfig().getString(name + ".username"));
        config.setPassword(this.getConfig().getString(name + ".password"));
        config.addDataSourceProperty("cachePrepStmts", this.getConfig().getBoolean(name + ".cachePrepStmts", true));
        config.addDataSourceProperty("prepStmtCacheSize",
                this.getConfig().getInt(name + ".prepStmtCacheSize", 250));
        config.addDataSourceProperty("prepStmtCacheSqlLimit",
                this.getConfig().getInt(name + ".prepStmtCacheSqlLimit", 2048));
        config.setAutoCommit(false);
        hikariDataSource = new HikariDataSource(config);
        return hikariDataSource;
    }

    public void saveDefaultConfig() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            this.saveResource("config.yml", false);
        }
    }

    @Override
    public abstract void onEnable();

    @Override
    public abstract void onDisable();
}
