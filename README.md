# F1 Widget Android 🏁

Widget Android natif en Kotlin affichant le tracé du circuit et les horaires de toutes les sessions du prochain Grand Prix de Formule 1.

## 🎯 Fonctionnalités

- 🗺️ **Tracé du circuit** : Affichage visuel du prochain circuit
- 📅 **Horaires complets** : FP1, FP2, FP3, Qualifications, Sprint (si applicable) et Course
- 🔄 **Mise à jour automatique** : Refresh toutes les heures via WorkManager
- 🌐 **API gratuite** : Utilise l'API Jolpica F1 (successeur d'Ergast)
- 🎨 **Design moderne** : Interface sombre avec couleurs F1 officielles

## 📦 Technologies utilisées

- **Kotlin** : Langage principal
- **Android Widgets API** : RemoteViews pour le widget
- **Retrofit** : Appels API REST
- **WorkManager** : Mises à jour en arrière-plan
- **Coroutines** : Programmation asynchrone
- **Jolpica F1 API** : Données de calendrier F1

## 🛠️ Installation

### Prérequis

- Android Studio Hedgehog | 2023.1.1 ou supérieur
- JDK 17
- Android SDK 26+ (Android 8.0)

### Étapes

1. Clone le repository :
```bash
git clone https://github.com/yomlaiolo/f1-widget-android.git
cd f1-widget-android
```

2. Ouvre le projet dans Android Studio

3. Synchronise Gradle

4. Build et lance l'app

5. Ajoute le widget à ton écran d'accueil :
   - Long press sur l'écran d'accueil
   - Sélectionne "Widgets"
   - Trouve "F1 Widget" et glisse-le sur l'écran

## 📱 Utilisation

Une fois le widget ajouté :
- Il affichera automatiquement le prochain Grand Prix
- Le tracé du circuit sera visible
- Tous les horaires des sessions seront listés
- Mise à jour automatique toutes les heures

## 📁 Structure du projet

```
app/src/main/
├── java/com/ton_package/f1widget/
│   ├── widget/
│   │   ├── F1Widget.kt                    # Widget provider principal
│   │   └── F1WidgetUpdateWorker.kt        # Background updates
│   ├── data/
│   │   ├── F1ApiService.kt                # Interface Retrofit
│   │   ├── models/                        # Modèles de données
│   │   └── repository/
│   │       └── F1Repository.kt            # Logique de récupération
│   └── utils/
├── res/
│   ├── layout/
│   │   └── f1_widget.xml                  # Layout du widget
│   ├── xml/
│   │   └── f1_widget_info.xml             # Métadonnées widget
│   └── drawable/
│       └── circuits/                       # Images des circuits
└── AndroidManifest.xml
```

## 🏎️ API Utilisée

**Jolpica F1 API** (gratuite)
- Base URL : `http://api.jolpi.ca/ergast/f1/`
- Endpoint calendrier : `/current.json` ou `/{year}.json`
- Endpoint prochain GP : `/current/next.json`
- Compatible avec l'ancien format Ergast

## ⚙️ Configuration

Le widget se configure automatiquement. Pour personnaliser :

1. **Fréquence de mise à jour** : Modifie `TimeUnit.HOURS` dans `F1Widget.kt`
2. **Images de circuits** : Ajoute tes propres images dans `res/drawable/circuits/`
3. **Couleurs** : Personnalise dans `widget_background.xml` et `f1_widget.xml`

## 🗃️ Images des circuits

⚠️ **Important** : Les images des circuits ne sont pas incluses dans ce repo.

Pour ajouter les tracés :

1. Télécharge les images depuis [Wikipedia Commons](https://commons.wikimedia.org/wiki/Category:Formula_One_circuit_maps)
2. Convertis-les en PNG transparent (fond noir)
3. Redimensionne-les à environ 800x600px
4. Place-les dans `app/src/main/res/drawable/` avec les noms :
   - `circuit_monaco.png`
   - `circuit_silverstone.png`
   - `circuit_monza.png`
   - etc.

## 🚀 Améliorations futures

- [ ] Configuration du widget (choix du GP)
- [ ] Click actions (ouvrir app avec détails)
- [ ] Notifications avant chaque session
- [ ] Thèmes personnalisables
- [ ] Multiple tailles de widget
- [ ] Mode sombre/clair
- [ ] Support pour historique des courses

## 📝 Licence

MIT License - Libre d'utilisation et modification

## 👤 Auteur

**Tom Laiolo** - [@yomlaiolo](https://github.com/yomlaiolo)

## 👏 Remerciements

- [Jolpica F1 API](http://api.jolpi.ca/ergast/f1/) pour les données
- [Ergast Developer API](http://ergast.com/mrd/) pour le format original
- [OpenF1](https://openf1.org/) pour l'inspiration

---

**Note** : Ce widget est un projet indépendant et n'est pas affilié à Formula 1, FIA ou Liberty Media.
