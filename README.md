# Sulautetun-Ohjelmointiprojekti-S2017
Sulautetun järjestelmän ohjelmointiprojekti, OAMK Syksy 2017

Lada: The Ultimate Challenge 2017

7.11.2017
Lisätty mukautettu Madgwick-suodatusta hyödyntävä lähdekoodi (lähde: https://www.arduino.cc/en/Tutorial/Genuino101CurieIMUOrientationVisualiser). Koodia mukautettu niin, että se antaa ulos vain tarvitsemamme kallistuskulman. 

Lisätty java-koodi, jossa luetaan sarjamonitorilta kallistuskulmatietoja ja päätellään näistä mihin suuntaan auton kuuluisi kääntyä. Koodi toimii proof-of-conceptina, jota voidaan hyödyntää varsinaisessa pelissä sarjamonitoridatan lukemiseen. Käytetty RXTX-kirjastoa (lähde: http://rxtx.qbang.org/wiki/index.php/Main_Page) ja mukautettua lähdekoodia (lähde: https://playground.arduino.cc/Interfacing/Java).

Lisätty myös SerialRotationReader.java, josta siis voidaan luoda sarjamonitorin haistelija.
-VV

Lisätty CreateMap.java jolla luodaan pelialue. LadaGame.java toimii projektissa pääluokkana.
-MMise

Lisätty luokkakaavio
-MMise

13.11 
Saatiin auto- ja karttaluokat yhteensopiviksi
-MMise

Lisätty highscoreserver-scripti, jonne olisi tarkoitus lähettää pistemääriä ja nimiä pelistä -VV

14.11

Lisätty LadaGameSerial, jossa auton ohjaus tapahtuu Genuinoa kallistelemalla. Ohjelmassa on myös EventHandler, joka sulkee sarjaportin kun ohjelma suljetaan. Ohjauksen herkkyyttä ei ole vielä säädetty. Poistettu myöhemmin, että saadaan merged ja forkit toimimaan kunnolla -VV
