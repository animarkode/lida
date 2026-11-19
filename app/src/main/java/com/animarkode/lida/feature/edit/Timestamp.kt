package com.animarkode.lida.feature.edit

private val TIMESTAMP = Regex("""^\d{1,2}:[0-5]\d(:[0-5]\d)?$""")

/** Blank is allowed (no timestamp); otherwise `M:SS`, `MM:SS` or `H:MM:SS`. */
fun isValidTimestamp(input: String): Boolean = input.isEmpty() || TIMESTAMP.matches(input)
