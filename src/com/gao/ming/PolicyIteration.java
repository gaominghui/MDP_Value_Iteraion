package com.gao.ming;

/*
 * Created on Aug 18, 2004
 *
 * $Log: PolicyIteration.java,v $
 * Revision 1.4  2004/08/24 14:41:24  bh
 * Changed the name from PolicyEvaluation to PolicyEvaluator.
 *
 * Revision 1.3  2004/08/23 23:34:40  bh
 * Source clean.
 *
 * Revision 1.2  2004/08/23 15:50:59  bh
 * Terminate states are now treated uniformly.  But then an if statement
 * is needed to bypass updating the actions of terminate states.
 *
 * Revision 1.1  2004/08/19 03:19:19  bh
 * Tested against the textbook 3x4 world.  The transition model is still
 * not ideal.
 *
 */


import java.util.Vector;

/**
 * Implement the policy iteration algorithm to solve a MDP.
 * 
 * @author bh
 *
 */
public class PolicyIteration {

	MarkovDecisionProcess mdp;
	PolicyEvaluator pe;
	
	public PolicyIteration(MarkovDecisionProcess mdp) {
		this.mdp = mdp;
		pe = new PolicyEvaluator(mdp);
	}
	
	public void generateRandomPolicy() {
		State s=mdp.getStartState();
		while(s != null) {
			mdp.setAction(s, mdp.getRandomAction());
			s = mdp.getNextState();
		}
		/*
		mdp.setAction(1,1, 3);
		mdp.setAction(2,1, 1);
		mdp.setAction(3,1, 3);
		mdp.setAction(1,2, 3);
		mdp.setAction(3,2, 0);
		mdp.setAction(1,3, 2);
		mdp.setAction(2,3, 2);
		mdp.setAction(3,3, 1);
		mdp.setAction(1,4, 1);
		*/
	}
	
	public int solve() {
		
		int numIterations = 0;
		
		//generateRandomPolicy();
		mdp.generateProperPolicy();
		
		boolean changed = false;
		
		do {
			
			changed = false;

			// Solve for current utilities given current policy
			pe.solve();
			
			State state = mdp.getStartState();
			while(state != null) {
				
				// Don't update the action of a terminate state
				if(state.terminate){
					state = mdp.getNextState();
					continue;
				}
		
				Action action=mdp.getStartAction();
				Action policyAction = mdp.getAction(state);
				
				double maxCurrentUtil = -1e30;
				double policyUtility = 0;
				Action maxAction = null;
				
				// The following while loop computes \max_a\sum T(s,a,s')U(s')
				while(action != null){
				
					Vector T = mdp.getTransition(state, action);
					int s = T.size();
					double nextUtil = 0;
					for(int i=0; i<s; ++i) {
						Transition t=(Transition)T.get(i);
						double prob=t.probability;
						State sPrime=t.nextState;
						nextUtil += (prob * mdp.getUtility(sPrime));
					}
					
					// It's the policy action, store the computed
					// utility.
					if(action == policyAction) {
						policyUtility = nextUtil;
					}
					
					if(nextUtil > maxCurrentUtil){
						maxCurrentUtil = nextUtil;
						maxAction = action;
					}
					
					action = mdp.getNextAction();
				}
				
				if(maxCurrentUtil > policyUtility){
					mdp.setAction(state, maxAction);
					changed = true;
				}
		
				state = mdp.getNextState();
				
			} //while (s!=null)
			
			numIterations ++;
			
			//System.out.println("num iterations:" + numIterations);
			//mdp.dumpHTML(false);
			
		} while(changed);
		
		return numIterations;
	}
	
}
