# F1 Widget Android 🏎️

Widget Android natif en Kotlin affichant le tracé du circuit et les horaires de toutes les sessions du prochain Grand Prix de Formule 1.

## 🎯 Fonctionnalités

- 🗺️ **Tracé du circuit** : Affichage visuel du prochain circuit
- 📅 **Horaires complets** : FP1, FP2, FP3, Qualifications, Sprint (si applicable) et Course
- 🔄 **Mise à jour automatique** : Refresh toutes les heures via WorkManager
- 🌐 **API gratuite** : Utilise l'API Jolpica F1 (successeur d'Ergast)
- 🎨 **Design moderne** : Interface sombre avec couleurs F1 officielles
- 🛑 **Fallback robuste** : Image par défaut si le circuit n'est pas trouvé

## 📦 Technologies utilisées

- **Kotlin** : Langage principal
- **Android Widgets API** : RemoteViews pour le widget
- **Retrofit** : Appels API REST
- **WorkManager** : Mises à jour en arrière-plan
- **Coroutines** : Programmation asynchrone
- **Jolpica F1 API** : Données de calendrier F1

## 📱 Installation rapide (APK prêt à l'emploi)

### Option 1 : Télécharger l'APK depuis GitHub Actions

1. Va sur l'onglet [**Actions**](https://github.com/yomlaiolo/f1-widget-android/actions)
2. Clique sur le dernier workflow **"Build Android APK"** qui a réussi (✅)
3. Scroll en bas jusqu'à la section **"Artifacts"**
4. Télécharge **"f1-widget-debug"**
5. Extrait le ZIP et installe l'APK sur ton téléphone

### Option 2 : Télécharger depuis Releases

1. Va sur l'onglet [**Releases**](https://github.com/yomlaiolo/f1-widget-android/releases)
2. Télécharge le fichier `.apk` de la dernière version
3. Installe sur ton téléphone

### 📲 Installation sur Android

1. **Active "Sources inconnues"** :
   - Paramètres > Sécurité > Installer des applications inconnues
   - Autorise ton gestionnaire de fichiers

2. **Installe l'APK** :
   - Ouvre le fichier APK
   - Clique sur "Installer"
   - Attends la fin de l'installation

3. **Ajoute le widget** :
   - Long press sur l'écran d'accueil
   - Sélectionne "Widgets"
   - Trouve "F1 Widget" et glisse-le sur l'écran
   - Le widget va automatiquement charger le prochain GP

## 🛠️ Développement local

### Prérequis

- Android Studio Hedgehog | 2023.1.1 ou supérieur
- JDK 17
- Android SDK 26+ (Android 8.0)
- Gradle 8.2

### Étapes

1. Clone le repository :
```bash
git clone https://github.com/yomlaiolo/f1-widget-android.git
cd f1-widget-android
```

2. Ouvre le projet dans Android Studio

3. Laisse Gradle se synchroniser

4. Build et lance l'app :
```bash
./gradlew assembleDebug
# ou directement depuis Android Studio
```

5. L'APK sera générée dans :
```
app/build/outputs/apk/debug/app-debug.apk
```

## 📱 Utilisation

Une fois le widget ajouté :
- ✅ Il affichera automatiquement le prochain Grand Prix
- ✅ Le tracé du circuit sera visible (ou placeholder si image non disponible)
- ✅ Tous les horaires des sessions seront listés (en heure locale)
- ✅ Mise à jour automatique toutes les heures
- ✅ Fonctionne hors ligne avec les dernières données en cache

## 📁 Structure du projet

```
app/src/main/
├── java/com/yomlaiolo/f1widget/
│   ├── widget/
│   │   ├── F1Widget.kt                    # Widget provider principal
│   │   └── F1WidgetUpdateWorker.kt        # Background updates
│   ├── data/
│   │   ├── F1ApiService.kt                # Interface Retrofit
│   │   ├── models/                        # Modèles de données
│   │   └── repository/
│   │       └── F1Repository.kt            # Logique de récupération
├── res/
│   ├── layout/
│   │   └── f1_widget.xml                  # Layout du widget
│   ├── xml/
│   │   └── f1_widget_info.xml             # Métadonnées widget
│   └── drawable/
│       ├── circuit_default.xml            # Placeholder par défaut
│       └── circuits/                       # Images des circuits (optionnel)
└── AndroidManifest.xml
```

## 🏎️ API Utilisée

**Jolpica F1 API** (gratuite)
- Base URL : `http://api.jolpi.ca/ergast/f1/`
- Endpoint calendrier : `/current.json` ou `/{year}.json`
- Endpoint prochain GP : `/current/next.json`
- Compatible avec l'ancien format Ergast
- ✅ Pas d'authentification requise
- ✅ Pas de limite de requêtes

## ⚙️ Configuration

Le widget se configure automatiquement. Pour personnaliser :

### 1. Fréquence de mise à jour

Modifie dans `F1Widget.kt` :
```kotlin
val updateRequest = PeriodicWorkRequestBuilder<F1WidgetUpdateWorker>(
    1, TimeUnit.HOURS  // Change ici (ex: 30, TimeUnit.MINUTES)
)
```

### 2. Ajouter des images de circuits personnalisées

Consulte le guide complet : [docs/CIRCUITS.md](docs/CIRCUITS.md)

Résumé rapide :
1. Télécharge les tracés depuis [Wikipedia Commons](https://commons.wikimedia.org/wiki/Category:Formula_One_circuit_maps)
2. Place-les dans `app/src/main/res/drawable/`
3. Nomme-les : `circuit_monaco.png`, `circuit_silverstone.png`, etc.

### 3. Couleurs et thème

Modifie `f1_widget.xml` et `widget_background.xml`

## 🚀 Builds automatiques (CI/CD)

Le projet utilise GitHub Actions pour :
- ✅ Build automatique sur chaque push
- ✅ APK disponible dans les Artifacts
- ✅ Création de releases avec APK attachée

Pour créer une release :
```bash
git tag v1.0.0
git push origin v1.0.0
```

Ou manuellement depuis l'onglet Actions > "Create Release APK" > Run workflow

## 🚀 Améliorations futures

- [ ] Configuration du widget (choix du GP ou pilote favori)
- [ ] Click actions (ouvrir app avec détails complets)
- [ ] Notifications 1h avant chaque session
- [ ] Thèmes personnalisables (couleurs d'équipes)
- [ ] Multiple tailles de widget (petit, moyen, grand)
- [ ] Mode sombre/clair manuel
- [ ] Support pour historique des courses
- [ ] Widget pour le classement des pilotes/constructeurs
- [ ] Intégration OpenF1 pour données live pendant les courses

## 🐛 Debugging

Si le widget ne se met pas à jour :

1. **Vérifie la connexion internet**
2. **Force un refresh** : Retire et re-ajoute le widget
3. **Logs** : Utilise `adb logcat | grep F1Widget`
4. **Vérifie WorkManager** : Ouvre les paramètres Android > Apps > F1 Widget > Utilisation de la batterie

## 📝 Licence

MIT License - Libre d'utilisation et modification

## 👤 Auteur

**Tom Laiolo** - [@yomlaiolo](https://github.com/yomlaiolo)

🏛️ Epitech Toulouse

## 👏 Remerciements

- [Jolpica F1 API](http://api.jolpi.ca/ergast/f1/) pour les données
- [Ergast Developer API](http://ergast.com/mrd/) pour le format original
- [OpenF1](https://openf1.org/) pour l'inspiration
- Communauté F1 pour la passion du sport

---

**Note** : Ce widget est un projet indépendant et n'est pas affilié à Formula 1, FIA ou Liberty Media.

🏎️ **Enjoy your F1 Widget!**
