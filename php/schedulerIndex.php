


<!DOCTYPE html>
<html>
<body>
    <form>
    <table style="border: 0px;">
    <tr>
      <td>Starting Date</td>
      <td> <input type="date" id="Birthdate" placeholder="d/m/yyyy" name="Birthdate" required></td>
    </tr>
    <tr>
     <td>Starting time</td>
     <td><input type="time" name="oilqty" size="3" maxlength="3" /></td>
    </tr>
    <tr>
     <td>Source Directory</td>
     <td><input type="text" name="sparkqty" size="3" maxlength="3" /></td>
    </tr>
    <tr>
      <td>How many puzzles (50, 75, 100)</td>
      <td><input type="number" name="number" min=50 max=100 size="4" maxlength="4"></td>
    </tr>    
    <tr>
     <td colspan="2" style="text-align: center;"><input type="submit" value="Submit Order" /></td>
    </tr>
    </table>
    </form>



</body>


<?php
// Algorithm
//Description of problem: User must input information, and that information is then compiled into a csv file.
//


// inputs:
// Starting Day:
// Starting Time(s):
// Source Directory: (eg: C:\TeluguPuzzles.Com\1\puzzles)
// How many puzzles: 50, 75, 100
// Default Message: (Announcement #id) (Event #id) (ICS370)
//---------------------------------------------------------
// Outputs: 
// Schedule.csv
// csv file saved locally (the source directory)



// Program will request user for inputs listed above.
// once all inputs have been made, the program will process information (error checks) in this order:
// Starting day and starting time is checked for correct input, or if there is a misinput.
// program will check source directory to make sure it has enough puzzles to post, based on what the user inputted in "How many Puzzles"
// example: If user selects 50, and there is less than 50 puzzles in the source directory, an error will be returned
// example: If directory is empty, an error will occur
// At this point, if no errors occur, the inputs are ok to use.
// A CSV file is then created based on the user inputs. 
// The inputs will be appended to fit a csv file format.
// Here is how the format of the csv file will look like
// id / date / time / AM_PM / Slide No
//
// Based on the starting time(s) added by the user, it should look something like this in the csv file:
//
// 10:00 AM
// 10:00 PM
// 10:00 AM
// 10:00 PM
// -----------
//     OR
// -----------
// 12:00 AM
// 1:30 PM
// 12:00 PM
// 1:30 AM
// 
//(Time is incremented by every 12 hours, This is how the time(s) swap between AM and PM for each puzzle.)
//Dates are incremented with consideration of the following:
//[1] No of days in each month
//[2] Change of the year
//[3] Leap Year 
// Based on what the user inputted for "How many puzzles" will determine the amount of ID's and Slide No's.  
// The program will output a confirmation message, that signifies that the csv file has been made.
// The program will then end after confirmation message has been provided.
//


?>