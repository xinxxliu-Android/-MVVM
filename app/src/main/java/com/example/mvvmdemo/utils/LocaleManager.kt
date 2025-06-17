import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.Locale
import javax.inject.Inject

class LocaleManager @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val PREF_NAME = "language_pref"
        private const val KEY_LANGUAGE = "language"
        
        const val LANGUAGE_ENGLISH = "en"
        const val LANGUAGE_CHINESE = "zh"
    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, LANGUAGE_CHINESE) ?: LANGUAGE_CHINESE
    }

    fun setLanguage(language: String) {
        prefs.edit().putString(KEY_LANGUAGE, language).apply()
    }

    fun getLocale(): Locale {
        return when (getLanguage()) {
            LANGUAGE_ENGLISH -> Locale.ENGLISH
            else -> Locale.CHINESE
        }
    }

    fun updateConfiguration(config: Configuration) {
        config.setLocale(getLocale())
        context.createConfigurationContext(config)
    }

    fun updateResources(context: Context): Context {
        val locale = getLocale()
        Locale.setDefault(locale)
        
        val config = context.resources.configuration
        config.setLocale(locale)
        
        return context.createConfigurationContext(config)
    }
} 