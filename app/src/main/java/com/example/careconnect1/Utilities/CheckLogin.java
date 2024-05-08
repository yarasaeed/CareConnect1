package com.example.careconnect1.Utilities;

import android.content.Context;
import android.content.Intent;

import com.example.careconnect1.UI.LogIn;
import com.example.careconnect1.Utilities.UserData;

public class CheckLogin {
    public static void start(Context context, Class<?> activity){
        UserData userData = new UserData(context);
        if(userData.isLogin()){
            Intent intent = new Intent(context, activity);
            context.startActivity(intent);
        }else{
            Intent intent = new Intent(context, LogIn.class);
            context.startActivity(intent);
        }

    }
}
