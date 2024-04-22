
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Solution {

    private record Point(int index, int edgeCost){}
    private record Step(int index, int costFromStart, int discounts){}

    private static final int NOT_POSSIBLE_TO_REACH_DESTINATION = -1;
    private List<Point>[] graph;

    public int minimumCost(int numberOfCities, int[][] highways, int discounts) {
        createGraph(numberOfCities, highways);
        return dijkstraSearchForPathWithSmallestCost(numberOfCities, discounts);
    }

    private int dijkstraSearchForPathWithSmallestCost(int numberOfCities, int discounts) {
        PriorityQueue<Step> minHeapForCost = new PriorityQueue<>((x, y) -> x.costFromStart - y.costFromStart);
        minHeapForCost.add(new Step(0, 0, discounts));

        int[][] minCostToCity = new int[numberOfCities][discounts + 1];
        for (int i = 0; i < numberOfCities; ++i) {
            Arrays.fill(minCostToCity[i], Integer.MAX_VALUE);
        }
        minCostToCity[0][discounts] = 0;

        while (!minHeapForCost.isEmpty()) {
            Step current = minHeapForCost.poll();

            if (current.costFromStart > minCostToCity[current.index][current.discounts]) {
                continue;
            }
            if (current.index == numberOfCities - 1) {
                return current.costFromStart;
            }

            for (Point next : graph[current.index]) {

                if (minCostToCity[next.index][current.discounts] > current.costFromStart + next.edgeCost) {
                    minCostToCity[next.index][current.discounts] = current.costFromStart + next.edgeCost;
                    minHeapForCost.add(new Step(next.index, minCostToCity[next.index][current.discounts], current.discounts));
                }
                if (current.discounts > 0
                        && minCostToCity[next.index][current.discounts - 1] > current.costFromStart + next.edgeCost / 2) {
                    minCostToCity[next.index][current.discounts - 1] = current.costFromStart + next.edgeCost / 2;
                    minHeapForCost.add(new Step(next.index, minCostToCity[next.index][current.discounts - 1], current.discounts - 1));
                }
            }
        }
        return NOT_POSSIBLE_TO_REACH_DESTINATION;
    }

    private void createGraph(int numberOfCities, int[][] highways) {
        graph = new List[numberOfCities];
        for (int i = 0; i < numberOfCities; ++i) {
            graph[i] = new ArrayList<>();
        }

        for (int[] highway : highways) {
            int from = highway[0];
            int to = highway[1];
            int toll = highway[2];

            graph[from].add(new Point(to, toll));
            graph[to].add(new Point(from, toll));
        }
    }
}
