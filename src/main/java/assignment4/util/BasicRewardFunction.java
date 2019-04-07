package assignment4.util;

import java.util.List;

import assignment4.BasicGridWorld;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;

public class BasicRewardFunction implements RewardFunction {

	int goalX;
	int goalY;
	List<double[]> wrecks;
	List<double[]> lights;

	public BasicRewardFunction(int goalX, int goalY, List<double[]> wrecks, List<double[]> lights) {
		this.goalX = goalX;
		this.goalY = goalY;
		this.wrecks = wrecks;
		this.lights = lights;
	}

	@Override
	public double reward(State s, GroundedAction a, State sprime) {

		// get location of agent in next state
		ObjectInstance agent = sprime.getFirstObjectOfClass(BasicGridWorld.CLASSAGENT);
		int ax = agent.getIntValForAttribute(BasicGridWorld.ATTX);
		int ay = agent.getIntValForAttribute(BasicGridWorld.ATTY);

		// are they at goal location?
		if (ax == this.goalX && ay == this.goalY) {
			return 500.;
		}
		
		for(double[] location : wrecks) {
			if(location[0] == ax && location[1] == ay) {
				return -50.;
			}
		}
		
		for(double[] location : lights) {
			if(location[0] == ax && location[1] == ay) {
				return -5.;
			}
		}

		return -0.05;
	}

}
