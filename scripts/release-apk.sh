#!/bin/bash
# Release APK to GitHub Releases (devops release automation)
# Usage: ./scripts/release-apk.sh [version] [notes]
# Token is read from git_acc.json (never committed - see .gitignore)
#
# Rollback: delete the release at
#   https://github.com/yanyannoodle34-debug/Myapp/releases
# then re-run this script with the previous APK.

set -e

VERSION="${1:-v1.1}"
NOTES="${2:-API Dashboard release $VERSION}"
REPO="yanyannoodle34-debug/Myapp"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
ACC_FILE="$PROJECT_ROOT/git_acc.json"
APK_DEBUG="$PROJECT_ROOT/MyApplication/app/build/outputs/apk/debug/app-debug.apk"

if [ ! -f "$ACC_FILE" ]; then
  echo "ERROR: git_acc.json not found at $ACC_FILE"
  exit 1
fi

TOKEN=$(python3 -c "import json;print(json.load(open('$ACC_FILE')).get('token',''))")
if [ -z "$TOKEN" ]; then
  echo "ERROR: no token in git_acc.json"
  exit 1
fi

pick_apk() {
  if [ -f "$APK_DEBUG" ]; then echo "$APK_DEBUG"; return; fi
  echo "ERROR: no APK found. Build first: gradle assembleDebug" >&2
  exit 1
}

APK=$(pick_apk)
APK_NAME="api-dashboard-$VERSION.apk"
echo "APK: $APK ($APK_NAME)"

echo "Creating release $VERSION..."
RELEASE_JSON=$(curl -s -X POST \
  -H "Authorization: token $TOKEN" \
  -H "Accept: application/vnd.github+json" \
  "https://api.github.com/repos/$REPO/releases" \
  -d "{\"tag_name\":\"$VERSION\",\"name\":\"API Dashboard $VERSION\",\"body\":\"$NOTES\",\"draft\":false,\"prerelease\":false}")

UPLOAD_URL=$(python3 -c "import json,sys;d=json.load(sys.stdin);print(d.get('upload_url','').split('{')[0])" <<< "$RELEASE_JSON")
if [ -z "$UPLOAD_URL" ]; then
  echo "ERROR: release creation failed:"
  echo "$RELEASE_JSON"
  exit 1
fi
echo "Release created."

echo "Uploading APK..."
curl -s -X POST \
  -H "Authorization: token $TOKEN" \
  -H "Content-Type: application/vnd.android.package-archive" \
  --data-binary @"$APK" \
  "$UPLOAD_URL?name=$APK_NAME" | python3 -c "import json,sys;d=json.load(sys.stdin);print('Asset:',d.get('browser_download_url','UPLOAD FAILED'))"

echo "Done: https://github.com/$REPO/releases/tag/$VERSION"
