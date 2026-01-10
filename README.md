![MC Telemetry Banner](images/banner.png)

# mc-telemetry-compat

!!! **Important notice** !!!

Because this project depends on [MC Telemetry Core](https://github.com/MC-Telemetry/mc-telemetry-core), the artifacts of which are not fully published yet as of the time of writing, this project includes mc-telemetry-core using `includeBuild("../mc-telemetry-core)`. As such that project must available next to this project for this project to build. Also, the import of this project might fail until the mc-telemetry-core is explicitly built (`gradle :neoforge:build :fabric:build -x runGameTestServer` in mc-telemetry-core).
