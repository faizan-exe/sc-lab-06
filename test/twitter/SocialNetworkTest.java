package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TODO: Testing strategy
     * guessFollowsGraph:
     * Partitioning based on quantity of tweets:
     *  1. No Tweets
     *  2. Exactly one tweet
     *  3. More than one tweet
     *  Partitioning based on authors:
     *  1. Author not being a follower
     *  2. Author being a followee
     * Partitioning based on cases:
     *  1. Uppercase
     *  2. Lowercase
     *  3. ToggleCase
     *  
     *  influencers:
     *  Partition based on quantity of followees
     *  1. Empty graph
     *  2. One member graph
     *  3. More than one member graph
     *
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Tweet tweet1 = new Tweet(1, "kumar", "I don't mention anyone", d1);
    private static final Tweet tweet2 = new Tweet(2, "Rob", "Thanks to @keVin @john @joseph", d1);
    private static final Tweet tweet3 = new Tweet(3, "LisA", "Thanks to @kevin @JOHN @kumar @rob @joseph", d1);
    private static final Tweet tweet4 = new Tweet(4, "roB", "Hi @chad", d1);
    private static final Tweet tweet5 = new Tweet(5, "kumar", "@kumar is great", d1);
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGuessFollowsGraphNoTweetsEmptyGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph when no tweets are present", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphSingleTweetNoFollowingNonEmptyGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1));
        
        assertFalse("expected non-empty graph", followsGraph.isEmpty());
        assertTrue("expected a single node for kumar", followsGraph.containsKey("kumar"));
        assertEquals("expected kumar to follow none", 0, followsGraph.get("kumar").size());
    }
    
    @Test
    public void testGuessFollowsGraphSingleTweetNonEmptyGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet2));
        
        assertFalse("expected non empty graph", followsGraph.isEmpty());
        assertEquals("map should contain a single key", 1, followsGraph.keySet().size());
        assertTrue("expected rob to be a node in the network", followsGraph.containsKey("rob"));
        assertEquals("rob should follow three people", 3, followsGraph.get("rob").size());
    }
      
    @Test
    public void testGuessFollowsGraphMultipleTweetsNonEmptyGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet2, tweet3, tweet4));
        
        assertFalse("expected non empty graph", followsGraph.isEmpty());
        
        assertEquals("map should contain 2 keys", 2, followsGraph.keySet().size());
        assertTrue("map should contain a Rob", followsGraph.containsKey("rob"));
        assertTrue("map should contain a Lisa", followsGraph.containsKey("lisa"));

        assertEquals("rob should follow 4 people", 4, followsGraph.get("rob").size());
        assertEquals("lisa should follow 5 people", 5, followsGraph.get("lisa").size());
    }
    
    @Test
    public void testGuessFollowsGraphMultipleTweetsUserFollowingHimselfNonEmptyGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet5));
        
        assertFalse("expected non-empty graph", followsGraph.isEmpty());
        assertEquals("has a single node", 1, followsGraph.keySet().size());
        assertTrue("kumar cannot follow himself", followsGraph.get("kumar").isEmpty());
    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }
    
    @Test
    public void testInfluencersSingleEntry() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("mark", new HashSet<>(Arrays.asList("ralph", "johnson")));
        List<String> influencers = SocialNetwork.influencers(graph);
        
        assertEquals("expected a singleton list", 1, influencers.size());
        assertEquals("expected mark to be the influencer","mark", influencers.get(0));
    }
    
    @Test
    public void testInfluencersMoreThanOneEqualFollowers() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("mark", new HashSet<>(Arrays.asList("ralph", "johnson")));
        graph.put("roy", new HashSet<>(Arrays.asList("chad", "john")));
        
        List<String> influencers = SocialNetwork.influencers(graph);
        assertEquals("expected two element list", 2, influencers.size());
    }
    
    @Test
    public void testInfluencersMoreThanOneNonEqualFollowers() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("mark", new HashSet<>(Arrays.asList("ralph", "johnson")));
        graph.put("patrick", new HashSet<>(Arrays.asList("rob", "pamela", "jason")));
        graph.put("roy", new HashSet<>(Arrays.asList("chad", "john")));
        
        List<String> influencers = SocialNetwork.influencers(graph);
        
        assertEquals("expected three element list", 3, influencers.size());
        assertEquals("expected patrick to be the influencer", "patrick", influencers.get(0));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}