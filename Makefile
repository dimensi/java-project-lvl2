build-dist:
	gradle clean
	gradle installDist

run:
	./build/install/app/bin/app $(arg) ./file1.json ./file2.json

checkstyle:
	gradle check

build: checkstyle
	gradle build

run-dist: build-dist run


.PHONY: build