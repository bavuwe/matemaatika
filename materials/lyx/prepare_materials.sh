#!/usr/bin/env bash
elyxer --noconvert latex_content.lyx mata.html
java -jar ../MataParser/MataIndex.jar
