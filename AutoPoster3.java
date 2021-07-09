import java.io.BufferedReader;
import java.io.FileReader;
import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.GraphResponse;
import com.restfb.types.User;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class AutoPoster3 {
	
	
	public static String[] readConfigFile(String configFilePath) throws Exception{
		String[] configInfo = new String[3];
		File cfg = new File(configFilePath);
		Scanner reader = new Scanner(cfg);
		while(reader.hasNextLine()) {
			String[] cfgData = reader.nextLine().split(":");
			String a = cfgData[0].trim();
			String b = cfgData[1].trim();
			if(a.equals("access_token")) {
				configInfo[0] = b;
				System.out.println(b);
			} else if (a.equals("app_secret")) {
				configInfo[1] = b;
				System.out.println(b);
			} else if (a.equals("page_id")) {
				configInfo[2] = b;
				System.out.println(b);
			} else {System.out.println("Error message");}  //Need a try/catch here
		}
		reader.close();
		return configInfo;
	}
	
	public static LocalDateTime convertToLocalDateTime (String date, String time, String am_pm) {
		
		//set date and time input format patterns
		DateTimeFormatter readDatePattern = DateTimeFormatter.ofPattern("M/dd/yyyy");
		DateTimeFormatter readTimePattern = DateTimeFormatter.ofPattern("h:mm a");
		
		//create localDateTime object
		LocalDateTime ldt = LocalDateTime.of(LocalDate.parse(date, readDatePattern), 
				LocalTime.parse(time + " " + am_pm, readTimePattern));
		
		return ldt;
	}
	
	
	public static void schedulePost(String configFilePath, String photoFilePath) throws Exception {
		
		//call scheduler class, convertToJSON method to get schedule
		Scheduler.convertToJSON();
		
		//
		String[] config = readConfigFile(configFilePath);
		
		FacebookClient fbClient = new DefaultFacebookClient(config[0], config[1], Version.LATEST);
		
		//read csv file row by row storing data into variables
		BufferedReader csvReader = new BufferedReader(new FileReader("C:\\Users\\j7b5w2\\eclipse-workspace\\ICS370_AutoPoster\\posting_schedule.csv"));
		String row;
		String inId = "";
		String inDate = "";
		String inTime = "";
		String inAmPm = "";
		String inSlide = "";
		
		//find current date and time set to now object
		LocalDateTime now = LocalDateTime.now();
		
		//read line of csv file as long as it is not null
		while ((row = csvReader.readLine()) != null) {
			//split row on commas and put into array
			String[] data = row.split(",");
			//ignore first line of csv that contains headers
			if(!(data[0].equals("id"))) {
			    inId = data[0];
			    inDate = data[1];
				inTime = data[2];
				inAmPm = data[3];
				inSlide = data[4];
			
				//convert date, time, and ampm to a Date object
				LocalDateTime nextTime = convertToLocalDateTime(inDate, inTime, inAmPm);
				
				//set variable for image to be used in post
				String photo = photoFilePath + inSlide;
				
				//create byte array for photograph
				byte[] array = Files.readAllBytes(Paths.get(photo));
				
				//Message to post with id added
				String msg = "Post #" + inId;
  
				//find the difference in milliseconds between now and scheduled date and time
				long diff = ChronoUnit.MILLIS.between(now, nextTime);
				
				//if diff is positive, then the scheduled date/time are in the future and
				//skips dates/times in the past
				if (diff >= 0) {
				
					//put the program to sleep for the difference in time
					Thread.sleep(diff);
				
  				    //Graph api to publish photo and message
		            GraphResponse publishPhotoResponse = fbClient.publish(config[2]+"/photos", GraphResponse.class,
				    BinaryAttachment.with("tree.jpg", array),
				    Parameter.with("message", msg));
		
		            //confirmation message of photo publishing
		            System.out.println("Published photo ID: " + publishPhotoResponse.getId());
		            
				}
				//reset now time
		        now = LocalDateTime.now();
		        System.out.println(now);
			}
		}
		csvReader.close();
		System.out.println("Your messages have been scheduled to post on " + fbClient.fetchObject(config[2], User.class).getName());
	}
	public static void main(String[] args) throws Exception {
		
		
		//The filepath to the .txt configuration file containing
		//Facebook access token, app secret, and page ID
		String configFilePath = "E:\\ICS370\\AutoPoster_Config.txt";
		
		//The filepath to the file containing the photos (Slide1, Slide2, etc)
		String photoFilePath = "C:\\Users\\j7b5w2\\Desktop\\";
		
		schedulePost(configFilePath, photoFilePath);
	}				
} 


		
		
	

