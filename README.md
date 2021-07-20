# Running with Docker
1. Create environment file from example.env
2. Create your local schema using schema/schema.sql
3. `docker build -t dw-sync-iam .`
4. `docker run --env-file .env dw-sync-iam`
