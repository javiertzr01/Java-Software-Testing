#Note:#  
**At this point in time, I realized that I misinterpreted the instructions. Hence, I just redid my code and just starting on my testing. Apologies for the late submission.**

## Software-Testing-Mini-Campaign
**Name: Javier Teo
Student ID: 1005033**

The goal of this software application is to compare 2 CSV files and output the mismatched entries in a new CSV file.  
The entirety of this code uses Java and does not require any external libraries at this point in time.  
Note: At this point in time, this only works for the test cases provided (With regards to headings etc.)  
## Use Case Diagram
![Use Case Diagram](https://i.imgur.com/7pSKZ9p.jpeg)
## Installation
Clone this repository into your desired folder

`git clone https://github.com/javiertzr01/Software-Testing-Mini-Campaign.git`

## Usage
1. Add your 2 desired csv files to the same folder
**Note: It is also possible to skip step 1, if you use a `path` instead of names in step 2**
2. Open App.java and replace the names of the csv file in the code
![Replace <Insert First CSV File Name> and <Insert Second CSV File Name](https://i.imgur.com/Q1hmqk7.png)
3. Run the code on your IDE (e.g F5 in Visual Studio Code)
4.  A CSV file named "exceptions.csv" should be created/modified, containing the relevant outputs

## Testing
### Equivalence Class Partitioning
The below partitioned class is used to divide our test cases into equivalent class partitions to sort out the different types of test cases to reduce testing time. The idea is that each case in the class is representative of the whole class. Therefore, if one case passes in a particular class, all cases in that same class should also pass. Likewise, if one case fails in a particular class, all cases in that same class should also fail.

### Equivalence Classes
 **1. FileType**  
 There is a need to ensure the validity of the input filename (the existence of the file) and the filetype extension (Only `.csv` accepted)  
> **Valid FileType**  
> `*.csv` files ( `*` can be any filename of an existing file [Case-Sensitive])  

Boundary Value Analysis:  
(Assuming file exists)  
 - Middle Value: `accepted_file.csv`  
 - Boundary Value:   
		-	Nil  

> **Invalid FileType**  
> `*.< extension >` where extension is not a `.csv` or `*` is not a name of an existing file  

Boundary Value Analysis:  

 - Middle Value: `unaccepted_file.xlsx`  
 - Boundary Value:  
		 - `unaccepted_file.csv.xlsx` ( File extension not accepted )  
		 - `missing_file.csv` ( File not found )  
		 - `missing_extension` ( File extension not found )  
		 - `.csv` ( Filename not found, File not found )  
		 - `#$%^.csv` ( Unaccepted characters for filenames, File not found )  
		 - `12345.csv` ( Non-string input **[Missing quotation marks in step 2 of Usage]** )  

 **2. FilePath**   
 In the case where user decides to use `path` instead of name, there is a need to ensure that the filepath is valid  
> **Valid FilePath**  
> `<FilePath>` that is accepted on the filesystem used  

Boundary Value Analysis:  
(Assuming windows filesystem)  
 - Middle Value: `C:\Desktop\Summer2018.csv`  
 - Boundary Value:  
		 - `.\Summer2018.csv` ( Relative file path )  
> **Invalid FilePath**  
> `<FilePath>` not accepted on filesystem  
  
Boundary Value Analysis:  

 - Middle Value: `/non/existent/path`  
 - Boundary Value:  
		 - `./incorrect/relative/path` ( File does not exist in that path )  
		 - `/path//extra/symbols` ( Incorrect path format for filesystem )  

