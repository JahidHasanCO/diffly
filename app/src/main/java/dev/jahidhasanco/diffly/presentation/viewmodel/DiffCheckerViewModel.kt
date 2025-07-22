package dev.jahidhasanco.diffly.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakaztahir.codeeditor.highlight.model.CodeLang
import dev.jahidhasanco.diffly.di.AppModule
import dev.jahidhasanco.diffly.domain.model.DiffEntry
import dev.jahidhasanco.diffly.domain.model.DiffViewType
import dev.jahidhasanco.diffly.domain.usecase.CalculateDiffUseCase
import dev.jahidhasanco.diffly.navigation.NavigationRouter
import dev.jahidhasanco.diffly.presentation.theme.MonokaiDeepTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DiffCheckerViewModel(
    private val calculateDiffUseCase: CalculateDiffUseCase,
    private val router: NavigationRouter
) : ViewModel() {

    private val prefs = AppModule.prefsManager

    private val _diffResult = MutableStateFlow<List<DiffEntry>>(emptyList())
    val diffResult: StateFlow<List<DiffEntry>> = _diffResult

    private val _selectedLanguage = MutableStateFlow(CodeLang.Basic)
    val selectedLanguage: StateFlow<CodeLang> = _selectedLanguage

    val theme = MonokaiDeepTheme()

    private val _oldText = MutableStateFlow("")
    val oldText: StateFlow<String> = _oldText


    private val _newText = MutableStateFlow("")
    val newText: StateFlow<String> = _newText

    private val _realTimeDiff = MutableStateFlow(prefs.realTimeDiff)
    val realTimeDiff: StateFlow<Boolean> = _realTimeDiff

    private val _expanded = MutableStateFlow(false)
    val expanded: StateFlow<Boolean> = _expanded

    private val _selectedViewType = MutableStateFlow(
        DiffViewType.valueOf(
            prefs.selectedViewType ?: DiffViewType.TWO_SIDE.name
        )
    )
    val selectedViewType: StateFlow<DiffViewType> = _selectedViewType

    fun updateOldText(value: String) {
        _oldText.value = value
        if (_realTimeDiff.value) calculateDiff(_oldText.value, _newText.value)
    }

    fun updateNewText(value: String) {
        _newText.value = value
        if (_realTimeDiff.value) calculateDiff(_oldText.value, _newText.value)
    }

    fun setRealTimeDiff(value: Boolean) {
        _realTimeDiff.value = value
        prefs.realTimeDiff = value
        if (value) calculateDiff(_oldText.value, _newText.value)
    }

    fun setExpanded(value: Boolean) {
        _expanded.value = value
    }

    fun selectViewType(type: DiffViewType) {
        _selectedViewType.value = type
        prefs.selectedViewType = type.name
        _expanded.value = false
    }

    fun swapTexts() {
        val temp = _oldText.value
        _oldText.value = _newText.value
        _newText.value = temp
        if (_realTimeDiff.value) calculateDiff(_oldText.value, _newText.value)
    }


    fun selectLanguage(language: CodeLang) {
        _selectedLanguage.value = language
    }

    fun calculateDiff(oldText: String, newText: String) {
        viewModelScope.launch {
            val result = calculateDiffUseCase(oldText, newText)
            Log.d("DiffChecker", "Diff calculated: $result")
            _diffResult.value = result
        }
    }

    fun navigateToDiffViewer() {
        router.navigateToDiffViewer()
    }

    fun goBack() {
        router.goBack()
    }
}