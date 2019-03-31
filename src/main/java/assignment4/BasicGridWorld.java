package assignment4;

import java.util.List;

import assignment4.util.AgentPainter;
import assignment4.util.AtLocation;
import assignment4.util.BadPainter;
import assignment4.util.LocationPainter;
import assignment4.util.Movement;
import assignment4.util.WallPainter;
import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.core.objects.MutableObjectInstance;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.MutableState;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.SADomain;
import burlap.oomdp.visualizer.StateRenderLayer;
import burlap.oomdp.visualizer.Visualizer;

public class BasicGridWorld implements DomainGenerator {

	public static final String ATTX = "x";
	public static final String ATTY = "y";

	public static final String CLASSAGENT = "agent";
	public static final String CLASSLOCATION = "location";

	public static final String ACTIONNORTH = "north";
	public static final String ACTIONSOUTH = "south";
	public static final String ACTIONEAST = "east";
	public static final String ACTIONWEST = "west";

	public static final String PFAT = "at";

	// ordered so first dimension is x
	protected int[][] map ;
	protected static int mapx;
	protected static int mapy;
	protected static List<double[]> wrecks;
	protected static List<double[]> lights;
	
	public BasicGridWorld(int[][] map,int mapx, int mapy, List<double[]> wrecks, List<double[]> lights){
		this.map = map;
		this.mapx = mapx;
		this.mapy = mapy;
		this.wrecks = wrecks;
		this.lights = lights;
	}


	@Override
	public Domain generateDomain() {

		SADomain domain = new SADomain();

		Attribute xatt = new Attribute(domain, ATTX,
				Attribute.AttributeType.INT);
		xatt.setLims(0, mapx);

		Attribute yatt = new Attribute(domain, ATTY,
				Attribute.AttributeType.INT);
		yatt.setLims(0, mapy);

		ObjectClass agentClass = new ObjectClass(domain, CLASSAGENT);
		agentClass.addAttribute(xatt);
		agentClass.addAttribute(yatt);

		ObjectClass locationClass = new ObjectClass(domain, CLASSLOCATION);
		locationClass.addAttribute(xatt);
		locationClass.addAttribute(yatt);

		for(int i =0; i<wrecks.size(); i++) {
			ObjectClass wreckClass = new ObjectClass(domain, "wreck" + Integer.toString(i));
			wreckClass.addAttribute(xatt);
			wreckClass.addAttribute(yatt);
		}

		for(int i =0; i<lights.size(); i++) {
			ObjectClass lightClass = new ObjectClass(domain, "light" + Integer.toString(i));
			lightClass.addAttribute(xatt);
			lightClass.addAttribute(yatt);
		}

		new Movement(ACTIONNORTH, domain, 0, map);
		new Movement(ACTIONSOUTH, domain, 1, map);
		new Movement(ACTIONEAST, domain, 2, map);
		new Movement(ACTIONWEST, domain, 3, map);

		new AtLocation(domain);

		return domain;
	}

	public static State getExampleState(Domain domain) {
		State s = new MutableState();
		ObjectInstance agent = new MutableObjectInstance(
				domain.getObjectClass(CLASSAGENT), "agent0");
		agent.setValue(ATTX, 0);
		agent.setValue(ATTY, 0);

		s.addObject(agent);

		ObjectInstance location = new MutableObjectInstance(
				domain.getObjectClass(CLASSLOCATION), "location0");
		location.setValue(ATTX, mapx);
		location.setValue(ATTY, mapy);

		s.addObject(location);
		

		for(int i =0; i<wrecks.size(); i++) {
			ObjectInstance wreck = new MutableObjectInstance(
					domain.getObjectClass("wreck" + Integer.toString(i)), "wreck" + Integer.toString(i));
			wreck.setValue(ATTX, wrecks.get(i)[0]);
			wreck.setValue(ATTY, wrecks.get(i)[1]);
			s.addObject(wreck);
		}

		for(int i =0; i<lights.size(); i++) {
			ObjectInstance light = new MutableObjectInstance(
					domain.getObjectClass("light" + Integer.toString(i)), "light" + Integer.toString(i));
			light.setValue(ATTX, lights.get(i)[0]);
			light.setValue(ATTY, lights.get(i)[1]);
			s.addObject(light);
		}

		return s;
	}

	public StateRenderLayer getStateRenderLayer() {
		StateRenderLayer rl = new StateRenderLayer();
		rl.addStaticPainter(new WallPainter(map));
		rl.addObjectClassPainter(CLASSLOCATION, new LocationPainter(map));
		rl.addObjectClassPainter(CLASSAGENT, new AgentPainter(map));

		for(int i =0; i<wrecks.size(); i++) {
			rl.addObjectClassPainter("wreck" + Integer.toString(i), new BadPainter(map));
		}

		for(int i =0; i<lights.size(); i++) {
			rl.addObjectClassPainter("light" + Integer.toString(i), new BadPainter(map));
		}

		return rl;
	}

	public Visualizer getVisualizer() {
		return new Visualizer(this.getStateRenderLayer());
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}



}
