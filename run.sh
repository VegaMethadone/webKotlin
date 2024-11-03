


docker build -t mypostgresql:latest .
docker run -d --name testPostgres -p 5432:5432 -e POSTGRES_PASSWORD=0000 -e POSTGRES_USER=postgres mypostgresql:latest