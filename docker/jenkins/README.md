# Build image
```
cd jreader/docker/jenkins
docker build -t jreader-jenkins .
```

# Run Jenkins container
```
docker run --name jreader-jenkins -v /home/uzonyib/code/jreader-jenkins-home:/var/jenkins_home -p 8090:8080 -u <USER> -dt jreader-jenkins
```

# Start Jenkins container
```
docker start jreader-jenkins
```

# Update plugin list
```
JENKINS_HOST=username:password@myhost.com:port
curl -sSL "http://$JENKINS_HOST/pluginManager/api/xml?depth=1&xpath=/*/*/shortName|/*/*/version&wrapper=plugins" | perl -pe 's/.*?<shortName>([\w-]+).*?<version>([^<]+)()(<\/\w+>)+/\1 \2\n/g'|sed 's/ /:/'
```
