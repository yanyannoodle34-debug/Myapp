# AI for APIs (AFA) — *AI in APIs*

## Version History

| Version | Code | Date | Highlights |
|---------|------|------|------------|
| 1.1 | 2 | 2026-09-17 | GitHub repo search + rate limit, Settings screen, dark cool theme, copy/share response, ad on refresh, VPN-proof ads |
| 1.0 | 1 | 2026-09-16 | 31 APIs dashboard, 11 GPT providers, splash + periodic ads, auto APK backup |

## Quick Reference

### What Each Tool Does

| Tool | Purpose | When to Use |
|------|---------|-------------|
| **Java/JDK** | Compile Kotlin/Java code | Always needed for Android builds |
| **Android SDK** | Android libraries, build tools, platform APIs | Required for Android app compilation |
| **Gradle** | Build system, dependency management | Runs build tasks (`./gradlew assembleDebug`) |
| **Kotlin** | Programming language for Android apps | Writing app code |
| **ADB** | Android Debug Bridge - device communication | Debugging on physical device/emulator |

---

## Skills for AI Agents/Models

Use these skills when working on this project:

| Skill | When to Use | What It Does |
|-------|-------------|--------------|
| **kotlin-specialist** | Writing Kotlin code, coroutines, Android patterns | Kotlin best practices, Flow API, Android Kotlin |
| **flutter-expert** | Building cross-platform UI with Flutter | Widget development, Riverpod/Bloc, GoRouter |
| **react-native-expert** | Cross-platform with React Native | Navigation, native modules, FlatList |
| **nextjs-developer** | Web dashboard for app analytics | App Router, server components, API routes |
| **devops-engineer** | CI/CD, Docker, deployment | Build automation, GitHub Actions, containerization |
| **code-reviewer** | Reviewing pull requests | Bug detection, security, code quality |
| **test-master** | Writing unit/E2E tests | Test strategy, mocking, coverage |
| **debugging-wizard** | Fixing build/runtime errors | Error tracing, root cause analysis |
| **security-reviewer** | App security audit | OWASP, secrets scanning, auth review |

### Quick Skill Commands
```
# For Kotlin/Android development
Use skill: kotlin-specialist

# For Flutter cross-platform
Use skill: flutter-expert

# For React Native
Use skill: react-native-expert

# For CI/CD setup
Use skill: devops-engineer
```

---

## Environment Paths

```bash
# Add to ~/.bashrc
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export ANDROID_HOME=/opt/android-sdk
export KOTLIN_HOME=/opt/kotlinc
export PATH=$JAVA_HOME/bin:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$KOTLIN_HOME/bin:$PATH
```

---

## Installed Versions

| Component | Version | Path |
|-----------|---------|------|
| OpenJDK | 17.0.18 | `/usr/lib/jvm/java-17-openjdk` |
| Android SDK | Latest | `/opt/android-sdk` |
| Build Tools | 34.0.0 | `platforms/android-34` |
| Platform | Android 34 | `platforms;android-34` |
| Gradle | 8.5 | System installed |
| Kotlin | 1.9.22 | `/opt/kotlinc` |

---

## Common Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Clean build
./gradlew clean

# Install on device (if ADB available)
./gradlew installDebug

# Run Kotlin compiler
kotlinc myfile.kt

# Check Android SDK packages
sdkmanager --list_installed
```

---

## Project Structure

```
MApp/
├── MyApplication/                # Android project
│   ├── app/                      # Main app module
│   │   ├── build.gradle          # App-level build config
│   │   ├── src/main/
│   │   │   ├── java/             # Kotlin/Java source files
│   │   │   │   ├── DashboardActivity.kt
│   │   │   │   ├── ApiDetailActivity.kt
│   │   │   │   ├── adapters/
│   │   │   │   ├── models/
│   │   │   │   ├── services/
│   │   │   │   ├── viewmodels/
│   │   │   │   └── utils/
│   │   │   ├── res/              # Resources (layouts, strings, etc.)
│   │   │   └── AndroidManifest.xml
│   │   └── build/outputs/apk/   # Built APKs
│   ├── build.gradle              # Project-level build config
│   ├── settings.gradle
│   └── gradle.properties
├── downloads/                    # Local backup
│   └── Android/backup/
├── .git/                         # Git repository
├── .gitignore
├── README.md                     # This file
└── git_acc.json                  # GitHub credentials (not in git)

/sdcard/shared/downloads/Android/ # External backup
├── app-debug.apk
├── app-release.apk
└── code_backup/                  # Source code backup
```

---

## Backup Summary

| What | Where | Auto? |
|------|-------|-------|
| Debug APK | `/sdcard/shared/downloads/Android/app-debug.apk` | ✅ Yes |
| Release APK | `/sdcard/shared/downloads/Android/app-release.apk` | ✅ Yes |
| Source Code | `/sdcard/shared/downloads/Android/code_backup/` | ✅ Yes |
| GitHub | https://github.com/yanyannoodle34-debug/Myapp.git | Manual |
| Local Copy | `MApp/downloads/Android/backup/` | ✅ Yes |

---

## App Features (API Dashboard)

**31 Public APIs** from GitHub with live status checking.

### Keyword Search
Search bar filters by multiple keywords (space/comma separated) across:
- API name, description, category
- Search **tags** (e.g. `crypto`, `weather`, `anime`, `game`)
- GitHub repo name and base URL

Examples: `cat dog` → Cat Facts + Dog API · `test rest` → JSONPlaceholder, HTTPBin, ReqRes

### GPT Providers (11)
| Provider | Free | Model |
|----------|------|-------|
| OpenAI | ❌ | gpt-3.5-turbo |
| Anthropic | ❌ | claude-3-haiku |
| Google AI | ✅ | gemini-pro |
| Mistral | ❌ | mistral-7b |
| Groq | ✅ | llama3-8b |
| DeepSeek | ❌ | deepseek-chat |
| Nevida AI | ✅ | nevida-1 |
| Together AI | ✅ | llama-3-8b |
| HuggingFace | ✅ | llama-3-8b |
| OpenRouter | ❌ | llama-3-8b |
| Ollama (Local) | ✅ | llama3 (no key) |

### Ads
| When | Duration | Skippable | VPN blocked? |
|------|----------|-----------|--------------|
| App start (splash) | 5s countdown | Auto-continues | Auto-retries once, then Browser button |
| Every 5 min of use | 5s | Skip button | Same fallback |
| Refresh / Check All | 5s, then checks run | Skip button | Same fallback |

Ad URL loaded in WebView with reload-on-error + open-in-browser fallback.

### GitHub Mode (💻 GitHub toggle)
- Live repo search via `api.github.com/search` (press 🔍, min 2 chars)
- Rate-limit banner: `remaining/limit` (60/hr anonymous, **5000/hr with token**)
- Token configured in **Settings** (gear icon) — stored in SharedPreferences
- Repos map into the same list: tap ▶ to test, tap card for details

### Settings (gear icon / menu)
- GitHub personal token + **Check Limit** button
- Per-provider GPT API keys (saved on device only, never in git)

### Response Viewer
- Scrollable monospace response card (260dp, always-visible scrollbar)
- **Copy** button → clipboard, **Share** button → share sheet

---

## APK & Code Backup

**APK Backup Path:** `/sdcard/shared/downloads/Android/`
**Code Backup Path:** `/sdcard/shared/downloads/Android/code_backup/`

After every build, APKs and source code are automatically backed up:

```
/sdcard/shared/downloads/Android/
├── app-debug.apk              # Debug APK
├── app-release.apk            # Release APK
└── code_backup/               # Source code backup
    ├── MyApplication/         # Kotlin/Java files
    ├── downloads/             # Other files
    └── ...
```

### Manual Backup Commands
```bash
# Copy APK to backup
cp app/build/outputs/apk/debug/*.apk /sdcard/shared/downloads/Android/

# List backed up files
ls -la /sdcard/shared/downloads/Android/

# View code backup
ls -la /sdcard/shared/downloads/Android/code_backup/
```

### Build & Auto Backup
```bash
# Debug build + backup APK + backup code
gradle assembleDebug

# Release build + backup
gradle assembleRelease
```

---

## GitHub Backup

**Repository:** https://github.com/yanyannoodle34-debug/Myapp.git

### Git Commands
```bash
# Save changes
git add .
git commit -m "description of changes"
git push

# Pull latest changes
git pull origin main

# Check status
git status

# View commit history
git log --oneline -10
```

### Auto Backup Script
```bash
# Quick save and push
git add . && git commit -m "update $(date +%Y-%m-%d)" && git push
```

### Credentials
- Token stored in: `git_acc.json` (keep secure, don't share)
- Remote: `origin` → `https://github.com/yanyannoodle34-debug/Myapp.git`

---

## Adding New Components

### Install Additional SDK Packages
```bash
sdkmanager "platforms;android-35"
sdkmanager "build-tools;35.0.0"
sdkmanager "extras;android;m2repository"
```

### Install Emulator (on compatible systems)
```bash
sdkmanager "emulator"
sdkmanager "system-images;android-34;google_apis;x86_64"
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| `JAVA_HOME not set` | Run `export JAVA_HOME=/usr/lib/jvm/java-17-openjdk` |
| `SDK not found` | Run `export ANDROID_HOME=/opt/android-sdk` |
| `ADB not working` | Expected on Alpine Linux (musl). Use `sdkmanager` for builds only |
| `Gradle build fails` | Run `./gradlew clean` then retry |
| `Kotlin not found` | Run `export PATH=/opt/kotlinc/bin:$PATH` |

---

## Notes

- **ADB limitation**: ADB binary requires glibc, incompatible with Alpine Linux. Use for device communication only.
- **Build works**: Java, Gradle, Android SDK, and Kotlin all work for building APKs.
- **Agent changes**: When switching AI models/agents, reference this file to restore environment.
- **Skills**: Use `skill: <skill-name>` to load specialized knowledge for specific tasks.

---

## Agent Handoff Checklist

When switching to a new AI model/agent, ensure it knows:

1. [ ] Read this README.md first
2. [ ] Environment is set up (JAVA_HOME, ANDROID_HOME, KOTLIN_HOME)
3. [ ] Build command: `./gradlew assembleRelease`
4. [ ] APK backup: `downloads/Android/backup/`
5. [ ] Available skills: kotlin-specialist, devops-engineer, test-master, etc.

---

*Last updated: September 16, 2026*
