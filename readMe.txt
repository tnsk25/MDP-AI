Input File Format
     Copy and Paste the contents mentioned between the lines. That is the line format. Input format is mentioned in the 
     comments section of the input file. Input File can contain as many comments as needed. Before each input, comment is 
     given on how to specify / write them. 
--------------------------------------------------------------------------------------------------------------------------------
#size of the gridworld
# row_no  * col_no
size : 4 5

#list of location of walls
# the wall is mentioned as row_no col_no . Multiple walls are written separated by comma. FYI no need of comma between 
# row_no and col_no . Row_no start from 1 and end at row . Col_No start from 1 and end at col. [1-row]  [1-col]   
walls : 2 2 , 3 2

#list of terminal states (row,column,reward)
# Same as above. Limit is  [1-row]  [1-col] . The third value is the value of terminal state. 
terminal_states : 3 5 -3 , 4 5 +2, 2 4 +1

#reward in non-terminal states

reward : -0.04

#transition probabilites

transition_probabilities : 0.8 0.1 0.1 0

discount_rate : 0.85

epsilon : 0.001


----------------------------------------------------------------------------------------------------------------------------


Output Format 

##########################################################

Iteration: 1
 -0.04 -0.04 -0.04 -0.04 -0.04
 -0.04 null -0.04 1.0 -0.04
 -0.04 null -0.04 -0.04 -3.0
 -0.04 -0.04 -0.04 -0.04 2.0

##########################################################

Above is the output format. The origin or (1,1) is at top left and bottom right is rowNo * colNo.
Walls are represented as NULL / null  
 