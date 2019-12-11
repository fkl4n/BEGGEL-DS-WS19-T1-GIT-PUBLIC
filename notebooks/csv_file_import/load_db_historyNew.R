library(yaml)
library(stringr)
library(data.table)
load_db_history2 = function(input_path, input_file_extension_path = '../../data/file_extension/languages.yml', input_encoding = "UTF-8",input_na_strings = "null", input_sep = ";"){
  #setwd("/Users/mathiasfedder/git/BEGGEL-DS-WS19-T1-GIT/notebooks/csv_file_import")
  #input_path = "../../Workspace/MySQL.csv"
  #input_sep = ";"
  
  db_git_history = fread(input_path,sep = input_sep,stringsAsFactors = TRUE)
  
  db_git_history$timestamp = as.POSIXct(db_git_history$timestamp, format='%Y-%m-%dT%H:%M:%S')

     # Create a file extensions map
  ext_map = load_file_extensions()
 
   # Add programming language column to db_git_history based on file extension 
  #db_git_history["programmingLanguage"] = lapply(db_git_history["file"], lapplyHelper, ext_map)
  
  db_git_history <- mutate(db_git_history,programmingLanguage = str_split(file, ".(?=[.])",simplify = TRUE)[,2])
  db_git_history$programmingLanguage <- with(ext_map, programmingLanguage[match(db_git_history$programmingLanguage, extension,nomatch = "unknown")])
  
  # Flatten the resulting List to a vector
  db_git_history["programmingLanguage"] = unlist(db_git_history$programmingLanguage, use.names=FALSE)
  
  
  # Convert the FileExtensions to factors
  db_git_history$programmingLanguage = as.factor(db_git_history$programmingLanguage)
  
  
  return(db_git_history)
}

load_file_extensions = function(input_file_extension_path = '../../data/file_extension/languages.yml'){
  file_extensions = yaml.load_file(input_file_extension_path)
  
  ext_key = list()
  ext_value = list()
  
  tmp_names = names(file_extensions)
  
  i=1
  
  for(name in tmp_names){
    tmp_list = file_extensions[name]
    
    # removes all File Extensions but Programming Languages
    if(tmp_list[[name]]$type != "programming"){
      next;
    }
    
    for(extension in tmp_list[[name]]$extensions){
      ext_key[i] = extension
      ext_value[i] = name
      i = i + 1
    }
  }
  
  ext_map = data.frame(extension = unlist(ext_key, use.names=FALSE), programmingLanguage = unlist(ext_value, use.names = FALSE))
  
  return(ext_map)
}