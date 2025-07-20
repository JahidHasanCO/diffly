package dev.jahidhasanco.diffly.data.repository

import dev.jahidhasanco.diffly.domain.model.DiffEntry
import dev.jahidhasanco.diffly.domain.repository.DiffRepository
import dev.jahidhasanco.diffly.domain.util.MyersDiff

class DiffRepositoryImpl(
    private val myersDiff: MyersDiff
) : DiffRepository {
    override fun calculateDiff(
        oldText: String,
        newText: String
    ): List<DiffEntry> {
        val oldLines = oldText.lines()
        val newLines = newText.lines()
        return myersDiff.calculateDiff(oldLines, newLines)
    }
}