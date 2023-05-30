open-api:
	./gradlew showcase:openApiPreCodeGen
	./gradlew openapi:openApiGenerate

nuke-environment:
	./gradlew --stop
	killall java || true
	git clean -xdf
	rm -rf ~/.gradle/caches
