Nyisd meg a cmd-t és kövesd az útmutatást

Fordítsd le az appot az alábbi gradlew paranccsal:
./gradlew build

Docker rendszerkép létrehozása:
docker build -t equipmentrentalapp .

Vizsgáld meg a rendelkezésre álló rendszerképeket az alábbi paranccsal:
docker images

Indítsd el az újonan létrehozott képfájból a konténeredet:
docker run -d -p 8080:8080 equipmentrentalapp

Ellenőrizd, hogy rendben elindult és próbáld is ki.
docker ps
