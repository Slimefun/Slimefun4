# Contributing to Slimefun
This document outlines various ways how you can help contribute to Slimefun and make this a bigger and better project.<br>
All contributions must be inline with our [Code of Conduct](https://github.com/Slimefun/Slimefun4/blob/master/.github/CODE_OF_CONDUCT.md) and [License](https://github.com/Slimefun/Slimefun4/blob/master/LICENSE).
Please also follow the templates for Issues and Pull Requests we provide.

## :beetle: 1. Issues: Bug Reports
One of the foundations for good software is reliability. To facilitate this reliability, our community must work together to crush bugs that arise. 
This of course requires good information and knowledge about ongoing bugs and issues though.

You can help this project by reporting a bug on our [Issues Tracker](https://github.com/Slimefun/Slimefun4/issues).<br>
Please adhere to the provided template and provide as much information as possible.
For more info on how to make good and helpful bug reports, check out our article on [How to report bugs](https://github.com/Slimefun/Slimefun4/wiki/How-to-report-bugs).

If you encounter an issue which has already been reported, please don't open a new one.<br>
It would be awesome though if you could post a comment on the existing issue which explains how you were able to reproduce this yourself.
The more context and information we get, the easier we can fix it.

## :hammer_and_wrench: 2. Pull Requests: Bug Fixes
Bugs that have been reported need to be fixed of course.<br>
Any open Issue on our [Issues Tracker](https://github.com/Slimefun/Slimefun4/issues) is waiting to be fixed.

This is an Open-Source project and we love Pull Requests. 
So if you have an idea on how to approach a known issue, feel free to make a [Pull Request](https://github.com/Slimefun/Slimefun4/pulls) which fixes this bug.
You can also comment on the existing Issue, proposing your idea or communicating that you wanna work on this.

## :wrench: 3. Pull Requests: Additions/Changes
Slimefun is an Open-Source project and anyone is allowed to make changes or add content to this plugin!

Please visit our [Discord Server](https://discord.gg/slimefun) and share your ideas first, we hate to reject changes because the community disagrees.<br>
So communicating your intended changes before-hand will ensure that you don't put too much work into something that might get rejected.

We also have a suggestions section in our Discord Server too. Suggestions can be placed in the `#suggestions` channel and community members can vote on a suggestion.
Suggestions which gotten enough votes will be moved to `#approved`.
Therefore our `#approved` is a great place to start looking for ideas on what to add or change, since it will definitely be something a large number of people agree with.

Also consider making an addon for your additions when they get too large, too abstract or too "niche".
You can check out our [Developer Guide](https://github.com/Slimefun/Slimefun4/wiki/Developer-Guide) for a guide on how to create a Slimefun addon..

## :earth_africa: 4. Pull Requests: Translations
Another great way to contribute to Slimefun is by working on translations for the project.
Slimefun's translation is available on [Crowdin](https://crowdin.com/project/slimefun).
Just find a language you are fluent in and translate away. But make sure to submit a "Review Request" when you are done.
One of our Language Moderators will review the changes and submit a Pull Request to the project for you.

#### Language Moderation
Very active community translators will have the option to become a "Language Moderator". 
Language Moderators are responsible for proof-reading any new translations for their designated language and correct it when they see a mistake.

For more info on how or what to translate, check out our article on [How to translate Slimefun](https://github.com/Slimefun/Slimefun4/wiki/Translating-Slimefun).

## :scroll: 5. Pull Requests: Wiki contributions
Slimefun is a very large project and might be quite intimidating for new players.
That's why good documentation is always nice and helpful.
If you have played with Slimefun for a while and gotten yourself familiar with how things work, please consider contributing your experiences and knowledge to others via the wiki!
It would help out a lot :heart:

You can find a tutorial on how to contribute to our wiki right here:<br>
https://github.com/Slimefun/Slimefun4/wiki/Expanding-the-Wiki

## :star: 6. Pull Requests: Code Quality
Slimefun uses [sonarcloud.io](https://sonarcloud.io/dashboard?id=Slimefun_Slimefun4) to monitor Code Quality.

We always welcome quality improvements to the code and the "Code Smells" section on [sonarcloud.io](https://sonarcloud.io/dashboard?id=Slimefun_Slimefun4) is a great place to start.
But please keep in mind that some design patterns may not be changed too abruptly if an addon depends on them. 
To prevent any accidents from happening, please contact us on our [Discord Server](https://discord.gg/slimefun) before-hand and state your intended changes.

#### Documentation
Code documentation is also a great way to improve the maintainability of the project.
1. Every class and every public method should have a Javadocs section assigned to it. 
2. Classes should also include an `@author` tag to indicate who worked on that class.
3. Methods and parameters should be annotated with `@Nullable` or `@Nonnull` to indicate whether or not null values are accepted.

Feel free to visit our [Javadocs](https://slimefun.github.io/javadocs/Slimefun4/docs/overview-summary.html)

#### Unit Tests
Unit Tests help us test the project to work as intended in an automated manner.<br>
More or better Unit Tests are always good to have, so feel free to submit a Test and place it in our [src/test/java](https://github.com/Slimefun/Slimefun4/tree/master/src/test/java/io/github/thebusybiscuit/slimefun4/testing) directory

We are using [Junit 5 - Jupiter](https://github.com/junit-team/junit5/) and [MockBukkit](https://github.com/seeseemelk/MockBukkit) as our testing environment.<br>
Every new Unit Test should have a `@DisplayName` annotation with a plain text description on what the Unit Test tests.

## :toolbox: How to compile Slimefun4
Slimefun is written in Java and uses [Maven](https://maven.apache.org/) for compilation.<br>
To compile Slimefun yourself, follow these steps:

1. Clone the project via git<br>
`$ git clone https://github.com/Slimefun/Slimefun4/`
2. Compile the project using Maven<br>
`$ mvn clean package`
3. Extract the compiled `Slimefun-v4.X-UNOFFICIAL.jar` from your `/target/` directory.

If you are already using an IDE, make sure to import the project via git and set it up as a *Maven project*. 
Then you should be able build it via Maven using the goals `clean package`.

If you have any further questions, then please join our [Discord Support Server](https://discord.gg/slimefun) and ask your questions in the `#programming-help` channel.<br>
**Note that we will not accept any bug reports from custom-compiled versions of Slimefun**.

## :black_nib: Code Style guidelines
The general gist when it comes to code style: **Try to be consistent!**.<br>
Try to stay inline with the code that surrounds you, having an entire package or even a single file that's filled with plenty of different and inconsistent code styles is just hard to read or maintain. That's why we wanna make sure everyone follows these principles.

*Note that these are just guidelines, we may request changes on your pull request if we think there are changes necessary. 
But we won't reject your Pull Request completely due to a few styling inconsistencies, we can always refactor code later. 
But do try to follow our code style as best as you can.*

#### 1. Imports
* Don't use wildcard (`*`) imports!
* Don't import unused classes!
* Don't use static imports!
* Always use imports, even in javadocs, don't write out the full location of a class.
#### 2. Annotations
* Methods and parameters should be annotated with `@Nullable` (`javax.annotation.Nullable`) or `@Nonnull`(`javax.annotation.Nonnull`)!
* Methods that override a method must be annotated with `@Override`!
* Interfaces with only one method should be annotated using `@FunctionalInterface`!
* If you deprecate a method, add an `@deprecated` section to the javadocs explaining why you did it.
#### 3. Documentation
* Every class and every public method should have a Javadocs section assigned to it.
* New packages should have a `package-info.java` file with documentation about the package.
* Classes should have an `@author` tag.
* If there are any other relevant classes related to yours, add them using the `@see` tag.
#### 4. Unit Tests
* Try to write Unit Tests where possible.
* Unit Test classes and methods should have no access modifier, not `public`, `protected` nor `private`.
* Each Test should have a plain text `@DisplayName` annotation!
#### 5. General best-practices
* Do not use `Collection#forEach(x -> ...)`, use a proper `for (...)` loop!
* Do not create new `Random` objects, use `ThreadLocalRandom.current()` instead!
* Always declare Maps or Collections using their base type! (e.g. `List<String> list = new ArrayList<>();`)
* When doing String operations like `String#toUppercase()`, always specify `Locale.ROOT` as an argument!
* When reading or writing files, always specify the encoding using `StandardCharsets.UTF_8`!
* Do not declare multiple fields/variables on the same line! (e.g. Don't do this: `int x, y, z;`)
* Use a Logger, try to avoid `System.out.println(...)` and `Throwable#printStacktrace()`, use `Logger#log` instead!
* Do not use Exceptions to validate data, empty catch blocks are a very bad practice, use other means like a regular expression to validate data.
* If a parameter is annotated with `@Nonnull`, you should enforce this behaviour by doing `Validate.notNull(variable, "...");` and give a meaningful message about what went wrong
* Any `switch/case` should always have a `default:` case at the end.
* If you are working with a resource that must be closed, use a `try/with-resource`, this will automatically close the resource at the end. (e.g. `try (InputStream stream = ...) {`)
* Array designators should be placed behind the type, not the variable name. (e.g. `int[] myArray`)
* Enums must be compared using `==`, not with `.equals()`!
* Avoid direct string concatenation, use a `StringBuilder` instead!
* If you need both the key and the value from a Map, use `Map#entrySet()`!
#### 6. Naming conventions
* Classes should be in *PascalCase* (e.g. `MyAwesomeClass`)
* Enum constants should be in *SCREAMING_SNAKE_CASE* (e.g. `MY_ENUM_CONSTANT`)
* Constants (`static final` fields) should be in *SCREAMING_SNAKE_CASE* (e.g. `MY_CONSTANT_FIELD`)
* Variables, parameters and fields should be in *camelCase* (e.g. `myVariableOrField`)
* All methods should be in *camelCase* (e.g. `myMethod`)
* Packages must be all lowercase, consecutive words should generally be avoided. (e.g. `io.github.thebusybiscuit.slimefun4.core.something`)
#### 7. Style preferences
* Use **Spaces**, not Tabs!
* One class per file! Please don't put multiple classes into one file, this also applies to enums, make a seperate file for new classes or enums.
* Try to keep ternary operators to a minimum, only in return statements. (e.g. avoid doing this: `int y = x == null ? 1: 2`)
* Try to keep so-called "guard blocks" to a minimum. One guard block is fine but having multiple guard blocks before getting to the actual code... Well, you might wanna refactor your code there. Example:
```java
// guard block
if (something) {
  return;
}

// Actual code...
```
* if/else statements should always include a bracket, please avoid one-line statements. (e.g. Avoid doing: `if (x == 0) return;`)
* We do not enforce any particular width or column limit, just try to prevent your lines from becoming too long. But please avoid line-wrapping.
* Annotations that target the return type of the method should be inline. Annotations which target the method itself should be written in the line above:
```java
@Override // <- Describes the method itself. `@Nullable` describes only the return type.
public @Nullable String getString() {
  // [...]
}
```
* Comments should never go on the same line as code! Always above or below.
* When you deviate from this style, add formatter comments and explain why. Example:
```java
// @formatter:off - This array represents a 3x3 grid and should be shown as such.
String[] arrays = {
    "1", "2", "3",
    "4", "5", "6",
    "7", "8", "9"
};
// @formatter:on
```
* Make sure that empty lines are truly empty, they should not contain any whitespace characters.
* Empty blocks like constructors should not occupy more than one line. (e.g. `private MyClass() {}`)
* Modifiers for classes and fields must follow this order:<br>
`[public/protected/private] [abstract] [static] [final]`
* We recommend using horizontal whitespaces like this:
  * In variable assignments: `int x = 123;`
  * In a for-loop: `for (int i = 0; i < 10; i++) {`
  * Before and after statement parenthesis: `if (x != null) {`
  * Inbetween array initializers: `int[] array = { 1, 2, 3 };`
  * After the double slash of a comment: `// This is a comment`
* Slimefun follows the **1TBS / OTBS** Bracket-Style standard (One true brace style):
```java
private void example(int x) {
    if (x < 0) {
        // x < 0
    } else if (x > 0) {
        // x > 0
    } else {
        // x == 0
    }
}
```
