architectury {
    common(rootProject.property("enabled_platforms").toString().split(","))
}

loom {
    accessWidenerPath.set(project.layout.projectDirectory.file("src/main/resources/mcotelcompat.accesswidener"))
}

sourceSets {
    val main by getting

/*    val gametest by creating {
        compileClasspath += main.output + main.compileClasspath
        runtimeClasspath += main.output + main.runtimeClasspath
    }*/
}

dependencies {
    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric_loader_version")}")
    // Remove the next line if you don't want to depend on the API
    modApi("dev.architectury:architectury:${rootProject.property("architectury_version")}")

    // opentelemetry
    compileOnly("io.opentelemetry:opentelemetry-api:${rootProject.property("otel_version")}")

    modCompileOnly("de.mctelemetry:mc-telemetry-core:${rootProject.property("mcotelcore_version_raw")}") {
        isTransitive = false
    }

    // GeckoLib
    modCompileOnly("software.bernie.geckolib:geckolib-common-1.21.1:${rootProject.property("geckolib_version")}")
}
