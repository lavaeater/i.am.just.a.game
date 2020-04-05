# iamjustagame

A [LibGDX](http://libgdx.badlogicgames.com/) project generated with [gdx-liftoff](https://github.com/tommyettinger/gdx-liftoff).

## WIP feature of the day

None for now, I have to have a life.

## Done features
### Hud Showing Corona Progress

So I've done HUDs before, it's not *that* hard, but Scene2D is, in my mind, kind of a pain. Wrapping text, getting stuff to align, everything is a bit clunky and not "just working". That's what working as a "regular" developer for 20 years gets you, I guess, laziness and expecting stuff to hand everything to you for free. But the HUD will show updated info on the number of Infected, Removed (and Dead) and Susceptible. Maybe it could even give us a count of how many idiots there are that won't stay at home even with symptoms?

### Areas
Areas are now setup so that every area can contain anything, houses, restaurants, workplaces. They all have at least one travelhub, so that people can teleport to and from them easily. This makes the map at least slightly nicer-looking.



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