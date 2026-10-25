import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

public class gen {

    static class Tweet {
        int id;
        int time;
        Tweet next;

        Tweet(int id, int time, Tweet next) {
            this.id = id;
            this.time = time;
            this.next = next;
        }
    }

    static class TwitterHeap {
        private int globalTime = 0;
        private final Map<Integer, Tweet> userTweets = new HashMap<>();
        private final Map<Integer, Set<Integer>> following = new HashMap<>();

        public void postTweet(int userId, int tweetId) {
            int t = ++globalTime;
            Tweet head = userTweets.get(userId);
            userTweets.put(userId, new Tweet(tweetId, t, head));
        }

        public List<Integer> getNewsFeed(int userId) {
            PriorityQueue<Tweet> pq = new PriorityQueue<>((a, b) -> Integer.compare(b.time, a.time));

            Tweet own = userTweets.get(userId);
            if (own != null) pq.add(own);

            Set<Integer> followees = following.get(userId);
            if (followees != null) {
                for (int fid : followees) {
                    if (fid == userId) continue;
                    Tweet t = userTweets.get(fid);
                    if (t != null) pq.add(t);
                }
            }

            List<Integer> feed = new ArrayList<>(10);
            while (!pq.isEmpty() && feed.size() < 10) {
                Tweet top = pq.poll();
                feed.add(top.id);
                if (top.next != null) pq.add(top.next);
            }
            return feed;
        }

        public void follow(int followerId, int followeeId) {
            if (followerId == followeeId) return;
            following.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
        }

        public void unfollow(int followerId, int followeeId) {
            if (followerId == followeeId) return;
            Set<Integer> set = following.get(followerId);
            if (set != null) set.remove(followeeId);
        }
    }

    static class SimpleTweet {
        int id;
        int time;
        SimpleTweet(int id, int time) {
            this.id = id;
            this.time = time;
        }
    }

    static class TwitterNaive {
        private int globalTime = 0;
        private final Map<Integer, List<SimpleTweet>> userTweets = new HashMap<>();
        private final Map<Integer, Set<Integer>> following = new HashMap<>();

        public void postTweet(int userId, int tweetId) {
            int t = ++globalTime;
            userTweets.computeIfAbsent(userId, k -> new ArrayList<>()).add(new SimpleTweet(tweetId, t));
        }

        public List<Integer> getNewsFeed(int userId) {
            List<SimpleTweet> allTweets = new ArrayList<>();
            if (userTweets.containsKey(userId)) {
                allTweets.addAll(userTweets.get(userId));
            }
            Set<Integer> followees = following.get(userId);
            if (followees != null) {
                for (int fid : followees) {
                    if (fid != userId && userTweets.containsKey(fid)) {
                        allTweets.addAll(userTweets.get(fid));
                    }
                }
            }
            allTweets.sort((a, b) -> Integer.compare(b.time, a.time));
            List<Integer> feed = new ArrayList<>(10);
            for (int i = 0; i < Math.min(10, allTweets.size()); i++) {
                feed.add(allTweets.get(i).id);
            }
            return feed;
        }

        public void follow(int followerId, int followeeId) {
            if (followerId == followeeId) return;
            following.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
        }

        public void unfollow(int followerId, int followeeId) {
            if (followerId == followeeId) return;
            Set<Integer> set = following.get(followerId);
            if (set != null) set.remove(followeeId);
        }
    }

    static class Op {
        String type;
        int arg1;
        int arg2;

        Op(String type, int arg1, int arg2) {
            this.type = type;
            this.arg1 = arg1;
            this.arg2 = arg2;
        }
    }

    static String formatFeed(List<Integer> feed) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < feed.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(feed.get(i));
        }
        sb.append("]");
        return sb.toString();
    }

    static void writeTestCase(File inFile, File outFile, List<Op> ops) throws IOException {
        try (PrintWriter inPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(inFile)))) {
            inPw.println(ops.size());
            for (Op op : ops) {
                if (op.type.equals("postTweet") || op.type.equals("follow") || op.type.equals("unfollow")) {
                    inPw.println(op.type + " " + op.arg1 + " " + op.arg2);
                } else if (op.type.equals("getNewsFeed")) {
                    inPw.println(op.type + " " + op.arg1);
                }
            }
            inPw.flush();
        }

        TwitterHeap th = new TwitterHeap();
        TwitterNaive tn = new TwitterNaive();
        List<String> results = new ArrayList<>();

        for (Op op : ops) {
            if (op.type.equals("postTweet")) {
                th.postTweet(op.arg1, op.arg2);
                tn.postTweet(op.arg1, op.arg2);
                results.add("null");
            } else if (op.type.equals("getNewsFeed")) {
                List<Integer> feedH = th.getNewsFeed(op.arg1);
                List<Integer> feedN = tn.getNewsFeed(op.arg1);
                if (!feedH.equals(feedN)) {
                    throw new IllegalStateException("Verification failure: heap feed " + feedH + " != naive feed " + feedN);
                }
                results.add(formatFeed(feedH));
            } else if (op.type.equals("follow")) {
                th.follow(op.arg1, op.arg2);
                tn.follow(op.arg1, op.arg2);
                results.add("null");
            } else if (op.type.equals("unfollow")) {
                th.unfollow(op.arg1, op.arg2);
                tn.unfollow(op.arg1, op.arg2);
                results.add("null");
            }
        }

        try (PrintWriter outPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outFile)))) {
            for (int i = 0; i < results.size(); i++) {
                if (i > 0) outPw.print(" ");
                outPw.print(results.get(i));
            }
            outPw.println();
            outPw.flush();
        }
    }

    public static void main(String[] args) throws IOException {
        File cur = new File(".");
        File baseDir;
        if (new File(cur, "Testcases").exists() || new File(cur, "Generator").exists() || new File(cur, "Problem.md").exists()) {
            baseDir = cur;
        } else if (new File(cur, "../Testcases").exists() || new File(cur, "../Problem.md").exists()) {
            baseDir = new File(cur, "..");
        } else if (new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Design Twitter").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Design Twitter");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Design Twitter");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(20261025);
        int tweetCounter = 1;

        for (int test = 0; test < 100; test++) {
            List<Op> ops = new ArrayList<>();

            if (test == 0) {
                // TUF Example 1
                ops.add(new Op("postTweet", 1, 5));
                ops.add(new Op("getNewsFeed", 1, 0));
                ops.add(new Op("follow", 1, 2));
                ops.add(new Op("postTweet", 2, 6));
                ops.add(new Op("getNewsFeed", 1, 0));
                ops.add(new Op("unfollow", 1, 2));
                ops.add(new Op("getNewsFeed", 1, 0));
            } else if (test == 1) {
                // TUF Example 2: empty feed
                ops.add(new Op("getNewsFeed", 1, 0));
                ops.add(new Op("postTweet", 1, 101));
                ops.add(new Op("getNewsFeed", 1, 0));
            } else if (test == 2) {
                // Exactly 10 tweets
                for (int i = 1; i <= 10; i++) {
                    ops.add(new Op("postTweet", 1, i));
                }
                ops.add(new Op("getNewsFeed", 1, 0));
            } else if (test == 3) {
                // 15 tweets: only top 10 returned
                for (int i = 1; i <= 15; i++) {
                    ops.add(new Op("postTweet", 1, i));
                }
                ops.add(new Op("getNewsFeed", 1, 0));
            } else if (test == 4) {
                // Follow someone who hasn't posted
                ops.add(new Op("postTweet", 1, 10));
                ops.add(new Op("follow", 1, 2));
                ops.add(new Op("getNewsFeed", 1, 0));
            } else if (test == 5) {
                // Unfollow someone never followed
                ops.add(new Op("unfollow", 1, 2));
                ops.add(new Op("getNewsFeed", 1, 0));
            } else if (test == 6) {
                // Mutual follow
                ops.add(new Op("postTweet", 1, 11));
                ops.add(new Op("postTweet", 2, 22));
                ops.add(new Op("follow", 1, 2));
                ops.add(new Op("follow", 2, 1));
                ops.add(new Op("getNewsFeed", 1, 0));
                ops.add(new Op("getNewsFeed", 2, 0));
            } else if (test == 7) {
                // Star graph: user 1 follows 15 users
                for (int u = 2; u <= 15; u++) {
                    ops.add(new Op("postTweet", u, u * 100));
                    ops.add(new Op("follow", 1, u));
                }
                ops.add(new Op("getNewsFeed", 1, 0));
            } else if (test == 8) {
                // Interleaved tweets
                for (int i = 1; i <= 5; i++) {
                    ops.add(new Op("postTweet", 1, i * 2));
                    ops.add(new Op("postTweet", 2, i * 2 + 1));
                }
                ops.add(new Op("follow", 1, 2));
                ops.add(new Op("getNewsFeed", 1, 0));
            } else if (test == 9) {
                // Rapid follow/unfollow
                ops.add(new Op("postTweet", 2, 50));
                ops.add(new Op("follow", 1, 2));
                ops.add(new Op("getNewsFeed", 1, 0));
                ops.add(new Op("unfollow", 1, 2));
                ops.add(new Op("getNewsFeed", 1, 0));
                ops.add(new Op("follow", 1, 2));
                ops.add(new Op("getNewsFeed", 1, 0));
            } else if (test < 30) {
                // Small cases Q in [20, 80], users in [1, 10]
                int q = 20 + (test - 10) * 3;
                int maxUsers = 5 + rng.nextInt(5);
                for (int i = 0; i < q; i++) {
                    int opType = rng.nextInt(4);
                    int u1 = 1 + rng.nextInt(maxUsers);
                    if (opType == 0) {
                        ops.add(new Op("postTweet", u1, tweetCounter++));
                    } else if (opType == 1) {
                        ops.add(new Op("getNewsFeed", u1, 0));
                    } else if (opType == 2) {
                        int u2 = 1 + rng.nextInt(maxUsers);
                        while (u2 == u1) u2 = 1 + rng.nextInt(maxUsers);
                        ops.add(new Op("follow", u1, u2));
                    } else {
                        int u2 = 1 + rng.nextInt(maxUsers);
                        while (u2 == u1) u2 = 1 + rng.nextInt(maxUsers);
                        ops.add(new Op("unfollow", u1, u2));
                    }
                }
            } else if (test < 60) {
                // Medium cases Q in [100, 1000], users in [1, 50]
                int q = 100 + (test - 30) * 30;
                int maxUsers = 15 + rng.nextInt(35);
                for (int i = 0; i < q; i++) {
                    int opType = rng.nextInt(10);
                    int u1 = 1 + rng.nextInt(maxUsers);
                    if (opType < 4) {
                        ops.add(new Op("postTweet", u1, tweetCounter++));
                    } else if (opType < 7) {
                        ops.add(new Op("getNewsFeed", u1, 0));
                    } else if (opType < 9) {
                        int u2 = 1 + rng.nextInt(maxUsers);
                        if (u2 == u1) u2 = (u1 % maxUsers) + 1;
                        ops.add(new Op("follow", u1, u2));
                    } else {
                        int u2 = 1 + rng.nextInt(maxUsers);
                        if (u2 == u1) u2 = (u1 % maxUsers) + 1;
                        ops.add(new Op("unfollow", u1, u2));
                    }
                }
            } else if (test < 80) {
                // Large cases Q in [2000, 5000], users in [1, 200]
                int q = 2000 + (test - 60) * 150;
                int maxUsers = 50 + rng.nextInt(150);
                for (int i = 0; i < q; i++) {
                    int opType = rng.nextInt(10);
                    int u1 = 1 + rng.nextInt(maxUsers);
                    if (opType < 4) {
                        ops.add(new Op("postTweet", u1, tweetCounter++));
                    } else if (opType < 7) {
                        ops.add(new Op("getNewsFeed", u1, 0));
                    } else if (opType < 9) {
                        int u2 = 1 + rng.nextInt(maxUsers);
                        if (u2 == u1) u2 = (u1 % maxUsers) + 1;
                        ops.add(new Op("follow", u1, u2));
                    } else {
                        int u2 = 1 + rng.nextInt(maxUsers);
                        if (u2 == u1) u2 = (u1 % maxUsers) + 1;
                        ops.add(new Op("unfollow", u1, u2));
                    }
                }
            } else {
                // Max constraint cases Q = 10000, users in [1, 500]
                int q = 10000;
                int maxUsers = 500;
                for (int i = 0; i < q; i++) {
                    int opType;
                    if (test == 80) {
                        // High getNewsFeed
                        opType = (rng.nextInt(10) < 7) ? 1 : rng.nextInt(4);
                    } else if (test == 81) {
                        // High postTweet
                        opType = (rng.nextInt(10) < 7) ? 0 : rng.nextInt(4);
                    } else if (test == 82) {
                        // High follow/unfollow
                        opType = (rng.nextBoolean()) ? 2 : 3;
                    } else {
                        opType = rng.nextInt(4);
                    }

                    int u1 = 1 + rng.nextInt(maxUsers);
                    if (opType == 0) {
                        ops.add(new Op("postTweet", u1, tweetCounter++));
                    } else if (opType == 1) {
                        ops.add(new Op("getNewsFeed", u1, 0));
                    } else if (opType == 2) {
                        int u2 = 1 + rng.nextInt(maxUsers);
                        if (u2 == u1) u2 = (u1 % maxUsers) + 1;
                        ops.add(new Op("follow", u1, u2));
                    } else {
                        int u2 = 1 + rng.nextInt(maxUsers);
                        if (u2 == u1) u2 = (u1 % maxUsers) + 1;
                        ops.add(new Op("unfollow", u1, u2));
                    }
                }
            }

            String inFileName = String.format("input%02d.txt", test);
            String outFileName = String.format("output%02d.txt", test);

            File inFile = new File(inDir, inFileName);
            File outFile = new File(outDir, outFileName);

            writeTestCase(inFile, outFile, ops);
        }

        System.out.println("Generated 100 testcases successfully.");
    }
}
