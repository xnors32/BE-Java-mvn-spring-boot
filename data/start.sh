#!/bin/sh
set -e
DB_PATH="/app/data/inventori.db"
mkdir -p "$(dirname "$DB_PATH")"
if [ ! -f "$DB_PATH" ]; then
    echo "Initializing database from seed data..."
    sqlite3 "$DB_PATH" < /app/init.sql
    echo "Database initialized."
fi
exec java -jar app.jar
