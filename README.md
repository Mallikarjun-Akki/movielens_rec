MovieLens Recommender
=====================
Experimenting with Mahout Recommender classes. This implements a user-similarity recommender for a portion of the MovieLens (1M) dataset.

- copy to mahout home dir
- add the module to mahout root pom.xml
- adjust this pom.xml file to reflect the version of mahout used (this one is written on the 0.8 dev branch)

To compile and run:
mvn compile
mvn exec:java -Dexec.mainClass="com.maldyapps.MovielensRecommender" 
