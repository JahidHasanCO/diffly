package dev.jahidhasanco.diffly.domain.model

enum class DiffType {UNCHANGED, ADDED, DELETED, CHANGED }

data class DiffEntry(
    val oldLine: String?,
    val newLine: String?,
    val type: DiffType,
    val charDiffs: List<CharDiff>? = null
)