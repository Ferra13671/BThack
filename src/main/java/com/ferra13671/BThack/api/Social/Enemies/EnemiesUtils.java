package com.ferra13671.BThack.api.Social.Enemies;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Social.SocialUtils;
import net.minecraft.entity.player.PlayerEntity;

public final class EnemiesUtils {

    public static void saveEnemies() {
        SocialUtils.save("Enemies/Enemies.txt", EnemyList.enemies);
    }

    public static boolean addEnemy(String enemy) {
        EnemyList.loadEnemies();
        if (!EnemyList.enemies.contains(enemy)) {
            EnemyList.enemies.add(enemy);
            saveEnemies();
            return true;
        } else {
            BThack.log("This enemy already exists!");
        }
        return false;
    }

    public static boolean removeEnemy(String enemy) {
        EnemyList.loadEnemies();
        if (EnemyList.enemies.contains(enemy)) {
            EnemyList.enemies.remove(enemy);
            saveEnemies();
            return true;
        } else {
            BThack.log("This enemy doesn't exist!");
        }
        return false;
    }

    public static boolean isEnemy(PlayerEntity player) {
        String name = player.getNameForScoreboard();
        return EnemyList.enemies.contains(name);
    }
    public static boolean isEnemy(String name) {
        return EnemyList.enemies.contains(name);
    }
}
