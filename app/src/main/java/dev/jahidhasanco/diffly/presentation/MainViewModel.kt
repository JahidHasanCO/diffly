package dev.jahidhasanco.diffly.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jahidhasanco.diffly.domain.model.DiffEntry
import dev.jahidhasanco.diffly.domain.usecase.CalculateDiffUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val calculateDiffUseCase: CalculateDiffUseCase
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
