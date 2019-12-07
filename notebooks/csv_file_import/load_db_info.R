load_db_infos = function(input_path = '../../dbec/DBRanking.csv', input_encoding = "UTF-8", input_na_strings = "null"){
  db_infos = read.csv(input_path, header=TRUE, encoding = input_encoding, na.strings = input_na_strings)
  
  # Remove unneccesary columns
  drop_list = c("Name", "Ausschlieﬂlich.ein.Cloud.Service", "DBaaS.Angebote", "Anmerkung")
  db_infos = db_infos[ , !(names(db_infos) %in% drop_list)]
  
  # The following variables were imported as factors, but they should be numerical 
  columns_to_numeric = c("Punkte",
                         "Rang", 
                         "Suchmaschinen",
                         "Erscheinungsjahr",
                         "Graph.DBMS",
                         "Document.Stores",
                         "Key.Value.Stores",
                         "Relational.DBMS",
                         "Time.Series.DBMS",
                         "Multivalue.DBMS",
                         "Object.oriented.DBMS",
                         "RDF.Stores",
                         "Wide.Column.Stores",
                         "Navigational.DBMS",
                         "Event.Stores",
                         "Native.XML.DBMS",
                         "Content.Stores")
  
  db_infos[columns_to_numeric] = lapply(db_infos[columns_to_numeric], as.numeric)
  
  # The following variables were imported as factors, but they should be characters
  columns_to_chr = c("Kurzbeschreibung",
                     "Website",
                     "Technische.Dokumentation",
                     "Entwickler",
                     "Aktuelle.Version",
                     "Server.Betriebssysteme")
  
  db_infos[columns_to_chr] = lapply(db_infos[columns_to_chr], as.character)
  
  return(db_infos)
}