JAVAC=javac
JAVA=java
JAVADOC=javadoc

default: all

all:	FORCE
	${JAVAC} -d ../classes pregame/*.java
	${JAVAC} -d ../classes ./*.java
	${JAVAC} -d ../classes gameengine/*.java
	${JAVAC} -d ../classes gameengine/gameobjects/*.java
	${JAVAC} -d ../classes gameengine/tasksystem/*.java
	${JAVAC} -d ../classes graphicsengine/*.java
	${JAVAC} -d ../classes graphicsengine/graphics/*.java
	${JAVAC} -d ../classes input/*.java

pregame:FORCE
	${JAVAC} -d ../classes pregame/*.java

gameengine:	FORCE
	${JAVAC} -d ../classes gameengine/*.java

gameobjects:	FORCE
	${JAVAC} -d ../classes gameengine/gameobjects/*.java

tasksystem:	FORCE
	${JAVAC} -d ../classes gameengine/tasksystem/*.java

graphicsengine:	FORCE
	${JAVAC} -d ../classes graphicsengine/*.java

graphics: FORCE
	${JAVAC} -d ../classes graphicsengine/graphics/*.java

input:		FORCE
	${JAVAC} -d ../classes input/*.java

doc14:		FORCE
	${JAVADOC} -package -breakiterator -d ../doc pregame input gameengine gameengine.tasksystem gameengine.gameobjects graphicsengine

doc13:		FORCE
	${JAVADOC} -package -d ../doc pregame input gameengine gameengine.tasksystem gameengine.gameobjects graphicsengine

FORCE:
