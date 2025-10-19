# Renovate Migration for lsd-cucumber

This document outlines the migration from Dependabot to Renovate for better dependency management.

## 🎯 Why Renovate?

- **Native test vs production dependency separation** (no complex workarounds needed)
- **Better Gradle scope awareness** (understands `testImplementation` vs `implementation`)
- **More flexible configuration** and grouping options
- **Simplified maintenance** (no validation scripts required)

## 📊 Configuration Mapping

### Preserved Groupings

✅ **lsd-dependencies**: `io.github.lsd-consulting:**`
✅ **cucumber-dependencies**: `io.cucumber:**`
✅ **utility-dependencies**: `com.j2html:**`
✅ **kotlin-dependencies**: `org.jetbrains.kotlin:**`, `org.jetbrains.dokka:**`
✅ **testing-dependencies**: All test-scoped dependencies (auto-detected)
✅ **github-actions**: Weekly schedule, `ci:` prefix

### Preserved Schedules

- **Production dependencies**: Daily (any time)
- **Test dependencies**: Daily (any time)
- **GitHub Actions**: Weekly (`before 4am on monday`)

## 🚀 Migration Steps

### 1. Enable Renovate
- Install Renovate GitHub App on the repository
- The `renovate.json` config will be automatically detected

### 2. Test Period (Recommended)
Run both systems in parallel initially:
- Keep Dependabot config temporarily
- Let Renovate create a few PRs to verify behavior
- Compare PR quality and accuracy

### 3. Disable Dependabot
Once satisfied with Renovate:
```bash
# Remove Dependabot config
rm .github/dependabot.yml

# Commit the change
git add .github/dependabot.yml renovate.json
git commit -m "feat: migrate from Dependabot to Renovate for better dependency management"
git push
```

## 🔍 Verification

After migration, verify:
- ✅ Test dependencies get `test:` prefix (no semantic release)
- ✅ Production dependencies get `fix:` prefix (triggers semantic release)
- ✅ Dependencies are properly grouped (lsd, cucumber, utility, kotlin)
- ✅ GitHub Actions get `ci:` prefix
- ✅ Schedules are respected

## 🎛️ Configuration Benefits

### What's Better with Renovate

1. **No overlapping directories error** - Renovate doesn't have this limitation
2. **No complex ignore lists** - Uses natural exclusion patterns
3. **Better test detection** - Automatically detects test scope dependencies
4. **Smarter grouping** - Can group by manager, dep type, or custom patterns
5. **Rich PR descriptions** - Better changelog and release note integration

### What's the Same

- Same commit prefixes for semantic-release compatibility
- Same grouping structure (lsd, cucumber, utility, kotlin, testing)
- Same schedules (daily for deps, weekly for actions)
- Same update types (minor, patch)

## 🚨 Rollback Plan

If issues arise, you can quickly rollback:
1. Re-enable Dependabot by restoring `.github/dependabot.yml`
2. Disable Renovate GitHub App temporarily
3. Delete `renovate.json` if needed

The existing Dependabot config is preserved in git history for easy restoration.