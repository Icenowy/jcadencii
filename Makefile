JAVAC=javac
all: cadencii/Cadencii.jar
cadencii/Cadencii.jar:
	$(JAVAC) cadencii/Cadencii.java cadencii/org/kbinani/cadencii/*.java cadencii/org/kbinani/*.java cadencii/org/kbinani/apputil/*.java cadencii/org/kbinani/media/*.java cadencii/org/kbinani/vsq/*.java cadencii/org/kbinani/cadencii/*.java cadencii/org/kbinani/componentmodel/*.java cadencii/org/kbinani/windows/forms/*.java cadencii/org/kbinani/xml/*.java -encoding UTF8 
# -target 1.5 -source 1.5
	cd cadencii && jar cfm Cadencii.jar Cadencii.mf Cadencii.class org/kbinani/cadencii/*.class org/kbinani/*.class org/kbinani/apputil/*.class org/kbinani/media/*.class org/kbinani/vsq/*.class org/kbinani/cadencii/*.class org/kbinani/componentmodel/*.class org/kbinani/windows/forms/*.class org/kbinani/xml/*.class
install: cadencii/Cadencii.jar
	mkdir -p build
	cp -r blob/* build/
	cp cadencii/Cadencii.jar build/
