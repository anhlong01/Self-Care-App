package com.lph.selfcareapp.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Role {
    @SerializedName("roleName")
    @Expose
    private String rolename;
    public String getRolename() {
        return rolename;
    }
    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public Role(String rolename) {
        this.rolename = rolename;
    }

    @Override
    public int hashCode() {
        return rolename.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true; // Nếu cùng tham chiếu, trả về true
        if (obj == null || getClass() != obj.getClass()) return false; // Kiểm tra kiểu
        Role role = (Role) obj;
        return rolename.equals(role.rolename); //
    }
}
