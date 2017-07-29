# ConcurrentBikeRides

This Java program takes in a list of bike rides of specified format and prints out a list of all relevant possible time segments with the number of concurrent rides during that interval noted.

### Input format:  
7:13 AM, 7:23 AM  
6:50 AM, 7:08 AM  
7:10 AM, 7:30 AM  
6:52 AM, 7:33 AM  
6:58 AM, 7:23 AM

### Output format:  
6:50 AM, 6:52 AM, 1  
6:52 AM, 6:58 AM, 2  
6:58 AM, 7:08 AM, 3  
7:08 AM, 7:10 AM, 2  
7:10 AM, 7:13 AM, 3  
7:13 AM, 7:23 AM, 4  
7:23 AM, 7:30 AM, 2  
7:30 AM, 7:33 AM, 1  
