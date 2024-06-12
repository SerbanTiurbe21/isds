# Serviciul InterviewScoreDocument

Serviciul InterviewScoreDocument este un microserviciu care gestionează documentele de evaluare a interviurilor. Acesta oferă funcționalități pentru crearea, preluarea și actualizarea documentelor de evaluare a interviurilor, implementând mecanisme de verificare pentru a preveni duplicarea documentelor.

## Descriere

Serviciul InterviewScoreDocument este implementat folosind Spring Boot, oferind un layer robust de gestionare a documentelor de evaluare a interviurilor. Acesta facilitează gestionarea și stocarea documentelor prin integrare cu un repository, și include mecanisme avansate pentru gestionarea situațiilor de eroare, cum ar fi existența unui document duplicat. 

## Configurarea Proiectului

Serviciul InterviewScoreDocument este configurat printr-un set de reguli definite în codul sursă care include:
  - Metode pentru crearea, preluarea și actualizarea documentelor de evaluare a interviurilor
  - Verificări pentru prevenirea duplicării documentelor
  - Gestionarea excepțiilor pentru cazurile în care un document de evaluare a interviului există deja

## Dockerfile

Proiectul include un `Dockerfile` pentru containerizarea și desfășurarea ușoară a serviciului InterviewScoreDocument. Acesta este configurat pentru a rula pe portul 8084.

## Rularea Serviciului InterviewScoreDocument cu Docker

Pentru a rula serviciul InterviewScoreDocument într-un container Docker, urmează pașii simpli de mai jos pentru a construi și rula imaginea. 

### Construirea Imaginii Docker

  - Deschide un terminal și navighează în directorul sursă al proiectului InterviewScoreDocument, unde se află `Dockerfile`.
  - Rulează următoarea comandă pentru a construi imaginea Docker pentru InterviewScoreDocument. Acest pas va crea o imagine Docker locală etichetată ca `isds-service`:

    `docker build -t isds-service .`

### Rularea Containerului Docker

După construirea imaginii, poți rula containerul folosind imaginea creată:

`docker run -p 8084:8084 isds-service`

Această comandă va porni un container din imaginea interview-score-document, mapând portul 8084 al containerului pe portul 8084 al mașinii tale locale. Asta înseamnă că poți accesa serviciul InterviewScoreDocument navigând la http://localhost:8084 în browserul tău.

:bangbang: Însă acest pas nu este necesar pentru că există un Dockerfile în repository-ul central de unde se vor porni toate containerele. :bangbang:
