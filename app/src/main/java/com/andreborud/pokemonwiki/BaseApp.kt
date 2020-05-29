package com.andreborud.pokemonwiki

import android.app.Application
import com.andreborud.pokemonwiki.di.apiModule
import com.andreborud.pokemonwiki.di.pokemonDetailsVMModule
import com.andreborud.pokemonwiki.di.pokemonListVMModule
import com.ww.roxie.Roxie
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Roxie
        Roxie.enableLogging(object : Roxie.Logger {
            override fun log(msg: String) {
                Timber.tag("Roxie").d(msg)
            }
        })

        startKoin {
            androidLogger()
            // Android context
            androidContext(this@BaseApp)
            // modules
            modules(listOf(pokemonListVMModule, pokemonDetailsVMModule, apiModule))
        }
    }
}