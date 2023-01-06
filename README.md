# Alaska

Why the name `Alaska`? No idea, just sounds good.

I wanted to play with SBT multi-project builds (the way I split the project is not the best, but it's good enough for my
purposes), custom tasks, typeclasses, cats-parse and cats-effect.
This work is something similar to the job I had.

TODO:
- [x] readme
- [x] custom task generating a file similar to something I had to parse one day
- [x] csv serializer implemented as a typeclass
- [ ] parser of the "strange format"
- [ ] simple implementation of the fanout (raw Scala, no Cats Effect)
- [ ] implementation of the fanout with Cats Effect, as a practical exercise with this lib
(streaming lib could be useful, but I want to reinvent the wheel, for science)
- [ ] benchmark (simple impl. vs CE; input around 1.5 GB)

What the fanout is? `Valid.Text` should land in one file, `Valid.Num` in another, errors somewhere else.
They should be valid csv files (apart from errors, which can contain plain text). In case of empty result, the
valid files should be created anyway (with the header).

There should be a lot of Unit Tests.
