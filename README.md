# Eclipse Krazo Extensions

The Eclipse Krazo Extensions are additional view engines which can be used in conjunction with [Jakarta MVC](https://github.com/eclipse-ee4j/mvc-api) and [Eclipse Krazo](https://github.com/eclipse-ee4j/krazo). The
extensions try to keep up to date with the current Krazo release, but individual extensions might be bound to an
older release (e. g. the Handlebars extensions) because of missing `jakarta.*` namespace compatible template engines.

## Current extensions

- [Asciidoc](./asciidoc)
- [Freemarker](./freemarker)
- [Handlebars (MVC 1.0 / Krazo 1.0 ONLY)](./handlebars)
- [Jinja2](./jinja2)
- [Mustache](./mustache)
- [Pebble](./pebble)
- [StringTemplate ST4](./stringtemplate)
- [Thymeleaf](./thymeleaf)
- [Velocity](./velocity)

## Running the testsuite

Eclipse Krazo Extensions contains a testsuite which is configured to run against Glassfish. For additional environments please file a pull request.

### Preconditions for development
- Maven 3.8.x
- JDK 11 or newer
- _optional_: SNAPSHOTs are up to date in your local repository for latest versions

Please ensure you use the [Jakarta EE Codestyle](https://github.com/eclipse-ee4j/ee4j/tree/master/codestyle) to avoid conflicts and a lot of whitespace changes.

### Glassfish
To run the Krazo testsuite with Eclipse Glassfish, you need to follow these steps:

1. Download Eclipse Glassfish (min. Glassfish 7) from the [official download page](https://glassfish.org/download) and unzip it.
2. Start Eclipse Glassfish via `glassfishX/glassfish/bin/startserv`. Replace the `X` with your Glassfish version.
3. Go into the `testsuite` package of Eclipse Krazo Extentions and execute `mvn clean verify -Ptestsuite-glassfish`

### Developer resources

This section contains a few resources relevant for Krazo Extension developers.

#### Jenkins CI Pipeline

This project uses a [Jenkins Pipeline](https://www.jenkins.io/doc/book/pipeline/) as its [CI pipeline](https://ci.eclipse.org/krazo/view/Extensions/job/krazo-extensions-ci/). The
pipeline is configured to run on all branches and pull requests. As there is no webhook available, the repository
is scanned every **two minutes**.

#### license-tool-plugin for checking dependencies

To check if a dependency requires a CQ, the command `mvn -Pstaging org.eclipse.dash:license-tool-plugin:license-check -Ddash.skip=false` can be executed. This creates a summary file in `target/dash/summary`.
