# Semestrální projekt: Testování webové aplikace TweetBird

**Předmět:** TS1
**Autor:** Hitzger Vít, Štefan Jiránek
**Akademický rok:** 2026
**Vyučující:** Václav Smítka

---

## 1. Úvod a popis aplikace

Tento projekt se zaměřuje na návrh a realizaci testovací strategie pro webovou aplikaci **TweetBird**.

**TweetBird** je zjednodušený klon sociální sítě Instagram. Umožňuje uživatelům registrace, přihlašování, psaní krátkých příspěvků a lajkování příspěvků].

### Použité technologie aplikace (SUT - System Under Test)
* **Frontend:** HTML/CSS/JS
* **Backend:** PHP
* **Databáze:** MySQL

---

## 2. Cíl a rozsah testování

Cílem projektu bylo ověřit funkčnost, spolehlivost a bezpečnost klíčových modulů aplikace. Testování bylo zaměřeno především na backendové API a kritické uživatelské scénáře na frontendu.

### Rozsah testování (Scope)
* Správa uživatelských účtů (Registrace, Přihlášení, Profil).
* Tvorba a zobrazování příspěvků.
* Interakce (Lajky, Sledování uživatelů).

## 3. Testovací strategie a použité metody

V projektu byly kombinovány různé přístupy k testování, aby bylo dosaženo co nejlepšího pokrytí kódu a funkcionalit.

### Typy testů
1.  **Jednotkové testy (Unit Tests):** Testování izolovaných funkcí a logiky na backendu (validace dat, pomocné funkce).
2.  **Integrační testy (Integration Tests):** Ověření správné komunikace mezi API endpointy a databází.
3.  **End-to-End (E2E) testy:** Automatizované testování celých uživatelských toků v prohlížeči.
4.  **Explorativní testování:** Manuální testování zaměřené na odhalení neočekávaného chování UI.


### Prerekvizity
[DOPLŇTE instrukce, jak aplikaci zprovoznit, např.:]
```bash
# Naklonování repozitáře
git clone [https://github.com/](https://github.com/)[VÁŠ_LOGIN]/tweetbird.git
cd tweetbird

# Instalace závislostí pro aplikaci i testy
npm install
# nebo pokud máte separátní složku pro testy:
# cd testing && npm install