# I am just a game

A [LibGDX](http://libgdx.badlogicgames.com/) project generated with [gdx-liftoff](https://github.com/tommyettinger/gdx-liftoff).

## How I work on this project

I try to follow this paradigm for working on this project: **P**lan, **W**ork and **E**valuate (or **A**sess), where I plan for five what I will do for the coming say 25 minutes of time, and then after that time is up I smash together an planning / evaluation for the coming 20 minutes.

## WIP feature of the day

It appears that I can come up with features that I perform no valuable work on, whatsoever. This time it seems my thought process was that I was going to start working on Social Needs, but hey, I implemented Path Finding instead. So, social needs is on some kind of todo-list for now.

### Is this readme actually a blog of sorts?

Maybe it is, in fact. I will now abandon the development branch and simply work like a gitflow-thing instead.

### Cooler map-generation

#### What I Want:
I want a city that builds itself - on rock and roll. I want to start with some kind of simple seed, that then sprawls outward from that seed and builds city blocks containing homes, restaurants and workplaces - and of course, the almightly travel hub. The shape of these city blocks need not be square, but they could be. Ideally they should look "organic" or "natural", as an actual city would look. But I want them to be blocks - so maybe the builder should work within that concept, sort of take a start node, any node on the map, and split off a new city block - they should not be overlapping. This is a good starting point, work from this.

Goal: Does the map builder make city blocks? If so, this feature is DONE!

#### Define a block!

A block is a system of streets and places. Blocks may be residential (majority of places are homes) or commercial (majority is restaurants and work places). They always have a Travel Hub. The system of streets can be created using different patterns of construction. I would very much like some kind of organic-looking street system. But a good enough start would be Main Street / Side Streets. This means we have a Travel Hub. From that travel hub we have one street going in a random direction.

Or all directions? Start with one.

Travel Hub -> Street -> Every node on the street has a side street. Every side street has a house on each or not each side.


### Time step

The time steps, fifteen minutes at a time. Fine and dandy. But a bit dull, innit? This means the AI only makes decisions every fifteen minutes, which perhaps is fine, but it also means that every single NPC makes their decisions at basically the same exact time. I will start by just changing the time step, that should make a difference to begin with. 

This is a very interesting area. I want to be able to speed up the game if needed... and slow it down. But also we want to avoid the NPCs making expensive choices ALL the time. Hmm.

### Social needs 

So, I removed social needs because they worked by making people just walk up to their closest friend. That seems a bit dull, let's have them do something else - the easiest is of course for them to arrange to meet at someones's house or at a restaurant. What we are going towards here is of course changing the entire need economy to something more abstract. Perhaps we need "work hours" - defined by some job a person has at a workplace, instead of just spots where they replenish their money stat.

### Ambling and free time

To be described.

## Done features

### Path finding

So, the entire map was changed into a glorious messy graph-based structure instead of, well, nothing. Previously only places and NPCs had coordinates and I check if players were inside boxes and stuff to keep track of them arriving to places and stuff. That changed with this. Places have nodes, which makes it easy to figure out what the "goal" node for the path finding module is. This now makes it so that people only walk on the streets. The only issue with this is that people now "stack up" and seem to be fewer than they really are... or?

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