
plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.10"
}

javafx {
    version = "11.0.2"
    modules(
            "javafx.controls",
            "javafx.fxml"
    )
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainClassName = "main.MainApplication"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.jfoenix:jfoenix:9.0.10")
    implementation(project(":data"))
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "main.Launcher"
    }
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
}

tasks.register("rebuild and move", Copy::class) {
    group = "custom"
    dependsOn("clean")
    dependsOn("jar")

    from("$buildDir/libs/app.jar")

    mkdir("$buildDir/out/")
    into("$buildDir/out/")
}
