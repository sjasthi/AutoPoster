#!/usr/bin/python
from facebook import GraphAPI
import datetime as dt
import time
import csv

#################################################################################################################

#add id for the facebook group page
group_id =  'XXXXXXXXXXXXXX'

#add user access token
user_access_token = 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX'

#add folder filepath where images are stored
image_fp = 'X:\\XXXX\\XXXXXX\\XXXXXXXX\\'

#add CSV configuration filepath of schedule for posts
config_file = "X:\XXXXXXXX\XXXXXXXXXXXXXX\XXXXXXX.csv"

##################################################################################################################

def post_to_fb():

    #open configuration file
    with open(config_file) as csvfile:

        readCSV = csv.reader(csvfile, delimiter=',')

        #read each row in CSV file and store values in variables
        for row in readCSV:
            if row[0] != "id": #eliminates  column header row
                inId = row[0]
                inDate = row[1]
                inTime = row[2]
                inAmPm = row[3]
                inSlide = row[4]

                #split the date and time strings
                fDate = inDate.split("/")
                fTime = inTime.split(":")

                #add 12 to hours if time is PM
                if inAmPm == "PM":
                    fTime[0] = str(int(fTime[0]) + 12)

                #add slide number to image filepath and id number to message post
                image_location = image_fp + inSlide
                message = 'Python test post#' + inId

                #get the current date and time
                nowtime = dt.datetime.now()

                #pass row variables into datetime object for posting - datetime(year, month, day, hour, minute)
                posttime = dt.datetime(int(fDate[2]), int(fDate[0]), int(fDate[1]), int(fTime[0]), int(fTime[1]) )

                #find the difference between the post and now times
                difference = (posttime - nowtime)

                #if the post date is in the future, proceed to posting process
                #this prevents past post date and times from being repeated if the program is interrupted
                if difference.total_seconds() >= 0:
                
                    #put the program to sleep for the difference time in seconds
                    time.sleep(difference.total_seconds())
                    print('post: ' + inId)

                    #create Facebook graph API object with access token
                    graph = GraphAPI(access_token = user_access_token)

                    #post image to group page with the message
                    print(graph.put_photo(open(image_location, "rb"), message=message, album_path = group_id + "/photos"))

def main():

    #call post to facebook method
    post_to_fb()

#call main to execute program
main()



