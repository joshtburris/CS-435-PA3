CURRENT_DIRECTORY=~/cs435_workspace/PA3/src

all: clean build run

clean:
	clear
	-/usr/local/hadoop/bin/hadoop fs -rm -R /PA3/results_ideal
	-rm -R results_ideal
	-/usr/local/hadoop/bin/hadoop fs -rm -R /PA3/results_taxation
	-rm -R results_taxation
	-/usr/local/hadoop/bin/hadoop fs -rm -R /PA3/results_bomb
	-rm -R results_bomb
	-/usr/local/hadoop/bin/hadoop fs -rm /spark_log/app-*
	-rm -R ~/spark-2.4.4-bin-hadoop2.7/work/app-*

build:
	mvn -DskipTests clean package 
	#-DskipTests clean

run:
	${SPARK_HOME}/bin/spark-submit --class IdealPageRank --deploy-mode cluster --supervise target/PA3-1.0.jar --executor-cores 1 --executor-memory 2g --driver-memory 2g
	#${SPARK_HOME}/bin/spark-submit --class TaxationPageRank --deploy-mode cluster --supervise target/PA3-1.0.jar --executor-cores 1 --executor-memory 2g --driver-memory 2g
	#${SPARK_HOME}/bin/spark-submit --class WikiBomb --deploy-mode cluster --supervise target/PA3-1.0.jar --executor-cores 1 --executor-memory 2g --driver-memory 2g
	#--executor-cores 1 --executor-memory 2g --driver-memory 2g
	#--deploy-mode cluster --master spark://boise:30223

get:
	-/usr/local/hadoop/bin/hadoop fs -get /PA3/results_ideal results_ideal
	-/usr/local/hadoop/bin/hadoop fs -get /PA3/results_taxation results_taxation
	-/usr/local/hadoop/bin/hadoop fs -get /PA3/results_bomb results_bomb

tar:
	tar -czvf Joshua-Burris-PA3.tar src target Makefile pom.xml libexec resources
