# Ev Emulator

![Ev Emulator](branding/ev-logo.png)

Ev Emulator est une application Android d’émulation basée sur Libretro.
Importez vos propres jeux depuis un dossier de votre appareil et retrouvez-les
par console dans votre bibliothèque.

- Interface mobile et prise en charge des manettes.
- Commandes virtuelles masquables pendant la partie, avec écran tactile actif.
- Sauvegardes rapides et reprise des parties.
- Bannière publicitaire dans le menu et pendant le jeu sur mobile (variante Play).
- Interface Android TV conservée, sans bannière intégrée à ses écrans.

Aucun jeu ni BIOS n’est fourni. Les compatibilités dépendent des moteurs et des jeux.
La version de développement utilise exclusivement des annonces de test.

## Développement

Dans Android Studio, sélectionner le module `lemuroid-app`, la variante
`playBundleDebug` et la configuration `EV Emulator Mobile`.
Voir [les notes du projet](EV-EMULATOR.md) pour la compilation et les étapes
restantes avant publication. Le logo fourni est conservé dans `branding/`.

## Origine et licences

Ev Emulator est un fork de [Lemuroid](https://github.com/Swordfish90/Lemuroid),
qui utilise [LibretroDroid](https://github.com/Swordfish90/LibretroDroid) et est
issu de [Retrograde](https://github.com/retrograde/retrograde-android).
Les crédits d’origine et la licence [GPL-3.0](COPYING) sont conservés.
Les moteurs et dépendances conservent leurs propres licences.