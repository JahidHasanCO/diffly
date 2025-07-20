package dev.jahidhasanco.diffly.domain.model

enum class CharDiffType {
    UNCHANGED, INSERTED, DELETED
}

data class CharDiff(
    val char: Char,
    val type: CharDiffType
)
