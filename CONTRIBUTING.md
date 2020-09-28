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
Slimefun's translation is available on [gitlocalize.com](https://gitlocalize.com/repo/3841).
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
