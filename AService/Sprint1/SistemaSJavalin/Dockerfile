## ----------------------------------------------------------
## Dockerfile for SistemaSJavalin (Calcolo Espressione)
## ----------------------------------------------------------

FROM eclipse-temurin:17.0.5_8-jre-focal
## ADD extracts the tar
ADD build/distributions/SistemaSJavalin-1.0.tar /
WORKDIR /SistemaSJavalin-1.0/bin

## Comando per avviare il server
CMD ["bash", "SistemaSJavalin"]

## ISTRUZIONI PER L'USO:
## 1) gradlew distTar
## 2) docker build -t sistemasjavalin:1.0 .
## 3) docker run -it --rm --name calcolatore -p 8080:8080 sistemasjavalin:1.0

## http://localhost:8080/eval?x=0.0
## result: 1.0


