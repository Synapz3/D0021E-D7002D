package Sim;

// An example of how to build a topology and starting the simulation engine

public class Run {
	public static void main (String [] args)
	{
 		//Creates two links
		Link link1 = new Link(1);
		Link link2 = new Link(1);
		Link R1TOR2 = new Link(1);
		
		// Create two end hosts that will be
		// communicating via the router
		//Node host1 = new Node(1,1);
		//Node host2 = new Node(2,1);

		//CBR
		Generator_CBR host1 = new Generator_CBR(1,1);
        Node host2 = new Node(2,1);

		//NORMAL
		//Generator_NORMAL host1 = new Generator_NORMAL(1,1);
		//Sink host2 = new Sink(2,1);

		//POISSON
		//Generator_POISSON host1 = new Generator_POISSON(1,1);
		//Sink host2 = new Sink(2,1);

		//Connect links to hosts
		host1.setPeer(link1);
		host2.setPeer(link2);


		// Creates as router and connect
		// links to it. Information about 
		// the host connected to the other
		// side of the link is also provided
		// Note. A switch is created in same way using the Switch class
		Router R1 = new Router(1,3, 5);
		Router R2 = new Router(2,2,2);

		R1.connectInterfaceToNode(1, link1, host1);
		R2.connectInterfaceToNode(1, link2, host2);

		R1.connectInterfaceToRouter(0, R1TOR2, R2);
		R2.connectInterfaceToRouter(0, R1TOR2, R1);

		System.out.println("Connected link with first router: " + R1TOR2._connectorA.toString() +
				" and second router: " + R1TOR2._connectorB.toString());


		R1.sendRIP();

		// Start the simulation engine and of we go!
		Thread t=new Thread(SimEngine.instance());

		t.start();
		try
		{
			t.join();
		}
		catch (Exception e)
		{
			System.out.println("The motor seems to have a problem, time for service?" +  e.toString());
		}
	}
}
