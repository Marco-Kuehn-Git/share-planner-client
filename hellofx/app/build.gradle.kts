
plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.10"
}

javafx {
    version = "11"
    modules(
            "javafx.controls",
            "javafx.fxml"
    )
}

application {
    mainClassName = "client.MainApplication"
}

dependencies {
    implementation(project(":data"))
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "client.Launcher"
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
