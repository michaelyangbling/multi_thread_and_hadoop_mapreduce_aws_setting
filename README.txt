README

Please note professor said that for students who got this class from waitlist in the second week, they can submits this Homework 1
a week after the deadline without losing points. Here I submit this homework 2 days after the deadline.

- Weather analysis is done in IntelliJ IDEA for JAVA, you just need to change the csv directory to run the code.

- Standalone WordCount can be done in following steps:
First, using Maven, use  "mvn clean install" to make a .jar file( remember use pom.xml and cd to project directory). 
Second, use " bin/hadoop jar  <put your .jar path here> <input directory here>  < output directory here( should not exist yet >"
In the second step, remember cd to hadoop directory and change export JAVA_HOME in advance ( according to official document).


-AWS wordcount:
just provide argument: WordCount <input aws S3 path>   <output aws S3 path>


