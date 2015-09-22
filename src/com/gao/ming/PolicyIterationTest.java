package com.gao.ming;

/*
 * Created on Aug 18, 2004
 */

import java.io.FileNotFoundException;

/**
 * @author bh
 *
 */
public class PolicyIterationTest {

    MarkovDecisionProcess mdp;

    /**
     * Constructor for PolicyIterationTest.
     * @param name
     */
    public static void main(String[] args) {
	try {
	    MDPFileParser parser = new MDPFileParser("./src/com/gao/ming/textbook.txt");
	    MarkovDecisionProcess mdp = parser.parse();

	    PolicyIteration pi = new PolicyIteration(mdp);

	    pi.solve();
	    mdp.dumpHTML(false);

	} catch (FileNotFoundException e){
	    System.out.println(e);
	}
    }



}
