package com.example.androidapp2020;

import java.util.HashMap;
import java.util.Map;

public class ProfileData {
    public String game; // 만들어야 할 것
    // 체크박스
    public Map<String, Boolean> game_selected = new HashMap<>();
    public Map<String, Boolean> lol_position = new HashMap<>();
    public Map<String, Boolean> battle_map = new HashMap<>();
    public Map<String, Boolean> battle_squad = new HashMap<>();
    public Map<String, Boolean> over_position = new HashMap<>();
    public Map<String, Boolean> fifa4_game = new HashMap<>();

    // 티어 정리 용 ->스피너
    public Long lol_selected1;
    public Long lol_selected2;
    public Long battle_selected;
    public Long over_selected;
    public Long fifa4_selected;
    public Long kart_selected1;
    public Long kart_selected2;
    public Long hearth_selected1;
    public Long hearth_selected2;
    public Long star2_selected1;
    public Long star2_selected2;

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("game_selected", game_selected);
        result.put("lol_position", lol_position);
        result.put("lol_selected1", lol_selected1);
        result.put("lol_selected2", lol_selected2);
        result.put("battle_selected", battle_selected);
        result.put("battle_map", battle_map);
        result.put("battle_squad", battle_squad);
        result.put("over_selected", over_selected);
        result.put("over_position", over_position);
        result.put("fifa4_selected", fifa4_selected);
        result.put("fifa4_game", fifa4_game);
        result.put("kart_selected1", kart_selected1);
        result.put("kart_selected2", kart_selected2);
        result.put("hearth_selected1", hearth_selected1);
        result.put("hearth_selected2", hearth_selected2);
        result.put("star2_selected1", star2_selected1);
        result.put("star2_selected2", star2_selected2);

        return result;
    }
}
