package dev.jahidhasanco.diffly.di

import dev.jahidhasanco.diffly.data.repository.DiffRepositoryImpl
import dev.jahidhasanco.diffly.domain.repository.DiffRepository
import dev.jahidhasanco.diffly.domain.usecase.CalculateDiffUseCase
import dev.jahidhasanco.diffly.domain.util.CharLevelDiff
import dev.jahidhasanco.diffly.domain.util.MyersDiff

object AppModule {
    private val myersDiff = MyersDiff()
    private val repository: DiffRepository = DiffRepositoryImpl(myersDiff)
    val calculateDiffUseCase = CalculateDiffUseCase(repository)
}