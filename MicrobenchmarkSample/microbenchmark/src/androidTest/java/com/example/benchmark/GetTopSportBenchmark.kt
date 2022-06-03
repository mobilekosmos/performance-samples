package com.example.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.filters.SmallTest
import com.example.benchmark.data.*
import org.junit.Rule
import org.junit.Test

@SmallTest
class GetTopSportBenchmark {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private val sportStats = listOf(
        Summary(Sport.HIKE, 92),
        Summary(Sport.RUN, 77),
        Summary(Sport.TOURING_BICYCLE, 322),
        Summary(Sport.E_TOURING_BICYCLE, 656),
        Summary(Sport.HIKE, 91),
        Summary(Sport.RUN, 76),
        Summary(Sport.TOURING_BICYCLE, 321),
        Summary(Sport.E_TOURING_BICYCLE, 655),
        Summary(Sport.HIKE, 90),
        Summary(Sport.RUN, 76),
        Summary(Sport.TOURING_BICYCLE, 320),
        Summary(Sport.E_TOURING_BICYCLE, 654),
        Summary(Sport.HIKE, 89),
        Summary(Sport.RUN, 75),
        Summary(Sport.TOURING_BICYCLE, 319),
        Summary(Sport.E_TOURING_BICYCLE, 653),
        Summary(Sport.HIKE, 88),
        Summary(Sport.RUN, 74),
        Summary(Sport.TOURING_BICYCLE, 318),
        Summary(Sport.E_TOURING_BICYCLE, 652),
        Summary(Sport.HIKE, 87),
        Summary(Sport.RUN, 73),
        Summary(Sport.TOURING_BICYCLE, 317),
        Summary(Sport.E_TOURING_BICYCLE, 651),
        Summary(Sport.HIKE, 88),
        Summary(Sport.RUN, 72),
        Summary(Sport.TOURING_BICYCLE, 316),
        Summary(Sport.E_TOURING_BICYCLE, 650),
        Summary(Sport.HIKE, 89),
        Summary(Sport.RUN, 71),
        Summary(Sport.TOURING_BICYCLE, 315),
        Summary(Sport.E_TOURING_BICYCLE, 649),
    )

    /*
        With 4 items in list:
        184 ns 1 allocs PROFILED_GetTopSportBenchmark.benchmarkGetTopSportManualIteration
        366 ns 4 allocs PROFILED_GetTopSportBenchmark.benchmarkGetTopSportSequence
        386 ns 4 allocs PROFILED_GetTopSportBenchmark.benchmarkGetTopSportAggregated
     */

    /*
        With 32 items in list:
        1,026 ns 1 allocs PROFILED_GetTopSportBenchmark.benchmarkGetTopSportManualIteration
        1,627 ns 4 allocs PROFILED_GetTopSportBenchmark.benchmarkGetTopSportSequence
        2,005 ns 4 allocs PROFILED_GetTopSportBenchmark.benchmarkGetTopSportAggregated
    */

    @Test
    fun benchmarkGetTopSportAggregated() {
        benchmarkRule.measureRepeated {
            getTopSportAggregated(sportStats)
        }
    }

    @Test
    fun benchmarkGetTopSportSequence() {
        benchmarkRule.measureRepeated {
            getTopSportSequence(sportStats)
        }
    }

    @Test
    fun benchmarkGetTopSportManualIteration() {
        benchmarkRule.measureRepeated {
            getTopSportManualIteration(sportStats)
        }
    }
}