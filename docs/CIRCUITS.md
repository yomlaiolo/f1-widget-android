# Guide des Images de Circuits F1

## 🗃️ Images nécessaires

Les images des circuits sont stockées **localement** dans le projet en tant que Vector Drawables (SVG convertis en XML Android).

Dossier : `app/src/main/res/drawable/`

### Liste complète des circuits 2026

| # | Circuit ID (API) | Nom du fichier | Circuit |
|---|---|---|---|
| 1 | `bahrain` | `circuit_bahrain.xml` | Bahrain International Circuit |
| 2 | `jeddah` | `circuit_jeddah.xml` | Jeddah Corniche Circuit |
| 3 | `albert_park` | `circuit_albert_park.xml` | Albert Park Circuit (Australie) |
| 4 | `suzuka` | `circuit_suzuka.xml` | Suzuka International Racing Course |
| 5 | `shanghai` | `circuit_shanghai.xml` | Shanghai International Circuit |
| 6 | `miami` | `circuit_miami.xml` | Miami International Autodrome |
| 7 | `imola` | `circuit_imola.xml` | Autodromo Enzo e Dino Ferrari |
| 8 | `monaco` | `circuit_monaco.xml` | Circuit de Monaco |
| 9 | `villeneuve` | `circuit_villeneuve.xml` | Circuit Gilles Villeneuve (Canada) |
| 10 | `catalunya` | `circuit_catalunya.xml` | Circuit de Barcelona-Catalunya |
| 11 | `red_bull_ring` | `circuit_red_bull_ring.xml` | Red Bull Ring (Autriche) |
| 12 | `silverstone` | `circuit_silverstone.xml` | Silverstone Circuit |
| 13 | `hungaroring` | `circuit_hungaroring.xml` | Hungaroring |
| 14 | `spa` | `circuit_spa.xml` | Circuit de Spa-Francorchamps |
| 15 | `zandvoort` | `circuit_zandvoort.xml` | Circuit Zandvoort |
| 16 | `monza` | `circuit_monza.xml` | Autodromo Nazionale di Monza |
| 17 | `baku` | `circuit_baku.xml` | Baku City Circuit |
| 18 | `marina_bay` | `circuit_marina_bay.xml` | Marina Bay Street Circuit (Singapour) |
| 19 | `americas` | `circuit_americas.xml` | Circuit of the Americas (Austin) |
| 20 | `rodriguez` | `circuit_rodriguez.xml` | Autódromo Hermanos Rodríguez (Mexico) |
| 21 | `interlagos` | `circuit_interlagos.xml` | Autódromo José Carlos Pace (Brésil) |
| 22 | `vegas` | `circuit_vegas.xml` | Las Vegas Street Circuit |
| 23 | `losail` | `circuit_losail.xml` | Losail International Circuit (Qatar) |
| 24 | `yas_marina` | `circuit_yas_marina.xml` | Yas Marina Circuit (Abu Dhabi) |
| 25 | `madrid` | `circuit_madrid.xml` | Circuit de Madrid |
| — | — | `circuit_default.xml` | Image par défaut (fallback) |

## 💾 Comment ajouter un circuit

### Étape 1 : Obtenir le SVG

Télécharge le SVG du tracé du circuit depuis :
- **Wikipedia Commons** : https://commons.wikimedia.org/wiki/Category:Formula_One_circuit_maps
- **F1 Sketch** : https://f1sketch.com/

### Étape 2 : Convertir en Vector Drawable

#### Option A : Android Studio (recommandé)
1. Clic droit sur `res/drawable/` → **New** → **Vector Asset**
2. Sélectionne **Local file (SVG, PSD)**
3. Choisis ton fichier SVG
4. Nomme-le `circuit_{circuitId}` (ex: `circuit_monaco`)
5. Clique **Next** → **Finish**

#### Option B : Outil en ligne
1. Va sur https://inloop.github.io/svg2android/
2. Upload ton SVG
3. Copie le XML généré
4. Crée le fichier `circuit_{circuitId}.xml` dans `res/drawable/`

### Étape 3 : Vérifier

1. `Build > Make Project` dans Android Studio
2. Le fichier doit apparaître dans `R.drawable.circuit_xxx`
3. Le widget chargera automatiquement l'image via `CircuitImageManager`

## 🎨 Spécifications recommandées

- **Format** : Vector Drawable XML (converti depuis SVG)
- **Viewport** : ~800x600 recommandé
- **Tracé** : Blanc (#FFFFFF) ou couleur claire sur fond transparent
- **Style** : Minimaliste, lignes claires

> **Note** : Si un SVG est très complexe (>200 paths), utilise plutôt un PNG dans `drawable-xxhdpi/` nommé `circuit_{circuitId}.png`.

## 🎯 Image par défaut

Le fichier `circuit_default.xml` existe déjà et sert de fallback lorsqu'un circuit n'a pas d'image dédiée.

## ⚙️ Architecture

La logique de chargement est centralisée dans `CircuitImageManager.kt` :

```kotlin
// Retourne le drawable ID pour un circuitId donné
CircuitImageManager.getCircuitDrawableRes(context, circuitId)
// → R.drawable.circuit_monaco ou R.drawable.circuit_default
```

Le widget (`F1Widget.kt`) appelle cette méthode pour afficher l'image du circuit.
Aucun téléchargement réseau n'est nécessaire — tout est local.

## 🔗 Ressources utiles

- [Wikipedia F1 Circuits](https://commons.wikimedia.org/wiki/Category:Formula_One_circuit_maps)
- [F1 Sketch](https://f1sketch.com/)
- [SVG to Android Converter](https://inloop.github.io/svg2android/)
- [GIMP](https://www.gimp.org/) - Éditeur d'images gratuit
