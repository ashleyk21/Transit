package transit;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 */
public class Transit {
	private TNode trainZero; // a reference to the zero node in the train layer

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */ 
	public Transit() { trainZero = null; }

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */
	public Transit(TNode tz) { trainZero = tz; }
	
	/*
	 * Getter method for trainZero
	 *
	 * DO NOT remove from this file.
	 */
	public TNode getTrainZero () {
		return trainZero;
	}

	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0. Store the zero node in the train layer in
	 * the instance variable trainZero.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 */
	public void makeList(int[] trainStations, int[] busStops, int[] locations) {

	    TNode walk, bus, train;
		TNode walkLoci, busLoci, trainLoci;

		walk = new TNode();
		bus = new TNode(0, null, walk);
		train = new TNode(0, null, bus);

		walkLoci = walk;
		busLoci = bus;
		trainLoci = train;
		trainZero = train;

		int w = 0, b = 0, t = 0;
		while(w<locations.length){
			walkLoci.setNext(new TNode());
			walkLoci = walkLoci.getNext();
			walkLoci.setLocation(locations[w]);
			

			if(b<busStops.length && busStops[b] == locations[w]){
				busLoci.setNext(new TNode());
				busLoci = busLoci.getNext();
				busLoci.setLocation(busStops[b]);
				busLoci.setDown(walkLoci);
				

				if(t<trainStations.length && trainStations[t] == busStops[b]){
					trainLoci.setNext(new TNode());
					trainLoci = trainLoci.getNext();
					trainLoci.setLocation(trainStations[t]);
					trainLoci.setDown(busLoci);
					
					t++;
				}
				b++;
			}
			w++;
		}
		
	}
	
	/**
	 * Modifies the layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param station The location of the train station to remove
	 */
	public void removeTrainStation(int station) {
	    if (trainZero.getLocation() == station) {
			trainZero = trainZero.getNext();
			return;
		}
		TNode previous = trainZero;
		TNode temp = trainZero.getNext();

        while (temp != null) {
            if (temp.getLocation() == station) {
                    previous.setNext(temp.getNext());
            }
			
			previous = temp;
			temp = temp.getNext();
	}
}

	/**
	 * Modifies the layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param busStop The location of the bus stop to add
	 */
	
	public void addBusStop(int busStop) {
		TNode busZero = this.getTrainZero().getDown ();
		TNode searchResult = searchStop (busStop, busZero);
		if (searchResult == null) {
			TNode walkZero = this.getTrainZero().getDown().getDown();
			TNode walkLocation = searchStop (busStop, walkZero);
			if (walkLocation != null){
				TNode currentBus = busZero;
				while (currentBus.getNext() != null){
					if (busStop > currentBus.getLocation() && busStop < currentBus.getNext().getLocation() ) {
						TNode newStop = new TNode (busStop);
						newStop.setDown(walkLocation);
						newStop.setNext (currentBus.getNext());
						currentBus.setNext(newStop);
						break;
					}
					//Advance bus
					currentBus = currentBus.getNext();
				}
				if( currentBus.getNext() == null){
					// If we make it here, we are at the last stop
					TNode newStop = new TNode (busStop);
					newStop.setDown(walkLocation);
					//set current's next bus stop to newstop
					currentBus.setNext(newStop);
				}
			}
		}
	}
	
	private TNode searchStop (int location, TNode node) {
		// Here we will search horizontally to return a node
		TNode searchResult = null;
		TNode current = node;
		// start horizontal search
		while (current != null){
			if (current.getLocation() == location) {
			// set search result to current node
			searchResult = current;
			break;
			}
			else {
				// Advance node
				current = current.getNext();
		}
		}
		return searchResult;
	}
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param destination An int representing the destination
	 * @return
	 */
	public ArrayList<TNode> bestPath(int destination) {

	    ArrayList<TNode> journey = new ArrayList<>();

		TNode temp0 = trainZero.getNext();
		TNode prev0 = trainZero;
		
		while (temp0!= null && temp0.getLocation() <= destination) {
			journey.add(prev0);
			if (prev0.getLocation()==destination) {
				return journey;
			}
			prev0 = temp0;
			temp0 = temp0.getNext();
		}

		journey.add(prev0);

		TNode temp1 = prev0.getDown().getNext();
		TNode prev1 = prev0.getDown();
		
		while (temp1!= null && temp1.getLocation() <= destination) {
			journey.add(prev1);
			if (prev1.getLocation()==destination) {				
				return journey;
			}
			prev1 = temp1;
			temp1 = temp1.getNext();
		}

		journey.add(prev1);
		
		TNode temp2 = prev1.getDown().getNext();
		TNode prev2 = prev1.getDown();
		
		while (temp2!= null && temp2.getLocation() <= destination) {
			journey.add(prev2);
			if (prev2.getLocation()==destination) {
				return journey;
			}
			prev2 = temp2;
			temp2 = temp2.getNext();
		}

		journey.add(prev2);

		return journey;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @return A reference to the train zero node of a deep copy
	 */
	public TNode duplicate() {

	    ArrayList<Integer> trainStations = new ArrayList<Integer>();
		TNode temp = trainZero.getNext();
		while (temp!=null) {
			trainStations.add(temp.getLocation());
			temp = temp.getNext();
		}

		ArrayList<Integer> busStops = new ArrayList<Integer>();
		TNode temp1 = trainZero.getDown().getNext();
		while (temp1!=null) {
			busStops.add(temp1.getLocation());
			temp1 = temp1.getNext();
		}

		ArrayList<Integer> locations = new ArrayList<Integer>();
		TNode temp2 = trainZero.getDown().getDown().getNext();
		while (temp2!=null) {
			locations.add(temp2.getLocation());
			temp2 = temp2.getNext();
		}

		

		TNode walk, bus, train;
		TNode walkLoci, busLoci, trainLoci;

		walk = new TNode();
		bus = new TNode(0, null, walk);
		train = new TNode(0, null, bus);

		walkLoci = walk;
		busLoci = bus;
		trainLoci = train;
		TNode dupes = train;

		int w = 0, b = 0, t = 0;
		while(w<locations.size()){
			walkLoci.setNext(new TNode());
			walkLoci = walkLoci.getNext();
			walkLoci.setLocation(locations.get(w));
			

			if(b<busStops.size() && busStops.get(b) == locations.get(w)){
				busLoci.setNext(new TNode());
				busLoci = busLoci.getNext();
				busLoci.setLocation(busStops.get(b));
				busLoci.setDown(walkLoci);
				

				if(t<trainStations.size() && trainStations.get(t) == busStops.get(b)){
					trainLoci.setNext(new TNode());
					trainLoci = trainLoci.getNext();
					trainLoci.setLocation(trainStations.get(t));
					trainLoci.setDown(busLoci);
					
					t++;
				}
				b++;
			}
			w++;
		}
		return dupes;
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public void addScooter(int[] scooterStops) {

	    TNode bus = trainZero.getDown();
		TNode walk = trainZero.getDown().getDown();
        TNode scooter = new TNode(0, null, walk);
        bus.setDown(scooter);
        bus = bus.getNext();

        int counter = 0;
        while (counter < scooterStops.length && walk.getNext() != null) {
            walk = walk.getNext();
            if (walk.getLocation() == scooterStops[counter]) {
                TNode a = new TNode(walk.getLocation(), null, walk);
				scooter.setNext(a);
                scooter = scooter.getNext();
                if (bus != null && bus.getLocation() == scooterStops[counter]) {
                    bus.setDown(scooter);
                    bus = bus.getNext();
                }
                counter++;
            }
        }

        while (counter < scooterStops.length) {
            TNode b = new TNode(scooterStops[counter], null, null);
			scooter.setNext(b);
            scooter = scooter.getNext();
            counter++;
			
        }
	}

	/**
	 * Used by the driver to display the layered linked list. 
	 * DO NOT edit.
	 */
	public void printList() {
		// Traverse the starts of the layers, then the layers within
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// Output the location, then prepare for the arrow to the next
				StdOut.print(horizPtr.getLocation());
				if (horizPtr.getNext() == null) break;
				
				// Spacing is determined by the numbers in the walking layer
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print("--");
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print("-");
				}
				StdOut.print("->");
			}

			// Prepare for vertical lines
			if (vertPtr.getDown() == null) break;
			StdOut.println();
			
			TNode downPtr = vertPtr.getDown();
			// Reset horizPtr, and output a | under each number
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				while (downPtr.getLocation() < horizPtr.getLocation()) downPtr = downPtr.getNext();
				if (downPtr.getLocation() == horizPtr.getLocation() && horizPtr.getDown() == downPtr) StdOut.print("|");
				else StdOut.print(" ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
	
	/**
	 * Used by the driver to display best path. 
	 * DO NOT edit.
	 */
	public void printBestPath(int destination) {
		ArrayList<TNode> path = bestPath(destination);
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the number if this node is in the path, otherwise spaces
				if (path.contains(horizPtr)) StdOut.print(horizPtr.getLocation());
				else {
					int numLen = String.valueOf(horizPtr.getLocation()).length();
					for (int i = 0; i < numLen; i++) StdOut.print(" ");
				}
				if (horizPtr.getNext() == null) break;
				
				// ONLY print the edge if both ends are in the path, otherwise spaces
				String separator = (path.contains(horizPtr) && path.contains(horizPtr.getNext())) ? ">" : " ";
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print(separator + separator);
					
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print(separator);
				}

				StdOut.print(separator + separator);
			}
			
			if (vertPtr.getDown() == null) break;
			StdOut.println();

			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the vertical edge if both ends are in the path, otherwise space
				StdOut.print((path.contains(horizPtr) && path.contains(horizPtr.getDown())) ? "V" : " ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
}
