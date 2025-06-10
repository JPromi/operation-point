# Operation Point
**Alle Einsätze der Feuerwehren in Österreich an einer Stelle.**

## Informationen
### Ziel
Ziel dieser Anwendung ist es, alle Einsätze der Feuerwehr in Österreich an einer Stelle zu sammeln.
Die Daten werden in einem regelmäßigen Intervall von den jeweiligen Schnittstellen abgefragt und in einer Datenbank gespeichert.

Nachdem jedes Bundesland eine eigene Art hat, haben wir ein einheitliches Format erstellt, welches die Daten bestmöglich abspeichert. Manche Felder sind speziell für nur ein System.

### Features
- Abfrage der Aktiven Einsätze
- Abfrage eines einzelnen Einsatzes
- Abfrage der Feuerwehren (erstellt durch Einsatz)

### Daten genauigkeit
Bei manchen Einsätzen kann es vorkommen, dass die Daten nicht vollständig sind oder fehler enthalten. Ebenso kann es passieren das Gerätschaften/Fahrzeuge oder Feuerwehren in die falsche Tabelle eingetragen werden. Das liegt daran, dass es nicht immer eindeutig erkennbar ist, ob es sich für eine Feuerwehr oder eine Gerätschaft/Fahrzeug handelt.\
Diese Daten können in der Datenbank korrigiert werden.

### Ideen
- Abfrage der Einsätze in einem bestimmten Zeitraum
- Abfrage der Einsätze nach einem bestimmten Ort
- Abfrage der Einsätze von einer bestimmten Feuerwehr
- Admin Pannel zur Verwaltung der Feuerwehren, Gerätschaften und Einsätze

### Resources
| Bundesland       | Beschreibung                                    | Links                                                                                                                       |
|------------------|-------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| Niederösterreich | Reverse Engineering der Wastl Mobile, GRISU app | [Wastl Modile](https://infoscreen.florian10.info/ows/wastlmobileweb/), [GRISU APP](https://github.com/Grisu-NOE/mobile-app) |
| Oberösterreich   | Öffentliche Publizierung                        | [OÖLFV Dokumentation](https://einsaetze.ooelfv.at/publikationsformen)                                                       |
| Steiermark       | Reverse Engineering der Einsatzkarte            | [Einsatzkarte](https://einsatzuebersicht.lfv.steiermark.at/lfvasp/einsatzkarte/karte_app_public.html)                       |
| Tirol            | Reverse Engineering der Einsatz App Tirol       | [Aktuelle Einsätze Tirol](https://play.google.com/store/apps/details?id=alarmierung.lfv.his.alarmierungen&hl=de)            |
| Burgenland       | Reverse Engineering der Einsatzkarte            | [Feurwehreinsatzkarte Burgenland](https://www.lsz-b.at/fileadmin/fw/lsz_demo.html)                                          |

Für Wien, Salzburg, Kärnten und Vorarlberg gibt es keine öffentliche Schnittstelle, welche einsätze bereitstellt.

### Datenquellen
- Tirol: https://ffw-einsatzmonitor.at/lfs/proxytirol.php
- Steiermark: https://einsatzuebersicht.lfv.steiermark.at/einsatzkarte/data/public_current.json
- Oberösterreich: https://cf-einsaetze.ooelfv.at/webext2/rss/json_laufend.txt
- Niederösterreich: https://infoscreen.florian10.info/OWS/wastlMobile/getEinsatzAktiv.ashx
- Burgenland: https://www.lsz-b.at/fileadmin/fw_apps/api/
- Wien: Nicht verfügbar
- Salzburg: Nicht verfügbar
- Kärnetn: Nicht verfügbar
- Vorarlberg: Nicht verfügbar

## Entwicklung
### Einrichtung
1. Bennene die Datei `application.example.properties` in `application.properties` um.
2. Passen Sie die Variablen in der Datei `application.properties` an.
3. Starten Sie die Anwendung mit `gradle bootRun`.
4. Um die Abfrage von Datenquellen zu bearbeiten, gehe in die Datenbank und ändere dort die Tabelle `crawl_service`.

### Swagger
Die Swagger-Dokumentation kann unter [`/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html) erreicht werden.

### Daten

#### Federal States
| Bundesland       | Abkürzungen        |
|------------------|--------------------|
| Niederösterreich | la, lower-austria  |
| Oberösterreich   | ua, upper-austria  |
| Steiermark       | st, styria         |
| Tirol            | ty, tyrol          |
| Burgenland       | bl, bg, burgenland |
Diese Abkürzungen werden für `/operation/list/{federalState}` verwendet.