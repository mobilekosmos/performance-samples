package com.example.benchmark.data

enum class Sport { HIKE, RUN, TOURING_BICYCLE, E_TOURING_BICYCLE }

data class Summary(val sport: Sport, val distance: Int)

fun main() {
    val sportStats = listOf(
        Summary(Sport.HIKE, 92),
        Summary(Sport.RUN, 77),
        Summary(Sport.TOURING_BICYCLE, 322),
        Summary(Sport.E_TOURING_BICYCLE, 656)
    )
//    val sportStats = listOf(
//        Summary(Sport.E_TOURING_BICYCLE, 656)
//    )

    // Write kotlin code to print the top sport by distance excluding eBikes.

    printTopSport(getTopSportAggregated(sportStats))
    // Benchmark: 380-402ns and 4 alloc
    // Problem: this call iterates twice over the list.

    // Alternative 1. Using sequence.
    printTopSport(getTopSportSequence(sportStats))
    // Benchmark: 358-369ns and 4 alloc
    // Problem: worth the extra overhead?

    // Alternative 2. Iterating manually over list.
    printTopSport(getTopSportManualIteration(sportStats, Sport.E_TOURING_BICYCLE))
    // Benchmark: 185ns and 1 alloc
    // Problem: more work, readability, complexity, etc.

    // For more on the micro benchmarks check #GetTopSportBenchmark.kt

    // TODO Alternative 3?
}

private fun printTopSport(maxEntry: Summary?) {
    if (maxEntry == null) {
        println("You were lazy, start doing some sport.")
    } else {
        println("Top sport by distance: $maxEntry")
    }
}

fun getTopSportSequence(
    sportStats: List<Summary>
): Summary? {
    return sportStats
        .asSequence()
        .filter { it.sport.name != Sport.E_TOURING_BICYCLE.name }
        .maxByOrNull { it.distance }
}

fun getTopSportAggregated(sportStats: List<Summary>): Summary? {
    return sportStats
        .filter { it.sport.name != Sport.E_TOURING_BICYCLE.name }
        .maxByOrNull { it.distance }
}

// TODO: check if simplification is possible, make an extension function out of it, write tests.
fun getTopSportManualIteration(sportStats: List<Summary>, excludedSport: Sport? = null): Summary? {
    val iterator = sportStats.iterator()
    if (!iterator.hasNext()) return null
    var maxElem = iterator.next()
    // If this is the only item in the list
    if (!iterator.hasNext()) {
        return if (excludedSport != null && maxElem.sport.name == excludedSport.name) {
            null
        } else {
            maxElem
        }
    }
    var maxValue = maxElem.distance
    do {
        val e = iterator.next()
        if (excludedSport != null) {
            if (e.sport.name != excludedSport.name) {
                val v = e.distance
                if (maxValue < v) {
                    maxElem = e
                    maxValue = v
                }
            }
        } else {
            val v = e.distance
            if (maxValue < v) {
                maxElem = e
                maxValue = v
            }
        }
    } while (iterator.hasNext())
    return maxElem
}