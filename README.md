# iamjustagame

A [LibGDX](http://libgdx.badlogicgames.com/) project generated with [gdx-liftoff](https://github.com/tommyettinger/gdx-liftoff).

### Current WIP feature

Areas for population etc. We have "places" of the types workplace, restaurant, etc. Now we will add "areas" where we can add houses or places as needed. The vision in my head is that every area has a travel hub, and then some number of "places". So areas are either residential or commercial, just like in a city planning scenario. They are also square-shaped. This will make it easier to divide them up for the places needed.

So, an area is, say, a 100x100. It has children - places. Their positions are relative to the areas position. Easy. All this is managed by some abstract base class.


### Done features




## Gradle

This project uses [Gradle](http://gradle.org/) to manage dependencies. Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands. Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `pack`: packs GUI assets from `raw/ui`. Saves the atlas file at `assets/ui`.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.