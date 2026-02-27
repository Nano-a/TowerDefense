#!/bin/bash
# Script de lancement Tower Defense
cd "$(dirname "$0")"
mkdir -p bin
if [ ! -d "bin/utilisateurinterface" ]; then
  echo "Compilation..."
  javac -cp ".:res/libs/*" -d bin $(find src -name "*.java")
fi
java -cp "bin:res:res/libs/*" utilisateurinterface.CadreJeu
