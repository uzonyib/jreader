# Build image
```
cd jreader/docker/dev
docker build -t jreader-dev .
```

# Run container in interactive mode
```
docker run --name jreader-dev -v <JREADER_DIR>:/data/jreader -p 8080:8080 -it jreader-dev
```

# Start container in interactive mode
```
docker start -i jreader-dev
```

# Build jReader
```
cd /data/jreader
mvn clean install
```

# Run jReader locally from Docker container
```
mvn appengine:devserver -pl jreader-web
```
jReader will be available on `http://localhost:8080/`

