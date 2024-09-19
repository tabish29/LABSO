# Guida all'Utilizzo dell'Applicazione Server Multi-Client

Questo progetto consiste in un'applicazione server multi-client in Java che simula l'apertura di conti bancari e il trasferimento di fondi tra di essi. L'applicazione supporta due modalità di trasferimento:

1. **Modalità Standard (`transfer`)**: In questa modalità, il trasferimento di fondi avviene in modo immediato. I fondi vengono trasferiti tra i conti senza alcun ritardo.

2. **Modalità Interattiva (`transfer_i`)**: In questa modalità, i conti coinvolti nel trasferimento non possono essere modificati da altre operazioni se non da quella che ha iniziato il trasferimento. Questo garantisce che il trasferimento sia gestito in modo esclusivo fino al completamento.


## Istruzioni per Compilare e Usare l'Applicazione

### Passo 1: Aprire i Prompt dei Comandi

Aprire un prompt dei comandi per il server e  diversi prompt dei comandi per i client.
   - Assicurarsi di aprire tanti prompt dei comandi quanti sono i client che si desidera utilizzare (almeno due client sono consigliati per osservare la concorrenza).

### Passo 2: Compilare i File Java

 Una volta entrati nella cartella compilare tutti i file Java presenti nella cartella con il comando:

```java
   javac *.java
```
### Passo 3: Avviare il Server
Avviare in un prompt di comandi il server con il seguente comando:

```java
   java Server n_porta
```
### Passo 4: Avviare i Client
Avviare in prompt diversi il numero di client che si vogliono aggiungere con il seguente comando,sostituire n-porta con lo stesso numero inserito nella fase precedente:

```java
  java Client localhost n-porta
```
### Passo 5: Visualizzare i Comandi Disponibili
Per visualizzare i comandi disponibili e la loro funzionalità, digitare il comando info all'interno del client e premere Invio