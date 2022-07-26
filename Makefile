build-dist:
	gradle clean
	gradle installDist

run:
	./build/install/app/bin/app $(arg)

run-dist: build-dist run