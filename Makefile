# Ensimag 2A POO - TP 2018/19
# ============================
#
# Ce Makefile permet de compiler le test de l'ihm en ligne de commande.
# Alternative (recommandee?): utiliser un IDE (eclipse, netbeans, ...)
# Le but est ici d'illustrer les notions de "classpath", a vous de l'adapter
# a votre projet.
#
# Organisation:
#  1) Les sources (*.java) se trouvent dans le repertoire src
#     Les classes d'un package toto sont dans src/toto
#     Les classes du package par defaut sont dans src
#
#  2) Les bytecodes (*.class) se trouvent dans le repertoire bin
#     La hierarchie des sources (par package) est conservee.
#     L'archive gui_bin/gui.jar contient les classes de l'interface graphique
#
# Compilation:
#  Options de javac:
#   -d : repertoire dans lequel sont places les .class compiles
#   -classpath : repertoire dans lequel sont cherches les .class deja compiles
#   -sourcepath : repertoire dans lequel sont cherches les .java (dependances)

all: testInvader testLecture testCarte testDeplacement testPath testElementaire testEvolue  

#test fourni par le sujet
testInvader:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/tests/TestInvader.java

testCarte:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/tests/TestCarte.java

#Scenario simple d'un deplacement
testDeplacement:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/tests/TestDeplacement.java

#Scenario simple d'un robot qui trouve son chemin	
testPath:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/tests/TestPath.java
	
testElementaire:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/tests/TestElementaire.java
	

testEvolue:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/tests/TestEvolue.java

testLecture:
	javac -d bin -sourcepath src src/tests/TestLecteurDonnees.java

# Execution:
# on peut taper directement la ligne de commande :
#   > java -classpath bin:bin/gui.jar TestInvader
# ou bien lancer l'execution en passant par ce Makefile:vi
#   > make exeInvader
exeInvader: 
	java -classpath bin:bin/gui.jar tests/TestInvader
	
exeCarte: 
	java -classpath bin:bin/gui.jar tests/TestCarte

exeDeplacement: 
	java -classpath bin:bin/gui.jar tests/TestDeplacement
	
exePath: 
	java -classpath bin:bin/gui.jar tests/TestPath
	
exeElementaire: 
	java -classpath bin:bin/gui.jar tests/TestElementaire $(CARTE)
		
exeEvolue: 
	java -classpath bin:bin/gui.jar tests/TestEvolue $(CARTE)
	
exeLecture: 
	java -classpath bin TestLecteurDonnees cartes/carteSujet.map

clean:
	rm -rf bin/*/*.class
	rm -rf bin/*.class
