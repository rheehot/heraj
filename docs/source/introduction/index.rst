Introduction
============

Heraj is a java framework for aergo. See `aergo`_ for technical details.

.. _aergo: http://docs.aergo.io

Install
-------

You can install with maven or gradle.

Maven
^^^^^

.. code-block:: xml

  <repositories>
    <repository>
      <id>jcenter</id>
      <url>https://jcenter.bintray.com</url>
    </repository>
  </repositories>

  ...

  <dependencies>
    <dependency>
      <groupId>io.aergo</groupId>
      <artifactId>heraj-transport</artifactId>
      <version>${herajVersion}</version>
    </dependency>
    <dependency>
      <groupId>io.aergo</groupId>
      <artifactId>heraj-wallet</artifactId>
      <version>${herajVersion}</version>
    </dependency>
    <dependency>
      <groupId>io.aergo</groupId>
      <artifactId>heraj-smart-contract</artifactId>
      <version>${herajVersion}</version>
    </dependency>
  </dependencies>

Gradle
^^^^^^

.. code-block:: groovy

  repositories {
    jcenter()
  }

  ...

  dependencies {
    implementation "io.aergo:heraj-transport:${herajVersion}"
    implementation "io.aergo:heraj-wallet:${herajVersion}"
    implementation "io.aergo:heraj-smart-contract:${herajVersion}"
  }

Compatibility
-------------

Heraj can be used in jdk7 or higher. But jdk8 is recommanded.

======== ================ ================ ========================
heraj    aergo            jdk              android
======== ================ ================ ========================
1.4.x    2.2.x            7 or higher      3.0 (API 11) or higher
1.3.x    2.0.x, 2.1.x     7 or higher      3.0 (API 11) or higher
1.2.2    1.3.x            7 or higher      3.0 (API 11) or higher
======== ================ ================ ========================

Annotations
-----------

There ares 4 type of annotations.

- @ApiAudience.Public : Intended for use by heraj user. It will not be changed if possible.
- @ApiAudience.Private : Intended for use only within hera itself. Do not use it.
- @ApiStability.Stable : Can evolve while retaining compatibility for minor release boundaries.
- @ApiStability.Unstable : No guarantee is provided as to reliability or stability.

When using heraj, you have keep meaning of those annotations in mind.
