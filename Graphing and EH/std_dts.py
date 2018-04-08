import pandas
import numpy as np
import csv
import sys

#import moddij
#def path_std_dts(src, dest):

#input number you want to search
num1 = raw_input('Enter source to start\n');
pin1 = raw_input('Enter Pincode for source')
num2= raw_input('Enter destination to reach\n');
pin2 = raw_input('Enter Pincode for destination')
    #read csv, and split on "," the line
csv_file = csv.reader(open('Br_Offices.csv', "rb"), delimiter=",")


class Error(Exception):
   """Base class for other exceptions"""
   pass

class sourceAndDestinationSame (Error):
   """Raised when Source and Destination are same"""
   pass
class NonDeliveryType (Error) : 
    """When Post office is Non delivery Types"""

try:
   if pin1==pin2 and num1==num2 :
        raise sourceAndDestinationSame
except sourceAndDestinationSame:
    print("source and destination same")

Destiny_hub=0

for row in csv_file:
    if num1 == row[0] and pin1 == row[1] and row[5]=='NA':                   
        print row[0]+" ("+row[1]+") ==> "+row[6]
        Source_hub = row[6]               
    elif num1 == row[0] and pin1 == row[1]:       
        print row[0]+" ("+row[1]+") ==> "+row[5]+" ==> "+row[6] 
        Source_hub = row[6]
for row1 in csv_file:
    try:
        if (row1[2]=="Non-Delivery"):
            raise NonDeliveryTypes
    except NonDeliveryType:
        print("Post Office is Non delivery types")
    break    
        
    if num2==row1[0] and pin2==row1[1] and row1[5]=='NA':
        print row1[6]+" ==> "+row1[0]+" ("+row1[1]+")"
        Destiny_hub= row1[6]
    elif num2==row1[0] and pin2==row1[1]:
        print row1[6]+" ==> "+row[5]+" ==> "+row1[0]+" ("+row1[1]+")"
        Destiny_hub= row1[6]

#print Source_hub
#print Destiny_hub 

if Destiny_hub==0 : 
    print "The Hub is a Non Delivery Hub, So the algorithm Can't go through the desired route."