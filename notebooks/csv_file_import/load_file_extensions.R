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