/* Assignment 4: Scheduler
 * Class: ICS 370
 * Student: Justin Wilmot
 * Date: 5/27/2021
 * 
 * Program Description:		- Extends Assignment 2. Create a scheduling program
 * 							  for 100 Facebook posts allowing for multiple posts in both AM and PM.  
 * 
 * Requirements:			- All pertinent requirements of Assignment 2 must be adhered to.
 * 							- Users allowed to enter multiple input times for starting posts on a single input line.  
 *                          - In addition to saving the schedule to a CSV file, there must be a method
 *                            to return the schedule as JSON data.
 *                          - **see ppt example of output.
 * 
 */

import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.json.*;

public class Scheduler {
	
	
	/*
	 * inputDate method for the user to input starting date and validate it.  
	 * 
	 */
	public static String inputDate() {
		
		//opening statement
				System.out.println("Welcome to Scheduler");
				System.out.println();
				System.out.println();
		
		Scanner scan = new Scanner(System.in);
		String returnDate = "";
		boolean valid;
		
		//ask user to input starting day
		System.out.println("Enter the starting day: ");
				
		//validate user entry
		do {
			valid = true;
			try {
				//scan input
				returnDate = scan.nextLine();
				
				//establish what the acceptable date input pattern will be and what the output pattern will be
				DateTimeFormatter dateInPattern = DateTimeFormatter.ofPattern("M/d/yyyy");
				DateTimeFormatter dateOutPattern = DateTimeFormatter.ofPattern("M/dd/yyyy");
					
				//Check if input date is valid and in proper input pattern. Throw exception if not acceptable.  
				//Change date format to output pattern and save it back to input[0]
				LocalDate dateF = LocalDate.parse(returnDate, dateInPattern);
				returnDate = dateOutPattern.format(dateF);
					
			} catch (Exception e) {
					
				//if input is not valid, ask user to enter using proper format 
				valid = false;
				System.out.println();
				System.out.println("Sorry, we probably just need that date in a slightly different format.");
				System.out.println("Try using a MM/DD/YYYY format for the month, day, and year. ");
				System.out.println();
				System.out.println("Please try entering your date again: ");
			}
		} while (!valid);//continue to loop if there is not a valid entry
		System.out.println();
		
		return returnDate;//return date
	}
	
	
	/*
	 * inputTime method for user to input multiple starting times and validation.
	 * 
	 */
	public static ArrayList<String> inputTime() {
		
		//initialize scanner
		Scanner scanTime = new Scanner(System.in);
		
		//ask user to enter the time for the first puzzle
		System.out.println("Enter the times for posting this puzzle: ");
		
		//ArrayLists for input and output
		List<String> inList = new ArrayList<String>();
		ArrayList<String> outList = new ArrayList<String>();
		
		
		//initialize variables
		String input = "";
		String time = "";
		String ampm = "";
		String hour = "";
		String min = "";
		boolean valid;
		
		//validate user entry for time
		do {
			valid = true;
				
			//scan input
			input = scanTime.nextLine();
			
			//if there is at least one comma in the input string...
			if (input.indexOf(",") != -1) {
				//split the string by the commas and add to an ArrayList
				inList.addAll(Arrays.asList(input.split(",")));
			
			//otherwise there are no commas in the input string so just add the string to the ArrayList
			} else {inList.add(input);}
						
			
			//iterate through the ArrayList
			for(String var:inList) {
					
				//trim any leading or trailing whitespace
				var = var.trim();
				
				//if there is no space in the string change valid to false.
				if (!var.matches(".*\\s.*")) {
					valid = false;
					
				} else {
				
					//otherwise split string into time and AM/PM on the space between them
					String[] arrA = var.split("\\s");
					time = arrA[0];
					ampm = arrA[1].toUpperCase();
				}
					
				//if there is no colon in the time change valid to false.
				if (!time.contains(":")) {
						valid = false;
				} else {		
						//otherwise further split the time into hour and minute			
						String[] arrB = time.split(":");
						hour = arrB[0];
						min = arrB[1];
				}
				
				//check to see that hour and min are actually digits
				if (valid == true) {
					try {
						int hourTest = Integer.parseInt(hour);
						int minTest = Integer.parseInt(min);
					} catch (NumberFormatException e) {
						valid = false;
					}
				}
				
				
				//make sure the hour is between 1 and 12 and minutes between zero and 59
				//if otherwise, set valid to false.
				if (valid == true) {
					if(Integer.parseInt(hour) < 1 || Integer.parseInt(hour) > 12 || 
						Integer.parseInt(min) < 0 || Integer.parseInt(min) > 59) {
								valid = false;	
					} else {
						
						//Otherwise, format the entered time.
						//Establish acceptable input time pattern and set the output pattern
						DateTimeFormatter timeInPattern = DateTimeFormatter.ofPattern("H:mm");
						DateTimeFormatter timeOutPattern = DateTimeFormatter.ofPattern("h:mm");
								
						   
						//Change time format to output pattern and save it to output ArrayList
						LocalTime timeF = LocalTime.parse(time, timeInPattern);
						outList.add(timeOutPattern.format(timeF).toString());
					}
				}
							
				//Validate AM/PM. If it does not match, set valid to false. 		
				if (valid == true) {
					if(!(ampm.equals("AM")||ampm.equals("PM"))) {
						valid = false;
					} else {
						//Otherwise add to ArrayList.
						outList.add(ampm);
					}
				}
			}
					
			//if input fails, clear the ArrayLists, message the user and ask them to re-enter their times.		
			if (valid == false) {
				
				inList.clear();
				outList.clear();
				System.out.println();
				System.out.println("Hmmm, your times are not quite computing.");
				System.out.println("Make sure you are entering valid times like, 8:00 AM or 10:30 PM");
				System.out.println("For multiple times, please separate them with commas.");
				System.out.println(); 
				System.out.println("Please enter your times for posting this puzzle: ");
			}
				
					
		} while (!valid);//continue to loop if there is not a valid entry
		System.out.println();
		
		scanTime.close();
		return outList;//return time input array	
	}	
	
	
	/*
	 * createObjectList method calls both date and time input methods and uses it as a starting point to create
	 * FBPost objects.  The objects are put into an ArrayList and returned.
	 */
	public static ArrayList<FBPost> createObjectList() {
		
		//establish date formats,slashPattern for input/output and dashPattern for LocalDate parsing and +1 date
		DateTimeFormatter dateInPattern = DateTimeFormatter.ofPattern("M/d/yyyy");
		DateTimeFormatter dateOutPattern = DateTimeFormatter.ofPattern("M/dd/yyyy");
		
		//call date and time input methods.
		String date = inputDate();
		ArrayList<String> timeArr = inputTime();
		
		
		//create new ArrayList for FBPost objects
		ArrayList<FBPost> nc1 = new ArrayList<FBPost>();
				
		//add FBPost object with column headers to the ArrayList
		nc1.add(new FBPost("id","date","time","AM_or_PM","Slide No"));
		
		
		
		//create "grid" input loop for initial FBposts. Add them to ArrayList.
		int sizeCount = 0;
		int rowCount = 1;
		
		while(rowCount <= timeArr.size()/2) {//need to divide size in half since both time and AM/PM are in list.
			
			//add FBPost object with data to the ArrayList. "id" and "slide" make use of counter variable.
			nc1.add(new FBPost(""+rowCount,date,timeArr.get(sizeCount),timeArr.get(sizeCount+1),"Slide"+rowCount+".jpg"));
			rowCount++;
			sizeCount=sizeCount+2;
		}
		
		
		//create a loop to add remaining FBPosts with data to the ArrayList
		int counter = 1 + timeArr.size()/2;//start counter after initial inputs
		while(counter < 7) { /////////CHANGE Back to 101///////////////////////
			
			//get date and am_pm from previous "grid"
			date = nc1.get(counter-timeArr.size()/2).getDate();
			String am_pm = nc1.get(counter-timeArr.size()/2).getAm_pm();
			
			//if current time is AM, change to PM for next loop
			if (am_pm.equals("AM")) {
				am_pm = "PM";	     	 
			}  
			else {
				//Otherwise current time is PM so we need to identify the following day's date..
				LocalDate nextDay = LocalDate.parse(date, dateInPattern).plusDays(1);
				
				//Change format to date output pattern and save to date. Change PM to AM for next loop.
				date = dateOutPattern.format(nextDay);
				am_pm = "AM";
			}
			
			//add FBPost object with data to the ArrayList. "id" and "slide" make use of counter variable.
			nc1.add(new FBPost(""+counter,date,nc1.get(counter - timeArr.size()/2).getTime(),am_pm,"Slide"+counter+".jpg"));
			
			counter++; //increment counter
		}
		return nc1;//return ArrayList
	}
	
	
	/*
	 * createSchedule method calls createList method to bring in ArrayList of FBPost objects then outputs
	 * each object's data into comma separated rows for a csv file. 
	 */
	public static ArrayList<FBPost> createSchedule() throws Exception{
		
		//call createObjectList which returns an ArrayList of FBPost objects
		ArrayList<FBPost> postlist = createObjectList();
		
		//closing statement
		System.out.println("Thank you! A file \"posting_schedule.csv\" is also" +
	            " created for you.");
		System.out.println();
		
		//create csv file and output string
		FileWriter csv1 = new FileWriter("posting_schedule.csv");
		String output = "";
		
		//loop through the ArrayList. Output all data from each object separated by commas.
		for (FBPost x : postlist) {
			
			//create output string
			output = x.getId() + "," + x.getDate() + "," + x.getTime() + "," +
			 		  x.getAm_pm() + "," + x.getSlide() + "\n"; 
			 
			//output data to csv file
			csv1.append(output);
		}
		csv1.close();//close FileWriter
		
		return postlist;//return ArrayList
	}
	
	
	/*
	 * convertToJSON method calls createSchedule method to bring in ArrayList of FBPost objects then
	 * translates them to JSONObjects.
	 */
	
	public static JSONArray convertToJSON() throws Exception{
		
		//initialize new JSONArray
		JSONArray jList = new JSONArray();
		
		int counter = 0;
		
		//Call createSchedule method and iterate though ArrayList 
		//copying elements from FBPost object to JSONObject
		for(FBPost x: createSchedule()) {
			
			JSONObject obj = new JSONObject();
			obj.put("id", x.getId());
			obj.put("date", x.getDate());
			obj.put("time", x.getTime());
			obj.put("AM_or_PM", x.getAm_pm());
			obj.put("Slide No", x.getSlide());
			
			if(counter > 0) {//skips iteration '0' which has all column headers
				jList.put(obj);	//adds list object to JSONObject	
			}
			counter++;
		}
		return jList;//return JSONArray of data objects
	}
		
	/*
	 *  main method
	 */
	public static void main(String[] args) throws Exception{
		
		
		
		//call convertToJSON method and iterate through array printing objects
		for ( Object y : convertToJSON()) {
			
			System.out.println((JSONObject) y);
		}
	}
}

class FBPost
{	

	//for representing the id of FB Post​
	private String id;	

	//for representing the date of FB Post​
	private String date;	

	//for representing the time of FB post​
	private String time;	

	//for representing the AM or PM of FB Post​
	private String am_pm;	

	//for representing the image number to be posted
	private String slide;

	/**
	 * Default Constructor For FBPost Class
	 */
	 public FBPost()
	 {
 
	 };

	/**
	* Overloaded Constructor For FBPost Class
	*/
	 public FBPost(String a_id, String a_date, String a_time, String a_am_pm, String a_slide)
	 { 
		id = a_id;
		date = a_date;
		time = a_time;
		am_pm = a_am_pm;
		slide = a_slide;
	 }

	/**
	 * Set method for the variable id
	 */
	public void setId(String a_id)
	{
		id = a_id;
	}

	/**
	 * Set method for the variable date
	 */
	public void setDate(String a_date)
	{
		date = a_date;
	}

	/**
	 * Set method for the variable time
	 */
	public void setTime(String a_time)
	{
		time = a_time;
	}

	/**
	 * Set method for the variable am_pm
	 */
	public void setAm_pm(String a_am_pm)
	{
		am_pm = a_am_pm;
	}

	/**
	 * Set method for the variable slide
	 */
	public void setSlide(String a_slide)
	{
		slide = a_slide;
	}

	/**
	 * Get method for the variable id
	 */
	public String getId( )
	{
		return id;
	}

	/**
	 * Get method for the variable date
	 */
	public String getDate( )
	{
		return date;
	}

	/**
	 * Get method for the variable time
	 */
	public String getTime( )
	{
		return time;
	}

	/**
	 * Get method for the variable am_pm
	 */
	public String getAm_pm( )
	{
		return am_pm;
	}

	/**
	 * Get method for the variable slide
	 */
	public String getSlide( )
	{
		return slide;
	}

	/** 
	 * Returns the String representation of FBPost object 
	 */
	 public String toString()
	{
		 String temp = 
			"\nid = " + id +
			"\ndate = " + date +
			"\ntime = " + time +
			"\nam_pm = " + am_pm +
			"\nslide = " + slide;

		 return temp;
	}
}
	 

