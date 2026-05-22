# TweetBird - Webová sociální síť (Semestrální práce TS1)

Tento repozitář obsahuje semestrální práci z předmětu Testování softwaru (TS1). Zaměřuje se na analýzu, návrh testů a automatizaci reálných testovacích scénářů pro webovou aplikaci TweetBird.

**Autoři:** Vít Hitzger, Štefan Jiránek

---

## Popis funkcionality aplikace
TweetBird je webová sociální síť postavená na vlastní MVC architektuře v PHP.
* Správa účtů: Registrace a přihlášení s validací dat, správa relací přes $_SESSION.
* Příspěvky: Nahrávání obrázků s ořezem (knihovna GD). Limit velikosti: 500 KB, titulek min. 7 znaků.
* Interakce: Asynchronní lajkování (Fetch API) a dynamické načítání komentářů (min. délka 5 znaků).
* Moderování: Administrátor má přístup k dashboardu s právem spravovat uživatele a obsah.

---

## Analýza vstupů a kombinatorika (Pairwise)

### 1. Nastavení propagace příspěvku
Analýza ekvivalentních tříd (EC) a mezních hodnot pro číselné vstupy:
* Denní rozpočet: 100 Kč až 50 000 Kč (Hranice: 99, 100, 101, 49999, 50000, 50001)
* Doba trvání: 1 až 30 dní (Hranice: 0, 1, 2, 29, 30, 31)
* Cílový rádius: 1 až 80 km (Hranice: 0, 1, 2, 79, 80, 81)

Metodou Pairwise Testing bylo vygenerováno 9 optimálních testovacích scénářů (TC 1.01 - TC 1.09) pokrývajících všechny dvojice parametrů.

### 2. Nastavení soukromí
U kategoriálních dat byly zadefinovány validní volby (Public, Friends, Private) a otestovány okrajové stavy (např. odeslání prázdné nebo podvržené hodnoty přes API).

---

## Procesní testy (Control Flow Graph)
Pomocí nástroje CPT Manager byly navrženy průchody zaručující 100% pokrytí všech hran (TDL 2).

* Proces 1 (Registrace a příspěvek): Sleduje chování uživatele při registraci/přihlášení a detekci chybové smyčky u uploadu souboru většího než limit.
* Proces 2 (Správa komentáře): Ověřuje chování systému pro nepřihlášeného uživatele, zadání příliš krátkého komentáře a zobrazení moderačních prvků pro autora/admina.

---

## Automatizace: E2E Testy v Selenium WebDriver
Automatizované testy jsou implementovány v Javě s využitím frameworku JUnit 5 a rozděleny do dvou testovacích tříd podle jejich charakteru.

### 1. Třída ProcessTest (Procesní scénáře)
* userInteractingWithPosts – Simuluje ucelenou interakci: přihlášení uživatele, přechod na detail specifického postu, úspěšné přidání komentáře, ověření zobrazení tlačítka pro jeho smazání, smazání komentáře, přidání/odebrání lajku a následné odhlášení s kontrolou stavu UI.
* creatingUserPostingPostAndThenDeletingIt – Testuje vytvoření příspěvku: přihlášení, přechod na formulář, vyplnění titulku, nahrání lokalizovaného obrázku (controller.png), simulace kliku pro ořez, publikace a ověření kaskádového smazání příspěvku z feedu.

### 2. Třída WebTest (Jednotlivé funkční testy)
* testSuccessfulLogin – Ověření úspěšného přihlášení běžného uživatele a zobrazení tlačítka pro odhlášení.
* testSuccessfulAdminLogin – Kontrola přesměrování administrátora na URL /dashboard po úspěšném přihlášení.
* testUnsuccessfulLogin – Verifikace zobrazení chybové hlášky při zadání nesprávného hesla.
* testSuccessfulLogout – Kontrola korektního zničení relace a návratu prvků pro nepřihlášené uživatele.
* testUnauthorizedAccessRedirect – Ověření funkčnosti middleware (odstranění cookies, pokus o přístup na /dashboard a kontrola přesměrování na hlavní stránku).
* likePost – Testování přičtení a odečtení lajku u příspěvku po přihlášení.
* commentPost – Úspěšné vložení platného komentáře, ověření přítomnosti akce smazání a jeho odstranění.
* invalidCommentPost – Negativní test: pokus o odeslání textu "aha" (méně než 5 znaků) a ověření zobrazení chybové zprávy.
* createAdmin – Administrátorský test povýšení uživatele na roli administrátora pomocí tlačítka v dashboardu.
* deleteUser – Administrátorský test smazání uživatele z tabulky v dashboardu.

### Datově řízené testování (Parameterized z CSV)
* testRegistrationScenarios (Zdroj: /data/registration_data.csv) – Parametrizovaný test, který hromadně ověřuje různé registrační scénáře (kombinace uživatelských jmen, e-mailů, hesel) a porovnává chování s očekávaným úspěchem či konkrétním ID chybového elementu.
* testChangeDisplayName (Zdroj: /data/login-names.csv) – Datem řízená změna zobrazovaného jména v profilu uživatele, testující validní i nevalidní jména s následným ověřením success/error zpráv v UI.

---

## Spuštění testů

### Požadavky
* Java JDK 17+
* Maven
* Google Chrome + odpovídající ChromeDriver

```bash
mvn clean test
