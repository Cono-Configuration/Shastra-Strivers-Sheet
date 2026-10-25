# Design Twitter

## Problem Statement

Design a simplified version of Twitter where users can post tweets, follow or unfollow other users, and view the $10$ most recent tweets in their personal news feed.

Implement the `Twitter` class with the following methods:

- `Twitter()`: Initializes the Twitter object.
- `void postTweet(int userId, int tweetId)`: Composes a new tweet with ID `tweetId` by user `userId`. Each call to this function will be made with a unique `tweetId`.
- `List<Integer> getNewsFeed(int userId)`: Retrieves the $10$ most recent tweet IDs in the user's news feed. Each item in the news feed must be posted by users whom the user follows or by the user themself. Tweets must be ordered from most recent to least recent.
- `void follow(int followerId, int followeeId)`: The user with ID `followerId` starts following the user with ID `followeeId`.
- `void unfollow(int followerId, int followeeId)`: The user with ID `followerId` stops following the user with ID `followeeId`.

---

## Input Format

- The first line contains an integer $Q$, denoting the number of operations to execute.
- The next $Q$ lines each describe an operation in one of the following formats:
  - `postTweet <userId> <tweetId>`
  - `getNewsFeed <userId>`
  - `follow <followerId> <followeeId>`
  - `unfollow <followerId> <followeeId>`

---

## Output Format

- Print a single line containing $Q$ space-separated tokens corresponding to the results of each operation:
  - For operations that return `void` (`postTweet`, `follow`, `unfollow`), output `null`.
  - For `getNewsFeed`, output the list of up to $10$ tweet IDs enclosed in square brackets with comma-separated values (e.g. `[6,5]`, `[5]`, or `[]` if the feed is empty).

---

## Examples

### Example 1

**Input:**
```text
7
postTweet 1 5
getNewsFeed 1
follow 1 2
postTweet 2 6
getNewsFeed 1
unfollow 1 2
getNewsFeed 1
```

**Output:**
```text
null [5] null null [6,5] null [5]
```

**Explanation:**
1. `postTweet 1 5`: User $1$ posts tweet $5$ $\rightarrow$ outputs `null`.
2. `getNewsFeed 1`: User $1$'s feed contains their own tweet $\rightarrow$ outputs `[5]`.
3. `follow 1 2`: User $1$ follows User $2$ $\rightarrow$ outputs `null`.
4. `postTweet 2 6`: User $2$ posts tweet $6$ $\rightarrow$ outputs `null`.
5. `getNewsFeed 1`: User $1$ follows User $2$, so feed contains tweets from both users ordered by recency $\rightarrow$ outputs `[6,5]`.
6. `unfollow 1 2`: User $1$ unfollows User $2$ $\rightarrow$ outputs `null`.
7. `getNewsFeed 1`: Feed now excludes User $2$'s tweets $\rightarrow$ outputs `[5]`.

---

### Example 2

**Input:**
```text
3
getNewsFeed 1
postTweet 1 101
getNewsFeed 1
```

**Output:**
```text
[] null [101]
```

**Explanation:**
1. `getNewsFeed 1`: User $1$ has not posted or followed anyone yet $\rightarrow$ outputs `[]`.
2. `postTweet 1 101`: User $1$ posts tweet $101$ $\rightarrow$ outputs `null`.
3. `getNewsFeed 1`: Feed contains `[101]`.

---

## Constraints

- $1 \le Q \le 10^4$
- $1 \le \text{userId, followerId, followeeId} \le 500$
- $0 \le \text{tweetId} \le 10^4$
- All `tweetId` in `postTweet` calls are globally unique.
- In `follow` and `unfollow` calls, $\text{followerId} \ne \text{followeeId}$.
- At most $10^4$ total calls will be made to `postTweet`, `getNewsFeed`, `follow`, and `unfollow`.
