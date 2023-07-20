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
* guardare le texture e gli sprite
* isMovingX() per effetti visivi player (polvere ecc)