package dev.jahidhasanco.diffly.domain.usecase

import dev.jahidhasanco.diffly.domain.model.DiffEntry
import dev.jahidhasanco.diffly.domain.repository.DiffRepository

class CalculateDiffUseCase(
    private val repository: DiffRepository
) {
    operator fun invoke(oldText: String, newText: String): List<DiffEntry> {
        return repository.calculateDiff(oldText, newText)
    }
}