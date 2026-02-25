# F1 Widget Android 🏎️

Application Android avec widget pour suivre la Formule 1 - prochains Grands Prix, classements pilotes et constructeurs, calendrier complet de la saison.

## Fonctionnalités ✨

### Widget Android
- **Widget sur l'écran d'accueil** affichant les informations du prochain Grand Prix
- Mise à jour automatique toutes les 6 heures
- Design moderne avec drapeau du pays et dates du weekend
- Affichage des sessions (FP1, FP2, FP3, Sprint, Qualifications, Course)
- Images des circuits téléchargées depuis le CDN officiel Formula 1

### Application Complète

#### 📱 Page d'Accueil
- Carte détaillée du prochain Grand Prix
  - Nom de la course et du circuit
  - Drapeau du pays
  - Round actuel / total
  - Dates du weekend
  - Toutes les sessions avec dates et heures
- Aperçu du classement pilotes (Top 5)
- Aperçu du classement constructeurs (Top 5)
- Interface scrollable pour voir toutes les informations

#### 🏆 Page Classements
- Onglet **Pilotes** : Classement complet avec
  - Position et badge coloré (or/argent/bronze pour le podium)
  - Nom du pilote
  - Équipe avec couleur distinctive
  - Points
- Onglet **Constructeurs** : Classement des équipes
  - Position et badge coloré
  - Nom de l'équipe avec couleur
  - Points

#### 📅 Page Calendrier
- Liste complète de tous les Grands Prix de la saison
- Pour chaque course :
  - Numéro du round
  - Drapeau du pays
  - Nom du Grand Prix
  - Circuit
  - Date
- Indication visuelle des courses passées (grisées)
- Mise en évidence du prochain GP

## Architecture Technique 🏗️

### Technologies
- **Kotlin** : Langage de programmation
- **Jetpack Compose** : UI moderne et déclarative
- **Material Design 3** : Design system Google
- **Navigation Component** : Navigation entre écrans
- **ViewModel** : Gestion de l'état
- **Coroutines & Flow** : Programmation asynchrone
- **Retrofit** : Appels API REST
- **WorkManager** : Tâches en arrière-plan
- **Coil** : Chargement d'images

### Sources de Données
- **API Ergast F1** : Données de course, classements, calendrier
  - `http://api.jolpi.ca/ergast/f1/`
- **Formula 1 CDN** : Images officielles des circuits
  - `https://media.formula1.com/`

### Structure du Projet

```
app/src/main/java/com/yomlaiolo/f1widget/
├── data/
│   ├── models/          # Modèles de données (Race, Standing, etc.)
│   ├── repository/      # Repository pour accès aux données
│   └── F1ApiService.kt  # Interface Retrofit
├── ui/
│   ├── screens/         # Écrans Compose (Home, Standings, Calendar)
│   ├── components/      # Composants réutilisables
│   ├── navigation/      # Configuration navigation
│   └── MainViewModel.kt # ViewModel principal
├── utils/
│   ├── CountryFlags.kt        # Drapeaux par pays
│   ├── TeamColors.kt          # Couleurs des équipes F1
│   ├── DateFormatter.kt       # Formatage dates
│   └── CircuitImageManager.kt # Gestion images circuits
├── widget/
│   ├── F1Widget.kt            # Widget principal
│   └── F1WidgetUpdateWorker.kt # Worker mise à jour
└── MainActivity.kt      # Point d'entrée app
```

## Modèles de Données 📊

### Race (Grand Prix)
```kotlin
data class Race(
    val round: String,
    val raceName: String,
    val circuit: Circuit,
    val date: String,
    val time: String?,
    val firstPractice: Session?,
    val secondPractice: Session?,
    val thirdPractice: Session?,
    val qualifying: Session?,
    val sprint: Session?
)
```

### DriverStanding (Classement Pilote)
```kotlin
data class DriverStanding(
    val position: String,
    val points: String,
    val wins: String,
    val driver: Driver,
    val constructors: List<Constructor>
)
```

### ConstructorStanding (Classement Constructeur)
```kotlin
data class ConstructorStanding(
    val position: String,
    val points: String,
    val wins: String,
    val constructor: Constructor
)
```

## Configuration Requise 📋

- **Min SDK** : 26 (Android 8.0 Oreo)
- **Target SDK** : 34 (Android 14)
- **Kotlin** : 1.9.x
- **Gradle** : 8.x
- **JDK** : 17

## Permissions 🔐

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## Installation 🚀

1. **Cloner le repository**
   ```bash
   git clone https://github.com/yomlaiolo/f1-widget-android.git
   cd f1-widget-android
   ```

2. **Ouvrir dans Android Studio**
   - Android Studio Hedgehog (2023.1.1) ou supérieur recommandé

3. **Sync Gradle**
   - Laisser Android Studio télécharger les dépendances

4. **Compiler et installer**
   - Connecter un appareil Android ou lancer un émulateur
   - Cliquer sur "Run" ou `Shift + F10`

## Utilisation 📱

### Ajouter le Widget
1. Long press sur l'écran d'accueil Android
2. Sélectionner "Widgets"
3. Trouver "F1 Widget"
4. Glisser-déposer sur l'écran d'accueil
5. Le widget se met à jour automatiquement toutes les 6 heures

### Utiliser l'Application
1. Lancer l'app depuis le launcher
2. **Accueil** : Voir les infos du prochain GP + aperçu classements
3. **Classements** : Voir les classements complets pilotes et constructeurs
4. **Calendrier** : Explorer tous les GP de la saison
5. Tirer pour rafraîchir les données

## Roadmap 🗺️

### Version actuelle (1.0)
- ✅ Widget avec infos du prochain GP
- ✅ Application avec 3 écrans
- ✅ Classements pilotes et constructeurs
- ✅ Calendrier de la saison
- ✅ Images des circuits

### Prochaines versions
- 🔄 Support des notifications avant les courses
- 🔄 Détails par Grand Prix (météo, historique)
- 🔄 Support multi-langue
- 🔄 Thème clair/sombre personnalisé
- 🔄 Widget redimensionnable
- 🔄 Résultats en temps réel pendant les courses

## Crédits 🙏

- **Données** : [Ergast Developer API](http://ergast.com/mrd/)
- **Images** : Formula 1 Official CDN
- **Design** : Material Design 3

## Licence 📄

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

## Contact 📧

Pour toute question ou suggestion :
- GitHub Issues : [Issues](https://github.com/yomlaiolo/f1-widget-android/issues)
- Email : [Contact](mailto:91747577+yomlaiolo@users.noreply.github.com)

---

**Note** : Cette application n'est pas officiellement affiliée à la Formule 1, à la FIA ou à Liberty Media. Toutes les marques et logos appartiennent à leurs propriétaires respectifs.