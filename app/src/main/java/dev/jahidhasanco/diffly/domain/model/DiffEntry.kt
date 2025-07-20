package dev.jahidhasanco.diffly.domain.model

enum class DiffType { ADDED, DELETED, UNCHANGED }

data class DiffEntry(
    val oldLine: String?,
    val newLine: String?,
    val type: DiffType
)