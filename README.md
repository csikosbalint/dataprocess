# dataprocess

## Usage:
```mvn clean verify exec:java -Dexec.args=C:\SPY.csv```

### Results
- rundir \ ```future```  folder with iteration files
- ```past.json``` at rundir
 
### Example run
```[INFO] Scanning for projects...
   [INFO]                                                                         
   [INFO] ------------------------------------------------------------------------
   [INFO] Building dataprocess LATEST
   [INFO] ------------------------------------------------------------------------
   [INFO] 
   [INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ dataprocess ---
   [INFO] Deleting C:\Users\Balint Csikos\IdeaProjects\dataprocess\target
   [INFO] 
   [INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ dataprocess ---
   [WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
   [INFO] Copying 1 resource
   [INFO] 
   [INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ dataprocess ---
   [INFO] Changes detected - recompiling the module!
   [WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
   [INFO] Compiling 1 source file to C:\Users\Balint Csikos\IdeaProjects\dataprocess\target\classes
   [INFO] 
   [INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ dataprocess ---
   [WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
   [INFO] Copying 0 resource
   [INFO] 
   [INFO] --- maven-compiler-plugin:3.1:testCompile (default-testCompile) @ dataprocess ---
   [INFO] Changes detected - recompiling the module!
   [WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
   [INFO] Compiling 1 source file to C:\Users\Balint Csikos\IdeaProjects\dataprocess\target\test-classes
   [INFO] 
   [INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ dataprocess ---
   [INFO] Surefire report directory: C:\Users\Balint Csikos\IdeaProjects\dataprocess\target\surefire-reports
   Downloading: https://repo.maven.apache.org/maven2/org/apache/maven/surefire/surefire-junit4/2.12.4/surefire-junit4-2.12.4.pom
   Downloaded: https://repo.maven.apache.org/maven2/org/apache/maven/surefire/surefire-junit4/2.12.4/surefire-junit4-2.12.4.pom (3 KB at 3.7 KB/sec)
   Downloading: https://repo.maven.apache.org/maven2/org/apache/maven/surefire/surefire-providers/2.12.4/surefire-providers-2.12.4.pom
   Downloaded: https://repo.maven.apache.org/maven2/org/apache/maven/surefire/surefire-providers/2.12.4/surefire-providers-2.12.4.pom (3 KB at 21.2 KB/sec)
   Downloading: https://repo.maven.apache.org/maven2/org/apache/maven/surefire/surefire-junit4/2.12.4/surefire-junit4-2.12.4.jar
   37/37 KB   Downloaded: https://repo.maven.apache.org/maven2/org/apache/maven/surefire/surefire-junit4/2.12.4/surefire-junit4-2.12.4.jar (37 KB at 92.9 KB/sec)
   
   -------------------------------------------------------
    T E S T S
   -------------------------------------------------------
   Running com.fincance.dataprocess.EntrypointTest
   Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.101 sec
   
   Results :
   
   Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
   
   [INFO] 
   [INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ dataprocess ---
   [INFO] Building jar: C:\Users\Balint Csikos\IdeaProjects\dataprocess\target\dataprocess-LATEST.jar
   [INFO] 
   [INFO] >>> exec-maven-plugin:1.2.1:java (default-cli) > validate @ dataprocess >>>
   [INFO] 
   [INFO] <<< exec-maven-plugin:1.2.1:java (default-cli) < validate @ dataprocess <<<
   [INFO] 
   [INFO] --- exec-maven-plugin:1.2.1:java (default-cli) @ dataprocess ---
   127 ms for thread #27
   139 ms for thread #25
   124 ms for thread #30
   79 ms for thread #15
   <<output truncated for 10000 iteration>>
   11 ms for thread #18
   11 ms for thread #18
   10 ms for thread #18
   44894 ms
   [INFO] ------------------------------------------------------------------------
   [INFO] BUILD SUCCESS
   [INFO] ------------------------------------------------------------------------
   [INFO] Total time: 49.375 s
   [INFO] Finished at: 2017-05-26T01:01:03+02:00
   [INFO] Final Memory: 18M/292M
   [INFO] ------------------------------------------------------------------------
```

## Issues
- does not handles whitespaces in ```exec.args```. Workaround: use ```"``` , escape or avoid whitespaces in path

## Todo / Refactor
 - file write can be extracted
 - pool size could be optimalized
 - random UUID for output file could be reviewed
