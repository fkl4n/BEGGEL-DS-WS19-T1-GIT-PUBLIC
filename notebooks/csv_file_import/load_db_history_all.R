library(yaml)
library(stringr)
library(data.table)

load_db_history_all = function(path_to_check = "../../Workspace/histories", file_extensions_function_path = "../csv_file_import/load_file_extensions.R"){
  source(file_extensions_function_path)
  
  dataDir <- path.expand(path_to_check)
  dataFls <- dir(dataDir, pattern = "csv$", full.names = TRUE)
  db_git_history <- data.table::rbindlist(lapply(dataFls, data.table::fread, showProgress = TRUE))
  
  db_git_history$timestamp = as.POSIXct(db_git_history$timestamp, format='%Y-%m-%dT%H:%M:%S')

     # Create a file extensions map
  ext_map = load_file_extensions()
 
   # Add programming language column to db_git_history based on file extension 
  #db_git_history["programmingLanguage"] = lapply(db_git_history["file"], lapplyHelper, ext_map)
  
  db_git_history <- mutate(db_git_history,programmingLanguage = str_split(file, ".(?=[.])",simplify = TRUE)[,2])
  db_git_history$programmingLanguage <- with(ext_map, programmingLanguage[match(db_git_history$programmingLanguage, extension)])
  
  language_levels_with_na = levels(db_git_history$programmingLanguage)
  db_git_history$programmingLanguage = addNA(db_git_history$programmingLanguage)
  levels(db_git_history$programmingLanguage) = c(language_levels_with_na, "unkown")
  
  # Flatten the resulting List to a vector
  db_git_history["programmingLanguage"] = unlist(db_git_history$programmingLanguage, use.names=FALSE)
  
  
  # Convert the FileExtensions to factors
  db_git_history$programmingLanguage = as.factor(db_git_history$programmingLanguage)
  
  
  return(db_git_history)
}