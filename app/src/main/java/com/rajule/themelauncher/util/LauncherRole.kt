package com.rajule.themelauncher.util

import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

/** Prompts the system "set default launcher" flow, using the newest API the device supports. */
object LauncherRole {

    fun isDefaultLauncher(context: Context): Boolean {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
        val resolveInfo = context.packageManager.resolveActivity(intent, 0)
        return resolveInfo?.activityInfo?.packageName == context.packageName
    }

    fun requestDefaultLauncher(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = activity.getSystemService(Context.ROLE_SERVICE) as? RoleManager
            if (roleManager != null && roleManager.isRoleAvailable(RoleManager.ROLE_HOME)) {
                if (!roleManager.isRoleHeld(RoleManager.ROLE_HOME)) {
                    activity.startActivity(roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME))
                    return
                }
            }
        }
        runCatching {
            activity.startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
        }
    }
}
