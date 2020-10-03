package com.example.androidapp2020.Chat;

import android.widget.CheckBox;

public class FriendClass {
    private String name;
    private boolean checked;

    public FriendClass() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public FriendClass(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }
}
