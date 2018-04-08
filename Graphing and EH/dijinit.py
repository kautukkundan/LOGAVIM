# Python program for Dijkstra's single source shortest
# path algorithm. The program is for adjacency matrix
# representation of the graph
import numpy as np
import sys, csv
import pandas as pd
from collections import defaultdict

 

#Class to represent a graph
class Graph:  
    def __init__(self, vertices):
        self.V = vertices
        self.graph = [[0 for column in range(vertices)] 
                      for row in range(vertices)]
    
    
    def printPath(self, root, j):
        mapped = ['NewDelhi', 'Mumbai', 'Chennai', 'Bangalore', 'Kolkata', 'Hyderabad']
        if root[j] == -1 : #Base Case : If j is source
           
            print mapped[j]
            return
            
        self.printPath(root, root[j])
        print mapped[j]
        
    
    def printSolution(self, dist, src, des, root):
        #print("Vertex \t\tDistance from Source\tPath")
        for node in range(self.V):
             if(node==des):
                #print("\n%d ==> %d \t\t%d \t\t" % (src, node, dist[node])),
                self.printPath(root,node)
 
 
    def minDistance(self,dist,sptSet,q,des):
        # Initialize min value and min_index as -1
        minimum = sys.maxint
        min_index = -1
        #from the dist array,pick one which has min value and is till in queue
        for v in range(self.V):
            if dist[v] < minimum and sptSet[v] == False and v in q:
                minimum = dist[v]
                min_index = v
        return min_index    
    
    
    #Dijkstra's Algorithm
    def mod_DP_dijkstra(self, src, des):
 
        # The output array. dist[i] will hold the shortest distance from src to i
        # Initialize all distances as INFINITE 
        dist = [sys.maxint] * self.V
        #Parent array to store shortest path tree
        root = [-1] * self.V
        # Distance of source vertex from itself is always 0
        dist[src] = 0
        sptSet = [False] * self.V
        # Add all vertices in queue
        q = []
        for v in range(self.V):
            q.append(v)
             
        #Find shortest path for all vertices
        while q:
 
            # Pick the minimum dist vertex from the set of vertices
            # still in queue
            u = self.minDistance(dist,sptSet,q,des)    
            sptSet[u] = True

            # remove min element     
            q.remove(u)
 
            # Update dist value and parent index of the adjacent vertices of
            # the picked vertex. Consider only those vertices which are still in
            # queue
            for v in range(self.V):
                '''Update dist[i] only if it is in queue, there is
                an edge from u to v, and total weight of path from
                src to v through u is smaller than current value of
                dist[v]'''
                if (self.graph[u][v]>0 and sptSet[v] == False and
                   dist[v] > dist[u] + self.graph[u][v] and v in q):
                        dist[v] = dist[u] + self.graph[u][v]
                        root[v] = u
 
 
        # print the constructed distance array
        self.printSolution(dist,src,des,root)
 
    

#names = ['officename', 'pincode', 'officeType', 'Deliverystatus', 'circlename', 'longitude', 'latitude']
#name = ('officename', 'Pincode', 'Circlename', 'Latitude', 'Longitude')


df=pd.read_csv('Br_Offices.csv',delimiter=",")
print df.shape
def moddif(source_Hub, destination_Hub): 

        src = 'NewDelhi' 
        dest = 'Mumbai'

        url = 'https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins='+src+'&destinations='+dest+'&key=AIzaSyDUNbtArqyJdXw5ewEZIgo493_Lue49HUs'

        req = urllib.urlopen(url)

        data = req.read()

        res = json.loads(data)

        print(res["rows"][0]["elements"][0]["duration"]["text"]) 
    
#source_names = df.iloc[:,0:2].values.tolist()
#destin_names = df.iloc[:,0:2].values.tolist()
#row=df.iloc[:,4:5].values.tolist()
#col=df.iloc[:,4:5].values.tolist()

#print source_names
#print "\n"
#print destin_names

#driver program
g= Graph(6)
g.graph = [[0,1,2,3,4,5],
           [1,0,4,6,8,10],
           [2,4,0,9,12,15],
           [3,6,9,0,15,20],
           [4,8,12,16,0,12],
           [5,10,15,20,25,0]];
#for row in range(len(df)):
    #for col in range(len(df)):
       #g.graph.append([row*col])
#Adjacency Matrix


g.mod_DP_dijkstra(3,1)

#Future: Military Mails!!!!!

