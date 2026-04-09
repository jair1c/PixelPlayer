package com.theveloper.pixelplay.data.ai

import javax.inject.Inject
import javax.inject.Singleton

enum class AiSystemPromptType {
    PLAYLIST,
    METADATA,
    TAGGING,
    MOOD_ANALYSIS,
    PERSONA,
    GENERAL
}

@Singleton
class AiSystemPromptEngine @Inject constructor() {

    fun buildPrompt(basePersona: String, type: AiSystemPromptType, context: String = ""): String {
        val requirementLayer = when (type) {
            AiSystemPromptType.PLAYLIST -> """
                ---
                STRICT OUTPUT RULES:
                1. Your response MUST be ONLY a raw JSON array of song IDs.
                2. NO markdown code blocks (no ```json).
                3. NO conversational text, NO explanations.
                4. Example: ["id1", "id2", "id3"]
            """.trimIndent()

            AiSystemPromptType.METADATA -> """
                ---
                STRICT OUTPUT RULES:
                1. Your response MUST be ONLY a raw JSON object matching this schema: 
                   {"title": "...", "artist": "...", "album": "...", "genre": "..."}
                2. Fill in ONLY the requested fields, use null or empty string for others.
                3. NO markdown, NO conversational text.
            """.trimIndent()

            AiSystemPromptType.TAGGING -> """
                ---
                STRICT OUTPUT RULES:
                1. Provide a list of 5-8 descriptive tags (experimental, vibe-based).
                2. Return as a CSV string. No JSON, no markdown.
                3. Example: lo-fi, chill, nocturnal, rainy, study
            """.trimIndent()

            AiSystemPromptType.MOOD_ANALYSIS -> """
                ---
                STRICT OUTPUT RULES:
                1. Return a single word representing the primary mood.
                2. Then a 0-1 score for Energy, Valence, and Danceability.
                3. Format: Mood | Energy:0.X | Valence:0.X | Danceability:0.X
            """.trimIndent()

            AiSystemPromptType.PERSONA -> """
                ---
                STRICT OUTPUT RULES:
                1. Adopt the persona described in the context.
                2. Keep responses brief and relevant to the user's music taste.
            """.trimIndent()

            AiSystemPromptType.GENERAL -> ""
        }

        val contextLayer = if (context.isNotBlank()) {
            "--- USER_CONTEXT_START ---\n$context\n--- USER_CONTEXT_END ---"
        } else ""

        return """
            $basePersona
            
            $contextLayer
            
            $requirementLayer
        """.trimIndent()
    }
}
