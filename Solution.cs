
using System;
using System.Collections.Generic;

public class Solution
{
    private sealed record Point(int index, int edgeCost) { }
    private sealed record Step(int index, int costFromStart, int discounts) { }

    private static readonly int NOT_POSSIBLE_TO_REACH_DESTINATION = -1;
    private IList<Point>[]? graph;

    public int MinimumCost(int numberOfCities, int[][] highways, int discounts)
    {
        CreateGraph(numberOfCities, highways);
        return DijkstraSearchForPathWithSmallestCost(numberOfCities, discounts);
    }

    private int DijkstraSearchForPathWithSmallestCost(int numberOfCities, int discounts)
    {
        PriorityQueue<Step, int> minHeapForCost = new PriorityQueue<Step, int>();
        minHeapForCost.Enqueue(new Step(0, 0, discounts), 0);

        int[][] minCostToCity = new int[numberOfCities][];
        for (int i = 0; i < numberOfCities; ++i)
        {
            minCostToCity[i] = new int[discounts + 1];
            Array.Fill(minCostToCity[i], int.MaxValue);
        }
        minCostToCity[0][discounts] = 0;

        while (minHeapForCost.Count > 0)
        {
            Step current = minHeapForCost.Dequeue();

            if (current.costFromStart > minCostToCity[current.index][current.discounts])
            {
                continue;
            }
            if (current.index == numberOfCities - 1)
            {
                return current.costFromStart;
            }

            foreach (Point next in graph[current.index])
            {
                if (minCostToCity[next.index][current.discounts] > current.costFromStart + next.edgeCost)
                {
                    minCostToCity[next.index][current.discounts] = current.costFromStart + next.edgeCost;
                    minHeapForCost
                        .Enqueue(new Step(next.index, minCostToCity[next.index][current.discounts], current.discounts),
                        minCostToCity[next.index][current.discounts]);
                }
                if (current.discounts > 0
                        && minCostToCity[next.index][current.discounts - 1] > current.costFromStart + next.edgeCost / 2)
                {
                    minCostToCity[next.index][current.discounts - 1] = current.costFromStart + next.edgeCost / 2;
                    minHeapForCost
                        .Enqueue(new Step(next.index, minCostToCity[next.index][current.discounts - 1], current.discounts - 1),
                        minCostToCity[next.index][current.discounts - 1]);
                }
            }
        }
        return NOT_POSSIBLE_TO_REACH_DESTINATION;
    }

    private void CreateGraph(int numberOfCities, int[][] highways)
    {
        graph = new List<Point>[numberOfCities];
        for (int i = 0; i < numberOfCities; ++i)
        {
            graph[i] = new List<Point>();
        }

        foreach (int[] highway in highways)
        {
            int from = highway[0];
            int to = highway[1];
            int toll = highway[2];

            graph[from].Add(new Point(to, toll));
            graph[to].Add(new Point(from, toll));
        }
    }
}
