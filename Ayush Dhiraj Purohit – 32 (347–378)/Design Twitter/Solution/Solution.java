import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Solution {

    public static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[32768];
        private int head = 0;
        private int tail = 0;

        public FastScanner(InputStream in) {
            this.in = in;
        }

        private int read() throws IOException {
            if (head >= tail) {
                head = 0;
                tail = in.read(buffer, 0, buffer.length);
                if (tail <= 0) return -1;
            }
            return buffer[head++];
        }

        public String nextToken() throws IOException {
            int c = read();
            while (c <= 32) {
                if (c == -1) return null;
                c = read();
            }
            StringBuilder sb = new StringBuilder();
            while (c > 32) {
                sb.append((char) c);
                c = read();
            }
            return sb.toString();
        }

        public int nextInt() throws IOException {
            int c = read();
            while (c <= 32) {
                if (c == -1) return 0;
                c = read();
            }
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            int res = 0;
            while (c >= '0' && c <= '9') {
                res = res * 10 + (c - '0');
                c = read();
            }
            return res * sgn;
        }
    }

    public static class Tweet {
        public int id;
        public int time;
        public Tweet next;

        public Tweet(int id, int time, Tweet next) {
            this.id = id;
            this.time = time;
            this.next = next;
        }
    }

    public static class Twitter {
        private int globalTime;
        private final Map<Integer, Tweet> userTweets;
        private final Map<Integer, Set<Integer>> following;

        public Twitter() {
            this.globalTime = 0;
            this.userTweets = new HashMap<>();
            this.following = new HashMap<>();
        }

        public void postTweet(int userId, int tweetId) {
            int t = ++globalTime;
            Tweet head = userTweets.get(userId);
            userTweets.put(userId, new Tweet(tweetId, t, head));
        }

        public List<Integer> getNewsFeed(int userId) {
            PriorityQueue<Tweet> pq = new PriorityQueue<>((a, b) -> Integer.compare(b.time, a.time));

            Tweet ownTweet = userTweets.get(userId);
            if (ownTweet != null) {
                pq.add(ownTweet);
            }

            Set<Integer> followees = following.get(userId);
            if (followees != null) {
                for (int followeeId : followees) {
                    if (followeeId == userId) continue;
                    Tweet t = userTweets.get(followeeId);
                    if (t != null) {
                        pq.add(t);
                    }
                }
            }

            List<Integer> feed = new ArrayList<>(10);
            while (!pq.isEmpty() && feed.size() < 10) {
                Tweet top = pq.poll();
                feed.add(top.id);
                if (top.next != null) {
                    pq.add(top.next);
                }
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
            if (set != null) {
                set.remove(followeeId);
            }
        }
    }

    public static String formatFeed(List<Integer> feed) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < feed.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(feed.get(i));
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            FastScanner scanner = new FastScanner(System.in);
            PrintWriter writer = new PrintWriter(new BufferedOutputStream(System.out));

            String qStr = scanner.nextToken();
            if (qStr == null) {
                writer.flush();
                return;
            }

            int q = Integer.parseInt(qStr);
            Twitter twitter = new Twitter();

            for (int i = 0; i < q; i++) {
                String op = scanner.nextToken();
                if (op == null) break;

                if (i > 0) writer.print(" ");

                if (op.equalsIgnoreCase("postTweet")) {
                    int userId = scanner.nextInt();
                    int tweetId = scanner.nextInt();
                    twitter.postTweet(userId, tweetId);
                    writer.print("null");
                } else if (op.equalsIgnoreCase("getNewsFeed")) {
                    int userId = scanner.nextInt();
                    List<Integer> feed = twitter.getNewsFeed(userId);
                    writer.print(formatFeed(feed));
                } else if (op.equalsIgnoreCase("follow")) {
                    int followerId = scanner.nextInt();
                    int followeeId = scanner.nextInt();
                    twitter.follow(followerId, followeeId);
                    writer.print("null");
                } else if (op.equalsIgnoreCase("unfollow")) {
                    int followerId = scanner.nextInt();
                    int followeeId = scanner.nextInt();
                    twitter.unfollow(followerId, followeeId);
                    writer.print("null");
                }
            }

            writer.println();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
