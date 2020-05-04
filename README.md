# I am just a game

A [LibGDX](http://libgdx.badlogicgames.com/) project generated with [gdx-liftoff](https://github.com/tommyettinger/gdx-liftoff).

## How I work on this project

I try to follow this paradigm for working on this project: **P**lan, **W**ork and **E**valuate (or **A**sess), where I plan for five what I will do for the coming say 25 minutes of time, and then after that time is up I smash together an planning / evaluation for the coming 20 minutes.

## Current thoughts

So, is this readme a blog? Or what is it? Is it perhaps shit?

## Current Immediate TODO
* Turn off Corona - done
* Change City to small one-block city - done
* Fix stats handling and take a look at Eat-Neutral-Eat cycle and NPC state - WIP
* Social Needs and Boredom
* Money should have no upper limit

Needs need to be categorized into musts and wants... or something. If you do not eat or sleep, you die, but you can manage to exist without money or social context - maybe. 

The entire purpose of working with the needs in the game is to make it more complex, to create more advanced behavior that sort of can steer the inhabitants of the city into more random behavior.

So needs might be categorized into two groups, as of now. And that would be a boolean, simply put. It is "Essential" or not.

Let's do something fun with Sealed Classes or something.

### Pathfinding improvements
What would I want to do with my "game" and the concepts I've explored in it? One issue I see right now is that we have tons of nodes which degrades performance - when we also deal with tons of NPCs. How do we optimize that? Another thing is that the NPCs choose strange paths, for some reason. I am not entirely sure why. I think the cost estimate for every path is whack and also, the prioritization of paths. A* requires some kind of cost - well the cost might be related to distance between nodes, which is a change I reintroduced. But I also do not see why the player would take some of the routes. To work on that we need to reduce the map to only one living area and one commercial area. We don't need a ton of stuff in each - which is easy enought to accomplish. So, performance, there is another aspect as well - we could precalculate paths for every node to every Place. So, wherever the NPC is currently, we can just lookup the path to a place - which would be infinitely faster than calculating it ad hoc for everyone. On the other hand, that lookup would be very large indeed after a while. Perhaps implemented as a cache that can hold a certain number of paths? An assumption would be that paths to TravelHubs might vary quite a bit but paths FROM travelhubs to the places would not - prime for pre-calculations!

So, that about pathfinding and stuff. 

### Needs Improvements
#### Eating-Neutral-Eating Cycle
One observation has been that NPCs that are eating are in fact oscillating between fulfilling the need and Neutral. This goes back to the state machine and how everything is implemented there. Perhaps we need some better mechanisms for controlling the players state instead of a state machine. With better I simply mean better, not more complicated. So, why is it going eating-neutral-eating? If we could fix that we wouldn't have to fix everything else. 
#### Social Needs
Our NPCs need to have fun. Really, what they need is more complex and multi-faceted needs. We need to turn this city into a chaotic mess of life. NPCs need memories, attitudes, relations. They can all have a... graph perhaps? Or could the state of the world simply be a graph? Containing some kind of weird data about everything? So we could have counts on how often a person goes somewhere and also a measure of how easily bored he is and what people he meets and reputations and such. We need to up the RPG elements. 
#### Turn off the Corona
The infection cycle is fun, but not that fun. We need to go back to just building a simulated city with people, that's what I find really interesting.

### Time step
The time steps, fifteen minutes at a time. Fine and dandy. But a bit dull, innit? This means the AI only makes decisions every fifteen minutes, which perhaps is fine, but it also means that every single NPC makes their decisions at basically the same exact time. I will start by just changing the time step, that should make a difference to begin with. 

This is a very interesting area. I want to be able to speed up the game if needed... and slow it down. But also we want to avoid the NPCs making expensive choices ALL the time. Hmm.

### Social needs + Neutral States with Ambling
#### Social Needs
So, I removed social needs because they worked by making people just walk up to their closest friend. That seems a bit dull, let's have them do something else - the easiest is of course for them to arrange to meet at someones's house or at a restaurant. What we are going towards here is of course changing the entire need economy to something more abstract. Perhaps we need "work hours" - defined by some job a person has at a workplace, instead of just spots where they replenish their money stat.
#### Ambling and free time
What does an NPC do when he is not hungry, tired or poor? Well, this should be controlled by needs as well, we had them before: Social and Fun. So when an NPC feels lonely or bored, there should be activities they could be doing then. 

## Done features
### Current Map Generation
Map generation is now done using text-based templates like this swrstsh where s is street, w is workplace, r restaurant, t travelhub and of course, h is for home. Using this we can more easily modify the layout of city blocks so that we get the perfect city - or just the city we want for now.

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