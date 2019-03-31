#!/usr/bin/env bash
elyxer --noconvert latex_content.lyx mata.html
java -jar ../MataParser/MataIndex.jar

# this used to fix rendering problems with vectors
sed -i "s/<span class=\"symbolover\">⟶/<span class=\"symbolover\">→/g" mata.html

mv mata.html ../../app/src/main/assets
mv mata.index ../../app/src/main/assets
mv mata.structure ../../app/src/main/assets

