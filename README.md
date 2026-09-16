# Android Development Environment

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
├── downloads/
│   └── Android/
│       └── backup/         # APK backup folder (auto-created)
├── MyApplication/
│   ├── app/                # Main app module
│   │   ├── build.gradle    # App-level build config
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/   # Kotlin/Java source files
│   │   │   │   ├── res/    # Resources (layouts, strings, etc.)
│   │   │   │   └── AndroidManifest.xml
│   │   │   └── test/       # Unit tests
│   │   └── ...
│   ├── build.gradle        # Project-level build config
│   ├── settings.gradle     # Project settings
│   └── gradle.properties   # Gradle properties
└── README.md               # This file
```

---

## APK Backup Location

**Default backup path:** `MApp/downloads/Android/backup/`

After every build, APKs are automatically copied to the backup folder:
- Release APKs → `downloads/Android/backup/app-release.apk`
- Debug APKs → `downloads/Android/backup/app-debug.apk`

### Manual Copy Commands
```bash
# Copy specific APK to backup
cp app/build/outputs/apk/release/*.apk ../downloads/Android/backup/

# List backed up APKs
ls -la ../downloads/Android/backup/
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
