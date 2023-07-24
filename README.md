# About
Using stream api to filter/sort data

![filtering](/images/filtering.PNG)

#### Short description

First, the application gets data from the URL and then you hava the ability to:
* Sort any column by ascending or descending order
* Filter any column by text input
* Set names and surnames to uppercase or lower case
* Create a [map](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html) where the key corresponds to the last 3 digits of the IP address, and then print the map to the `output.txt`

## Technologies
#### Project is created with:

* Java 17.0.7
* Maven 3.9.3
* Javafx 19.0.2.1
	
## Setup
#### First:

* [download and install java JDK version 17 and set JAVA_HOME](https://docs.oracle.com/cd/E19182-01/821-0917/inst_jdk_javahome_t/index.html)
* clone or download the repo
* `cd ../project_directory` loacate to project directory

#### For just running the project:

* `mvnw clean javafx:run`

#### To build .jar and run the .jar file:

* `mvnw clean package` builds .jar file
* `cd ../project_dir/target` locate .jar
* `./app-version-shaded.jar` run the .jar
