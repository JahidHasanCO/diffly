package dev.jahidhasanco.diffly.domain.repository

import dev.jahidhasanco.diffly.domain.model.DiffEntry

interface DiffRepository {
    fun calculateDiff(oldText: String, newText: String): List<DiffEntry>
}