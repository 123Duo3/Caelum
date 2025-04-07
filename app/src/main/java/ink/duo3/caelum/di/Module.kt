package ink.duo3.caelum.di

import ink.duo3.caelum.viewmodel.MainViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val module = module {
    singleOf(::MainViewModel)
}