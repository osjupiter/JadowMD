[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fosjupiter%2FJadowMD.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2Fosjupiter%2FJadowMD?ref=badge_shield)

JadowMD
===

JadowMD is command line tool to generate html documents from markdown files.


Overview
---

JadowMD is provided as single jar file and works with jre8+,
so JadowMD has a lot of portability.
You can generate documents from markdown without annoying environment managements or dependency controll but with only jre and a single jar.

This tool is written in Kotlin.

Feature
---

### PlantUML support

Write a code block with the language identifier `puml`.

```puml
@startuml
A -> B
A <-- B
@enduml
```
[demo](https://osjupiter.github.io/JadowMD/demos/jadodoc.html)


Download
---

You can get a compiled jar file from [Releases](https://github.com/osjupiter/JadowMD/releases).

How to Use
---

execute the following code.

```cmd
java -jar jadowmd.jar  [target markdown] -o [output file]
```

or

```cmd
java -jar jadowmd.jar -r [target directory] -o [output file]
```


[demo](https://osjupiter.github.io/JadowMD/demos/jadodoc.html) 


Now JadowMD supports a specified single template using [bulma](https://bulma.io/) and [bulmaswatch](https://jenil.github.io/bulmaswatch/).
This template generates a single html file from multiple source markdown files.
For details, See [generated html source code](https://github.com/osjupiter/JadowMD/blob/master/docs/demos/jadodoc.html).




## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fosjupiter%2FJadowMD.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2Fosjupiter%2FJadowMD?ref=badge_large)