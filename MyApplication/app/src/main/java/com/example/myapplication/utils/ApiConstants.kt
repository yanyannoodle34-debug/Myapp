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
            documentation = "https://jsonplaceholder.typicode.com",
            tags = listOf("test", "fake", "rest", "prototype", "sample")
        ),
        ApiItem(
            id = "pokeapi",
            name = "PokeAPI",
            description = "All the Pokemon data you'll ever need",
            baseUrl = "https://pokeapi.co/api/v2",
            category = "Entertainment",
            icon = "🎮",
            githubRepo = "PokeAPI/pokeapi",
            documentation = "https://pokeapi.co/docs/v2",
            tags = listOf("pokemon", "game", "data", "fun")
        ),
        ApiItem(
            id = "openweather",
            name = "OpenWeatherMap",
            description = "Weather data, forecasts, and historical data",
            baseUrl = "https://api.openweathermap.org/data/2.5",
            category = "Weather",
            icon = "🌤️",
            githubRepo = "openweathermap/openweathermap-api",
            documentation = "https://openweathermap.org/api",
            tags = listOf("weather", "forecast", "temperature", "climate")
        ),
        ApiItem(
            id = "restcountries",
            name = "REST Countries",
            description = "Information about countries from REST API",
            baseUrl = "https://restcountries.com/v3.1",
            category = "Data",
            icon = "🌍",
            githubRepo = "restcountries/restcountries",
            documentation = "https://restcountries.com",
            tags = listOf("country", "geography", "population", "currency")
        ),
        ApiItem(
            id = "dogapi",
            name = "Dog API",
            description = "All things dogs - breeds, images, and facts",
            baseUrl = "https://api.thedogapi.com/v1",
            category = "Animals",
            icon = "🐕",
            githubRepo = "thedogapi/thedogapi-android",
            documentation = "https://thedogapi.com",
            tags = listOf("dog", "pet", "animal", "breed", "image")
        ),
        ApiItem(
            id = "catfacts",
            name = "Cat Facts",
            description = "Random cat facts for your amusement",
            baseUrl = "https://catfact.ninja",
            category = "Animals",
            icon = "🐱",
            githubRepo = "vsamford/catfacts-api",
            documentation = "https://catfact.ninja",
            tags = listOf("cat", "pet", "animal", "fun", "fact")
        ),
        ApiItem(
            id = "agify",
            name = "Agify.io",
            description = "Predict age based on a name",
            baseUrl = "https://api.agify.io",
            category = "Fun",
            icon = "🎂",
            githubRepo = "2tsumo/agify",
            documentation = "https://agify.io",
            tags = listOf("age", "predict", "name", "fun", "ai")
        ),
        ApiItem(
            id = "bored",
            name = "Bored API",
            description = "Generate random activities when you're bored",
            baseUrl = "https://bored-api.apphb.com",
            category = "Fun",
            icon = "🎯",
            githubRepo = "dump247/bored-api",
            documentation = "https://bored-api.apphb.com",
            tags = listOf("activity", "random", "fun", "suggestion")
        ),
        ApiItem(
            id = "advice",
            name = "Advice Slip",
            description = "Get random advice slips",
            baseUrl = "https://api.adviceslip.com",
            category = "Fun",
            icon = "💡",
            githubRepo = "davemilligan/advice-slip-api",
            documentation = "https://api.adviceslip.com",
            tags = listOf("advice", "random", "quote", "wisdom")
        ),
        ApiItem(
            id = "randomuser",
            name = "Random User",
            description = "Generate random user data",
            baseUrl = "https://randomuser.me/api",
            category = "Data",
            icon = "👤",
            githubRepo = "randomuser/randomuser-api",
            documentation = "https://randomuser.me/documentation",
            tags = listOf("user", "profile", "fake", "generator", "data")
        ),
        ApiItem(
            id = "quotable",
            name = "Quotable",
            description = "Random quotes from famous people",
            baseUrl = "https://api.quotable.io",
            category = "Fun",
            icon = "💬",
            githubRepo = "lukePeavey/quotable",
            documentation = "https://github.com/lukePeavey/quotable",
            tags = listOf("quote", "inspiration", "famous", "saying")
        ),
        ApiItem(
            id = "openbrewery",
            name = "Open Brewery DB",
            description = "Brewery data from around the world",
            baseUrl = "https://api.openbrewerydb.org/v1",
            category = "Food",
            icon = "🍺",
            githubRepo = "openbrewerydb/openbrewerydb",
            documentation = "https://www.openbrewerydb.org",
            tags = listOf("brewery", "beer", "drink", "location")
        ),
        ApiItem(
            id = "github",
            name = "GitHub API",
            description = "GitHub REST API for users, repos, and more",
            baseUrl = "https://api.github.com",
            category = "Developer",
            icon = "💻",
            githubRepo = "github/rest-api-description",
            documentation = "https://docs.github.com/en/rest",
            tags = listOf("github", "code", "developer", "repo", "git")
        ),
        ApiItem(
            id = "jsonbin",
            name = "JSONBin.io",
            description = "Free JSON storage service",
            baseUrl = "https://api.jsonbin.io/v3",
            category = "Storage",
            icon = "📦",
            githubRepo = "jsonbin-io/jsonbin.io",
            documentation = "https://jsonbin.io",
            tags = listOf("json", "storage", "database", "save")
        ),
        ApiItem(
            id = "httpbin",
            name = "HTTPBin",
            description = "HTTP Request & Response Testing Service",
            baseUrl = "https://httpbin.org",
            category = "Testing",
            icon = "🔗",
            githubRepo = "postmanlabs/httpbin",
            documentation = "https://httpbin.org",
            tags = listOf("http", "test", "request", "response", "header")
        ),
        ApiItem(
            id = "fakestore",
            name = "Fake Store API",
            description = "Fake store for e-commerce testing",
            baseUrl = "https://fakestoreapi.com",
            category = "E-commerce",
            icon = "🛒",
            githubRepo = "keikaavousi/fake-store-api",
            documentation = "https://fakestoreapi.com",
            tags = listOf("shop", "product", "cart", "ecommerce", "store")
        ),
        ApiItem(
            id = "dummyjson",
            name = "DummyJSON",
            description = "Dummy data for testing - users, posts, todos",
            baseUrl = "https://dummyjson.com",
            category = "Testing",
            icon = "📝",
            githubRepo = "Ovi/DummyJSON",
            documentation = "https://dummyjson.com",
            tags = listOf("dummy", "test", "user", "post", "todo", "sample")
        ),
        ApiItem(
            id = "reqres",
            name = "ReqRes",
            description = "Hosted REST API for testing",
            baseUrl = "https://reqres.in/api",
            category = "Testing",
            icon = "🔄",
            githubRepo = "benifriedman/reqres",
            documentation = "https://reqres.in",
            tags = listOf("rest", "test", "user", "api", "hosted")
        ),
        ApiItem(
            id = "viacep",
            name = "ViaCEP",
            description = "Brazilian postal code API",
            baseUrl = "https://viacep.com.br/ws",
            category = "Location",
            icon = "📮",
            githubRepo = "brasilapi/viacep",
            documentation = "https://viacep.com.br",
            tags = listOf("cep", "address", "brazil", "postal", "location")
        ),
        ApiItem(
            id = "opencagedata",
            name = "OpenCage Geocoder",
            description = "Forward and reverse geocoding API",
            baseUrl = "https://api.opencagedata.com/geocode/v1",
            category = "Location",
            icon = "📍",
            githubRepo = "OpenCageData/geocoding-api",
            documentation = "https://opencagedata.com/api",
            tags = listOf("geocode", "map", "location", "address", "coordinate")
        ),
        ApiItem(
            id = "exchangerate",
            name = "ExchangeRate API",
            description = "Free currency exchange rates",
            baseUrl = "https://api.exchangerate-api.com/v4",
            category = "Finance",
            icon = "💱",
            githubRepo = "ExchangeRate-API/website",
            documentation = "https://www.exchangerate-api.com",
            tags = listOf("currency", "exchange", "money", "rate", "finance")
        ),
        ApiItem(
            id = "coingecko",
            name = "CoinGecko",
            description = "Cryptocurrency data API",
            baseUrl = "https://api.coingecko.com/api/v3",
            category = "Finance",
            icon = "🪙",
            githubRepo = "coingecko/api-documentation",
            documentation = "https://docs.coingecko.com",
            tags = listOf("crypto", "bitcoin", "coin", "token", "price")
        ),
        ApiItem(
            id = "openlibrary",
            name = "Open Library",
            description = "Open-source book database API",
            baseUrl = "https://openlibrary.org/api",
            category = "Education",
            icon = "📚",
            githubRepo = "openlibrary/openlibrary",
            documentation = "https://openlibrary.org/developers/api",
            tags = listOf("book", "library", "read", "author", "isbn")
        ),
        ApiItem(
            id = "nasa",
            name = "NASA API",
            description = "NASA open data and space imagery",
            baseUrl = "https://api.nasa.gov",
            category = "Science",
            icon = "🚀",
            githubRepo = "nasa/api-docs",
            documentation = "https://api.nasa.gov",
            tags = listOf("space", "nasa", "apod", "mars", "photo")
        ),
        ApiItem(
            id = "dictionary",
            name = "Dictionary API",
            description = "Free English dictionary API",
            baseUrl = "https://api.dictionaryapi.dev/api/v2",
            category = "Education",
            icon = "📖",
            githubRepo = "freeapis/freeapis",
            documentation = "https://dictionaryapi.dev",
            tags = listOf("word", "definition", "meaning", "english")
        ),
        ApiItem(
            id = "chucknorris",
            name = "Chuck Norris API",
            description = "Random Chuck Norris jokes",
            baseUrl = "https://api.chucknorris.io",
            category = "Fun",
            icon = "😄",
            githubRepo = "chucknorrisio/chucknorris-api",
            documentation = "https://api.chucknorris.io",
            tags = listOf("joke", "random", "fun", "humor")
        ),
        ApiItem(
            id = "dragonball",
            name = "Dragon Ball API",
            description = "Dragon Ball character data",
            baseUrl = "https://dragonball-api.com/api",
            category = "Entertainment",
            icon = "🐉",
            githubRepo = "public-apis/public-apis",
            documentation = "https://dragonball-api.com",
            tags = listOf("anime", "manga", "character", "dbz")
        ),
        ApiItem(
            id = "rickandmorty",
            name = "Rick and Morty API",
            description = "All Rick and Morty data",
            baseUrl = "https://rickandmortyapi.com/api",
            category = "Entertainment",
            icon = "🧪",
            githubRepo = "afuh/rick-and-morty-api",
            documentation = "https://rickandmortyapi.com",
            tags = listOf("rick", "morty", "character", "episode", "location")
        ),
        ApiItem(
            id = "studioGhibli",
            name = "Studio Ghibli API",
            description = "Studio Ghibli films data",
            baseUrl = "https://ghibliapi.vercel.app",
            category = "Entertainment",
            icon = "🎬",
            githubRepo = "ghibliapi/ghibli-api",
            documentation = "https://ghibliapi.vercel.app",
            tags = listOf("film", "movie", "anime", "ghibli")
        ),
        ApiItem(
            id = "opendota",
            name = "OpenDota",
            description = "Dota 2 statistics API",
            baseUrl = "https://api.opendota.com/api",
            category = "Gaming",
            icon = "⚔️",
            githubRepo = "odota/core",
            documentation = "https://docs.opendota.com",
            tags = listOf("dota", "game", "match", "hero", "stats")
        ),
        ApiItem(
            id = "valorant",
            name = "Valorant API",
            description = "Valorant game data",
            baseUrl = "https://valorant-api.com/v1",
            category = "Gaming",
            icon = "🎯",
            githubRepo = "valorant-api/valorant-api",
            documentation = "https://valorant-api.com",
            tags = listOf("valorant", "game", "agent", "weapon", "skin")
        ),
        ApiItem(
            id = "usgs_quakes",
            name = "USGS Earthquakes",
            description = "Live global earthquake data (latest M1+ quake)",
            baseUrl = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=1&orderby=time",
            category = "Disasters",
            icon = "🌋",
            githubRepo = "usgs/earthquake-eventpages",
            documentation = "https://earthquake.usgs.gov/fdsnws/event/1/",
            tags = listOf("earthquake", "usgs", "seismic", "disaster", "quake", "magnitude")
        ),
        ApiItem(
            id = "usgs_quakes_7days",
            name = "USGS Quakes 4.5+ (7d)",
            description = "Significant earthquakes worldwide, past 7 days",
            baseUrl = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.geojson",
            category = "Disasters",
            icon = "🌍",
            githubRepo = "usgs/earthquake-eventpages",
            documentation = "https://earthquake.usgs.gov/earthquakes/feed/",
            tags = listOf("earthquake", "usgs", "seismic", "disaster", "geojson", "feed")
        ),
        ApiItem(
            id = "nasa_eonet",
            name = "NASA EONET",
            description = "Live natural events: wildfires, storms, volcanoes",
            baseUrl = "https://eonet.gsfc.nasa.gov/api/v3/events?limit=5",
            category = "Disasters",
            icon = "🛰️",
            githubRepo = "nasa/eonet",
            documentation = "https://eonet.gsfc.nasa.gov/docs/v3",
            tags = listOf("nasa", "wildfire", "storm", "volcano", "disaster", "event")
        ),
        ApiItem(
            id = "noaa_alerts",
            name = "NOAA Weather Alerts",
            description = "Active US weather alerts and warnings",
            baseUrl = "https://api.weather.gov/alerts/active?limit=1",
            category = "Disasters",
            icon = "⛈️",
            githubRepo = "weather-gov/api",
            documentation = "https://www.weather.gov/documentation/services-web-api",
            tags = listOf("noaa", "weather", "alert", "warning", "storm", "disaster")
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
