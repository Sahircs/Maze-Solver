# Where do I put my files?

You should store all of your Java source files in the `src` directory, or subdirectories within the `src` directory.

You should store any graphics files (e.g. `.jpg`) or other non source-code resources in the `resources` directory, or subdirectories within the `src` directory.

# How do I compile/run my code

For convenience we've supplied some execution scripts that will compile/run code with the `--add-modules` flag set (for Java FX). Use `javac.sh` to compile on Linux and Mac OS, or `javac.bat` to compile on Windows. Likewise, use `java.sh` to run on Linux and Mac OS, or `java.bat` to run on Windows. E.g.

```
$ cd comp16412-coursework-1_username
$ ./javac.sh src/MazeApplication.java  
$ ./java.sh MazeApplication
```

Note that in the above example, on line 3, the name of the class file to run (`MazeApplication`) is **NOT** prefixed with the source path.

# How do I compile/run the tests?

We've supplied a shell file, `run_tests.sh`, that should work on Linux/Mac OS.

The file `run_tests.bat` should do the same thing on Windows. This file is purely for your convenience and (a) won't be used in marking, (b) isn't something we're going to provide lots of support for!


