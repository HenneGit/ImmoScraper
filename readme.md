# ImmoScraper

## Installation

Für die Installation muss das Projekt heruntergeladen werden.\
Um den ImmoScraper zu verwenden, wird ein Rechner mit **Java** (min. Java **Version 11**) benötigt.\
Um das Programm auszuführen muss die ausführbare **.jar-Datei** gestartet werden.\
Nun sollte eine Anwendung erscheinen und das Programm ist bereit!

## Handbuch

Wenn die ausführbare .jar-Datei gestartet wurde, sollte so ein Fenster erscheinen:

![alt text](./screenshots/ImmoScraper_Start.png "ImmoScraper - Startoberfläche")

Hier hat man die Möglichkeit mit **Los!** eine neue Suchanfrage zu einer Wohnung/Immobilie zu stellen oder eine bereits erstellte Suchanfrage zu bearbeiten.

#### Suchanfrage erstellen
Beim Klick auf **Los!** erscheint eine neue Maske:

![alt text](./screenshots/ImmoScraper_Query.png "ImmoScraper - Neue Suchanfrage")

Damit ImmoScraper etwas finden kann, werden hier ein paar **Informationen** zur gesuchten Wohnung/Immobilie benötigt.

Beispiel:\
![alt text](./screenshots/ImmoScraper_Example.png "ImmoScraper - Beispiel")

#### Suchanfrage speichern
Ein Klick auf **Speichern** speichert die derzeit eingegebenen Informationen als Suchanfrage unter dem angegebenen Namen.
![alt text](./screenshots/ImmoScraper_Save.png "ImmoScraper - Speichern")


Um eine Suchanfrage zu **bearbeiten** geht man zunächst zurück zum Start:
![alt text](./screenshots/ImmoScraper_Back.png "ImmoScraper - Zurück zum Start")

Hier wählt man die jeweilige Suchanfrage, die bearbeitet werden soll, aus der Drop-Down-Liste aus und klickt auf **Los!**:
![alt text](./screenshots/ImmoScraper_Edit.png "ImmoScraper - Suchanfrage auswählen")

Nun hat man die Möglichkeit seine Suchanfrage zu bearbeiten, sollte sich etwas geändert oder sollte man sich vertippt haben.

Ein Klick auf **Löschen** entfernt die jeweilige Suchanfrage wieder.
![alt text](./screenshots/ImmoScraper_Delete.png "ImmoScraper - Löschen")

Mit einem Klick auf **Suchen** werden die Informationen aus den Feldern der Suchanfrage als Query-String in den immoscout24-Link eingefügt.\
*(z.B. www.immobilienscout24.de/Suche/de/ + searchQuery + /wohnung-mieten?enteredFrom=one_step_search" )*\

Die Scraping-Einheit von ImmoScraper sucht sich die nächsten Wohnungen/Immobilien mit den jeweiligen Kriterien heraus und gibt die Informationen zurück.
Der Controller sendet diesbezüglich dann eine E-Mail mit den Informationen an die angegebene E-Mail-Adresse.

## Schnittstellen

ImmoScraper selbst spricht keine externen Schnittstellen an.\
Aber damit ImmoScraper Informationen zu Wohnungen/Immobilien nach bestimmten Suchkriterien finden kann, muss eine Suchanfrage angelegt werden.\
Damit eine Suchanfrage gespeichert wird und wiederverwendbar/bearbeitbar ist, wird die interne Schnittstelle **Repository** angesprochen. Durch das Ansprechen der Schnittstelle werden Funktionen im **Backend** aufgerufen, die die Informationen der Queries in einer **CSV-Datei** persistent abspeichern.
