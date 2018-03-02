package com.yueyue.todolist.modules.avatar.preference;

import android.text.TextUtils;

import com.yueyue.todolist.base.BasePreference;

/**
 * author : yueyue on 2018/3/4 10:03
 * desc   :
 */
@Deprecated
public class AvatarPreference extends BasePreference {

    private static final String AVATAR_KEY = "avatar_key";

    private AvatarPreference() {
    }

    public static AvatarPreference getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final AvatarPreference sInstance = new AvatarPreference();
    }

    public void setAvatar(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        setString(AVATAR_KEY, value);
    }

    public String getAvatar() {
        return getString(AVATAR_KEY, "");
    }
}
