package com.example.todoir

import android.content.Context
import java.util.Locale

object LocaleHelper {

    fun updateLocale(context: Context, langCode: String): Context {
        val locale = Locale(langCode)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }
}
