# Ausführung der _CarRental_ Applikation mit Docker

## Beschreibung
Das hier beschriebene Vorgehen sorgt dafür, dass die Applikation in einer definierten Umgebung ausgeführt wird. Dies kann eine gute Alternative sein, falls es bei der Standard-Entwicklungsumgebung zu Problemen kommt, bspw. durch unpassende Node-Versionen, o.ä.

## Erstellen eines Docker-Image
Zunächst muss mithilfe des beiliegenden Dockerfiles ein Docker-Image erstellt werden. Dies geschieht durch aufruf des folgenden Kommandos in diesem Verzeichnis:
```bash
docker build -t car-rental .
```
Dadurch wird ein Image namens `car-rental` angelegt. Dieses ist darauf ausgerichtet, eine Umgebung bereitzustellen, die zuverlässig zur Ausführung der _CarRental_ Applikation genutzt werden kann. 

## Ausführen der Applikation
Wenn das Image erstellt ist, kann die Applikation wie folgt gestartet werden:
```bash
docker run -p8080:8080 -p35729:35729 --network=docker_default -v "$(pwd)/CarRental:/app" car-rental 
```
Dieses Kommando **muss** ebenfalls in diesem Verzeichnis eingegeben werden, damit es das Projektverzeichnis korrekt im Container einhängt. Auch die Angabe von `--network=docker_default` ist wichtig, damit der Zugriff auf die Datenbanken in den anderen Docker-Containern funktioniert. 

Wenn Sie die Container mit dem `docker-compose` File aus der Veranstaltung gestartet haben, müsste o.g. Netzwerkname stimmen. Sollten Sie Probleme haben, können Sie das Kommando `docker network ls` verwenden, um Docker-Netzwerke zu finden, die infrage kommen. Der Name dürfte auf _default_ enden.

Für den Zugriff innerhalb des Docker-Netzwerks muss der Hostname in `CarRental/src/main/resources/application.yml` auf `docker-mongo-1` geändert werden (wiederum unter der Annahme, dass `docker-compose` standardmäßig verwendet wurde).

Der MySQL-Server wäre entsprechend unter `docker-mysql` erreichbar.

Die Ports 8080 und 35729 (Live-Update der Oberfläche während der Entwicklung) müssen frei sein, oder umkonfiguriert werden.

Es sollte dann ein Build anlaufen, an dessen Ende die Applikation gestartet und unter http://localhost:8080 erreichbar ist.

Dadurch, dass das Projektverzeichnis in den Container gemountet wird, sollten Anpassungen am Code direkt zu einem Reload der Applikation führen und gleich sichtbar werden (Log beobachten). Grundsätzlich sollten entsprechende Entwicklungszyklen nicht wesentlich zeitaufwändiger sein als bei einer Ausführung der IDE.

Für nachfolgende wiederholte Verwendung kann der erstellte Container einfach gestartet werden. Dies geht deutlich schneller, als der initiale `docker run`.