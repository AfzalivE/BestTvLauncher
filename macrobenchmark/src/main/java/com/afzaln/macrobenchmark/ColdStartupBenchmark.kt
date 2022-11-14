package com.afzaln.macrobenchmark

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.FixMethodOrder
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@RunWith(AndroidJUnit4ClassRunner::class)
@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ColdStartupBenchmark : AbstractStartupBenchmark(StartupMode.COLD)
abstract class AbstractStartupBenchmark(private val startupMode: StartupMode) {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun s1tartupNoCompilation() = startup(CompilationMode.None())

    @Test
    fun s2tartupPartialCompilation() = startup(
        CompilationMode.Partial(
            baselineProfileMode = BaselineProfileMode.Disable,
            warmupIterations = 3
        )
    )

    @Test
    fun s3tartupFullCompilation() = startup(CompilationMode.Full())

    @Test
    fun s4tartupPartialWithBaselineProfiles() =
        startup(CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require))

    private fun startup(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "com.afzaln.besttvlauncher",
        metrics = listOf(StartupTimingMetric()),
        compilationMode = compilationMode,
        iterations = 2,
        startupMode = startupMode,
        setupBlock = {
            pressHome()
        }
    ) {
        startActivityAndWait()
        benchmarkTest()
    }
}
