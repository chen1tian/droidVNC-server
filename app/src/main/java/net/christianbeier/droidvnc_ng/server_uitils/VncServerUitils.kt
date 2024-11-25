package net.christianbeier.droidvnc_ng.server_uitils

import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import net.christianbeier.droidvnc_ng.Constants
import net.christianbeier.droidvnc_ng.MainService
import java.util.UUID
import java.util.UUID.*

/**
 * Vnc服务工具
 */
class VncServerUitils {
    companion object {

        var _isRunning = false

        /**
         * 是否运行
         */
        val isRunning: Boolean
            get() = _isRunning

        /**
         * 保存设置
         */
        fun saveSetting(
            context: Context,
            setting: VncServerSetting
        ) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context);
            val editor = prefs.edit()

            editor.putInt(Constants.PREFS_KEY_SETTINGS_PORT, setting.port)
            editor.putString(Constants.PREFS_KEY_SETTINGS_PASSWORD, setting.password)
            editor.putBoolean(Constants.PREFS_KEY_SETTINGS_FILE_TRANSFER, setting.fileTransfer)
            editor.putBoolean(Constants.PREFS_KEY_SETTINGS_VIEW_ONLY, setting.viewOnly)
            editor.putBoolean(Constants.PREFS_KEY_SETTINGS_SHOW_POINTERS, setting.showPointers)
            editor.putFloat(Constants.PREFS_KEY_SETTINGS_SCALING, setting.scaling)
            editor.putString(Constants.PREFS_KEY_SETTINGS_ACCESS_KEY, setting.accessKey)

            editor.apply()
        }

        /**
         * 获取设置
         */
        fun getSetting(context: Context): VncServerSetting {
            // 从PreferenceManager中读取默认值
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return VncServerSetting(
                prefs.getInt(Constants.PREFS_KEY_SETTINGS_PORT, 5900),
                prefs.getString(Constants.PREFS_KEY_SETTINGS_PASSWORD, "123456"),
                prefs.getBoolean(Constants.PREFS_KEY_SETTINGS_FILE_TRANSFER, false),
                prefs.getBoolean(Constants.PREFS_KEY_SETTINGS_VIEW_ONLY, false),
                prefs.getBoolean(Constants.PREFS_KEY_SETTINGS_SHOW_POINTERS, true),
                prefs.getFloat(Constants.PREFS_KEY_SETTINGS_SCALING, 1f),
                prefs.getString(
                    Constants.PREFS_KEY_SETTINGS_ACCESS_KEY,
                    randomUUID().toString().replace("-", "")
                )
            )
        }

        /**
         * 获取Intnet
         * @param setting 设置, 如果不传则使用默认设置
         * @return Intent
         */
        fun getIntent(context: Context, setting: VncServerSetting? = null): Intent {

            val runSetting = if (setting != null) setting else getSetting(context)

            val intent = Intent(
                context,
                MainService::class.java
            )
            intent.putExtra(
                MainService.EXTRA_PORT,
                runSetting.port
            )
            intent.putExtra(
                MainService.EXTRA_PASSWORD,
                runSetting.password
            )
            intent.putExtra(
                MainService.EXTRA_FILE_TRANSFER,
                runSetting.fileTransfer
            )
            intent.putExtra(
                MainService.EXTRA_VIEW_ONLY,
                runSetting.viewOnly
            )
            intent.putExtra(
                MainService.EXTRA_SHOW_POINTERS,
                runSetting.showPointers
            )
            intent.putExtra(
                MainService.EXTRA_SCALING,
                runSetting.scaling
            )
            intent.putExtra(
                MainService.EXTRA_ACCESS_KEY,
                runSetting.accessKey
            )

            //    intent.putExtra(
            //        MainService.EXTRA_FALLBACK_SCREEN_CAPTURE,
            //        true
            //    )

            return intent
        }

        /**
         * 启动服务
         */
        fun start(context: Context, setting: VncServerSetting? = null): Boolean {

            val runSetting = if (setting != null) setting else getSetting(context)
            if (setting != null) {
                saveSetting(context, setting)
            }

            val intent = getIntent(context, runSetting)

            intent.action = MainService.ACTION_START

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }

            _isRunning = true
            return _isRunning
        }

        /**
         * 停止服务
         */
        fun stop(context: Context, setting: VncServerSetting? = null): Boolean {

            val runSetting = if (setting != null) setting else getSetting(context)
            if (setting != null) {
                saveSetting(context, setting)
            }

            val intent = getIntent(context, runSetting)
            intent.action = MainService.ACTION_STOP
            context.startService(intent)
            _isRunning = false
            return _isRunning
        }
    }
}