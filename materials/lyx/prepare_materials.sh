#!/usr/bin/env bash
elyxer --noconvert latex_content.lyx mata.html
java -jar ../MataParser/MataIndex.jar

mv mata.html ../../app/src/main/assets
mv mata.index ../../app/src/main/assets
mv mata.structure ../../app/src/main/assets

