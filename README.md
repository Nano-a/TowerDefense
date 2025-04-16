```markdown
# Tower Defense 🏰

Bienvenue dans **Tower Defense**, un projet captivant développé en Java par **Abderrahman Ajinou** et **Gebrayel Maroun**. Ce jeu allie stratégie, logique et créativité pour offrir une expérience immersive où vous devez protéger votre base contre des vagues d’ennemis en construisant des tours tactiques. Conçu avec rigueur et passion, ce projet illustre notre engagement à produire un code robuste, une interface utilisateur soignée, et des compétences en développement logiciel prêtes à impressionner. 🚀

## 🎯 À propos du projet

**Tower Defense** est un jeu classique de type tower defense, implémenté avec **Java Swing** pour une interface graphique fluide et des bibliothèques comme **Jackson** pour la gestion des niveaux. Voici ce qui rend ce projet unique :

- **Architecture modulaire** : Organisé en packages (`composants`, `gestionnaire`, `mecaniques`, etc.) pour une maintenance aisée.
- **Gameplay dynamique** : Tours évolutives, ennemis variés, et un algorithme A* pour des chemins optimisés.
- **Interface intuitive** : Boutons personnalisés, journal de logs en temps réel, et infobulles interactives.
- **Engagement** : Développé avec soin pour répondre aux exigences académiques tout en visant une qualité professionnelle.

Ce projet est une vitrine de notre capacité à collaborer, résoudre des problèmes complexes, et livrer un produit fonctionnel. Nous sommes motivés à continuer d’apprendre et à contribuer à des projets innovants ! 💡

## 📋 Prérequis

Pour exécuter **Tower Defense**, assurez-vous d’avoir les outils suivants :

| Prérequis | Description | Lien/Instruction |
|-----------|-------------|------------------|
| **Git** 🛠️ | Pour cloner le dépôt. | Téléchargez depuis [git-scm.com](https://git-scm.com). |
| **JDK** ☕ | Version 8 ou supérieure (JDK 11 recommandé). | Téléchargez depuis [oracle.com](https://www.oracle.com/java/technologies/javase-downloads.html) ou [openjdk.java.net](https://openjdk.java.net). |
| **Système d’exploitation** 💻 | Windows, Linux, ou macOS. | Compatible avec Swing pour l’interface graphique. |
| **Terminal** 🖥️ | Pour compiler/exécuter (optionnel si IDE utilisé). | Command Prompt (Windows), Terminal (Linux/macOS). |
| **Linux uniquement** 🐧 | Bibliothèque `libx11-dev` pour JNativeHook. | Installez via `sudo apt-get install libx11-dev`. |

### Bibliothèques externes

Incluses dans `res/libs` :

- `jackson-annotations-2.9.4.jar`
- `jackson-core-2.9.4.jar`
- `jackson-databind-2.9.4.jar`
- `java-json.jar`
- `jnativehook-2.1.0.jar`

### Ressources

Les dossiers suivants doivent être présents :

- `res/Icons` : `Pause.png`, `Pausee.png`
- `res/Levels` : Fichiers JSON (`1.json`, `2.json`, etc.), CSV (`TerrainMapTest1.csv`, etc.)
- `res/TowerIcons` : `Maison.png`, `TestIcon1.png`, `TestIcon2.png`
- `res/TowerSprites` : `BaseSprite.png`, `FarSprite.png`, `FarSpritee.png`, `Mage2Sprite.png`, `MageSprite.png`, `MegaSprite.png`, `RapidSprite.png`, `RapidSpritee.png`
- Fichier de configuration : `Tower-Defense/Excel.json` (créé automatiquement si absent)

*Note* : Aucun outil de build (Maven, Gradle) n’est requis. La compilation peut être manuelle ou via un IDE.

## 🚀 Installation et exécution

Suivez ces étapes pour lancer **Tower Defense** et plonger dans l’action :

1. **Cloner le dépôt** :

   ```bash
   git clone https://github.com/Nano-a/TowerDefense.git
   ```

2. **Accéder au dossier** :

   ```bash
   cd TowerDefense
   ```

3. **Configurer et exécuter** :

   ### Option 1 : Via Visual Studio Code

   1. Ouvrez le projet dans VS Code :
      - Importez le dossier `TowerDefense`.
   2. Configurez le classpath :
      - Assurez-vous que `res/libs/*` est inclus dans les dépendances Java.
      - Marquez le dossier `res` comme dossier de ressources.
   3. Exécutez le jeu :
      - Ouvrez `src/utilisateurinterface/CadreJeu.java`.
      - Cliquez sur **"Run Java"** ou **"Debug Java"** dans VS Code.

   ### Option 2 : Compilation manuelle

   1. Compiler le jeu :
      - Sur **Windows** :

        ```bash
        javac -cp ".;res/libs/*" -d bin src/*/*.java
        ```
      - Sur **Linux/macOS** :

        ```bash
        javac -cp ".:res/libs/*" -d bin src/*/*.java
        ```
   2. Exécuter le jeu :
      - Sur **Windows** :

        ```bash
        java -cp "bin;res/libs/*" utilisateurinterface.CadreJeu
        ```
      - Sur **Linux/macOS** :

        ```bash
        java -cp "bin:res/libs/*" utilisateurinterface.CadreJeu
        ```

### Remarques

- Assurez-vous que le dossier `res` est dans le classpath pour charger les images et niveaux.
- Le jeu crée `Tower-Defense/Excel.json` dans le répertoire de travail ; vérifiez les permissions d’écriture.
- Si vous utilisez un autre IDE (Eclipse, IntelliJ), importez le projet, ajoutez `res/libs/*` au classpath, et marquez `res` comme dossier de ressources.

## 🛡️ Fonctionnalités principales

- **Gameplay stratégique** : Placez des tours pour bloquer les ennemis et protégez votre forteresse.
- **Tours évolutives** : Améliorez vos tours pour augmenter portée, vitesse, et dégâts.
- **Niveaux dynamiques** : Chargez des niveaux via JSON et CSV pour une rejouabilité infinie.
- **Interface intuitive** : Boutons personnalisés, infobulles, et journal de logs pour une expérience fluide.
- **Performance optimisée** : Multithreading pour des mises à jour fluides.

## 💻 Pourquoi ce projet séduit les recruteurs ?

**Tower Defense** est bien plus qu’un jeu ; c’est une démonstration de nos compétences techniques et de notre esprit d’équipe :

- **Maîtrise de Java** : Utilisation avancée de Swing, threading, et parsing JSON.
- **Conception logicielle** : Architecture modulaire avec séparation claire des responsabilités.
- **Résolution de problèmes** : Implémentation d’un algorithme A* pour les chemins ennemis.
- **Attention aux détails** : Interface soignée et logs pour un débogage efficace.
- **Motivation** : Ce projet reflète notre passion pour le développement et notre ambition de produire un travail de qualité.

Nous sommes impatients de mettre ces compétences au service de projets innovants et de continuer à évoluer comme développeurs ! 🌟

## 📝 Remarques importantes

- Le point d’entrée du jeu est `utilisateurinterface.CadreJeu`. Assurez-vous qu’il est correctement référencé.
- Les ressources dans `res/` doivent être intactes pour éviter les erreurs de chargement.
- Sur Linux, vérifiez que `libx11-dev` est installé pour JNativeHook.

## 👨‍💻 Auteurs

- **Abderrahman Ajinou**
- **Gebrayel Maroun**

Nous sommes fiers de ce projet et enthousiastes à l’idée de partager notre travail avec la communauté. Merci de tester **Tower Defense** ! Pour toute question ou suggestion, contactez-nous. 🙌

---

*Développé avec passion et détermination pour repousser les limites du code et du fun !* 🎮
```
