package de.mctelemetry.compat.gametest

import de.mctelemetry.compat.OTelCompatMod
import de.mctelemetry.compat.gametest.tests.GameLoadsCommonTest
import net.minecraft.gametest.framework.GameTest
import net.minecraft.gametest.framework.GameTestGenerator
import net.minecraft.gametest.framework.StructureUtils
import net.minecraft.gametest.framework.TestFunction
import java.lang.reflect.AccessFlag

class CommonGameTestFactory {
    //class with companion object because fabric apparently has trouble with top-level object declarations
    companion object {

        private val testClasses: List<Class<*>> = listOf(
            GameLoadsCommonTest::class.java,
        )

        @GameTestGenerator
        @JvmStatic
        fun generateCommonGameTests(): Collection<TestFunction> {
            val namespacesString = System.getProperty("neoforge.enabledGameTestNamespaces")
            if (namespacesString != null) {
                val namespaces = namespacesString
                    .split(",")
                    .mapNotNull {
                        it.trim()
                            .takeIf(String::isNotEmpty)
                    }
                if (namespaces.isNotEmpty() && OTelCompatMod.MOD_ID !in namespaces)
                    return emptyList()
            }
            return testClasses.flatMap(::generateCommonGameTests).toList()
        }

        fun generateCommonGameTests(clazz: Class<*>): Collection<TestFunction> {
            val instance: Any by lazy { // lazy because constructor might not exist, only create if needed
                clazz.getConstructor().newInstance()
            }
            val methodTestFunctions: List<Collection<TestFunction>> = clazz.methods.mapNotNull { met ->
                try {
                    val testAnnotations = met.getAnnotationsByType(GameTest::class.java)
                    val generatorAnnotation = met.getAnnotation(GameTestGenerator::class.java)
                    if (testAnnotations.isNotEmpty()) {
                        require(generatorAnnotation == null) { "GameTest and GameTestGenerator both present on $met in $clazz" }
                        val obj = if (AccessFlag.STATIC in met.accessFlags())
                            null
                        else
                            instance

                        val testBaseName = generateSequence(clazz, Class<*>::getDeclaringClass)
                            .toList()
                            .asReversed()
                            .joinToString(separator = ".", postfix = ".${met.name}") { it.simpleName }
                        val isMultiple = testAnnotations.size > 1
                        return@mapNotNull testAnnotations.mapIndexed { idx, annotation ->
                            val testName = if (isMultiple) "${testBaseName}.$idx" else testBaseName
                            TestFunction(
                                annotation.batch,
                                testName,
                                annotation.template.takeIf(String::isNotEmpty) ?: "mcotelcompat:gametestempty",
                                StructureUtils.getRotationForRotationSteps(annotation.rotationSteps),
                                annotation.timeoutTicks,
                                annotation.setupTicks,
                                annotation.required,
                                annotation.manualOnly,
                                annotation.attempts,
                                annotation.requiredSuccesses,
                                annotation.skyAccess,
                            ) {
                                met.invoke(obj, it)
                            }
                        }
                    }
                    if (generatorAnnotation != null) {
                        val obj = if (AccessFlag.STATIC in met.accessFlags())
                            null
                        else
                            instance
                        val result = met.invoke(obj) as Collection<*>
                        result.forEach {
                            it as TestFunction
                        }
                        @Suppress("UNCHECKED_CAST")
                        return@mapNotNull (result as Collection<TestFunction>)
                    }
                } catch (ex: Exception) {
                    throw RuntimeException("Exception during gametest-creation of $met in $clazz", ex)
                }
                return@mapNotNull null
            }
            require(methodTestFunctions.isNotEmpty()) { "No GameTest or GameTestGenerator found in $clazz" }
            return methodTestFunctions.flatten()
        }
    }
}
