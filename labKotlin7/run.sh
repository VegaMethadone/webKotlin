
cd Deploy

docker build -t tws-juddi:latest .
docker run -d --name tws-juddi -p 8999:8080 -e USERNAME=juddi -e PASSWORD=admin tws-juddi:latest

