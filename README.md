Prerequisites
Install all of the following software:

Git

Java >= 1.8

Apache Maven

How to run

Clone project and change into directory

Build:

  $> mvn clean package shade:shade
Run:

  $> java -jar target/uebung3-GruppeC.jar train Model train-ham train-spam
  
  $> java -jar target/uebung3-GruppeC.jar classify Model.json train-ham Results.txt
