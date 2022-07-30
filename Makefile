build-dist:
	gradle clean
	gradle installDist

arg = ./file1.json ./file2.json
run:
	./build/install/app/bin/app $(arg)

build:
	gradle check
	gradle build

run-dist: build-dist run


.PHONY: build