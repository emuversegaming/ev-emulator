# EV Emulator

Nouvelle base locale issue directement de Swordfish90/Lemuroid,
commit `53752bf29bc3f95c50f6c38f70cd4a53450a7098`.
Branche de travail : `codex/ev-emulator`. Base récupérée depuis l'amont.
Dépôt de destination : https://github.com/emuversegaming/ev-emulator, branche `master`.
L'ancien état distant est conservé localement dans `backup/ev-emulator-before-rebuild`.

## Personnalisation

- Nom : EV Emulator ; identifiant Android : `com.emuversegaming.evemulator`.
- Les packages Kotlin et les crédits Lemuroid restent conservés pour limiter
  les changements inutiles. Icône provisoire héritée de Lemuroid.
- Variante `play` : bannière adaptative sous le menu principal et sous le jeu
  mobile, séparée des commandes par une marge de 32 dp.
- Variante `free` : aucune dépendance publicitaire.
- Debug : identifiants de démonstration Google uniquement.
- Release : application AdMob `ca-app-pub-8171934056228590~1550648090`,
  bannière `ca-app-pub-8171934056228590/4563079381`.
- UMP actualise le consentement avant les requêtes publicitaires. Le formulaire
  est présenté dans le menu principal uniquement ; les choix publicitaires
  restent accessibles lorsqu'UMP l'exige. Le processus de jeu actualise son
  propre état UMP sans afficher de formulaire pendant une partie.
- Aucune annonce plein écran n'est ajoutée : le déclenchement à une pause
  volontaire et l'identifiant interstitiel restent à préciser.

L'affichage effectif dépend du consentement, du réseau et de la disponibilité
des annonces. Une bannière ne doit pas recouvrir l'image ou les commandes.
Le placement doit encore être testé sur téléphone en portrait et paysage,
avec les différentes configurations de commandes, notamment Nintendo DS.

## Compilation

JDK 17 ou compatible, SDK Android 35, sous-module `lemuroid-cores` requis.

```powershell
git submodule update --init --recursive
./gradlew.bat :lemuroid-app:compilePlayBundleDebugKotlin
./gradlew.bat :lemuroid-app:assemblePlayBundleDebug
```

Configurer `sdk.dir` dans `local.properties` ou `ANDROID_HOME`.
Le sous-module inclut la configuration des moteurs ; vérifier la présence
des binaires nécessaires avant de considérer un APK comme jouable.

Vérification du 19 septembre 2026 : `compilePlayBundleDebugKotlin` et
`assemblePlayBundleDebug` réussis avec le SDK Android 35. L'APK de développement
est généré dans `lemuroid-app/build/outputs/apk/playBundle/debug/`.
Le manifeste fusionné utilise `com.emuversegaming.evemulator.debug`.
Les essais sur téléphone (annonce, consentement, rotation, commandes et reprise)
restent à effectuer ; la compilation seule ne valide pas le comportement en jeu.

## Avant publication

- Remplacer le logo et configurer une clé de signature de production privée.
- Configurer les messages dans AdMob > Confidentialité et messages, fournir
  la politique de confidentialité et compléter les déclarations Google Play.
- Vérifier les exigences Google Play en vigueur, dont API cible et bibliothèques
  natives, ainsi que la compatibilité avec les pages mémoire de 16 Ko.
- Examiner les licences des moteurs avant monétisation. Lemuroid est sous
  GPL-3.0 : conserver les notices et fournir les sources correspondantes.
  La distribution liée au SDK propriétaire AdMob nécessite une vérification
  de compatibilité GPL / éventuelles exceptions auprès des ayants droit ;
  l'intégration technique ne constitue pas une validation de distribution.

Références :
- https://github.com/Swordfish90/Lemuroid
- https://www.gnu.org/licenses/gpl-faq.en.html#GPLIncompatibleLibs
- https://developers.google.com/admob/android/privacy
- https://support.google.com/admob/answer/6275345
