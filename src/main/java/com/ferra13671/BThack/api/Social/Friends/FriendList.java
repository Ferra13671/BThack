package com.ferra13671.BThack.api.Social.Friends;


import com.ferra13671.BThack.api.Social.SocialUtils;

import java.util.ArrayList;

public final class FriendList {
    public static ArrayList<String> friends = new ArrayList<>();

    public static void loadFriends() {
        SocialUtils.load("Friends/Friends.txt", friends);
    }
}
