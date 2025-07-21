package dev.jahidhasanco.diffly.di

import android.content.Context
import dev.jahidhasanco.diffly.data.local.PrefsManager
import dev.jahidhasanco.diffly.data.repository.DiffRepositoryImpl
import dev.jahidhasanco.diffly.domain.repository.DiffRepository
import dev.jahidhasanco.diffly.domain.usecase.CalculateDiffUseCase
import dev.jahidhasanco.diffly.domain.util.MyersDiff

object AppModule {

    private lateinit var _prefsManager: PrefsManager
    val prefsManager: PrefsManager
        get() = _prefsManager

    fun init(context: Context) {
        _prefsManager = PrefsManager(context)
    }

    private val myersDiff = MyersDiff()
    private val repository: DiffRepository = DiffRepositoryImpl(myersDiff)
    val calculateDiffUseCase = CalculateDiffUseCase(repository)

}