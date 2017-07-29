import java.util.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;

/* 
 * Concurrent Bike Rides
 * By: Steven Mazliach
 * 
 * This Java program takes in a list of bike rides of specified format
 *  and prints out a list of all relevant possible time segments with 
 *  the number of concurrent rides during that interval noted.
 *  
 */

public class BikeRides {
	
	private static Scanner stdin;

	static class Ride implements Comparable<Ride>{
		Date start;
		Date end;
		int overlaps;
		
		//Constructor for Ride object
		public Ride(Date start, Date end) {
			this.start = start;
			this.end = end;
			this.overlaps = 0;
		}
		
		@Override //Comparable function for sorting Rides in ascending order
		public int compareTo(Ride r) {
			if (this.start.after(r.start) || ((this.start.equals(r.start) && (this.end.after(r.end) ))) ) {
				return 1;
			} else if ((this.start.equals(r.start)) && (this.end.equals(r.end))) {
				return 0;
			} else {
				return -1;
			}
		}
		
		//Returns string description of ride (For Debugging)
		public String getRide() {
			DateFormat dateFormat = new SimpleDateFormat("HH:mm aa");
			dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
			if(this.overlaps != 0) {
				return "{start: " + dateFormat.format(this.start) +
						", end: " + dateFormat.format(this.end) +
						/*", duration: " + this.getDuration() + " min." + */ ", concurrent: " +
						String.valueOf(this.overlaps) + "}";
			}else {
				return "{start: " + dateFormat.format(this.start) +
						", end: " + dateFormat.format(this.end) +
						/*", duration: " + this.getDuration() + " min." + */ " }";
			}
		}
		
		//Returns string description of ride (Requested Output)
	    public String getRideOutput() {
			DateFormat dateFormat = new SimpleDateFormat("HH:mm aa");
			dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
			return (dateFormat.format(this.start) + ", " + dateFormat.format(this.end) + ", " + String.valueOf(this.overlaps));
		}
		
		//Returns duration of ride in minutes
		public String getDuration() {
			Duration duration = Duration.ofMillis(this.end.getTime() - this.start.getTime());
			return String.valueOf(duration.toMinutes());
		}

		//Returns number of overlaps for this Ride period
		public int getOverlaps() {
			return overlaps;
		}
		
		//Sets overlaps to specified value
		public void setOverlaps(int overlaps) {
			this.overlaps = overlaps;
		}

	}
	
	//Remove Rides with the same start and end times as other existing Rides in the list
	public static LinkedList<Ride> removeDuplicates(LinkedList<Ride> rides) {
		for (int k = 0; k < rides.size(); k++) {
			Ride curr_k = rides.get(k);
			for(int l = k+1; l < rides.size(); l++) {
				Ride curr_l = rides.get(l);
				if(ridesEqual(curr_l, curr_k)) {
					//System.out.println("removing " + curr_l.getRide());
					rides.remove(l);
				}
			}
		}
		return rides;
	}
	
	//Helper function to print the list of rides in a LinkedList (for Debugging)
	public static void printRideList(LinkedList<Ride> rides) {
		if(!rides.isEmpty()) {
			Ride curr = rides.getFirst();
			int index = 0;
			
			System.out.print("{\n");
			while(curr != null) {
				if(index == (rides.size() - 1)) {
					System.out.println("   " + curr.getRide());
				}else {
					System.out.println("   " + curr.getRide() + ",");
				}
				index++;
				if(index < rides.size()) {
					curr = rides.get(index);
				}else {
					break;
				}
			}
			System.out.println("}");
		}else {
			System.out.println("Empty List of Rides");
		}
	}
	
	//Helper function to print the list of rides in a LinkedList (Requested Output)
		public static void printConcurrentRidesOutput(LinkedList<Ride> rides) {
			if(!rides.isEmpty()) {
				Ride curr = rides.getFirst();
				int index = 0;
				
				System.out.print("\n");
				while(curr != null) {
					System.out.println(curr.getRideOutput());
					index++;
					if(index < rides.size()) {
						curr = rides.get(index);
					}else {
						break;
					}
				}
			}else {
				System.out.println("Empty List of Rides");
			}
		}
		
	
	//Returns true if some overlap exists between rides a and b
	public static boolean hasOverlap(Ride a, Ride b) {
		return ((b.end.after(a.start)) && (a.end.after(b.start)));
	}
	
	//Returns the period of time for which rides a and b overlap
	public static Ride getOverlapPeriod(Ride a, Ride b) {
		//DateFormat dateFormat = new SimpleDateFormat("HH:mm aa");
		//dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		if(a.start.after(b.start)) {
			if(b.end.after(a.end)) {
				//System.out.println("Between " + a.getRide() + " and " + b.getRide() + " overlap is between " + dateFormat.format(a.start) + " and " + dateFormat.format(a.end));
				return new Ride(a.start, a.end);
			}else {
				//System.out.println("Between " + a.getRide() + " and " + b.getRide() + " overlap is between " + dateFormat.format(a.start) + " and "  + dateFormat.format(b.end));
				return new Ride(a.start, b.end);
			}
		}else {
			if(a.end.after(b.end)) {
				//System.out.println("Between " + a.getRide() + " and " + b.getRide() + " overlap is between " + dateFormat.format(b.start) + " and " + dateFormat.format(b.end));
				return new Ride(b.start, b.end);
			}else {
				//System.out.println("Between " + a.getRide() + " and " + b.getRide() + " overlap is between " + dateFormat.format(b.start) + " and "  + dateFormat.format(a.end));
				return new Ride(b.start, a.end);
			}
		}
	}
	
	public static LinkedList<Date> getSortedListOfTimes(LinkedList<Ride> rides){
		LinkedList<Date> times = new LinkedList<Date>();
		for(int i = 0; i < rides.size(); i++) {
			Ride curr = rides.get(i);
			times.add(curr.start);
			times.add(curr.end);
		}
		//Remove duplicates:
		for(int j = 0; j < times.size(); j++) {
			Date curr1 = times.get(j);
			for(int k = j+1; k < times.size(); k++) {
				Date curr2 = times.get(k);
				if(curr1.equals(curr2)) {
					times.remove(k);
				}
			}
		}
		Collections.sort(times);
		return times;
	}
	
	//Print a list of dates to stdout
	public static void printDateList(LinkedList<Date> times) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm aa");
		dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		System.out.print("{ ");
		for(int i = 0; i < times.size(); i++) {
			if(i == (times.size() - 1)) {
				System.out.print(dateFormat.format(times.get(i)));
			}else {
				System.out.print(dateFormat.format(times.get(i)) + ", ");
			}
		}
		System.out.println("}");
	}
	
	//Returns list of all possible ride segments with at least one ride happening
	public static LinkedList<Ride> getTimeSegments(LinkedList<Date> times) {
		LinkedList<Ride> timeSegments = new LinkedList<Ride>();
		for(int i = 1; i < times.size(); i++) {
			Date prev = times.get(i-1);
			Date curr = times.get(i);
			Ride thisRide = new Ride(prev, curr);
			//thisRide.overlaps = 1;
			timeSegments.add(thisRide);
		}
		return timeSegments;
	}
	
	//Returns true if start and end times of a and b are both the same
	public static boolean ridesEqual(Ride a, Ride b) {
		return ((a.start.equals(b.start)) && (a.end.equals(b.end)));
	}
	
	
	public static void main(String[] args) throws ParseException {
		stdin = new Scanner(System.in);
		LinkedList<Ride> rides = new LinkedList<Ride>();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm aa");
		dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		
		//Collect rides from stdin
		while(stdin.hasNextLine()) {
			String entry = stdin.nextLine();
			String[] startEnd = entry.split(", ");
			if((startEnd[0].length() > 4) && (startEnd[1].length() > 4)) {
				//System.out.print("[" + startEnd[0] + "," + startEnd[1] + "]");
				Date thisStart = dateFormat.parse(startEnd[0]);
				//System.out.print(" " + thisStart.toString());
				Date thisEnd = dateFormat.parse(startEnd[1]);
				//System.out.println(" " + thisEnd.toString());
				Ride thisRide = new Ride(thisStart, thisEnd);
				rides.add(thisRide);
			}else {
				break;
			}
		}
		Collections.sort(rides); //Sorts the rides by ascending start time
		//printRideList(rides);
		
		LinkedList<Date> times = getSortedListOfTimes(rides);
		//System.out.println(" ");
		//printDateList(times);
		//System.out.println(" ");
		LinkedList<Ride> timeSegments = getTimeSegments(times);
		//printRideList(timeSegments);
		
		/*
		 * Loops through each ride for each timeSegment and checks if there is a valid overlap,
		 * if so, concurrent rides for that segment is increased by 1.
		 */
		for (int k = 0; k < timeSegments.size(); k++) {
			Ride curr_segment = timeSegments.get(k);
			for(int l = 0; l < rides.size(); l++) {
				Ride curr_ride = rides.get(l);
				if(hasOverlap(curr_segment, curr_ride)) {
					if(ridesEqual(getOverlapPeriod(curr_segment,curr_ride), curr_segment)) {
						int currOverlaps = curr_segment.getOverlaps();
						curr_segment.setOverlaps(currOverlaps+1);
					}
				}
			}
		}
		
		Collections.sort(timeSegments);
		//printRideList(timeSegments);
		printConcurrentRidesOutput(timeSegments);
	}
}
