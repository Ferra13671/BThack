package com.ferra13671.BThack.api.Social.Friends;


import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Social.SocialUtils;
import net.minecraft.entity.player.PlayerEntity;

public final class FriendsUtils {

    public static void saveFriends() {
        SocialUtils.save("Friends/Friends.txt", FriendList.friends);
    }

    public static boolean addFriend(String friend) {
        FriendList.loadFriends();
        if (!FriendList.friends.contains(friend)) {
            FriendList.friends.add(friend);
            saveFriends();
            return true;
        } else {
            BThack.log("This friend already exists!");
        }
        return false;
    }

    public static boolean removeFriend(String friend) {
        FriendList.loadFriends();
        if (FriendList.friends.contains(friend)) {
            FriendList.friends.remove(friend);
            saveFriends();
            return true;
        } else {
            BThack.log("This friend doesn't exist!");
        }
        return false;
    }

    public static boolean isFriend(PlayerEntity player) {
        String name = player.getNameForScoreboard();
        return FriendList.friends.contains(name);
    }

    public static boolean isFriend(String name) {
        return FriendList.friends.contains(name);
    }
}
