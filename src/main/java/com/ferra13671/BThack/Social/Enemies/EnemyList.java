package com.ferra13671.BThack.api.Social.Enemies;

import com.ferra13671.BThack.api.Social.SocialUtils;

import java.util.ArrayList;

public final class EnemyList {
    public static ArrayList<String> enemies = new ArrayList<>();

    public static void loadEnemies() {
        SocialUtils.load("Enemies/Enemies.txt", enemies);
    }
}
