#!/usr/bin/env bash

set -u

CLIENTS="${1:-100}"
SERVER_JAR="target/tttbasic-1.0-SNAPSHOT-server.jar"
CLIENT_JAR="target/tttbasic-1.0-SNAPSHOT-client.jar"
SERVER_LOG="target/stress-server.log"
CLIENT_LOG_DIR="target/stress-clients"
SERVER_PID=""
CLIENT_PIDS=()

find_java() {
  if [ -n "${JAVA_BIN:-}" ]; then
    echo "$JAVA_BIN"
    return 0
  fi

  if command -v java >/dev/null 2>&1; then
    command -v java
    return 0
  fi

  if command -v powershell.exe >/dev/null 2>&1; then
    JAVA_FROM_POWERSHELL="$(powershell.exe -NoProfile -Command "(Get-Command java -ErrorAction SilentlyContinue).Source" | tr -d '\r')"
    if [ -n "$JAVA_FROM_POWERSHELL" ]; then
      if command -v cygpath >/dev/null 2>&1; then
        cygpath -u "$JAVA_FROM_POWERSHELL"
      elif [[ "$JAVA_FROM_POWERSHELL" =~ ^([A-Za-z]):\\(.*)$ ]]; then
        DRIVE="$(echo "${BASH_REMATCH[1]}" | tr '[:upper:]' '[:lower:]')"
        REST="${BASH_REMATCH[2]//\\//}"
        if [ -d "/mnt/$DRIVE" ]; then
          echo "/mnt/$DRIVE/$REST"
        else
          echo "/$DRIVE/$REST"
        fi
      else
        echo "$JAVA_FROM_POWERSHELL"
      fi
      return 0
    fi
  fi

  for candidate in \
    "/c/Program Files/Java/jdk-21/bin/java.exe" \
    "/c/Program Files/Eclipse Adoptium/jdk-21/bin/java.exe" \
    "/c/Program Files/Microsoft/jdk-21/bin/java.exe"; do
    if [ -x "$candidate" ]; then
      echo "$candidate"
      return 0
    fi
  done

  return 1
}

JAVA_CMD="$(find_java || true)"

if [ -z "$JAVA_CMD" ]; then
  echo "Could not find Java."
  echo "Try running with an explicit Java path, for example:"
  echo "JAVA_BIN=\"/c/Program Files/Java/jdk-21/bin/java.exe\" bash stress-10000-clients.sh"
  exit 1
fi

if [ ! -f "$SERVER_JAR" ] || [ ! -f "$CLIENT_JAR" ]; then
  echo "Jar files are missing. Build them first:"
  echo "mvn package"
  exit 1
fi

mkdir -p "$CLIENT_LOG_DIR"

echo "Using Java: $JAVA_CMD"
echo "Starting Tic-Tac-Toe server..."
"$JAVA_CMD" -jar "$SERVER_JAR" > "$SERVER_LOG" 2>&1 &
SERVER_PID=$!

cleanup() {
  echo "Stopping clients..."
  for pid in "${CLIENT_PIDS[@]}"; do
    kill "$pid" 2>/dev/null || true
  done

  sleep 1

  for pid in "${CLIENT_PIDS[@]}"; do
    kill -9 "$pid" 2>/dev/null || true
  done

  echo "Stopping server..."
  if [ -n "$SERVER_PID" ]; then
    kill "$SERVER_PID" 2>/dev/null || true
    sleep 1
    kill -9 "$SERVER_PID" 2>/dev/null || true
  fi

  if command -v powershell.exe >/dev/null 2>&1; then
    powershell.exe -NoProfile -Command "Get-Process java -ErrorAction SilentlyContinue | Where-Object { \$_.Path -like '*java*' } | Stop-Process -Force" >/dev/null 2>&1 || true
  fi
}

trap cleanup EXIT INT TERM

sleep 3

if ! kill -0 "$SERVER_PID" 2>/dev/null; then
  echo "Server failed to start. Check $SERVER_LOG"
  exit 1
fi

echo "Launching $CLIENTS clients..."
echo "Warning: very large values, especially 10000, may freeze your computer."

for i in $(seq 1 "$CLIENTS"); do
  "$JAVA_CMD" -jar "$CLIENT_JAR" 1 > "$CLIENT_LOG_DIR/client-$i.log" 2>&1 &
  CLIENT_PIDS+=("$!")
done

echo "Waiting for the server to survive or crash..."
sleep 15

if kill -0 "$SERVER_PID" 2>/dev/null; then
  echo "Server is still running after $CLIENTS client attempts."
  echo "This machine did not crash the server within the wait period."
else
  echo "Server crashed after $CLIENTS client attempts."
fi

echo "Logs:"
echo "- Server: $SERVER_LOG"
echo "- Clients: $CLIENT_LOG_DIR"
