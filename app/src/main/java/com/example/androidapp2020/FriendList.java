package com.example.androidapp2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendList {

    //public Map<String, Boolean> friend_names = new HashMap<>();
    public List<String> friend_names = new ArrayList<>();

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("friend_names", friend_names);

        return result;
    }
}
