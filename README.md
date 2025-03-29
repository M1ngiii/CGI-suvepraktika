# CGI Suvepraktika 2025 Proovitöö

Autor: Mihkel Kulu  
Projekt: CGI Suvepraktika proovitöö 2025

---

## Ülevaade

Veebipõhine lennubroneerimise rakendus, mis võimaldab kasutajal:

- Sirvida ja filtreerida olemasolevaid lende
- Valida istekohad vastavalt oma eelistustele
- Saada automaatseid isetkoha soovitusi

### Tööaeg

Tööle kulus ligikaudu 2 täispikka päeva (umbes 16-17 tundi).

### Raskused

Peamised raskused olid Spring Bootiga seotud, kuna pole varem seda kasutanud. Peamiselt said probleemid lahendatud dokumentatsiooni lugedes. Errorite lahendamisega aitas ka AI. Back-end muidu ise kirjutatud, peale lendude genereerimise komponendi, kus kasutasin samuti AI-d, et kirjutada lihtsamat koodi.
Lisaks olid ajalised probleemid, kuna keskendun hetkel pigem lõputööle. Seetõttu alustasin projekti tegemist natukene liiga hilja. Osa koodi on lohakalt kirjutatud, kuna polnud palju aega.

---

## Tehnoloogiad

- **Frontend:** SvelteKit, TypeScript, TailwindCSS
- **Backend:** Spring Boot (Java 21), Maven
- **Keskkond:** Docker, Docker Compose

Valisin frontendi arenduseks SvelteKit-i, kuna mul oli varasem kogemus Svelte'iga ning leian, et see on lihtsam, kui teised front-end libraryd.

---

## Dockeriga käivitamise juhend

Projekti saab käivitada täielikult Dockeris. See võimaldab süsteemi üles seada ilma lokaalsete Java, Node.js või muude sõltuvuste installita.

### Eeldused

- [Docker](https://www.docker.com/) peab olema paigaldatud
- [Docker Compose](https://docs.docker.com/compose/) peab olema saadaval

### Sammud

**1. Klooni repositoorium**

```bash
git clone https://github.com/M1ngiii/CGI-suvepraktika.git
cd cgi-suvepraktika
```

**2. Käivita Docker Compose**

```bash
docker-compose up --build
```

**3. Ava rakendus brauseris**

- Frontend: http://localhost:3000
- Backend (API): http://localhost:8080

---
