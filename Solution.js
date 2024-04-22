
/**
 * @param {number} numberOfCities
 * @param {number[][]} highways
 * @param {number} discounts
 * @return {number}
 */
var minimumCost = function (numberOfCities, highways, discounts) {
    this.NOT_POSSIBLE_TO_REACH_DESTINATION = -1;
    this.graph = Array.from(new Array(numberOfCities), () => new Array());
    createGraph(highways);
    return dijkstraSearchForPathWithSmallestCost(numberOfCities, discounts);
};

/**
 * @param {number} index
 * @param {number} edgeCost
 */
function Point(index, edgeCost) {
    this.index = index;
    this.edgeCost = edgeCost;
}

/**
 * @param {number} index
 * @param {number} costFromStart
 * @param {number} discounts
 */
function Step(index, costFromStart, discounts) {
    this.index = index;
    this.costFromStart = costFromStart;
    this.discounts = discounts;
}

/**
 * @param {number} numberOfCities
 * @param {number} discounts
 * @return {number}
 */
function dijkstraSearchForPathWithSmallestCost(numberOfCities, discounts) {
    // PriorityQueue<Step>
    // const {MinPriorityQueue} = require('@datastructures-js/priority-queue');
    const minHeapForCost = new MinPriorityQueue({compare: (x, y) => x.costFromStart - y.costFromStart});
    minHeapForCost.enqueue(new Step(0, 0, discounts));

    const minCostToCity = Array.from(new Array(numberOfCities), () => new Array(discounts + 1).fill(Number.MAX_SAFE_INTEGER));
    minCostToCity[0][discounts] = 0;

    while (!minHeapForCost.isEmpty()) {
        const current = minHeapForCost.dequeue();

        if (current.costFromStart > minCostToCity[current.index][current.discounts]) {
            continue;
        }
        if (current.index === numberOfCities - 1) {
            return current.costFromStart;
        }

        for (let next of this.graph[current.index]) {

            if (minCostToCity[next.index][current.discounts] > current.costFromStart + next.edgeCost) {
                minCostToCity[next.index][current.discounts] = current.costFromStart + next.edgeCost;
                minHeapForCost.enqueue(new Step(next.index, minCostToCity[next.index][current.discounts], current.discounts));
            }
            if (current.discounts > 0
                    && minCostToCity[next.index][current.discounts - 1] > current.costFromStart + Math.floor(next.edgeCost / 2)) {
                minCostToCity[next.index][current.discounts - 1] = current.costFromStart + Math.floor(next.edgeCost / 2);
                minHeapForCost.enqueue(new Step(next.index, minCostToCity[next.index][current.discounts - 1], current.discounts - 1));
            }
        }
    }
    return this.NOT_POSSIBLE_TO_REACH_DESTINATION;
}

/**
 * @param {number[][]} highways
 * @return {void}
 */
function createGraph(highways) {
    for (let [from, to, toll] of highways) {
        this.graph[from].push(new Point(to, toll));
        this.graph[to].push(new Point(from, toll));
    }
}
