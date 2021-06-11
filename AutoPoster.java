import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.GraphResponse;
import com.restfb.types.User;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class AutoPoster {
	
	
	public static long convertToEpoch (String date, String time, String am_pm) {
		
		//set date and time input format patterns
		DateTimeFormatter readDatePattern = DateTimeFormatter.ofPattern("M/dd/yyyy");
		DateTimeFormatter readTimePattern = DateTimeFormatter.ofPattern("h:mm a");
		
		//create localDateTime object
		LocalDateTime schedPost = LocalDateTime.of(LocalDate.parse(date, readDatePattern), 
				LocalTime.parse(time + " " + am_pm, readTimePattern));
		
		//convert value of localDateTime object to Epoch format with time zone offset
		long timeInSeconds = schedPost.toEpochSecond(ZoneOffset.ofHours(-5));
		return timeInSeconds;
	}
	
	
	public static void schedulePost(FacebookClient fbClient, String id) throws Exception {
		
		//call scheduler class, convertToJSON method to get schedule
		Scheduler.convertToJSON();
		
		//read csv file row by row storing data into variables
		BufferedReader csvReader = new BufferedReader(new FileReader("C:\\Users\\j7b5w2\\eclipse-workspace\\ICS370_AutoPoster\\posting_schedule.csv"));
		String row;
		String inId = "";
		String inDate = "";
		String inTime = "";
		String inAmPm = "";
		String inSlide = "";
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split(",");
		    if(!(data[0].equals("id"))) {
			    inId = data[0];
			    inDate = data[1];
				inTime = data[2];
				inAmPm = data[3];
				inSlide = data[4];
				
				//call convertToEpoch method inputting date, time, and am_pm variables.  Returns long number
				long sPost = convertToEpoch(inDate, inTime, inAmPm);
				
				//set varia  ble for image to be used in post
				String photopath = "C:\\Users\\j7b5w2\\Desktop\\tree.jpg";
				
				//Message to post with id added
				String msg = "Post #" + inId;
				
				
				/*
				////////This method works at posting a message to the group page
				GraphResponse post = fbClient.publish(id+"/feed", GraphResponse.class, Parameter.with("message", msg));
		        System.out.println("fb.com/" + post.getId());
				*/
      
	        
				/*
				 ////////This method works for posting a photo to the group page /photos
				 ///////It does not work for posting a photo to the group page /feed
				@SuppressWarnings("deprecation")
				GraphResponse publishPhotoResponse = fbClient.publish(id+"/photos", GraphResponse.class,
				  BinaryAttachment.with("tree.jpg", new FileInputStream(new File(photopath))),
				  Parameter.with("message", msg));
		
				System.out.println("Published photo ID: " + publishPhotoResponse.getId());
				*/
				
		      
		       
				 ///////This method works for scheduling a text post to the group page /feed
				GraphResponse publishMessageResponse =
				  fbClient.publish(id+"/feed", GraphResponse.class,
						  Parameter.with("message", msg),
						  Parameter.with("published", false), 
						  Parameter.with("scheduled_publish_time", sPost), 
						  Parameter.with("unpublished_content_type", "SCHEDULED")
						  );
					
				System.out.println("Published message ID: " + publishMessageResponse.getId());
			    
				
				
				
				/*
				 //This method does not yet work.  Trying to schedule photo post to group page
				 //Get this error message: (#100) scheduled_publish_time can only be specified for page photos (code 100, subcode null)
				 
				GraphResponse publishPhotoResponse = fbClient.publish(id+"/feed", GraphResponse.class,
						  BinaryAttachment.with("tree.jpg", new FileInputStream(new File(photopath))),
						  Parameter.with("published", false),
						  Parameter.with("scheduled_publish_time", sPost),
						  Parameter.with("message", msg),
						  Parameter.with("unpublished_content_type", "SCHEDULED")
						  );
		
						System.out.println("Published photo ID: " + publishPhotoResponse.getId());
				*/
		    }
		}
		csvReader.close();
		System.out.println();
		System.out.println("Your messages have been scheduled to post on " + fbClient.fetchObject(id, User.class).getName());
      
	}
	
	
	public static void main(String[] args) throws Exception {
		
		//Copied token of FB Access token (for me I think)
		String accessToken = "EAAHIdfpi6qEBACjoDafIp2j0hm8H86FZCYvV4hDcsGiwJBAzdtLE9jDhcZCg094xAT1YQZC1tgRkVQKCMZBVcZCFuUUzM8EWdTZAB4zHiGIWfyt1hHIg1ibITOaj0or3ZAiLcyfhKPXZAqpokdkazWW3UGUcibTqoyEpaDlZCwfSCTj8BMG6pqiFwwJqs3bQSPVZCb5lWmqXX33QZDZD";
		///token expires=Thu Aug 05 22:44:35 CDT 2021
		
		String appID = "501884014226081";
		String appSec = "b299273d784215b949ee1e5d33e0bab5";
		
		
		/*
		AccessToken extendToken =
				  new DefaultFacebookClient(Version.LATEST).obtainExtendedAccessToken(appID,
				    appSec, accessToken);

				System.out.println("My extended access token: " + extendToken);
		*/
		
		FacebookClient fbClient = new DefaultFacebookClient(accessToken, appSec, Version.LATEST);
		 
		//Group Page ID
		String id = "845188109729480";
		
		schedulePost(fbClient, id);
	}
					
} 
