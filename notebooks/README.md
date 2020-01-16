# Notebooks Overview

## Content:

[Main page](/../../)  
|--Notebooks  
|----[Developer Instructions](#DeveloperInstructions)  
|----[User Instructions](#UserInstructions)  

## <a id="DeveloperInstructions"></a>Developer Instructions ##

In this project, many different research questions need to be answered.
To maintain a nice and clean project structure, there will be an individual notebook for each question.

If you want to add another notebook please add a folder to the current directory (./notebook/name/). 
Please use only lower-case letters and underscores for directory and file names.

> Example: "./notebook/csv_file_import/"

### Import .csv files 
To import data, there is already fully functional R code available. After creating a notebook,
you can use the following code to import the meta information of db-engines ranking and a git-history file,
 created by the table exporter. If neccessary, you can import all .csv files, that are available in the ./workspace/histories directory:
 
```
#Load db_info .csv file
source("../csv_file_import/load_db_info.R")
db_infos = load_db_infos()

#Load function to import db_history .csv file
#usage: load_db_history("path")
source("../csv_file_import/load_db_history2.R")
db_git_history = load_db_history2(input_path = "../../Workspace/DATABASE_NAME.csv")

#Load all .csv git history files available in ./workspace
source("../csv_file_import/load_db_history_all.R")
db_git_history = load_db_history_all()
```
>**Note: Replace DATABASE_NAME with the name of the database of the history you want to use.
>Make sure you put the required files to your workspace directory!**

### Import more utility functions

If you create code you want to use in several notebooks, please export your code as a ***function*** and save it in the ***directory "./notebooks/util/"*** as a R file.
You can import the function if you need it with the source function of R ([see usage above](#DeveloperInstructions)).

## <a id="UserInstructions"></a>User Instructions ##

Simply execute the given notebook with R or use the available .html file generated in the corresponding notebook directory (recommended).
