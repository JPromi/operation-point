# Operation Point
**Alle Einsätze der Feuerwehrn in Österreich an einer Stelle.**

## Ziel
Das Ziel dieser Anwendung ist es, alle Einsätze der Feuerwehrn in Österreich automatisch an einer Stelle zu sammeln.
Hierfür werden die Öffentlichen Daten über Schnittstelle gesammelt und in einer Datenbank gesammelt.

## Datenquellen
- Tirol: https://ffw-einsatzmonitor.at/lfs/proxytirol.php
- Steiermark: https://einsatzuebersicht.lfv.steiermark.at/einsatzkarte/data/public_current.json
- Oberösterreich: https://cf-einsaetze.ooelfv.at/webext2/rss/json_laufend.txt
- Niederösterreich: https://infoscreen.florian10.info/OWS/wastlMobile/getEinsatzAktiv.ashx
- Burgenland: https://www.lsz-b.at/fileadmin/fw_apps/api/
- Wien: Nicht verfügbar
- Salzburg: Nicht verfügbar
- Kärnetn: Nicht verfügbar
- Vorarlberg: Nicht verfügbar

## Einrichten
1. Bennene die Datei `application.example.properties` in `application.properties` um.
2. Passen Sie die Variablen in der Datei `application.properties` an.
3. Starten Sie die Anwendung mit `gradle bootRun`.
4. Um die Abfrage von Datenquellen zu bearbeiten, gehe in die Datenbank und ändere dort die Tabelle `crawl_service`.
5. 