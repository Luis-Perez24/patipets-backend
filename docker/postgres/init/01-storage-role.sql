\set storage_pass `echo "$POSTGRES_PASSWORD"`

ALTER ROLE supabase_storage_admin WITH LOGIN PASSWORD :'storage_pass';
