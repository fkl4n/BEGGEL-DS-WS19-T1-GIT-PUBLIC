library(yaml)
library(stringr)

load_db_history = function(input_path, input_file_extension_path = '../../data/file_extension/languages.yml', input_encoding = "UTF-8",input_na_strings = "null", input_sep = ";"){
  db_git_history = read.csv(input_path, header=TRUE, encoding = input_encoding, na.strings = input_na_strings, sep = input_sep)

  # Convert timestamp from factor to POSIXct format
  db_git_history["timestamp"] = lapply(db_git_history["timestamp"], function(x) as.POSIXct(x, format='%Y-%m-%dT%H:%M:%S'))
  
  
  # Create a file extensions map
  ext_map = load_file_extensions()
  
  # Add programming language column to db_git_history based on file extension 
  db_git_history["programmingLanguage"] = lapply(db_git_history["file"], lapplyHelper, ext_map)
  
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

#Double lapply: temporary solution
transformExtToLan = function(x, map){
  ext = paste(".", as.character(tail(unlist(str_split(x, "[.]")), 1)), sep="")
  
  ifelse(ext %in% map$extension,
         as.character(map$programmingLanguage[match(ext, map$extension)]),
         "unknown")
}

lapplyHelper = function(x, map){
  lapply(x, transformExtToLan, map)
}