package com.rajule.themelauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import com.rajule.themelauncher.model.AppRole

data class InstalledApp(
    val packageName: String,
    val label: String,
    val icon: Drawable
)

// Resolves a role (e.g. "the camera app") to a real installed package on this device.
class AppResolver(private val context: Context) {

    private val pm: PackageManager = context.packageManager

    fun listLaunchableApps(): List<InstalledApp> {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        return pm.queryIntentActivities(intent, 0)
            .mapNotNull { resolveInfo ->
                val pkg = resolveInfo.activityInfo?.packageName ?: return@mapNotNull null
                runCatching {
                    InstalledApp(
                        packageName = pkg,
                        label = resolveInfo.loadLabel(pm).toString(),
                        icon = resolveInfo.loadIcon(pm)
                    )
                }.getOrNull()
            }
            .distinctBy { it.packageName }
            .sortedBy { it.label.lowercase() }
    }

    fun isInstalled(packageName: String): Boolean =
        runCatching { pm.getApplicationInfo(packageName, 0) }.isSuccess

    fun labelFor(packageName: String): String? =
        runCatching {
            val info: ApplicationInfo = pm.getApplicationInfo(packageName, 0)
            pm.getApplicationLabel(info).toString()
        }.getOrNull()

    fun launchPackage(packageName: String): Boolean {
        val launchIntent = pm.getLaunchIntentForPackage(packageName) ?: return false
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return runCatching { context.startActivity(launchIntent) }.isSuccess
    }

    /** First installed candidate for this role, or null if none of the guesses are on the device. */
    fun autoResolve(role: AppRole): String? = role.candidatePackages.firstOrNull { isInstalled(it) }

    fun openAppSettings(packageName: String) {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.parse("package:$packageName")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching { context.startActivity(intent) }
    }
}
