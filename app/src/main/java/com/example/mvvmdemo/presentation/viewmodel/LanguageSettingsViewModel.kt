import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class LanguageSettingsViewModel @Inject constructor(
    private val localeManager: LocaleManager
) : ViewModel() {

    private val _currentLanguage = MutableStateFlow(localeManager.getLanguage())
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    fun setLanguage(language: String) {
        localeManager.setLanguage(language)
        _currentLanguage.value = language
    }
} 