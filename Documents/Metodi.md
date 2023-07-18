# Project metods layout

### App extends GameApplication

#### enum [EntityType]

#### initSettings()

* window settings and full screen
* starting scene
* runtime mode
* player options

#### initGame
* setlazy --> movimento camera morbido
* background

#### setLevel (not included)
* resets the position of the player at the start of the level
* Loads level from .tmx file

---

### MainLoadingScene extends LoadingScene

#### MainLoadingScene()
* loading scene (text, background)

---

### PlatformerFactory implements EntityFactory
* creazione delle entità nel gioco
#### newBackGround()

---

### App extends GameApplication extends Component
[physics, texture, animIdle, animWalk, jumps]

#### PLayerComponent()
* sprite and player overall definition

#### onAdded()

#### onUpdate()
* animations based on movement

#### left()

#### right()

#### stop()

---

## da fare
* caricare livello (to test)
* generare la piattaforma 
* inserire il giocatore
* movimento
* 