---
layout: default
title: FreeHEP - Vector Graphics
lead: Installation
slug: user
dir: .
---
You can refer to the latest released version of Vector Graphics using maven:

```xml
    <properties>
        <vectorgraphics.version>2.3</vectorgraphics.version>
    </properties>

	<dependencies>
		<dependency>
			<groupId>org.freehep</groupId>
			<artifactId>freehep-graphicsio</artifactId>
            <version>${vectorgraphics.version}</version>
		</dependency>
    </dependencies>
```
which automatically pulls in freehep-graphics2d, freehep-graphicsbase and freehep-io.

You should also declare any (or all) of the formats that you would like to include:

```xml
	<dependencies>
		<dependency>
			<groupId>org.freehep</groupId>
			<artifactId>freehep-graphicsio-emf</artifactId>
            <version>${vectorgraphics.version}</version>
		</dependency>
		<dependency>
			<groupId>org.freehep</groupId>
			<artifactId>freehep-graphicsio-java</artifactId>
            <version>${vectorgraphics.version}</version>
		</dependency>
		<dependency>
			<groupId>org.freehep</groupId>
			<artifactId>freehep-graphicsio-pdf</artifactId>
            <version>${vectorgraphics.version}</version>
		</dependency>
		<dependency>
			<groupId>org.freehep</groupId>
			<artifactId>freehep-graphicsio-ps</artifactId>
            <version>${vectorgraphics.version}</version>
		</dependency>
		<dependency>
			<groupId>org.freehep</groupId>
			<artifactId>freehep-graphicsio-svg</artifactId>
            <version>${vectorgraphics.version}</version>
		</dependency>
		<dependency>
			<groupId>org.freehep</groupId>
			<artifactId>freehep-graphicsio-swf</artifactId>
            <version>${vectorgraphics.version}</version>
		</dependency>
	<dependencies>
```