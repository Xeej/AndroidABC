package com.example.examplereg;

import android.content.Intent;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

public class VkApp extends android.app.Application {
    // Изенение пароля, смена токена
    public class Application extends android.app.Application {
        VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
            @Override
            public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
                if (newToken == null) {
// VKAccessToken is invalid
                    Intent intent = new Intent(Application.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME|Intent.FLAG_ACTIVITY_CLEAR_TOP);

                }
            }
        };

    }
    @Override
    public void onCreate(){
        super.onCreate();

        VKSdk.initialize(this);

    }
}
