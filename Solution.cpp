
#include <span>
#include <queue>
#include <vector>
#include <limits>
using namespace std;

class Solution {

    struct Point {
        size_t index;
        int edgeCost;

        Point() = default;
        Point(size_t index, int edgeCost): index{ index }, edgeCost{ edgeCost }{}
    };

    struct Step {
        size_t index;
        int costFromStart;
        int discounts;

        Step() = default;
        Step(size_t index, int costFromStart, int discounts):
                index{ index }, costFromStart{ costFromStart }, discounts{ discounts }{}
    };

    struct CompareSteps {
        auto operator()(const Step& first, const Step& second) const {
            return first.costFromStart > second.costFromStart;
        }
    };

    static const int NOT_POSSIBLE_TO_REACH_DESTINATION = -1;
    vector<vector<Point>> graph;

public:
    int minimumCost(int numberOfCities, const vector<vector<int>>& highways, int discounts) {
        createGraph(numberOfCities, highways);
        return dijkstraSearchForPathWithSmallestCost(numberOfCities, discounts);
    }

private:
    int dijkstraSearchForPathWithSmallestCost(int numberOfCities, int discounts) const {
        priority_queue<Step, vector<Step>, CompareSteps> minHeapForCost;
        minHeapForCost.emplace(0, 0, discounts);

        vector<vector<int>> minCostToCity
        (numberOfCities, vector<int>(discounts + 1, numeric_limits<int>::max()));
        minCostToCity[0][discounts] = 0;

        while (!minHeapForCost.empty()) {
            Step current = minHeapForCost.top();
            minHeapForCost.pop();

            if (current.costFromStart > minCostToCity[current.index][current.discounts]) {
                continue;
            }
            if (current.index == numberOfCities - 1) {
                return current.costFromStart;
            }

            for (const auto& next : graph[current.index]) {

                if (minCostToCity[next.index][current.discounts] > current.costFromStart + next.edgeCost) {
                    minCostToCity[next.index][current.discounts] = current.costFromStart + next.edgeCost;
                    minHeapForCost.emplace(next.index, minCostToCity[next.index][current.discounts], current.discounts);
                }
                if (current.discounts > 0
                    && minCostToCity[next.index][current.discounts - 1] > current.costFromStart + next.edgeCost / 2) {
                    minCostToCity[next.index][current.discounts - 1] = current.costFromStart + next.edgeCost / 2;
                    minHeapForCost.emplace(next.index, minCostToCity[next.index][current.discounts - 1], current.discounts - 1);
                }
            }
        }
        return NOT_POSSIBLE_TO_REACH_DESTINATION;
    }

    void createGraph(int numberOfCities, span<const vector<int>> highways) {
        graph.resize(numberOfCities);

        for (const auto& highway : highways) {
            int from = highway[0];
            int to = highway[1];
            int toll = highway[2];

            graph[from].emplace_back(to, toll);
            graph[to].emplace_back(from, toll);
        }
    }
};
