package com.example.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//@SmallTest
@RunWith(AndroidJUnit4::class)
class GetTopSportBenchmark {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun benchmarkGetTopSportAggregated() {
        benchmarkRule.measureRepeated {
            getTopSportAggregated(sportStats)
//            printTopSport(getTopSportAggregated(sportStats))
        }
    }

    @Test
    fun benchmarkGetTopSportSequence() {
        benchmarkRule.measureRepeated {
            getTopSportSequence(sportStats)
//            printTopSport(getTopSportSequence(sportStats))
        }
    }

    @Test
    fun benchmarkGetTopSportManual() {
        benchmarkRule.measureRepeated {
            getTopSportManual(sportStats)
//            printTopSport(getTopSportManual(sportStats))
        }
    }

    @Test
    fun benchmarkGetTopSportManualIteration() {
        benchmarkRule.measureRepeated {
            getTopSportManualIteration(sportStats)
//            printTopSport(getTopSportManualIteration(sportStats))
        }
    }
}

enum class Sport { HIKE, RUN, TOURING_BICYCLE, E_TOURING_BICYCLE }

data class Summary(val sport: Sport, val distance: Int)

val sportStats = listOf(
    Summary(Sport.HIKE, 92),
    Summary(Sport.RUN, 77),
    Summary(Sport.TOURING_BICYCLE, 322),
    Summary(Sport.E_TOURING_BICYCLE, 656)
)

fun main() {

    // Write kotlin code to print the top sport by distance excluding eBikes.

    printTopSport(getTopSportAggregated(sportStats))
    // Benchmark: 380-402ns and 4 alloc
    // Problem: this call requires multiple iteration over the list.

    // Alternative 1. Using sequence.
    printTopSport(getTopSportSequence(sportStats))
    // Benchmark: 358-369ns and 4 alloc
    // Problem: most likely not worth the extra overhead?

    // Alternative 2. Doing it manually, best performance.
    printTopSport(getTopSportManual(sportStats))
    // Benchmark: 181-186ns and 1 alloc.
    // Problem: it supposes there would always be data in the list and -1 being the lowest value.

    // Alternative 3. Iterating manually over list.
    printTopSport(getTopSportManualIteration(sportStats))
    // Benchmark: 185ns and 1 alloc
    // Problem: more work, readability, etc.
}

private fun printTopSport(maxEntry: Summary?) {
    println("top sport by distance: $maxEntry")
}

private fun getTopSportManual(
    sportStats: List<Summary>
): Summary? {
    return sportStats.maxByOrNull {
        when (it.sport.name) {
            Sport.E_TOURING_BICYCLE.name -> -1
            else -> it.distance
        }
    }
}

private fun getTopSportSequence(
    sportStats: List<Summary>
): Summary? {
    return sportStats
        .asSequence()
        .filter { it.sport.name != Sport.E_TOURING_BICYCLE.name }
        .maxByOrNull { it.distance }
}

private fun getTopSportAggregated(sportStats: List<Summary>): Summary? {
    return sportStats
        .filter { it.sport.name != Sport.E_TOURING_BICYCLE.name }
        .maxByOrNull { it.distance }
}

private fun getTopSportManualIteration(sportStats: List<Summary>): Summary? {
    val iterator = sportStats.iterator()
    if (!iterator.hasNext()) return null
    var maxElem = iterator.next()
    if (!iterator.hasNext()) {
        return if (maxElem.sport.name == Sport.E_TOURING_BICYCLE.name) {
            null
        } else {
            maxElem
        }
    }
    var maxValue = maxElem.distance
    do {
        val e = iterator.next()
        if (e.sport.name != Sport.E_TOURING_BICYCLE.name) {
            val v = e.distance
            if (maxValue < v) {
                maxElem = e
                maxValue = v
            }
        }
    } while (iterator.hasNext())
    return maxElem
}