# Project metods layout

### App extends GameApplication

#### enum *EntityType* [ ]
**Entity** player

#### initSettings()
* window settings and full screen
* starting scene
* runtime mode
* player options

#### initGame
* setlazy --> movimento camera morbido
* background



### MainLoadingScene extends LoadingScene

#### MainLoadingScene()
* loading scene (text, background)

---

### PlatformerFactory implements EntityFactory
* creazione delle entità nel gioco
#### newBackGround()

#### newPlayer()

---

### App extends GameApplication extends Component
[physics, texture, animIdle, animWalk, jumps]

#### PlayerComponent()
* sprite and player overall definition

#### onAdded()

#### onUpdate()
* animations based on movement

#### left()

#### right()

#### stop()

---

## to do
* Spawn and despawn di hal
* spostare dialogi in event
* centrare i dialogi
* creare i dialogi
* leve accendono luci
* leve accendono minigame
* minigame accende ascensore
* caricamento ascensore
* animazione ascensore apertura

## maybe to do
* isMovingX() per effetti visivi player (polvere ecc)
* angolazione torcia
* CRACCARE IL CAZZO DI CODICE DEI MINIGAME
* aggiungere il personaggio nei cryopod
* modificare il salto piu tieni premuto piu va in alto
