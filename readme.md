JadowMD
===

JadowMD is an alternative tool for document outputing and writing for markdown.

Overview
---

JadowMD is provided as single fat jar file and works with jre8+,
so JadowMD has a lot of portability.
You can generate documents from markdown without neither any environment management nor dependency controll.

This tool is written in Kotlin.

Download
---

You can get a compiled fatjar from [here]().

How to Use
---

execute the following code.

```cmd
java -jar jadowmd.jar -r [target directory]
```

You can see output demo.



Comparison with existing tools
---


#### gitbook

- requires node and npm.
- can generate pdf


mdwiki

- クライアント側に何も入れなくていい
- 保守されていない
- cons
    - codeblockを入れると時々落ちる



