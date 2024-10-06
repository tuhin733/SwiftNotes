package com.naim.swiftnotes.core.constant

import android.content.Context
import com.naim.swiftnotes.R

object SupportConst {

    const val TERMS_EFFECTIVE_DATE = "04/10/2024"

    /**
     * Provides a map of supporters categorized by their roles using localized strings.
     */
    fun getSupportersMap(context: Context): Map<String, List<String>> {
        return mapOf(
            context.getString(R.string.donators) to listOf("Aarav", "Aditi", "Akshay", "Ananya", "Arjun", "Bhavya", "Chaitanya"),
            context.getString(R.string.contributors) to listOf("Deepika", "Dev", "Gauri", "Harsh"),
            context.getString(R.string.translators) to listOf("Ishaan", "Kavya", "Kritika", "Lakshya", "Meera", "Neha", "Omkar", "Pranav", "Rhea", "Saanvi", "Shivam", "Tanvi", "Ved", "Yash", "Zara")
        )
    }
}