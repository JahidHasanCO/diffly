package dev.jahidhasanco.diffly.data.local
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dev.jahidhasanco.diffly.util.Constant.KEY_REALTIME_DIFF
import dev.jahidhasanco.diffly.util.Constant.KEY_VIEW_TYPE
import dev.jahidhasanco.diffly.util.Constant.PREFS_NAME

class PrefsManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var realTimeDiff: Boolean
        get() = prefs.getBoolean(KEY_REALTIME_DIFF, false)
        set(value) = prefs.edit { putBoolean(KEY_REALTIME_DIFF, value) }

    var selectedViewType: String?
        get() = prefs.getString(KEY_VIEW_TYPE, null)
        set(value) = prefs.edit { putString(KEY_VIEW_TYPE, value) }

    fun clearAll() {
        prefs.edit { clear() }
    }
}