
import java.util.*

class Solution {

    private data class Point(val index: Int, val edgeCost: Int) {}
    private data class Step(val index: Int, val costFromStart: Int, val discounts: Int) {}

    companion object {
        const val NOT_POSSIBLE_TO_REACH_DESTINATION = -1
    }

    private lateinit var graph: ArrayList<ArrayList<Point>>

    fun minimumCost(numberOfCities: Int, highways: Array<IntArray>, discounts: Int): Int {
        createGraph(numberOfCities, highways)
        return dijkstraSearchForPathWithSmallestCost(numberOfCities, discounts)
    }

    private fun dijkstraSearchForPathWithSmallestCost(numberOfCities: Int, discounts: Int): Int {
        val minHeapForCost = PriorityQueue<Step> { x, y -> x.costFromStart - y.costFromStart }
        minHeapForCost.add(Step(0, 0, discounts))

        val minCostToCity = ArrayList<IntArray>(numberOfCities)
        for (i in 0..<numberOfCities) {
            minCostToCity.add(IntArray(discounts + 1) { Int.MAX_VALUE })
        }
        minCostToCity[0][discounts] = 0

        while (!minHeapForCost.isEmpty()) {
            val current: Step = minHeapForCost.poll()

            if (current.costFromStart > minCostToCity[current.index][current.discounts]) {
                continue
            }
            if (current.index == numberOfCities - 1) {
                return current.costFromStart
            }

            for (next in graph[current.index]) {

                if (minCostToCity[next.index][current.discounts] > current.costFromStart + next.edgeCost) {
                    minCostToCity[next.index][current.discounts] = current.costFromStart + next.edgeCost
                    minHeapForCost.add(
                        Step(
                            next.index,
                            minCostToCity[next.index][current.discounts],
                            current.discounts
                        )
                    )
                }
                if (current.discounts > 0
                    && minCostToCity[next.index][current.discounts - 1] > current.costFromStart + next.edgeCost / 2
                ) {
                    minCostToCity[next.index][current.discounts - 1] = current.costFromStart + next.edgeCost / 2
                    minHeapForCost.add(
                        Step(
                            next.index,
                            minCostToCity[next.index][current.discounts - 1],
                            current.discounts - 1
                        )
                    )
                }
            }
        }
        return NOT_POSSIBLE_TO_REACH_DESTINATION
    }


    private fun createGraph(numberOfCities: Int, highways: Array<IntArray>) {
        graph = ArrayList<ArrayList<Point>>(numberOfCities)
        for (i in 0..<numberOfCities) {
            graph.add(ArrayList<Point>())
        }

        for ((from, to, toll) in highways) {
            graph[from].add(Point(to, toll))
            graph[to].add(Point(from, toll))
        }
    }
}
