## Documentation
* https://project-group-b.github.io/BehavioralEnrichment-Java/

## Import into Eclipse
1. Can follow this guide: https://stackoverflow.com/questions/2061094/importing-maven-project-into-eclipse
2. Basics:
    1. File -> Import -> Maven -> Existing Maven Project
    2. Browse to and select the root folder of the Maven project (containing the pom.xml)
    3. Click Next then Finish

## Development
* Miscellaneous Links:
    * [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
    * [Validating Form Input](https://spring.io/guides/gs/validating-form-input/)
    * [Grabbing a file from the file system (StackOverflow)](https://stackoverflow.com/questions/45908323/spring-boot-grabbing-a-file-from-the-file-system-in-a-get-request)
    * [Creating a REST service with Spring Boot](http://www.springboottutorial.com/creating-rest-service-with-spring-boot)
    * [Storing Images](https://stackoverflow.com/questions/348363/what-is-the-best-place-for-storing-uploaded-images-sql-database-or-disk-file-sy)
    
## Maven dependencies
* To add a dependency:
    * Open pom.xml in Eclipse
    * Go to "Dependencies" tab at the bottom
    * Click "Add..."
    * Search for your desired dependency and select it
    * Click "OK"
* Clean install of dependencies
    * Follow same instructions as the "Deploying Locally" section below, except...
    * Set "Goals" as `clean install`
    * Keep "Profiles" empty
    * Run the configuration and you dependencies should be installed

## Deploying Locally
1. Right click on project in Eclipse
2. Click "Run As" -> "Maven build..."
3. Name it as something you'll remember/recognize
4. Set Goals as "spring-boot:run"
    1. "Profiles" should be empty
5. You should see some output in the console ending with something like "Started BehaviorEnrichmentJavaApplication in..."
6. Application is now listening on port http://localhost:8080

## Deploying Permanently
1. Right click on project in Eclipse
2. Click "Run As" -> "Maven build..."
3. Set Goals as "-Dmaven.test.skip=true package"
4. Upload resulting .jar to deployment location.
5. Run .jar using "java -jar JAR_NAME"
6. Use "lsof -i :8080" to determine the PID of the process for future reference.
