package dev.jahidhasanco.diffly.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jahidhasanco.diffly.domain.model.DiffEntry
import dev.jahidhasanco.diffly.domain.usecase.CalculateDiffUseCase
import dev.jahidhasanco.diffly.navigation.NavigationRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DiffCheckerViewModel(
    private val calculateDiffUseCase: CalculateDiffUseCase,
    private val router: NavigationRouter
) : ViewModel() {

    private val _diffResult = MutableStateFlow<List<DiffEntry>>(emptyList())
    val diffResult: StateFlow<List<DiffEntry>> = _diffResult

    fun calculateDiff(oldText: String, newText: String) {
        viewModelScope.launch {
            val result = calculateDiffUseCase(oldText, newText)
            Log.d("DiffChecker", "Diff calculated: $result")
            _diffResult.value = result
        }
    }
}