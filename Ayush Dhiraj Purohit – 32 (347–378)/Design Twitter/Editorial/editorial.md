# Editorial: Design Twitter

## 1. Problem Overview

We are tasked with designing an in-memory social feed system resembling a simplified Twitter service. The platform must support four core operations:
1. `postTweet(userId, tweetId)`: A user posts a tweet.
2. `getNewsFeed(userId)`: Retrieve the $10$ most recent tweet IDs posted by the user or by users they follow, ordered from most recent to least recent.
3. `follow(followerId, followeeId)`: A user starts following another user.
4. `unfollow(followerId, followeeId)`: A user stops following another user.

---

## 2. Data Structure Design

### 2.1 Representing Tweets Chronologically
Every tweet must have a strict, globally unique chronological order. We can maintain a global integer counter `time = 0` that increments on every `postTweet` call.

For each user, their tweet history is naturally ordered by time (newest to oldest). We can represent a user's tweets as a **singly linked list**:
```java
static class Tweet {
    int id;
    int time;
    Tweet next; // Points to the next older tweet by the same user

    Tweet(int id, int time, Tweet next) {
        this.id = id;
        this.time = time;
        this.next = next;
    }
}
```
- When a user posts a tweet, we prepend a new `Tweet` node to their list.
- Prepending takes $\mathcal{O}(1)$ time.
- The head of the list always points to the user's most recent tweet.

### 2.2 Tracking Follow Relationships
Each user can follow multiple other users. We store these relationships in a hash map:
```java
Map<Integer, Set<Integer>> following = new HashMap<>();
```
- `follow(followerId, followeeId)` adds `followeeId` to `following[followerId]`.
- `unfollow(followerId, followeeId)` removes `followeeId` from `following[followerId]`.
- Both operations execute in $\mathcal{O}(1)$ average time.

---

## 3. Generating the News Feed (K-Way Merge via Max-Heap)

The news feed of user $U$ aggregates tweets from:
- The user $U$ themself.
- Every user $V \in \text{following}[U]$.

Notice that:
- User $U$ has a sorted list of tweets (most recent at head).
- Each followee $V$ has a sorted list of tweets (most recent at head).
- We want to retrieve the top $10$ overall most recent tweets across these $K = |\text{following}[U]| + 1$ sorted lists.

This is the classic **Merge K Sorted Lists** problem:
1. Initialize a **Max-Heap** (Priority Queue) comparing `Tweet.time` in descending order.
2. Insert the head tweet of user $U$ and each followee $V$ into the Max-Heap (ignoring users with no tweets).
3. Loop up to $10$ times:
   - If the heap is empty, break.
   - Extract the root tweet `top = heap.poll()`.
   - Append `top.id` to the result list.
   - If `top.next != null` (there is an older tweet from the same author), push `top.next` into the Max-Heap.
4. Return the aggregated list of up to $10$ tweet IDs.

Because we only extract at most $10$ tweets, the heap traversal takes at most $10$ extract and insert operations, making the feed generation exceptionally fast.

---

## 4. Step-by-Step Dry Run

Consider Example 1:
1. `postTweet(1, 5)`:
   - `time = 1`. User 1's tweet chain: `[id: 5, t: 1]`.
   - Output: `null`.
2. `getNewsFeed(1)`:
   - Heap contains: `[id: 5, t: 1]`.
   - Extract `5`.
   - Output: `[5]`.
3. `follow(1, 2)`:
   - User 1 now follows User 2: `following[1] = {2}`.
   - Output: `null`.
4. `postTweet(2, 6)`:
   - `time = 2`. User 2's tweet chain: `[id: 6, t: 2]`.
   - Output: `null`.
5. `getNewsFeed(1)`:
   - User 1 checks their own tweets (`id: 5, t: 1`) and User 2's tweets (`id: 6, t: 2`).
   - Heap contains: `[id: 6, t: 2]` and `[id: 5, t: 1]`.
   - Extract `6` ($t = 2$), then extract `5` ($t = 1$).
   - Output: `[6,5]`.
6. `unfollow(1, 2)`:
   - Remove 2 from `following[1]`.
   - Output: `null`.
7. `getNewsFeed(1)`:
   - Heap contains only User 1's own tweet `[id: 5, t: 1]`.
   - Output: `[5]`.

Total output: `null [5] null null [6,5] null [5]`.

---

## 5. Edge Cases & Boundary Analysis

1. **User has No Tweets & Follows Nobody:**
   - The heap remains empty; `getNewsFeed` returns `[]`.
2. **User Follows Accounts with No Tweets:**
   - Non-existent or empty tweet chains are skipped when initializing the heap.
3. **More than 10 Total Tweets:**
   - The extraction loop terminates after collecting exactly $10$ tweets.
4. **Idempotent Follows & Unfollows:**
   - `Set.add` and `Set.remove` naturally handle repeated follows or unfollowing non-followed users without exceptions.
5. **A User Cannot Unfollow Themself:**
   - Even if requested, own tweets are retrieved directly from `userTweets.get(userId)`, completely independent of the `following` set.
6. **Maximum Scale Constraints:**
   - Up to $10^4$ operations across $\le 500$ users.
   - At most $500$ followees per user $\rightarrow$ Max-Heap size is bounded by $501$.
   - Feed generation requires at most $\mathcal{O}(500 + 10 \log 500) \approx 600$ operations, executing in under $0.05\text{ ms}$.

---

## 6. Complexity Analysis

- **`postTweet(userId, tweetId)`:**
  - **Time:** $\mathcal{O}(1)$ to allocate and prepend the new tweet node.
- **`follow(followerId, followeeId)`:**
  - **Time:** $\mathcal{O}(1)$ average hash set insertion.
- **`unfollow(followerId, followeeId)`:**
  - **Time:** $\mathcal{O}(1)$ average hash set deletion.
- **`getNewsFeed(userId)`:**
  - **Time:** $\mathcal{O}(F \log F)$ or $\mathcal{O}(F + 10 \log F)$, where $F \le 500$ is the number of users followed.
- **Space Complexity:** $\mathcal{O}(T + E)$
  - $T$ is the total number of tweets posted (nodes in memory).
  - $E$ is the total number of follow edges in the social graph.
