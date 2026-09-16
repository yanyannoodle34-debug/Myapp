package com.example.myapplication.utils

import com.example.myapplication.models.ApiItem
import com.example.myapplication.models.GptProvider

object ApiConstants {

    val PUBLIC_APIS = listOf(
        ApiItem(
            id = "jsonplaceholder",
            name = "JSONPlaceholder",
            description = "Fake REST API for testing and prototyping",
            baseUrl = "https://jsonplaceholder.typicode.com",
            category = "Testing",
            icon = "📋",
            githubRepo = "typicode/jsonplaceholder",
            documentation = "https://jsonplaceholder.typicode.com"
        ),
        ApiItem(
            id = "pokeapi",
            name = "PokeAPI",
            description = "All the Pokemon data you'll ever need",
            baseUrl = "https://pokeapi.co/api/v2",
            category = "Entertainment",
            icon = "🎮",
            githubRepo = "PokeAPI/pokeapi",
            documentation = "https://pokeapi.co/docs/v2"
        ),
        ApiItem(
            id = "openweather",
            name = "OpenWeatherMap",
            description = "Weather data, forecasts, and historical data",
            baseUrl = "https://api.openweathermap.org/data/2.5",
            category = "Weather",
            icon = "🌤️",
            githubRepo = "openweathermap/openweathermap-api",
            documentation = "https://openweathermap.org/api"
        ),
        ApiItem(
            id = "restcountries",
            name = "REST Countries",
            description = "Information about countries from REST API",
            baseUrl = "https://restcountries.com/v3.1",
            category = "Data",
            icon = "🌍",
            githubRepo = "restcountries/restcountries",
            documentation = "https://restcountries.com"
        ),
        ApiItem(
            id = "dogapi",
            name = "Dog API",
            description = "All things dogs - breeds, images, and facts",
            baseUrl = "https://api.thedogapi.com/v1",
            category = "Animals",
            icon = "🐕",
            githubRepo = "thedogapi/thedogapi-android",
            documentation = "https://thedogapi.com"
        ),
        ApiItem(
            id = "catfacts",
            name = "Cat Facts",
            description = "Random cat facts for your amusement",
            baseUrl = "https://catfact.ninja",
            category = "Animals",
            icon = "🐱",
            githubRepo = "vsamford/catfacts-api",
            documentation = "https://catfact.ninja"
        ),
        ApiItem(
            id = "agify",
            name = "Agify.io",
            description = "Predict age based on a name",
            baseUrl = "https://api.agify.io",
            category = "Fun",
            icon = "🎂",
            githubRepo = "2tsumo/agify",
            documentation = "https://agify.io"
        ),
        ApiItem(
            id = "bored",
            name = "Bored API",
            description = "Generate random activities when you're bored",
            baseUrl = "https://bored-api.apphb.com",
            category = "Fun",
            icon = "🎯",
            githubRepo = "dump247/bored-api",
            documentation = "https://bored-api.apphb.com"
        ),
        ApiItem(
            id = "advice",
            name = "Advice Slip",
            description = "Get random advice slips",
            baseUrl = "https://api.adviceslip.com",
            category = "Fun",
            icon = "💡",
            githubRepo = "davemilligan/advice-slip-api",
            documentation = "https://api.adviceslip.com"
        ),
        ApiItem(
            id = "randomuser",
            name = "Random User",
            description = "Generate random user data",
            baseUrl = "https://randomuser.me/api",
            category = "Data",
            icon = "👤",
            githubRepo = "randomuser/randomuser-api",
            documentation = "https://randomuser.me/documentation"
        ),
        ApiItem(
            id = "quotable",
            name = "Quotable",
            description = "Random quotes from famous people",
            baseUrl = "https://api.quotable.io",
            category = "Fun",
            icon = "💬",
            githubRepo = "lukePeavey/quotable",
            documentation = "https://github.com/lukePeavey/quotable"
        ),
        ApiItem(
            id = "openbrewery",
            name = "Open Brewery DB",
            description = "Brewery data from around the world",
            baseUrl = "https://api.openbrewerydb.org/v1",
            category = "Food",
            icon = "🍺",
            githubRepo = "openbrewerydb/openbrewerydb",
            documentation = "https://www.openbrewerydb.org"
        )
    )

    val GPT_PROVIDERS = listOf(
        GptProvider(
            id = "openai",
            name = "OpenAI",
            baseUrl = "https://api.openai.com/v1",
            model = "gpt-3.5-turbo",
            apiKeyRequired = true,
            isFree = false,
            description = "GPT-3.5/GPT-4 models"
        ),
        GptProvider(
            id = "anthropic",
            name = "Anthropic",
            baseUrl = "https://api.anthropic.com/v1",
            model = "claude-3-haiku-20240307",
            apiKeyRequired = true,
            isFree = false,
            description = "Claude AI models"
        ),
        GptProvider(
            id = "google",
            name = "Google AI",
            baseUrl = "https://generativelanguage.googleapis.com/v1beta",
            model = "gemini-pro",
            apiKeyRequired = true,
            isFree = true,
            description = "Gemini AI models"
        ),
        GptProvider(
            id = "mistral",
            name = "Mistral AI",
            baseUrl = "https://api.mistral.ai/v1",
            model = "mistral-7b-instruct",
            apiKeyRequired = true,
            isFree = false,
            description = "Mistral open-source models"
        ),
        GptProvider(
            id = "groq",
            name = "Groq",
            baseUrl = "https://api.groq.com/openai/v1",
            model = "llama3-8b-8192",
            apiKeyRequired = true,
            isFree = true,
            description = "Fast Llama inference"
        ),
        GptProvider(
            id = "deepseek",
            name = "DeepSeek",
            baseUrl = "https://api.deepseek.com/v1",
            model = "deepseek-chat",
            apiKeyRequired = true,
            isFree = false,
            description = "DeepSeek V3 - fast & smart"
        ),
        GptProvider(
            id = "nevida",
            name = "Nevida AI",
            baseUrl = "https://api.nevida.ai/v1",
            model = "nevida-1",
            apiKeyRequired = true,
            isFree = true,
            description = "Nevida free AI API"
        ),
        GptProvider(
            id = "together",
            name = "Together AI",
            baseUrl = "https://api.together.xyz/v1",
            model = "meta-llama/Llama-3-8b-chat-hf",
            apiKeyRequired = true,
            isFree = true,
            description = "Open-source models"
        ),
        GptProvider(
            id = "huggingface",
            name = "HuggingFace",
            baseUrl = "https://api-inference.huggingface.co/models",
            model = "meta-llama/Llama-3-8b-instruct",
            apiKeyRequired = true,
            isFree = true,
            description = "Free inference API"
        ),
        GptProvider(
            id = "openrouter",
            name = "OpenRouter",
            baseUrl = "https://openrouter.ai/api/v1",
            model = "meta-llama/llama-3-8b-instruct",
            apiKeyRequired = true,
            isFree = false,
            description = "Multi-model router"
        ),
        GptProvider(
            id = "ollama",
            name = "Ollama (Local)",
            baseUrl = "http://localhost:11434/v1",
            model = "llama3",
            apiKeyRequired = false,
            isFree = true,
            description = "Local LLM (requires Ollama)"
        )
    )

    val CATEGORY_ICONS = mapOf(
        "Testing" to "📋",
        "Entertainment" to "🎮",
        "Weather" to "🌤️",
        "Data" to "📊",
        "Animals" to "🐾",
        "Fun" to "🎉",
        "Food" to "🍕",
        "Social" to "👥",
        "Finance" to "💰",
        "Health" to "🏥"
    )
}
