
This folder, JAR and script all exist for showcase/example.

Please note: This is NOT a project/composite-build (it's just a JAR taken from develocity's website/with some scripting)

Given your project is located at: /home/tomga/projects/monorepo/toolset/jvm , and you are running the below from that dir:

1. ./start-remote-build-cache-serve.sh /home/tomga/projects/monorepo/toolset/jvm 8080

    This starts the remote server on port 8080 at http://localhost:8080 , and also adds it so that your computer
    will start this locally each time it boots up. This means we can have a locally stored copy of our remote results
    which will allow any cached tasks during local development to be re-used even after blowing away our traditional  
    local cache (ie ~/.gradle). This is particularly useful during gradle plugin development for reasons I won't go
    into here.

2. To login, the default credentials are:
   - Username: DEFAULT_USERNAME
   - Password: DEFAULT_PASSWORD1!

3. Run './gradlew buildAll' until you have everything up to date: 47 actionable tasks: 47 up-to-date

4. Run './remove-gradle-cache.sh' - this will remove all build folders inside this repo, as well as any gradle caches

5. Run './gradle buildAll' 

   Where you will see something like:
   47 actionable tasks: 22 executed, 17 from cache, 8 up-to-date
   Publishing build scan...
   https://scans.gradle.com/s/f6tflpleouppu

    Looking at the build scan you should see things like:

        :standalone-projects:libraries:library-two:test             FROM-CACHE    0.018s	TASK
        :standalone-projects:libraries:example-library-three:test   FROM-CACHE    0.015s	TASK
        :standalone-projects:applications:application-one:test      FROM-CACHE    0.008s	TASK
        :standalone-projects:libraries:library-one:test             FROM-CACHE    0.008s	TASK


        Kind	TASK
        Path	:standalone-projects:libraries:library-two:test
        Duration	            0.018s
            - Fingerprinting inputs	    0.006s
            - Build cache	            0.009s
        Avoidance savings	    1.871s          <----- This value may be skewed, due to the remote running locally.
                                                       However, even if we assume a 1.5 second response from a cached
                                                       endpoint across a network (which is outrageous by the way, it
                                                       should never be this long in a good environment) - we are still
                                                       saving ~0.3 seconds per unit test class (which is what this project
                                                       does).

        Build cache result	Hit (remote)
            Cache key	da1062d3f2ef7859b9d502d7b74774fe
            Cache artifact	5.2 KiB / 22 entries
            Remote hit	0.005s at 1 MiB/s
            Unpack	0.004s

6. And looking at http://localhost:8080/ ,  we should see something like:

   Build cache
    The build cache contains 563 entries - 24.1 MiB disk space used, 200 MiB reserved for temp storage, 259.5 GiB free on disk.
