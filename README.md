# DCO

Serwer liczący korzysta z bazy danych MySQL. 
Należy pobrać ze strony http://dev.mysql.com/ i zainstalować serwer MySQL. Dokładny opis instalacji można znaleźć w Internecie,
np.w serwisie youtube.com. W celu ułatwienia sobiedalszych działań zaleca się wybranie typu instalacji „Developer Default”.
Po zainstalowaniuserwera MySQLnależy dodać użytkownika o nazwie OSM_CLIENT z hasłem somepass.
Użytkownikowi należy przypisać schemat domyślnypwr_osm_project.
Można to zrobić za pomocą narzędzia „MySQL Workbench”, które zostało zainstalowane razem z serwerem MySQL jeśli wybrano 
typ instalacji „Developer Default”.Kolejnym krokiem jest zaimportowanie danych do schematu pwr_osm_project. 
Plik zdanymi które  należy  zaimportować  znajduje  się  w  projekcie  pana  Cokana pod  nazwą 002_RestoreDB.sql.
Import danych również można wykonać z poziomu „MySQL Workbench”.
